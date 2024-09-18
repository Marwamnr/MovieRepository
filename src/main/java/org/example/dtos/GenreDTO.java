package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.entities.Genre;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private Long id;
    private String name;
    private List<MovieDTO> movies;

    public Genre toEntity() {
        Genre genre = new Genre();
        genre.setId(this.id);
        genre.setName(this.name);
        return genre;
    }

    public static GenreDTO fromEntity(Genre genre) {
        return new GenreDTO(
                genre.getId(),
                genre.getName(),
                genre.getMovies().stream().map(MovieDTO::fromEntity).collect(Collectors.toList())
        );
    }
}
