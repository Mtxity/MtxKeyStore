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
| `api.authToken` | development token | Shared authorization token |
| `api.includeTimestampsInGetAll` | `true` | Includes last-modified timestamps in bulk reads |
| `db.useDb` | `true` | Enables PostgreSQL load/save |
| `db.storeInterval` | `5` | Persistence interval in minutes |
| `db.insertAllAtOnce` | `true` | Writes the PostgreSQL snapshot in one statement |
| `nosql.useNosql` | `false` | Enables Valkey load/save |

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
  -H 'Authorization: basic e35acfe4e0b54d488b687a64914197ad' \
  'http://localhost:3000/kv/store?app_id=my-app&key=feature.flag&value=enabled'
```

Returns `200` with the previous and new values in `content`.

### Read one value

```bash
curl \
  -H 'Authorization: basic e35acfe4e0b54d488b687a64914197ad' \
  'http://localhost:3000/kv/get?app_id=my-app&key=feature.flag'
```

Returns `200` when found or `404` when the key does not exist.

### Read all values for an application

```bash
curl \
  -H 'Authorization: basic e35acfe4e0b54d488b687a64914197ad' \
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
