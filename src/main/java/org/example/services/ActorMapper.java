package org.example.services;

import org.example.dtos.CastDTO;
import org.example.entities.Actor;
import java.util.ArrayList;
import java.util.List;

public class ActorMapper {

    // Mapper en CastDTO til en Actor-entitet
    public static Actor mapToActor(CastDTO castDTO) {
        return new Actor(
                castDTO.getId(),
                castDTO.getName(),
                castDTO.getCharacter(),
                castDTO.getKnown_for_department()  // Mapper afdeling fra known_for_department
        );
    }

    // Mapper en liste af CastDTO'er til en liste af Actor-entiteter
    public static List<Actor> mapToActorList(List<CastDTO> castDTOList) {
        List<Actor> actors = new ArrayList<>();
        for (CastDTO castDTO : castDTOList) {
            actors.add(mapToActor(castDTO));  // Tilføjer hver mapped Actor til listen
        }
        return actors;  // Returnerer den samlede liste af Actor-entiteter
    }

    // Konverterer en Actor til dens entitetsrepræsentation
    public Actor toEntity(Actor actor) {
        Actor actorEntity = new Actor();
        actorEntity.setId(actor.getId());  // Sætter ID'et
        actorEntity.setName(actor.getName());  // Sætter navnet
        actorEntity.setCharacter(actor.getCharacter());  // Sætter karakteren
        actorEntity.setDepartment(actor.getDepartment()); // Mapper afdeling fra Actor-entiteten
        return actorEntity;  // Returnerer den mapperede Actor-entitet
    }
}
