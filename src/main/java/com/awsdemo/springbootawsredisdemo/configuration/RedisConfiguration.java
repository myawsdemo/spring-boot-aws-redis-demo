package com.awsdemo.springbootawsredisdemo.configuration;

import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
public class RedisConfiguration extends CachingConfigurerSupport {

    @Bean
    public RedisConnectionFactory redisStandaloneConfiguration() {
        List<String> nodes = Collections.singletonList("mycluster-2.lfgqyw.clustercfg.use2.cache.amazonaws.com:6379");
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration(nodes);
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                .enableAllAdaptiveRefreshTriggers()
                .build();
        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
                .validateClusterNodeMembership(false)
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(15))
                .shutdownTimeout(Duration.ZERO)
                .clientOptions(clusterClientOptions)
                .build();
        return new LettuceConnectionFactory(clusterConfiguration, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory lettuceConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);

        /**必须执行这个函数,初始化RedisTemplate*/
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


}
