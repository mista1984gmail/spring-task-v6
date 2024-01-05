package ru.clevertec.servlet.task.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.clevertec.servlet.task.config.LoadProperties;

import java.sql.Connection;
import java.sql.SQLException;

import static ru.clevertec.servlet.task.util.Constants.DATABASE_PASSWORD;
import static ru.clevertec.servlet.task.util.Constants.DATABASE_URL;
import static ru.clevertec.servlet.task.util.Constants.DATABASE_USERNAME;


public class DataSource {
	private final static String url = LoadProperties.getProperties().getProperty(DATABASE_URL);
	private final static String username = LoadProperties.getProperties().getProperty(DATABASE_USERNAME);
	private final static String password = LoadProperties.getProperties().getProperty(DATABASE_PASSWORD);
	private static HikariConfig config = new HikariConfig();
	private static HikariDataSource ds;

	static {
		config.setJdbcUrl( url );
		config.setUsername( username );
		config.setPassword( password );
		config.addDataSourceProperty( "cachePrepStmts" , "true" );
		config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
		config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
		ds = new HikariDataSource( config );
	}

	private DataSource() {}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
