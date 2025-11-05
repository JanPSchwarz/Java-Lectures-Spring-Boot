package org.example.springboot_lectures.lecture02_MongoDB.controller;

import org.example.springboot_lectures.lecture02_MongoDB.models.AsterixCharacter;
import org.example.springboot_lectures.lecture02_MongoDB.repository.AsterixRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AsterixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AsterixRepo asterixRepo;

    @BeforeEach
    void setUp() {
        asterixRepo.deleteAll();
    }

    @Test
    void characters_shouldReturnAllChars_whenCalled() throws Exception {
        // GIVEN
        AsterixCharacter asterix = new AsterixCharacter("1", "Asterix", 35, "Warrior");

        asterixRepo.save(asterix);

        // WHEN
        mockMvc.perform(get("/api/asterix/chars"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {
                                      "id": "1",
                                      "name": "Asterix",
                                      "age": 35,
                                      "profession": "Warrior"
                                    }
                                ]
                                """
                ));

        // THEN
    }

    @Test
    void characters_shouldReturnAllChars_whenCalledWithQueryName() throws Exception {
        // GIVEN
        AsterixCharacter asterix = new AsterixCharacter("1", "Asterix", 35, "Warrior");
        AsterixCharacter obelix = new AsterixCharacter("2", "Obelix", 35, "Warrior");


        asterixRepo.save(asterix);
        asterixRepo.save(obelix);

        String expected = """
                     {
                      "name": "Asterix",
                      "age":  35,
                      "profession": "Warrior"
                     }
                """;

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.get("/api/asterix/chars").param("name", "Obelix"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void newCharacter_shouldReturnTrue_whenNewCharacter() throws Exception {

        String providedJSON = """
                     {
                      "name": "Asterix",
                      "age":  35,
                      "profession": "Warrior"
                     }
                """;

        mockMvc.perform(post("/api/asterix/newChar").contentType(MediaType.APPLICATION_JSON).content(providedJSON))
                .andExpect(status().isOk())
                .andExpect(content().json(providedJSON))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void updateCharacter() {
    }

    @Test
    void deleteCharacter_shouldReturnTrue_whenCharWithIDDeleted() throws Exception {
        // GIVEN
        AsterixCharacter asterix = new AsterixCharacter("1", "Asterix", 35, "Warrior");
        AsterixCharacter obelix = new AsterixCharacter("2", "Obelix", 35, "Warrior");


        asterixRepo.save(asterix);
        asterixRepo.save(obelix);

        String expected = """
                     {
                      "id": "2",
                      "name": "Obelix",
                      "age":  35,
                      "profession": "Warrior"
                     }
                """;

        // WHEN
        mockMvc.perform(delete("/api/asterix/deleteChar/{id}", "1"))
                .andExpect(status().isOk());

        // THEN
        mockMvc.perform(get("/api/asterix/chars"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

    }

    @Test
    void deleteAll_shoudReturnTrue_whenAllCharsDeleted() throws Exception {
        // GIVEN
        AsterixCharacter asterix = new AsterixCharacter("1", "Asterix", 35, "Warrior");
        AsterixCharacter obelix = new AsterixCharacter("2", "Obelix", 35, "Warrior");


        asterixRepo.save(asterix);
        asterixRepo.save(obelix);

        String expected = """
                []
                """;

        // WHEN
        mockMvc.perform(delete("/api/asterix/deleteAll").param("name", "Obelix"))
                .andExpect(status().isOk());

        // THEN
        mockMvc.perform(get("/api/asterix/chars"))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

    }
}