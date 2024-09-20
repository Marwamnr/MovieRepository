package org.example.entities;

import jakarta.persistence.*; // Importerer JPA-annotationer
import lombok.Data; // Importerer Data annotation fra Lombok
import lombok.EqualsAndHashCode; // Importerer EqualsAndHashCode annotation fra Lombok
import java.util.HashSet; // Importerer HashSet
import java.util.Set; // Importerer Set

@Entity // Angiver at klassen er en JPA-entitet
@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@EqualsAndHashCode(of = "id") // Genererer equals() og hashCode() baseret på id
public class Director {

    @Id // Angiver ID-feltet som primær nøgle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genererer ID automatisk
    private Long id;  // Instruktørens unikke ID.

    private String name;  // Instruktørens navn.

    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL) // En-til-mange relation til Movie
    private Set<Movie> movies = new HashSet<>();  // Liste over film, instruktøren har lavet.

    // Tom konstruktør kræves af JPA for at kunne oprette objekter automatisk.
    public Director() {
    }
}
