package de.hase.hasev2;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
@SpringBootTest
class Hasev2ApplicationTests {

	@Test
	void contextLoads() {}

	@Test
	void databaseConnected() throws Exception {
		String dbPath = new File("database/database.sqlite").getAbsolutePath();
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
		DSLContext dbContext = DSL.using(connection);
	}
}
