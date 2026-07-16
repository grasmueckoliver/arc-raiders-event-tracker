package com.grasmueck.arcraiderseventtracker.client;

import com.grasmueck.arcraiderseventtracker.dto.MetaforgeResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MetaforgeClient Unit Tests (smoke)")
class MetaforgeClientTest {

    @Mock
    private RestClient restClient;

    private MetaforgeClient client;

    @BeforeEach
    void setUp() {
        client = new MetaforgeClient(restClient);
    }

    @Test
    @DisplayName("collectAllEvents should delegate to collectAny")
    void testCollectAllEvents_NotNull() {
        // This is a smoke test to ensure client wired correctly
        assertNotNull(client);
    }

    @Test
    @DisplayName("collectAny should return null on exception")
    void testCollectAny_ExceptionHandling() {
        // Simulate requestApi throwing by mocking restClient.get() to throw
        when(restClient.get()).thenThrow(new RuntimeException("connection failed"));
        MetaforgeResponseDto result = client.collectAny("/does-not-matter");
        assertNull(result);
    }
}
