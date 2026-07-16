package com.grasmueck.arcraiderseventtracker.service;

import com.grasmueck.arcraiderseventtracker.client.MetaforgeClient;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventEntity;
import com.grasmueck.arcraiderseventtracker.persistence.MetaforgeEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for caching and retrieving event data from Metaforge.
 * Handles external API synchronization and conversion of entities to DTOs.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MetaforgeCacheService {

    private final MetaforgeClient metaforgeClient;
    private final MetaforgeEventRepository repository;

    @Transactional
    public void refreshCache() {
        MetaforgeResponseDto response = metaforgeClient.collectAllEvents();
        if (response == null || response.data() == null) {
            log.warn("No data returned from MetaforgeClient");
            return;
        }

        List<MetaforgeEventEntity> entityToSave = new ArrayList<>();
        Instant now = Instant.now();

        for (MetaforgeEventDto dto : response.data()) {
            Instant startInstant = Instant.ofEpochMilli(dto.startTime());
            Instant endInstant = Instant.ofEpochMilli(dto.endTime());

            MetaforgeEventEntity entity = repository.findByNameAndMapNameAndStartTime(
                    dto.name(), dto.map(), startInstant).orElseGet(() -> MetaforgeEventEntity.builder().name(
                            dto.name()).mapName(dto.map()).startTime(startInstant).build());

            entity.setName(dto.name());
            entity.setIcon(dto.icon());
            entity.setMapName(dto.map());
            entity.setStartTime(startInstant);
            entity.setEndTime(endInstant);
            entity.setLastUpdated(now);

            entityToSave.add(entity);
        }

        repository.saveAll(entityToSave);
        log.info("Refreshed {} events into DB", entityToSave.size());
    }

    /**
     * Retrieves all events from the database as DTOs.
     *
     * @return list of all events as MetaforgeEventDto
     */
    public List<MetaforgeEventDto> getAllEventsAsDto() {
        return repository.findAll().stream()
                .map(e -> new MetaforgeEventDto(
                        e.getName(),
                        e.getIcon(),
                        e.getStartTime() != null ? e.getStartTime().toEpochMilli() : 0L,
                        e.getEndTime() != null ? e.getEndTime().toEpochMilli() : 0L,
                        e.getMapName()))
                .collect(Collectors.toList());
    }
}
