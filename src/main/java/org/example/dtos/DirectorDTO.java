package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entities.Director;
import java.util.List;
import java.util.stream.Collectors;

@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@AllArgsConstructor // Genererer en konstruktør med alle felter
@NoArgsConstructor // Genererer en standard konstruktør
public class DirectorDTO {
    private Long id;
    private String name;
    private List<MovieDTO> movies;

    // Konverterer DirectorDTO til Director entitet
    public Director toEntity() {
        Director director = new Director(); // Opretter en ny Director instans
        director.setId(this.id); // Sætter ID
        director.setName(this.name); // Sætter navn
        return director; // Returnerer Director entitet
    }

    // Konverterer Director entitet til DirectorDTO
    public static DirectorDTO fromEntity(Director director) {
        if (director == null) {
            return null; // Returnerer null hvis director er null
        }
        return new DirectorDTO(
                director.getId(), // Henter ID
                director.getName(), // Henter navn
                director.getMovies().stream() // Henter filmene
                        .map(MovieDTO::new)  // Bruger konstruktøren i MovieDTO
                        .collect(Collectors.toList()) // Samler til liste
        );
    }
}

