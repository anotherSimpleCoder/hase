package de.hase.hasev2;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
class Hasev2ApplicationTests {

	@Test
	void contextLoads() {}

	@Test
	void databaseConnected() throws Exception {
		Connection connection = DriverManager.getConnection("jdbc:sqlite:./database/database.sqlite");
		DSLContext dbContext = DSL.using(connection);
	}
}
