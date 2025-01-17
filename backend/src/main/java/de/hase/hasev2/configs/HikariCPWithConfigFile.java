package de.hase.hasev2.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariCPWithConfigFile {

    public static void main(String[] args) {
        // Konfigurationsdatei laden
        HikariConfig config = new HikariConfig("hikari.cfg");

        // DataSource erstellen
        DataSource dataSource = new HikariDataSource(config);

        // Verbindung testen
        try (var connection = dataSource.getConnection()) {
            System.out.println("Erfolgreich verbunden!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}