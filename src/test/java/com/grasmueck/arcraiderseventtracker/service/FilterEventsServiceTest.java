package com.grasmueck.arcraiderseventtracker.service;

import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FilterEventsService Tests")
class FilterEventsServiceTest {

    private FilterEventsService filterEventsService;
    private List<MetaforgeEventDto> testData;

    @BeforeEach
    void setUp() {
        filterEventsService = new FilterEventsService();
        testData = new ArrayList<>();

        // Add test data: Bird City events
        testData.add(new MetaforgeEventDto("Bird City", "icon1", 1000000, 2000000, "Buried City"));
        testData.add(new MetaforgeEventDto("Bird City", "icon2", 3000000, 4000000, "Buried City"));
        testData.add(new MetaforgeEventDto("Bird City", "icon3", 5000000, 6000000, "Buried City"));

        // Add test data: Nightfall events
        testData.add(new MetaforgeEventDto("Nightfall", "icon4", 7000000, 8000000, "Bayside"));
        testData.add(new MetaforgeEventDto("Nightfall", "icon5", 9000000, 10000000, "Bayside"));
    }

    @Test
    @DisplayName("Should filter events by name and map")
    void testFilterEventsByStartTimes_MatchingEvents() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "Bird City", "Buried City", testData);

        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should return empty list when no events match")
    void testFilterEventsByStartTimes_NoMatches() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "NonExistent", "NonExistent", testData);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return empty list when data is null")
    void testFilterEventsByStartTimes_NullData() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "Bird City", "Buried City", null);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return empty list when eventName is null")
    void testFilterEventsByStartTimes_NullEventName() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                null, "Buried City", testData);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return empty list when mapName is null")
    void testFilterEventsByStartTimes_NullMapName() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "Bird City", null, testData);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should return sorted times")
    void testFilterEventsByStartTimes_ReturnsSorted() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "Bird City", "Buried City", testData);

        assertEquals(3, result.size());
        // Verify sorted order
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) <= result.get(i + 1));
        }
    }

    @Test
    @DisplayName("Should handle partial string matches")
    void testFilterEventsByStartTimes_PartialMatch() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "Bird", "Buried", testData);

        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should be case-sensitive")
    void testFilterEventsByStartTimes_CaseSensitive() {
        ArrayList<Integer> result = filterEventsService.filterEventsByStartTimes(
                "bird city", "buried city", testData);

        assertEquals(0, result.size());
    }
}
