package com.rackspacecloud.metrics.tenantroutingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@Configuration
public class RoutingConfiguration {
    @Value("${redis.hostname}")
    private String hostName;

    @Value("${redis.port}")
    private int port;

    @Bean
    RedisStandaloneConfiguration redisStandaloneConfiguration(){
        return new RedisStandaloneConfiguration(hostName, port);
    }

    @Bean
    LettuceConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisStandaloneConfiguration());
    }

    @Bean(name = "redisTemplateTenantMeasurements")
    RedisTemplate<String, Set<String>> redisTemplateTenantMeasurements() {
        RedisTemplate<String, Set<String>> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
