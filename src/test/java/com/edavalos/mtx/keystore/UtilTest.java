package com.edavalos.mtx.keystore;

import com.edavalos.mtx.keystore.api.ApiConst;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTest {

    @Test
    public void testGetTimestamp() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS);

        // Need to ignore milliseconds otherwise test won't finish in time for results to match exactly
        String nowAdjustedExpected = now.format(DateTimeFormatter.ISO_DATE_TIME).split("\\.")[0];
        String nowAdjustedActual = Util.getTimestamp().split("\\.")[0];

        assertEquals(nowAdjustedExpected, nowAdjustedActual);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(Util.isEmpty(null));
        assertTrue(Util.isEmpty(""));
        assertFalse(Util.isEmpty("valid string"));
        assertFalse(Util.isEmpty("   "));
    }

    @Test
    public void testIsBlank() {
        assertTrue(Util.isBlank(""));
        assertTrue(Util.isBlank("   "));
        assertFalse(Util.isBlank("valid string"));
    }

    @Nested
    class GetMissingParamsTests {

        @Test
        public void testGetMissingParams_noMissingParams() {
            Map<String, String> params = Map.of(
                    "xk", "xv",
                    "yk", "yv"
            );

            String[] missing = Util.getMissingParams(params);
            assertEquals(0, missing.length);
        }

        @Test
        public void testGetMissingParams_blankParam() {
            Map<String, String> params = Map.of(
                    "xk", " ",
                    "yk", "yv"
            );

            String[] missing = Util.getMissingParams(params);
            assertEquals(1, missing.length);
            assertEquals("xk", missing[0]);
        }

        @Test
        public void testGetMissingParams_defaultParam() {
            Map<String, String> params = Map.of(
                    "xk", "xv",
                    "yk", ApiConst.DEFAULT_PARAM_STR
            );

            String[] missing = Util.getMissingParams(params);
            assertEquals(1, missing.length);
            assertEquals("yk", missing[0]);
        }
    }
}
