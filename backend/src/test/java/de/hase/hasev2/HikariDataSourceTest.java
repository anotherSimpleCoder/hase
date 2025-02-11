package de.hase.hasev2;

import de.hase.hasev2.config.HikariService;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
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
