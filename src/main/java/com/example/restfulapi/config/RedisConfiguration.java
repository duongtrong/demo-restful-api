package com.example.restfulapi.config;

import com.example.restfulapi.dto.Data;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource(value = "classpath:redis.properties")
public class RedisConfiguration extends CachingConfigurerSupport {

  @Value("${redis.hostName}")
  private String hostName;
  @Value("${redis.port}")
  private int port;
  @Value("${redis.timeout}")
  private int timeout;
  @Value("${redis.maxIdle}")
  private int maxIdle;
  @Value("${redis.maxTotal}")
  private int maxTotal;
  @Value("${redis.maxWaitMillis}")
  private int maxWaitMillis;
  @Value("${redis.blockWhenExhausted}")
  private boolean blockWhenExhausted;
  @Value("${redis.testOnBorrow}")
  private boolean testOnBorrow;

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration(hostName, port);
    JedisClientConfiguration.JedisPoolingClientConfigurationBuilder
        jedisPoolConfigBuilder = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)
        JedisClientConfiguration.builder();

    GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
    genericObjectPoolConfig.setMaxIdle(maxIdle);
    genericObjectPoolConfig.setMaxTotal(maxTotal);
    genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
    genericObjectPoolConfig.setTestOnBorrow(testOnBorrow);
    jedisPoolConfigBuilder.poolConfig(genericObjectPoolConfig);

    return new JedisConnectionFactory(redisStandaloneConfig, jedisPoolConfigBuilder.build());
  }

  @Bean
  public RedisTemplate<String, Data> redisTemplate() {
    RedisTemplate<String, Data> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(jedisConnectionFactory());

    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();

    redisTemplate.setKeySerializer(stringRedisSerializer);
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.setHashKeySerializer(stringRedisSerializer);
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
