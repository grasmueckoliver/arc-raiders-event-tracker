package com.grasmueck.arcraiderseventtracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MetaforgeCacheScheduler Tests")
class MetaforgeCacheSchedulerTest {

    @Mock
    private MetaforgeCacheService cacheService;

    private MetaforgeCacheScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new MetaforgeCacheScheduler(cacheService);
    }

    @Test
    @DisplayName("scheduledRefresh should call refreshCache")
    void testScheduledRefresh_CallsRefresh() {
        scheduler.scheduledRefresh();
        verify(cacheService, times(1)).refreshCache();
    }

    @Test
    @DisplayName("scheduledRefresh should swallow exceptions")
    void testScheduledRefresh_ExceptionHandled() {
        doThrow(new RuntimeException("boom")).when(cacheService).refreshCache();
        scheduler.scheduledRefresh();
        // if no exception is thrown here the method swallows the exception
        verify(cacheService, times(1)).refreshCache();
    }
}
