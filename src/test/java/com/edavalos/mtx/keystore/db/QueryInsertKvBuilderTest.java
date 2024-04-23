package com.edavalos.mtx.keystore.db;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryInsertKvBuilderTest {
    private QueryInsertKvBuilder queryBuilder;

    @Test
    public void testGetQuery_noRows() {
        queryBuilder = new QueryInsertKvBuilder(null, List.of());

        String expected = "";
        String actual = queryBuilder.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_oneRow() {
        queryBuilder = new QueryInsertKvBuilder(null, List.of(
                new KvRow("123", "k1", "v1", "now")
        ));

        String expected = "INSERT INTO \"MtxKvStore\".kv (" +
                "   app_id, key, val, lastSetTimestamp " +
                ") VALUES " +
                "('123', 'k1', 'v1', 'now');";
        String actual = queryBuilder.getQuery();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetQuery_multipleRows() {
        queryBuilder = new QueryInsertKvBuilder(null, List.of(
                new KvRow("123", "k1", "v1", "now"),
                new KvRow("123", "k2", "v2", "now"),
                new KvRow("123", "k3", "v3", "now"),
                new KvRow("123", "k4", "v4", "now")
        ));

        String expected = "INSERT INTO \"MtxKvStore\".kv (" +
                "   app_id, key, val, lastSetTimestamp " +
                ") VALUES " +
                "('123', 'k1', 'v1', 'now'), " +
                "('123', 'k2', 'v2', 'now'), " +
                "('123', 'k3', 'v3', 'now'), " +
                "('123', 'k4', 'v4', 'now');";
        String actual = queryBuilder.getQuery();
        assertEquals(expected, actual);
    }
}
