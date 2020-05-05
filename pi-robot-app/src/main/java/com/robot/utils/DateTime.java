package com.robot.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Lechu on 13.10.2016.
 */
public class DateTime {
    public static Date toDate(LocalDateTime startDate) {
        Instant instant = startDate.toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date toDateTime(LocalDateTime dataTime) {
        Instant instant = dataTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date now() {
        return toDateTime(LocalDateTime.now());
    }
}
