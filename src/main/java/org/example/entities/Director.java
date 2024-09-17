package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @OneToMany (mappedBy = "director", cascade = CascadeType.ALL)
    private Set<Movie> movies = new HashSet<>();

    public Director() {
    }
}
