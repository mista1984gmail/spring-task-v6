package ru.clevertec.servlet.task.config.liquibase;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.clevertec.servlet.task.util.Constants;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

	@Value("${spring.jdbc.driver_class}")
	private String databaseDriver;

	@Value("${spring.jdbc.connection.url}")
	private String databaseUrl;

	@Value("${spring.jdbc.connection.username}")
	private String databaseUsername;

	@Value("${spring.jdbc.connection.password}")
	private String databasePassword;

	@Value("${spring.liquibase.enabled}")
	private String liquibaseEnabled;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(databaseDriver);
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername(databaseUsername);
		dataSource.setPassword(databasePassword);

		return dataSource;
	}

	@Bean
	public SpringLiquibase liquibase() {

		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setShouldRun(Boolean.parseBoolean(liquibaseEnabled));
		liquibase.setChangeLog(Constants.LIQUIBASE_CHANGE_LOG);
		liquibase.setDataSource(dataSource());

		return liquibase;
	}

}
