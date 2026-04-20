package com.kaopiz.QLNCC.utils;

import java.util.regex.Pattern;

public final class RegexPatternUtils {

    public static final char LIKE_ESCAPE_CHAR = '\\';

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("([\\\\%_])");
    private static final String REGEX_REPLACEMENT = "\\\\$1";

    private RegexPatternUtils() {
    }

    public static String escapeLikePattern(String value) {
        if (value == null) {
            return null;
        }
        return ESCAPE_PATTERN.matcher(value).replaceAll(REGEX_REPLACEMENT);
    }
}