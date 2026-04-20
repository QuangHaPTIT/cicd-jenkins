package com.kaopiz.QLNCC.constants;

import java.util.Locale;

public final class GlobalConstants {
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Locale JAPANESE_LOCALE =
            new Locale.Builder().setLanguage("ja").setRegion("JP").build();

    public static final Locale ENGLISH_LOCALE =
            new Locale.Builder().setLanguage("en").setRegion("US").build();

    public static final String UTC_ZONE_ID = "UTC";
    public static final String ASIA_TOKYO_ZONE_ID = "Asia/Tokyo";

    public static final Long MAIN_VENDOR_ID = 1L;

    public static final int MAX_SCHEDULES_PER_GROUP = 6;

    private GlobalConstants() {

        throw new UnsupportedOperationException();
    }
}
