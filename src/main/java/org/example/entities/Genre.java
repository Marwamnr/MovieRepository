package org.example.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")  // Generer equals() og hashCode() baseret på id for at sikre korrekt funktionalitet
// i datastrukturer som HashSet og HashMap, hvor objektets identitet er afgørende.
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Genrens unikke ID.

    private String name;  // Genrens navn.

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();  // Liste over film i genren.

    // Tom konstruktør kræves af JPA for at kunne oprette objekter automatisk.
    public Genre() {
    }
}
