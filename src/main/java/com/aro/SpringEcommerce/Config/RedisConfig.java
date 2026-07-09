package com.aro.SpringEcommerce.Config;

import com.aro.SpringEcommerce.Cart.Cart;
import com.aro.SpringEcommerce.Entity.AppUser;
import com.aro.SpringEcommerce.Entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
        configs.put("products", RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new JacksonJsonRedisSerializer<>(Product.class)))
                .entryTtl(Duration.ofMinutes(10))
        );
        configs.put("users", RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair.fromSerializer(new JacksonJsonRedisSerializer<>(AppUser.class)))
                .entryTtl(Duration.ofMinutes(5))
        );

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(configs)
                .build();
    }

    @Bean
    public RedisTemplate<String, Cart> cartRedisTemplate(
            RedisConnectionFactory factory) {

        RedisTemplate<String, Cart> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(
                new JacksonJsonRedisSerializer<>(Cart.class)
        );

        template.afterPropertiesSet();

        return template;
    }
}