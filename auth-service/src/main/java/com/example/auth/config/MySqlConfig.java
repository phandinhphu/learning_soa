package com.example.auth.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:db.properties")
public class MySqlConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(env.getProperty("db.driverClassName"));
		config.setJdbcUrl(env.getProperty("db.url"));
		config.setUsername(env.getProperty("db.username"));
		config.setPassword(env.getProperty("db.password"));
		
		// Pool settings
		config.setMaximumPoolSize(Integer.parseInt(env.getProperty("jdbc.pool.size", "10")));
        config.setIdleTimeout(Long.parseLong(env.getProperty("jdbc.idleTimeout", "30000")));
        config.setMaxLifetime(Long.parseLong(env.getProperty("jdbc.maxLifetime", "1800000")));
        config.setConnectionTimeout(Long.parseLong(env.getProperty("jdbc.connectionTimeout", "30000")));
        
        return new HikariDataSource(config);
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
