package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Genre;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private Long id;
    private String name;

    public Genre toEntity() {
        Genre genre = new Genre();
        genre.setId(this.id);
        genre.setName(this.name);
        return genre;
    }

    public static GenreDTO fromEntity(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreDTO(
                genre.getId(),
                genre.getName()
        );
    }
}
