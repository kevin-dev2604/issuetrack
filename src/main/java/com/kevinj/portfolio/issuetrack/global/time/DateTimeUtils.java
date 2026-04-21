package com.kevinj.portfolio.issuetrack.global.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class DateTimeUtils {

    public static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DateTimeFormats.DEFAULT_DATE_TIME);

    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DateTimeFormats.DEFAULT_DATE);

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(DEFAULT_DATE_TIME_FORMATTER);
    }

    public static String format(LocalDate date) {
        return date.format(DEFAULT_DATE_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTime, ZoneId zoneId) {
        return LocalDateTime.parse(dateTime, DEFAULT_DATE_TIME_FORMATTER.withZone(zoneId == null ? ZoneId.systemDefault() : zoneId));
    }

    public static LocalDate parseDate(String date, ZoneId zoneId) {
        return LocalDate.parse(date, DEFAULT_DATE_FORMATTER.withZone(zoneId == null ? ZoneId.systemDefault() : zoneId));
    }

    public static boolean isParsable(String format, String period) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);


        try {
            if (format.equals(DateTimeFormats.DEFAULT_WEEK)) {
                formatter = new DateTimeFormatterBuilder()
                    .appendPattern(DateTimeFormats.DEFAULT_WEEK)
                    .parseDefaulting(ChronoField.DAY_OF_WEEK, 1) // 날짜 계산을 위해 월요일 강제 지정
                    .toFormatter();
            }

            formatter.parse(period);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
