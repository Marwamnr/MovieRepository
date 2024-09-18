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
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Skuespillerens unikke ID.

    private String name;  // Skuespillerens navn.

    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies = new HashSet<>();  // Liste over film, skuespilleren er med i.

    // Tom konstruktør kræves af JPA for at kunne oprette objekter automatisk.
    public Actor() {
    }
}
