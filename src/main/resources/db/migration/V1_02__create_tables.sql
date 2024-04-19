CREATE TABLE "MtxKvStore".kv (
    kv_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    app_id VARCHAR NOT NULL REFERENCES app(app_id),
    key VARCHAR NOT NULL,
    val VARCHAR NOT NULL,
    lastSetTimestamp VARCHAR NOT NULL
);

CREATE TABLE "MtxKvStore".app (
    app_id VARCHAR NOT NULL PRIMARY KEY,
    createdTimestamp VARCHAR NOT NULL
)