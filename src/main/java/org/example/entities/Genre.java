package org.example.entities;

import jakarta.persistence.*; // Importerer JPA-annotationer
import lombok.Data; // Importerer Data annotation fra Lombok
import lombok.EqualsAndHashCode; // Importerer EqualsAndHashCode annotation fra Lombok
import java.util.HashSet; // Importerer HashSet
import java.util.Set; // Importerer Set

@Entity // Angiver at klassen er en JPA-entitet
@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@EqualsAndHashCode(of = "id") // Genererer equals() og hashCode() baseret på id
public class Genre {

    @Id // Angiver ID-feltet som primær nøgle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genererer ID automatisk
    private Long id;  // Genre's unikke ID.

    private String name;  // Genre's navn.

    @ManyToMany(mappedBy = "genres") // Angiver en mange-til-mange relation til Movie
    private Set<Movie> movies = new HashSet<>();  // Set af film i denne genre.

    // Tom konstruktør kræves af JPA for at kunne oprette objekter automatisk.
    public Genre() {
    }
}
