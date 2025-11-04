package org.example.springboot_lectures.lecture02_MongoDB.service;

import org.example.springboot_lectures.lecture02_MongoDB.dto.AsterixCharacterDto;
import org.example.springboot_lectures.lecture02_MongoDB.models.AsterixCharacter;
import org.example.springboot_lectures.lecture02_MongoDB.repository.AsterixRepo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsterixService {

    private final MongoTemplate mongoTemplate;
    private final AsterixRepo asterixRepo;
    private final IdService IdService;

    public AsterixService(MongoTemplate mongoTemplate, AsterixRepo asterixRepo, IdService idService) {
        this.mongoTemplate = mongoTemplate;
        this.asterixRepo = asterixRepo;
        this.IdService = idService;
    }

    public List<AsterixCharacter> findAll() {
        return asterixRepo.findAll();
    }

    public List<AsterixCharacter> createCharSet() {
        List<AsterixCharacter> asterixCharacters = List.of(
                new AsterixCharacter(IdService.randomId(), "Asterix", 35, "Warrior"),
                new AsterixCharacter(IdService.randomId(), "Obelix", 35, "Supplier"),
                new AsterixCharacter(IdService.randomId(), "Miraculix", 60, "Druid"),
                new AsterixCharacter(IdService.randomId(), "Majestix", 60, "Chief"),
                new AsterixCharacter(IdService.randomId(), "Troubadix", 25, "Bard"),
                new AsterixCharacter(IdService.randomId(), "Gutemine", 35, "Chiefs Wife"),
                new AsterixCharacter(IdService.randomId(), "Idefix", 5, "Dog"),
                new AsterixCharacter(IdService.randomId(), "Geriatrix", 70, "Retiree"),
                new AsterixCharacter(IdService.randomId(), "Automatix", 35, "Smith"),
                new AsterixCharacter(IdService.randomId(), "Grockelix", 35, "Fisherman")
        );

        return saveAll(asterixCharacters);
    }

    public List<AsterixCharacter> saveAll(List<AsterixCharacter> asterixCharacters) {
        return asterixRepo.saveAll(asterixCharacters);
    }

    public AsterixCharacter createNewCharacter(AsterixCharacterDto asterixCharacterDto) {
        AsterixCharacter newChar = AsterixCharacter.builder()
                .age(asterixCharacterDto.age())
                .profession(asterixCharacterDto.profession())
                .name(asterixCharacterDto.name())
                .id(IdService.randomId())
                .build();

        asterixRepo.save(newChar);

        return asterixRepo.findById(newChar.id()).orElse(null);
    }

    public AsterixCharacter updateCharacterById(String id, AsterixCharacterDto asterixCharacterDto) {
        AsterixCharacter updatedChar = asterixRepo.findById(id).orElseThrow();


        if (asterixCharacterDto.name() != null) {
            updatedChar = updatedChar.withName(asterixCharacterDto.name());
        }
        if (asterixCharacterDto.age() != null) {
            updatedChar = updatedChar.withAge(asterixCharacterDto.age());
        }
        if (asterixCharacterDto.profession() != null) {
            updatedChar = updatedChar.withProfession(asterixCharacterDto.profession());
        }

        return asterixRepo.save(updatedChar);
    }

    public void deleteAll() {
        asterixRepo.deleteAll();
    }

    public void deleteById(String id) {
        asterixRepo.deleteById(id);
    }

    public List<AsterixCharacter> findByFilters(Optional<String> name, Optional<String> id, Optional<String> profession, Optional<Integer> minAge) {
        Query query = new Query();

        name.ifPresent(n -> query.addCriteria(Criteria.where("name").regex(n.trim(), "i")));

        id.ifPresent(i -> query.addCriteria(Criteria.where("_id").is(i)));

        profession.ifPresent(p -> query.addCriteria(Criteria.where("profession").regex(p.trim(), "i")));

        minAge.ifPresent(a -> query.addCriteria(Criteria.where("age").gte(a)));

        return mongoTemplate.find(query, AsterixCharacter.class);
    }
}