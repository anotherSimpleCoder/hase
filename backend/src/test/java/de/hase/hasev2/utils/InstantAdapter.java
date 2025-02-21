package de.hase.hasev2.utils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class InstantAdapter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @ToJson
    String toJson(Instant instant) {
        return FORMATTER.format(instant);
    }

    @FromJson
    Instant fromJson(String json) {
        return Instant.parse(json).truncatedTo(ChronoUnit.NANOS); // Ensure nanosecond precision
    }
}
