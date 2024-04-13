CREATE TABLE "MtxKvStore".app (
    app_db_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    appId VARCHAR NOT NULL
);