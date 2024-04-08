package com.edavalos.mtx.keystore;

import com.edavalos.mtx.keystore.config.SpringConfigLoader;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public final class Util {
    private Util() { }

    /**
     * Gets the current system's timestamp in UTC time with milliseconds precision
     * @return now at UTC as a formatted timestamp string
     */
    public static String getTimestamp() {
        return ZonedDateTime
                .now(SpringConfigLoader.getTimezone())
                .truncatedTo(ChronoUnit.MILLIS)
                .format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * Checks if this string is empty
     * @param s string to check
     * @return true if this string is null or of length 0, false otherwise
     */
    public static boolean isEmpty(String s) {
        if (s == null) {
            return true;
        }

        return s.isEmpty();
    }

    /**
     * Checks if this string is blank
     * @param s string to check
     * @return true if this string is null or only contains whitespaces, false otherwise
     */
    public static boolean isBlank(String s) {
        if (isEmpty(s)) {
            return true;
        }

        return s.isBlank();
    }
}
