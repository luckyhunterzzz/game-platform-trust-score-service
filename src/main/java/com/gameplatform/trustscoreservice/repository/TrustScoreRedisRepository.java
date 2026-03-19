package com.gameplatform.trustscoreservice.repository;

import com.gameplatform.trustscoreservice.domain.redis.TrustScoreSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TrustScoreRedisRepository {

    private static final String KEY_PREFIX = "trust-score:user:";

    private final RedisTemplate<String, TrustScoreSnapshot> redisTemplate;

    public void save(TrustScoreSnapshot snapshot) {
        String key = KEY_PREFIX + snapshot.getUserId();
        redisTemplate.opsForValue().set(key, snapshot);
    }
}
