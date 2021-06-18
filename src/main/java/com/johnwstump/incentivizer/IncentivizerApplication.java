package com.johnwstump.incentivizer;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.PropertyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@PropertySource("application.properties")
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
    public DataSource securityDataSource() throws PropertyVetoException {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        securityDataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
        securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        securityDataSource.setUser(env.getProperty("jdbc.user"));
        securityDataSource.setPassword(env.getProperty("jdbc.password"));

        securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

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
