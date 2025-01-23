package de.hase.hasev2.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class HikariService {
    private final HikariDataSource dataSource;

    @Autowired
    public HikariService(Environment environment) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        hikariConfig.setJdbcUrl(environment.getProperty("spring.datasource.url"));

        dataSource = new HikariDataSource(hikariConfig);
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}