package com.kevinj.portfolio.issuetrack.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.url}")
    private String redisUrl;

    /**
     * 1. Redis Connection Factory 설정
     * application.yml의 URL을 파싱하여 호스트, 포트, 패스워드 및 SSL(rediss) 여부를 판별합니다.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        URI uri = URI.create(redisUrl);
        String host = uri.getHost();
        int port = uri.getPort();

        // userInfo 부분에서 패스워드 추출 (format: default:password)
        String userInfo = uri.getUserInfo();
        String password = (userInfo != null && userInfo.contains(":")) ? userInfo.split(":")[1] : null;

        // Redis 단일 서버 설정
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        if (password != null && !password.isEmpty()) {
            redisStandaloneConfiguration.setPassword(password);
        }

        // Lettuce 클라이언트 설정 빌더 (SSL 프로토콜인 경우 보안 옵션 활성화)
        LettuceClientConfiguration.LettuceClientConfigurationBuilder clientConfigBuilder = LettuceClientConfiguration.builder();

        if ("rediss".equalsIgnoreCase(uri.getScheme())) {
            clientConfigBuilder.useSsl(); // SSL/TLS 적용 (Upstash 필수)
        }

        LettuceClientConfiguration lettuceClientConfiguration = clientConfigBuilder.build();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    /**
     * 2. RedisTemplate 설정
     * 자바 애플리케이션과 Redis 간 데이터 전송 시 키와 값이 깨지지 않도록 String 직렬화를 적용합니다.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 일반 Key-Value 직렬화
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // Hash 자료구조를 사용할 때의 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
