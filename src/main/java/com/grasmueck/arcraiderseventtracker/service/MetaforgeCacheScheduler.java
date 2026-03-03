package com.grasmueck.arcraiderseventtracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler that periodically refreshes the Metaforge cache.
 * Configurable via properties: metaforge.cache.enabled, metaforge.cache.initialDelayMs, metaforge.cache.fixedDelayMs
 */
@Component
@ConditionalOnProperty(name = "metaforge.cache.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class MetaforgeCacheScheduler {

    private final MetaforgeCacheService cacheService;

    @Scheduled(initialDelayString = "${metaforge.cache.initialDelayMs:5000}",
               fixedDelayString = "${metaforge.cache.fixedDelayMs:300000}")
    public void scheduledRefresh() {
        try {
            log.debug("Starting scheduled Metaforge cache refresh");
            cacheService.refreshCache();
            log.debug("Finished scheduled Metaforge cache refresh");
        } catch (Exception e) {
            log.error("Failed scheduled Metaforge cache refresh", e);
        }
    }
}
