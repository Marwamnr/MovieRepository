package org.example.dtos;

import lombok.Data;
import org.example.entities.Actor;

@Data // Genererer getter, setter, toString, equals, og hashCode metoder
public class CastDTO {
    private boolean adult;
    private int gender;
    private long id;
    private String known_for_department;
    private String name;
    private String original_name;
    private double popularity;
    private String profile_path;
    private int cast_id;
    private String character;
    private String credit_id;
    private int order;

    // Konverterer Actor entitet til CastDTO
    public static CastDTO fromEntity(Actor actor) {
        if (actor == null) {
            return null;
        }
        CastDTO dto = new CastDTO();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        dto.setCharacter(actor.getCharacter());
        dto.setKnown_for_department(actor.getDepartment());
        return dto;
    }

    // Konverterer CastDTO til Actor entitet
    public Actor toEntity() {
        Actor actor = new Actor();
        actor.setId(this.id);
        actor.setName(this.name);
        actor.setCharacter(this.character);
        actor.setDepartment(this.known_for_department);

        return actor; // Returnerer Actor entitet
    }
}
