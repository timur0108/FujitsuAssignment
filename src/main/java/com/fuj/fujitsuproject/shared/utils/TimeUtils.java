package com.fuj.fujitsuproject.shared.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class TimeUtils {

    public static LocalDateTime convertUnix2LocalDateTime(Long time) {
        return LocalDateTime
                .ofInstant(Instant.ofEpochSecond(time),
                        TimeZone.getDefault().toZoneId());
    }
}
