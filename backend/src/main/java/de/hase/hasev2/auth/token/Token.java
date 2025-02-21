package de.hase.hasev2.auth.token;

import java.time.Instant;

public record Token(
        String token,
        Instant expiresAt
) {}
