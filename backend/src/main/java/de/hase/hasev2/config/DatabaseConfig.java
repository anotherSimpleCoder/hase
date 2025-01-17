package de.hase.hasev2.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {
    private static HikariDataSource dataSource;

    static {
        try {
            // Konfigurationsdatei laden
            HikariConfig config = new HikariConfig("src/main/resources/hikari.cfg");
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Laden der Hikari-Konfiguration", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}