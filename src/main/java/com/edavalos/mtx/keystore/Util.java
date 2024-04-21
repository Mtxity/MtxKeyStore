package com.edavalos.mtx.keystore;

import com.edavalos.mtx.keystore.api.ApiConst;
import com.edavalos.mtx.keystore.config.SpringConfigLoader;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Returns any parameters that are either null, blank, or default. A parameter is considered to be default when its
     * value equals {@value com.edavalos.mtx.keystore.api.ApiConst#DEFAULT_PARAM_STR}
     * @param params Map containing each parameter name, and their provided values
     * @return A string array with all the parameter names that had corresponding blank or default values
     */
    public static String[] getMissingParams(Map<String, String> params) {
        List<String> missingParams = new ArrayList<>();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (isBlank(param.getValue()) || param.getValue().equals(ApiConst.DEFAULT_PARAM_STR)) {
                missingParams.add(param.getKey());
            }
        }
        return missingParams.toArray(new String[0]);
    }

    public static String getConcat(String delimiter, String s1, String s2) {
        return s1 + delimiter + s2;
    }

    public static String getConcat(String delimiter, String... strings) {
        if (strings.length == 2) {
            return getConcat(delimiter, strings[0], strings[1]);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length - 1; i++) {
            sb.append(strings[i]).append(delimiter);
        }
        return sb.append(strings[strings.length - 1]).toString();
    }

    public static String getConcat(String s1, String s2) {
        return getConcat(" ", s1, s2);
    }
}
