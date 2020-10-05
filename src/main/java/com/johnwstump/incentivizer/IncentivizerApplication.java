package com.johnwstump.incentivizer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.logging.Logger;

@SpringBootApplication
public class IncentivizerApplication {
    @Autowired
    private Environment env;

    private Logger logger = Logger.getLogger(getClass().getName());

    public static void main(String[] args) {
        SpringApplication.run(IncentivizerApplication.class, args);
    }

    @Bean
    public DataSource securityDataSource() {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        securityDataSource.setUser(env.getProperty("jdbc.user"));
        securityDataSource.setPassword(env.getProperty("jdbc.password"));

        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.minPoolSize"));
        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.maxIdleTime"));

        return securityDataSource;
    }

    private int getIntProperty(String propName) {
        String prop = env.getProperty(propName);

        if (prop == null) {
            throw new PropertyNotFoundException("Property " + propName + "is not defined.");
        }
        return Integer.parseInt(prop);
    }
}
