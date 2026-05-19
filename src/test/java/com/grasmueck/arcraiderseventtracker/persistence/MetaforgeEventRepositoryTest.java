package com.grasmueck.arcraiderseventtracker.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("MetaforgeEventRepository Tests")
class MetaforgeEventRepositoryTest {

    @Autowired
    private MetaforgeEventRepository repository;

    @Test
    @DisplayName("should save and find by composite unique fields")
    void testSaveAndFindByNameMapStartTime() {
        MetaforgeEventEntity entity = MetaforgeEventEntity.builder()
                .name("Test Event")
                .icon("icon")
                .mapName("TestMap")
                .startTime(Instant.ofEpochMilli(123456789L))
                .endTime(Instant.ofEpochMilli(123456999L))
                .lastUpdated(Instant.now())
                .build();

        MetaforgeEventEntity saved = repository.save(entity);
        assertNotNull(saved.getId());

        Optional<MetaforgeEventEntity> found = repository.findByNameAndMapNameAndStartTime(
                "Test Event", "TestMap", Instant.ofEpochMilli(123456789L));

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }
}
