package org.example.springboot_lectures.lecture02_MongoDB.controller;

import org.example.springboot_lectures.lecture02_MongoDB.dto.AsterixCharacterDto;
import org.example.springboot_lectures.lecture02_MongoDB.models.AsterixCharacter;
import org.example.springboot_lectures.lecture02_MongoDB.service.AsterixService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/asterix")
public class AsterixController {

    private final AsterixService asterixService;

    public AsterixController(AsterixService asterixService) {
        this.asterixService = asterixService;
    }

    @GetMapping("/chars")
    public List<AsterixCharacter> characters(@RequestParam Optional<String> name, @RequestParam Optional<String> id, @RequestParam Optional<String> profession, @RequestParam Optional<Integer> minAge) {
        return asterixService.findByFilters(name, id, profession, minAge);
    }

    @PostMapping("/create")
    public List<AsterixCharacter> create() {
        return asterixService.createCharSet();

    }

    @PostMapping("/newChar")
    public AsterixCharacter newCharacter(@RequestBody AsterixCharacterDto asterixCharacterDto) {

        return asterixService.createNewCharacter(asterixCharacterDto);
    }

    @PutMapping("/updateChar/{id}")
    public AsterixCharacter updateCharacter(@PathVariable String id, @RequestBody AsterixCharacterDto asterixCharacterDto) {
        return asterixService.updateCharacterById(id, asterixCharacterDto);
    }

    @DeleteMapping("/deleteChar/{id}")
    public void deleteCharacter(@PathVariable String id) {
        asterixService.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        asterixService.deleteAll();
    }

}
