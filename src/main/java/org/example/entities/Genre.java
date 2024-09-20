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
@EqualsAndHashCode(of = "id")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Genre's unique ID.

    private String name;  // Genre's name.

    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();  // Set of movies in this genre.

    public Genre() {
    }
}
