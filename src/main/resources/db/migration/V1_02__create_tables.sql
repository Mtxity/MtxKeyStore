CREATE TABLE "MtxKvStore".app (
    app_db_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    appId VARCHAR NOT NULL
);

CREATE TABLE "MtxKvStore".keystore (
    kvStore_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    app_id VARCHAR NOT NULL,
    key VARCHAR NOT NULL,
    val VARCHAR NOT NULL
);

CREATE TABLE "MtxKvStore".timestamps (
    ts_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    app_id VARCHAR NOT NULL,
    key VARCHAR NOT NULL,
    lastSetVar VARCHAR NOT NULL
);