package org.example.springboot_lectures.lecture02_MongoDB.service;

import org.example.springboot_lectures.lecture02_MongoDB.dto.AsterixCharacterDto;
import org.example.springboot_lectures.lecture02_MongoDB.models.AsterixCharacter;
import org.example.springboot_lectures.lecture02_MongoDB.repository.AsterixRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class AsterixServiceTest {

    AsterixRepo mockRepo;
    MongoTemplate mockTemplate;
    IdService mockIdService;

    AsterixService asterixService;

    @BeforeEach
    void setUp() {
        mockRepo = mock(AsterixRepo.class);
        mockTemplate = mock(MongoTemplate.class);
        mockIdService = mock(IdService.class);

        asterixService = new AsterixService(mockTemplate, mockRepo, mockIdService);
    }

    @Test
    void findAll_shouldReturnTrue_whenCalledWithValidList() {
        String expectedId = "1";
        AsterixCharacter expectedChar = new AsterixCharacter(expectedId, "Asterix", 35, "Warrior");

        when(mockRepo.findAll()).thenReturn(List.of(expectedChar));
        List<AsterixCharacter> actual = asterixService.findAll();
        List<AsterixCharacter> expectedResult = List.of(expectedChar);
        assertEquals(expectedResult, actual);
        verify(mockRepo).findAll();
    }

    @Test
    void createCharSet_shouldReturnTrue_whenValidListProvided() {
        List<AsterixCharacter> expectedCharList = List.of(
                new AsterixCharacter(mockIdService.randomId(), "Asterix", 35, "Warrior"),
                new AsterixCharacter(mockIdService.randomId(), "Obelix", 35, "Supplier"),
                new AsterixCharacter(mockIdService.randomId(), "Miraculix", 60, "Druid"),
                new AsterixCharacter(mockIdService.randomId(), "Majestix", 60, "Chief"),
                new AsterixCharacter(mockIdService.randomId(), "Troubadix", 25, "Bard"),
                new AsterixCharacter(mockIdService.randomId(), "Gutemine", 35, "Chiefs Wife"),
                new AsterixCharacter(mockIdService.randomId(), "Idefix", 5, "Dog"),
                new AsterixCharacter(mockIdService.randomId(), "Geriatrix", 70, "Retiree"),
                new AsterixCharacter(mockIdService.randomId(), "Automatix", 35, "Smith"),
                new AsterixCharacter(mockIdService.randomId(), "Grockelix", 35, "Fisherman")
        );
        when(mockRepo.saveAll(expectedCharList)).thenReturn(expectedCharList);

        List<AsterixCharacter> actual = asterixService.createCharSet();

        assertEquals(expectedCharList, actual);
        verify(mockRepo).saveAll(expectedCharList);
    }

    @Test
    void saveAll_shoudlReturnTrue_whenCalledWithValidList() {
        List<AsterixCharacter> expectedCharList = List.of(new AsterixCharacter(mockIdService.randomId(), "Asterix", 35, "Warrior"), new AsterixCharacter(mockIdService.randomId(), "Obelix", 35, "Supplier"));

        when(mockRepo.saveAll(expectedCharList)).thenReturn(expectedCharList);

        List<AsterixCharacter> actual = asterixService.saveAll(expectedCharList);

        assertEquals(expectedCharList, actual);
        verify(mockRepo).saveAll(expectedCharList);
    }

    @Test
    void createNewCharacter_shouldReturnCharacter_whenCalledWithValidData() {
        // GIVEN
        String expectedId = "1";
        AsterixCharacterDto asterixCharacterDto = new AsterixCharacterDto("Asterix", "Warrior", 35);
        AsterixCharacter asterix = new AsterixCharacter(expectedId, "Asterix", 35, "Warrior");

        Optional<AsterixCharacter> response = Optional.of(asterix);

        when(mockIdService.randomId()).thenReturn(expectedId);
        when(mockRepo.save(asterix)).thenReturn(asterix);
        when(mockRepo.findById(asterix.id())).thenReturn(response);

        // WHEN
        AsterixCharacter actual = asterixService.createNewCharacter(asterixCharacterDto);

        // THEN
        assertEquals(asterix, actual);
        verify(mockRepo).findById(asterix.id());
    }

    @Test
    void updateCharacterById_shouldReturnTrue_whenCharUpdated() {
        String expectedId = "1";
        AsterixCharacter originalChar = new AsterixCharacter(expectedId, "Asterix", 35, "Warrior");
        AsterixCharacterDto updateDto = new AsterixCharacterDto("Asterix", "Warrior", 25);

        AsterixCharacter expectedChar = new AsterixCharacter(expectedId, "Asterix", 25, "Warrior");

        when(mockIdService.randomId()).thenReturn(expectedId);
        when(mockRepo.findById(expectedId)).thenReturn(Optional.of(originalChar));
        when(mockRepo.save(expectedChar)).thenReturn(expectedChar);

        AsterixCharacter actual = asterixService.updateCharacterById(expectedId, updateDto);

        assertEquals(expectedChar, actual);
        verify(mockRepo).findById(expectedId);
    }

    @Test
    void deleteAll_shouldReturnTrue_whenDataIsDeleted() {
        AsterixCharacter character = new AsterixCharacter(mockIdService.randomId(), "Asterix", 35, "Warrior");
        mockRepo.save(character);

        asterixService.deleteAll();

        List<AsterixCharacter> actual = mockRepo.findAll();
        assertEquals(List.of(), actual);
    }

    @Test
    void deleteById() {
        String expectedId = "1";
        AsterixCharacter createdCharacter = new AsterixCharacter(expectedId, "Asterix", 35, "Warrior");
        mockRepo.save(createdCharacter);

        asterixService.deleteById(expectedId);

        Optional<AsterixCharacter> actual = mockRepo.findById(expectedId);

        assertNull(actual.orElse(null));
    }

    @ParameterizedTest
    @CsvSource({
            "'Asterix', '', '', , 1",
            "'', '1', '', , 1",
            "'', '', 'Warrior', , 2",
            "'', '', '', 30, 1",
            "'', '', '', 7, 3",
            "'', '', '', , 3"
    })
    void findByFilters_parametrized(String name, String id, String profession, Integer minAge, int expectedSize) {
        AsterixCharacter asterix = new AsterixCharacter("1", "Asterix", 35, "Warrior");
        AsterixCharacter obelix = new AsterixCharacter("2", "Obelix", 25, "Warrior");
        AsterixCharacter idefix = new AsterixCharacter("3", "Idefix", 8, "Supporter");

        mockRepo.saveAll(List.of(asterix, obelix, idefix));

        List<AsterixCharacter> expectedResult = switch (expectedSize) {
            case 1 -> {
                if ("Asterix".equals(name)) yield List.of(asterix);
                if ("1".equals(id)) yield List.of(asterix);
                if (minAge != null && minAge == 30) yield List.of(asterix);
                yield List.of();
            }
            case 2 -> List.of(asterix, obelix);
            case 3 -> List.of(asterix, obelix, idefix);
            default -> List.of();
        };

        when(mockTemplate.find(any(Query.class), eq(AsterixCharacter.class))).thenReturn(expectedResult);


        List<AsterixCharacter> actual = asterixService.findByFilters(Optional.ofNullable(name), Optional.ofNullable(id), Optional.ofNullable(profession), Optional.ofNullable(minAge));

        assertEquals(expectedSize, actual.size());
        assertEquals(expectedResult, actual);
    }
}