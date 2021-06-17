package com.johnwstump.incentivizer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@SpringBootApplication
public class IncentivizerApplication {

    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(IncentivizerApplication.class, args);
    }

    @Autowired
    public IncentivizerApplication(Environment env){
        this.env = env;
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

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    private int getIntProperty(String propName) {
        String prop = env.getProperty(propName);

        if (prop == null) {
            throw new PropertyNotFoundException("Property " + propName + "is not defined.");
        }
        return Integer.parseInt(prop);
    }
}
