CREATE TABLE "MtxKvStore".app (
    app_id VARCHAR NOT NULL PRIMARY KEY,
    created_timestamp VARCHAR NOT NULL
);

CREATE TABLE "MtxKvStore".kv (
    app_id VARCHAR NOT NULL REFERENCES "MtxKvStore".app(app_id),
    key VARCHAR NOT NULL,
    val VARCHAR NOT NULL,
    last_set_timestamp VARCHAR NOT NULL,
    PRIMARY KEY (app_id, key)
);