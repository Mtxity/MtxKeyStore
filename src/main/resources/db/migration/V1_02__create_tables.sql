CREATE TABLE "MtxKvStore".kv (
    app_id VARCHAR NOT NULL REFERENCES app(app_id),
    key VARCHAR NOT NULL,
    val VARCHAR NOT NULL,
    lastSetTimestamp VARCHAR NOT NULL,
    PRIMARY KEY (app_id, key)
);

CREATE TABLE "MtxKvStore".app (
    app_id VARCHAR NOT NULL PRIMARY KEY,
    createdTimestamp VARCHAR NOT NULL
)