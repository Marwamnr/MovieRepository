package org.example.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.entities.Actor;
import org.example.entities.Genre;

@Data
@Getter
@Setter

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

    public static CastDTO fromEntity(Actor actor) {
        return null;
    }


    public Actor toEntity() {
        Actor actor= new Actor();


        actor.setId(this.id );
        actor.setName(this.name);
        actor.setCharacter(this.character);
        actor.setDepartment(this.known_for_department);

        return actor;
    }
}
