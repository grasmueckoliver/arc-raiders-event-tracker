package com.grasmueck.arcraiderseventtracker.controller;

import com.grasmueck.arcraiderseventtracker.dto.FilteredEventsDto;
import com.grasmueck.arcraiderseventtracker.dto.MetaforgeEventDto;
import com.grasmueck.arcraiderseventtracker.service.FilterEventsService;
import com.grasmueck.arcraiderseventtracker.service.MetaforgeCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestEventsController Tests")
class RestEventsControllerTest {

    @Mock
    private MetaforgeCacheService cacheService;

    @Mock
    private FilterEventsService filterEventsService;

    private RestEventsController controller;

    @BeforeEach
    void setUp() {
        controller = new RestEventsController(cacheService, filterEventsService);
    }

    @Test
    @DisplayName("Should get all events successfully")
    void testGetAllEvents_Success() {
        // Arrange
        List<MetaforgeEventDto> events = List.of(
                new MetaforgeEventDto("Bird City", "icon1", 1000000, 2000000, "Buried City"),
                new MetaforgeEventDto("Nightfall", "icon2", 3000000, 4000000, "Bayside")
        );
        when(cacheService.getAllEventsAsDto()).thenReturn(events);

        // Act
        ResponseEntity<List<MetaforgeEventDto>> result = controller.getAllEvents();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        verify(cacheService, times(1)).getAllEventsAsDto();
    }

    @Test
    @DisplayName("Should return empty list when no events exist")
    void testGetAllEvents_EmptyList() {
        // Arrange
        when(cacheService.getAllEventsAsDto()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<MetaforgeEventDto>> result = controller.getAllEvents();

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(0, result.getBody().size());
    }

    @Test
    @DisplayName("Should filter events with valid parameters")
    void testGetFilteredEvents_ValidParameters() {
        // Arrange
        String[] args = {"Bird City", "Buried City"};
        List<MetaforgeEventDto> allEvents = List.of(
                new MetaforgeEventDto("Bird City", "icon1", 1000000, 2000000, "Buried City")
        );
        List<Integer> times = List.of(10, 15, 20);

        when(cacheService.getAllEventsAsDto()).thenReturn(allEvents);
        when(filterEventsService.filterEventsByStartTimes("Bird City", "Buried City", allEvents))
                .thenReturn(new ArrayList<>(times));

        // Act
        ResponseEntity<List<FilteredEventsDto>> result = controller.getFilteredEvents(args);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals("Bird City", result.getBody().get(0).name());
    }

    @Test
    @DisplayName("Should filter multiple events")
    void testGetFilteredEvents_MultipleFilters() {
        // Arrange
        String[] args = {"Bird City", "Buried City", "Nightfall", "Bayside"};
        List<MetaforgeEventDto> allEvents = List.of(
                new MetaforgeEventDto("Bird City", "icon1", 1000000, 2000000, "Buried City"),
                new MetaforgeEventDto("Nightfall", "icon2", 3000000, 4000000, "Bayside")
        );

        when(cacheService.getAllEventsAsDto()).thenReturn(allEvents);
        when(filterEventsService.filterEventsByStartTimes("Bird City", "Buried City", allEvents))
                .thenReturn(new ArrayList<>(List.of(10, 15)));
        when(filterEventsService.filterEventsByStartTimes("Nightfall", "Bayside", allEvents))
                .thenReturn(new ArrayList<>(List.of(20, 22)));

        // Act
        ResponseEntity<List<FilteredEventsDto>> result = controller.getFilteredEvents(args);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
    }

    @Test
    @DisplayName("Should return 400 when args is null")
    void testGetFilteredEvents_NullArgs() {
        // Act
        ResponseEntity<List<FilteredEventsDto>> result = controller.getFilteredEvents(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    @DisplayName("Should return 400 when args length is less than 2")
    void testGetFilteredEvents_InsufficientArgs() {
        // Arrange
        String[] args = {"Bird City"};

        // Act
        ResponseEntity<List<FilteredEventsDto>> result = controller.getFilteredEvents(args);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when args length is odd")
    void testGetFilteredEvents_OddArgsLength() {
        // Arrange
        String[] args = {"Bird City", "Buried City", "Nightfall"};

        // Act
        ResponseEntity<List<FilteredEventsDto>> result = controller.getFilteredEvents(args);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    @DisplayName("Should handle empty args array")
    void testGetFilteredEvents_EmptyArgs() {
        // Arrange
        String[] args = {};

        // Act
        ResponseEntity<List<FilteredEventsDto>> result = controller.getFilteredEvents(args);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    @DisplayName("Should call filter service for each pair")
    void testGetFilteredEvents_VerifyServiceCalls() {
        // Arrange
        String[] args = {"Event1", "Map1", "Event2", "Map2"};
        List<MetaforgeEventDto> allEvents = new ArrayList<>();

        when(cacheService.getAllEventsAsDto()).thenReturn(allEvents);
        when(filterEventsService.filterEventsByStartTimes(any(), any(), any()))
                .thenReturn(new ArrayList<>());

        // Act
        controller.getFilteredEvents(args);

        // Assert
        verify(filterEventsService, times(2)).filterEventsByStartTimes(any(), any(), any());
    }
}
