package com.rackspacecloud.metrics.tenantroutingservice.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//@Repository
public class TenantMeasurementsRepository {

    RedisTemplate<String, Set<String>> redisTemplate;

    private static final String KEY = "tenant-measurements";

    private HashOperations<String, String, Set<String>> hashOperations;

//    @Autowired
    public TenantMeasurementsRepository(
            @Qualifier("redisTemplateTenantMeasurements") RedisTemplate<String, Set<String>> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
    }

    public void addMeasurement(String tenantId, String measurement) {
        if(!hashOperations.hasKey(KEY, tenantId)) {
            hashOperations.put(KEY, tenantId, new HashSet<>(Arrays.asList(measurement)));
            return;
        }

        Set<String> measurements = hashOperations.get(KEY, tenantId);
        measurements.add(measurement);

        hashOperations.put(KEY, tenantId, measurements);
    }
}
