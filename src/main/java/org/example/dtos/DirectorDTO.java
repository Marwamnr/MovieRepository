package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Director;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDTO {
    private Long id;
    private String name;
    private List<MovieDTO> movies;

    public Director toEntity() {
        Director director = new Director();
        director.setId(this.id);
        director.setName(this.name);
        return director;
    }

    public static DirectorDTO fromEntity(Director director) {
        if (director == null) {
            return null;
        }
        return new DirectorDTO(
                director.getId(),
                director.getName(),
                director.getMovies().stream()
                        .map(MovieDTO::new)  // Use the constructor in MovieDTO
                        .collect(Collectors.toList())
        );
    }
}
