package de.hase.hasev2.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record KeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}
