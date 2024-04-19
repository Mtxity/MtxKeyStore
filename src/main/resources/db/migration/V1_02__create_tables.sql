CREATE TABLE "MtxKvStore".kv (
    kv_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    app_id VARCHAR NOT NULL,
    key VARCHAR NOT NULL,
    val VARCHAR NOT NULL,
    lastSetTime VARCHAR NOT NULL
);