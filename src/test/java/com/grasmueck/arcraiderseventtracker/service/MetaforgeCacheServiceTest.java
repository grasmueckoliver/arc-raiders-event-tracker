package com.grasmueck.arcraiderseventtracker.service;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventEntity;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MetaforgeCacheService Tests")
class MetaforgeCacheServiceTest {

    @Mock
    private MetaforgeClient metaforgeClient;

    @Mock
    private MetaforgeEventRepository repository;

    private MetaforgeCacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new MetaforgeCacheService(metaforgeClient, repository);
    }

    @Test
    @DisplayName("Should retrieve all events as DTOs")
    void testGetAllEventsAsDto_Success() {
        // Arrange
        MetaforgeEventEntity entity1 = MetaforgeEventEntity.builder()
                .name("Bird City")
                .icon("icon1")
                .mapName("Buried City")
                .startTime(Instant.ofEpochMilli(1000000))
                .endTime(Instant.ofEpochMilli(2000000))
                .build();

        MetaforgeEventEntity entity2 = MetaforgeEventEntity.builder()
                .name("Nightfall")
                .icon("icon2")
                .mapName("Bayside")
                .startTime(Instant.ofEpochMilli(3000000))
                .endTime(Instant.ofEpochMilli(4000000))
                .build();

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        // Act
        List<MetaforgeEventDto> result = cacheService.getAllEventsAsDto();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bird City", result.get(0).name());
        assertEquals("Nightfall", result.get(1).name());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no events exist")
    void testGetAllEventsAsDto_EmptyDatabase() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of());

        // Act
        List<MetaforgeEventDto> result = cacheService.getAllEventsAsDto();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should handle null startTime gracefully")
    void testGetAllEventsAsDto_NullStartTime() {
        // Arrange
        MetaforgeEventEntity entity = MetaforgeEventEntity.builder()
                .name("Bird City")
                .icon("icon1")
                .mapName("Buried City")
                .startTime(null)
                .endTime(Instant.ofEpochMilli(2000000))
                .build();

        when(repository.findAll()).thenReturn(List.of(entity));

        // Act
        List<MetaforgeEventDto> result = cacheService.getAllEventsAsDto();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0L, result.getFirst().startTime());
    }

    @Test
    @DisplayName("Should handle null endTime gracefully")
    void testGetAllEventsAsDto_NullEndTime() {
        // Arrange
        MetaforgeEventEntity entity = MetaforgeEventEntity.builder()
                .name("Bird City")
                .icon("icon1")
                .mapName("Buried City")
                .startTime(Instant.ofEpochMilli(1000000))
                .endTime(null)
                .build();

        when(repository.findAll()).thenReturn(List.of(entity));

        // Act
        List<MetaforgeEventDto> result = cacheService.getAllEventsAsDto();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0L, result.getFirst().endTime());
    }

    @Test
    @DisplayName("Should refresh cache successfully")
    void testRefreshCache_Success() {
        // Arrange
        MetaforgeEventDto dto = new MetaforgeEventDto("Bird City", "icon1", 1000000, 2000000, "Buried City");
        MetaforgeResponseDto response = new MetaforgeResponseDto(List.of(dto), 1000000);

        when(metaforgeClient.collectAllEvents()).thenReturn(response);
        when(repository.findByNameAndMapNameAndStartTime(any(), any(), any()))
                .thenReturn(Optional.empty());

        // Act
        cacheService.refreshCache();

        // Assert
        verify(metaforgeClient, times(1)).collectAllEvents();
        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("Should handle null response from API")
    void testRefreshCache_NullResponse() {
        // Arrange
        when(metaforgeClient.collectAllEvents()).thenReturn(null);

        // Act
        cacheService.refreshCache();

        // Assert
        verify(metaforgeClient, times(1)).collectAllEvents();
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should handle null data in response")
    void testRefreshCache_NullDataInResponse() {
        // Arrange
        MetaforgeResponseDto response = new MetaforgeResponseDto(null, 1000000);
        when(metaforgeClient.collectAllEvents()).thenReturn(response);

        // Act
        cacheService.refreshCache();

        // Assert
        verify(metaforgeClient, times(1)).collectAllEvents();
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should upsert existing events")
    void testRefreshCache_UpdateExistingEvent() {
        // Arrange
        MetaforgeEventDto dto = new MetaforgeEventDto("Bird City", "icon1", 1000000, 2000000, "Buried City");
        MetaforgeResponseDto response = new MetaforgeResponseDto(List.of(dto), 1000000);

        MetaforgeEventEntity existingEntity = MetaforgeEventEntity.builder()
                .name("Bird City")
                .icon("old_icon")
                .mapName("Buried City")
                .startTime(Instant.ofEpochMilli(1000000))
                .endTime(Instant.ofEpochMilli(2000000))
                .build();

        when(metaforgeClient.collectAllEvents()).thenReturn(response);
        when(repository.findByNameAndMapNameAndStartTime(any(), any(), any()))
                .thenReturn(Optional.of(existingEntity));

        // Act
        cacheService.refreshCache();

        // Assert
        verify(metaforgeClient, times(1)).collectAllEvents();
        verify(repository, times(1)).saveAll(anyList());
    }
}
