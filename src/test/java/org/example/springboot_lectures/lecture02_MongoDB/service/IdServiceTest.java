package org.example.springboot_lectures.lecture02_MongoDB.service;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IdServiceTest {

    @Test
    void findById() {
        IdService mockIdService = mock(IdService.class);
        String expectedUUID = UUID.randomUUID().toString();

        when(mockIdService.randomId()).thenReturn(expectedUUID);

        String actualUUID = mockIdService.randomId();
        assertEquals(expectedUUID, actualUUID);
    }

}