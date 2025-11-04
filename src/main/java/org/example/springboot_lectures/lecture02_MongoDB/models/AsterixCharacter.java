package org.example.springboot_lectures.lecture02_MongoDB.models;

import lombok.Builder;
import lombok.With;

@With
@Builder
public record AsterixCharacter(String id, String name, int age, String profession) {
}
