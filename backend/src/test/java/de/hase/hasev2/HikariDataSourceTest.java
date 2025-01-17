package de.hase.hasev2;
import de.hase.hasev2.config.DatabaseConfig;
import org.junit.jupiter.api.Test;




public class HikariDataSourceTest {

    @Test
    void testGetDataSource_ShouldReturnValidDataSource() {
        assert (DatabaseConfig.getDataSource() != null);

    }
}
