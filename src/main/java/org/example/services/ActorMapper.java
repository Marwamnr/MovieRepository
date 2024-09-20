package org.example.services;

import org.example.dtos.CastDTO;
import org.example.entities.Actor;

import java.util.ArrayList;
import java.util.List;

public class ActorMapper {


    public static Actor mapToActor(CastDTO castDTO) {
        return new Actor(
                castDTO.getId(),
                castDTO.getName(),
                castDTO.getCharacter(),
                castDTO.getKnown_for_department()  // Map department from known_for_department
        );
    }


    public static List<Actor> mapToActorList(List<CastDTO> castDTOList) {
        List<Actor> actors = new ArrayList<>();
        for (CastDTO castDTO : castDTOList) {
            actors.add(mapToActor(castDTO));
        }
        return actors;
    }

    public Actor toEntity(Actor actor) {
        Actor actorEntity = new Actor();
        actorEntity.setId(actor.getId());
        actorEntity.setName(actor.getName());
        actorEntity.setCharacter(actor.getCharacter());
        actorEntity.setDepartment(actor.getDepartment()); // Assuming you have this field in your Actor entity
        return actorEntity;
    }
}
