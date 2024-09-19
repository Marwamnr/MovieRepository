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
public class Director {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Instruktørens unikke ID.

    private String name;  // Instruktørens navn.
/*
    @OneToMany(mappedBy = "director", cascade = CascadeType.ALL)
    private Set<Movie> movies = new HashSet<>();  // Liste over film, instruktøren har lavet.
*/
    // Tom konstruktør kræves af JPA for at kunne oprette objekter automatisk.
    public Director() {
    }
}
