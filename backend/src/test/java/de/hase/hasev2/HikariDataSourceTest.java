package de.hase.hasev2;

import de.hase.hasev2.config.HikariService;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HikariDataSourceTest {
    @Autowired
    private HikariService hikariService;

    @Test
    void testGetDataSource_ShouldReturnValidDataSource() {
        var dataSource = hikariService.getDataSource();
        assertNotNull(dataSource);

        assertDoesNotThrow(() -> DSL.using(dataSource.getConnection()));
    }
}
