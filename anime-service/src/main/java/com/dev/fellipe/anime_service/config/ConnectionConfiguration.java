package com.dev.fellipe.anime_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfiguration {
    @Value("${database.url}")
    private String url;
    @Value("${database.usernmae}")
    private String username;
    @Value("${database.password}")
    private String password;

    @Bean
    @Primary
    public Connection connectionMySql() {
        return new Connection(url, username, password);
    }

    @Bean(name = "connectionMongoDB")
//    @Primary
    public Connection connectionMongo() {
        return new Connection("localhost", "devdojoMongo", "goku");
    }
}
