package org.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Movie {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    private String title;
    private String year;

    private double rating;

    @ManyToMany
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    public Movie(Long id, String title, String year, double rating, Set<Actor> actors, Set<Genre> genres, Director director) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.actors = actors;
        this.genres = genres;
        this.director = director;
    }

    public Movie() {
    }
}
