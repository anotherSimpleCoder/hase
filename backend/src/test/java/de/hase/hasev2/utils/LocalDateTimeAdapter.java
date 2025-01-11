package de.hase.hasev2.utils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @ToJson
    String toJson(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    @FromJson
    LocalDateTime fromJson(String json) {
        return LocalDateTime.parse(json, FORMATTER);
    }
}
