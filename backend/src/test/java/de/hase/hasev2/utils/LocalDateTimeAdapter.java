package de.hase.hasev2.utils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC);

    @ToJson
    String toJson(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    @FromJson
    LocalDateTime fromJson(String json) {
        return LocalDateTime.parse(json, FORMATTER);
    }
}
