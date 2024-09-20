package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Actor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorDTO {
    private Long id;
    private String name;
    private List<MovieDTO> movies;

    public Actor toEntity() {
        Actor actor = new Actor();
        actor.setId(this.id);
        actor.setName(this.name);
        return actor;
    }

    public static ActorDTO fromEntity(Actor actor) {
        if (actor == null) {
            return null;
        }
        return new ActorDTO(
                actor.getId(),
                actor.getName(),
                actor.getMovies().stream()
                        .map(MovieDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}
