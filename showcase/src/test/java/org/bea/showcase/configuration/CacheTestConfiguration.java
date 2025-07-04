package org.bea.showcase.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Profile("test")
@TestConfiguration
public class CacheTestConfiguration {

    @Bean
    public CacheManager testCacheManager() {
        return new CacheManager() {
            @Override
            public Cache getCache(String name) {
                Cache mockCache = mock(Cache.class);
                when(mockCache.get(any())).thenReturn(null);
                return mockCache;
            }

            @Override
            public Collection<String> getCacheNames() {
                return Collections.emptyList();
            }
        };
    }
}
