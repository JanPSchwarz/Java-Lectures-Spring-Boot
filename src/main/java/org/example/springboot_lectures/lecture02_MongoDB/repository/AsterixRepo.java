package org.example.springboot_lectures.lecture02_MongoDB.repository;

import org.example.springboot_lectures.lecture02_MongoDB.models.AsterixCharacter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsterixRepo extends MongoRepository<AsterixCharacter, String> {


}
