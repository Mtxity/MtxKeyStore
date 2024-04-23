package com.edavalos.mtx.keystore.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuerySelectKvBuilderTest {
    private QuerySelectKvBuilder queryBuilder;

    @Test
    public void testGetQuery_noRows() {
        queryBuilder = new QuerySelectKvBuilder(null);

        String expected = "SELECT * FROM \"MtxKvStore\".kv;";
        String actual = queryBuilder.getQuery();
        assertEquals(expected, actual);
    }
}
