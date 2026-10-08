package com.edavalos.mtx.keystore.db;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryDeleteKvBuilderTest {
    @Test
    public void testGetQuery() {
        QueryDeleteKvBuilder queryBuilder = new QueryDeleteKvBuilder(null, "123", "k1");

        assertEquals(
                "DELETE FROM \"MtxKvStore\".kv WHERE app_id = ? AND key = ?;",
                queryBuilder.getQuery());
    }
}
