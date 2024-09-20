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
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String character;

    private String department;

    @ManyToMany(mappedBy = "actors")
    private Set<Movie> movies = new HashSet<>();

    public Actor(long id, String name, String character, String department) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.department = department;
    }

    public Actor() {

    }


    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", character='" + character + '\'' +
                '}';
    }
}

