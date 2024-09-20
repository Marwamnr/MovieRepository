package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Genre;

@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@AllArgsConstructor // Genererer en konstruktør med alle felter
@NoArgsConstructor // Genererer en standard konstruktør
public class GenreDTO {
    private Long id; // Unik ID for genren
    private String name; // Navn på genren

    // Konverterer GenreDTO til Genre entitet
    public Genre toEntity() {
        Genre genre = new Genre(); // Opretter en ny Genre instans
        genre.setId(this.id); // Sætter ID
        genre.setName(this.name); // Sætter navn
        return genre; // Returnerer Genre entitet
    }

    // Konverterer Genre entitet til GenreDTO
    public static GenreDTO fromEntity(Genre genre) {
        if (genre == null) {
            return null; // Returnerer null hvis genre er null
        }
        return new GenreDTO(
                genre.getId(), // Henter ID
                genre.getName() // Henter navn
        );
    }
}
