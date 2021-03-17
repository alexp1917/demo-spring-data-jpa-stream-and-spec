package com.benjamintanone.springdatajpademojava;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;


@Import(PersistenceTestConfiguration.class)
@SpringBootTest
@Slf4j
class SpringDataJpaDemoJavaApplicationTests {

	@Autowired
	DataSource dataSource;

	@Test
	void contextLoads() {
	}

	@SneakyThrows
	@Test
	public void testMysqlFunctionsLoadedInH2() {
		Connection connection = dataSource.getConnection();
		ResultSet rsSimpleStr = connection.createStatement().executeQuery("select 'hello' as a");
		rsSimpleStr.next();
		String simpleString = rsSimpleStr.getString(1);
		log.trace(simpleString);

		ResultSet rsSimpleHash = connection.createStatement().executeQuery("select md5('hello') as a");
		rsSimpleHash.next();
		String simpleHash = rsSimpleHash.getString(1);
		log.info(simpleHash);

		assert simpleHash != null;
	}

}
