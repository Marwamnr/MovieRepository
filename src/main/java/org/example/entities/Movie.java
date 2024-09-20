package org.example.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private String release_date;
    private double rating;  // This field represents the movie rating
    private double popularity;  // Added popularity field
    private int voteCount;  // Added vote count field

    @Transient
    private List<Integer> genreIds = new ArrayList<>();

    @ManyToMany
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;

    public Movie() {
    }

    // Constructor with voteCount
    public Movie(Long id, String title, String release_date, double rating, double popularity, int voteCount, Set<Actor> actors, Set<Genre> genres, Director director) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.rating = rating;  // Initialize rating
        this.popularity = popularity;  // Initialize popularity
        this.voteCount = voteCount;  // Initialize vote count
        this.actors = actors;
        this.genres = genres;
        this.director = director;
    }
}
