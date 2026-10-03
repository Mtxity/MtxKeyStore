# MtxKeyStore

Small Spring Boot HTTP service for storing string key/value pairs grouped by application ID.
Values are held in memory and can be periodically persisted to PostgreSQL and/or Valkey.

The complete OpenAPI description is in [`swagger.yml`](./swagger.yml).

## Requirements

- JDK compatible with Spring Boot 4.1
- PostgreSQL when `db.useDb: true` (default)
- Valkey/Redis when `nosql.useNosql: true`
- Gradle is not required; use the included wrapper

## Run locally

```bash
./gradlew bootRun
```

The server listens on `http://localhost:3000` by default.

Run tests with:

```bash
./gradlew test
```

## Configuration

Runtime configuration is in `src/main/resources/application.yml` and can be overridden with
Spring configuration mechanisms.

Important settings:

| Setting | Default | Description |
| --- | --- | --- |
| `server.port` | `3000` | HTTP port |
| `api.requireAuthorization` | `true` | Requires the exact `Authorization` header value configured in `api.authToken` |
| `api.authToken` | `${MTX_AUTH_TOKEN}` | Exact shared authorization token read from the `MTX_AUTH_TOKEN` environment variable |
| `api.includeTimestampsInGetAll` | `true` | Includes last-modified timestamps in bulk reads |
| `db.useDb` | `true` | Enables PostgreSQL load/save |
| `db.storeInterval` | `5` | Persistence interval in minutes |
| `db.insertAllAtOnce` | `true` | Writes the PostgreSQL snapshot in one statement |
| `nosql.useNosql` | `false` | Enables Valkey load/save |
| `rcon.enabled` | `false` | Enables the `/rcon/send` endpoint |
| `rcon.host` | `localhost` | Configured RCON server hostname |
| `rcon.port` | `25575` | Configured RCON server TCP port |
| `rcon.password` | `${MTX_RCON_PASSWORD:}` | RCON password |
| `rcon.connectTimeoutMillis` | `5000` | TCP connection timeout |
| `rcon.readTimeoutMillis` | `5000` | RCON response timeout |

The default PostgreSQL connection is:

```text
jdbc:postgresql://localhost:5432/mtxkvstore
user: mtxkvstore
password: mtxkvstore
```

Flyway applies migrations from `src/main/resources/db/migration`. The database role and database
must exist before startup. When PostgreSQL persistence is enabled, create an `app` row for each
application ID before storing values; `kv.app_id` has a foreign-key constraint and the service does
not insert `app` rows.

For Valkey/Redis, configure `spring.data.redis.url`, for example:

```yaml
spring:
  data:
    redis:
      url: redis://localhost:6379
```

### Authorization token

Set `MTX_AUTH_TOKEN` before starting the service. The value of that environment variable becomes
the complete expected `Authorization` header value; it is not decoded or transformed as HTTP Basic
Authentication. The variable must be set when authorization is enabled.

```bash
export MTX_AUTH_TOKEN='replace-with-a-secret-token'
./gradlew bootRun
```

## HTTP API

When authorization is enabled, send the configured token exactly as the `Authorization` header.
The API uses query parameters for request data.

### Health check

```bash
curl 'http://localhost:3000/healthcheck'
```

### Store or update a value

```bash
curl -X POST \
  -H "Authorization: $MTX_AUTH_TOKEN" \
  'http://localhost:3000/kv/store?app_id=my-app&key=feature.flag&value=enabled'
```

Returns `200` with the previous and new values in `content`.

### Read one value

```bash
curl \
  -H "Authorization: $MTX_AUTH_TOKEN" \
  'http://localhost:3000/kv/get?app_id=my-app&key=feature.flag'
```

Returns `200` when found or `404` when the key does not exist.

### Read all values for an application

```bash
curl \
  -H "Authorization: $MTX_AUTH_TOKEN" \
  'http://localhost:3000/kv/get-all?app_id=my-app'
```

Returns an array of values in `content`, or `404` when no values exist.

### Response envelope

API endpoints return JSON with this shape:

```json
{
  "requestUri": "/kv/get?app_id=my-app&key=feature.flag",
  "response": "200 - OK",
  "statusCode": 200,
  "message": "Value successfully retrieved",
  "timestamp": "2026-10-02T12:00:00Z",
  "content": {
    "appId": "my-app",
    "key": "feature.flag",
    "value": "enabled",
    "lastModifiedTimestamp": "2026-10-02T11:59:00Z"
  }
}
```

### Send an RCON command

RCON uses the standard Source RCON protocol. Enable it and set the destination and password
before starting the service:

```bash
export MTX_RCON_PASSWORD='replace-with-the-rcon-password'
./gradlew bootRun
```

Then send a command using the same API authorization as the other endpoints:

```bash
curl -X POST \
  -H "Authorization: $MTX_AUTH_TOKEN" \
  --data-urlencode 'message=say Hello from MtxKeyStore' \
  'http://localhost:3000/rcon/send'
```

The RCON response is returned in `content`. The endpoint returns `503` when disabled and `502`
when the configured destination cannot authenticate or be reached.

Common status codes are `200` for success, `400` for missing parameters, `403` for missing or
invalid authorization, and `404` for missing keys or application values.

## Persistence model

The process loads configured backends at startup into the in-memory store. Writes update memory
immediately; the complete snapshot is written to enabled backends on the configured interval and
during shutdown. The in-memory store is therefore the request-time source of truth.

Valkey entries use the following key layout:

- application ID set: `mtxkvstore:apps`
- per-application hash: `mtxkvstore:app:<app_id>`

## Project layout

- `src/main/java/.../api` — HTTP handlers, processors, and response models
- `src/main/java/.../db` — PostgreSQL loading and snapshot persistence
- `src/main/java/.../nosql` — Valkey loading and snapshot persistence
- `src/main/resources/db/migration` — Flyway migrations
- `src/test` — unit tests

## Notes

- Values, keys, and application IDs are strings.
- The service has no delete endpoint.
- The configured authorization token is a development default; replace it before exposing the
  service outside a trusted environment.
