package se.metria.markkoll.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CachingService {
    @CacheEvict(value = "kartor", allEntries=true)
    public void evictKartorCache() {
    };
}
