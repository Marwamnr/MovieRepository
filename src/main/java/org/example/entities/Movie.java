package org.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.dtos.MovieDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Movie {

    @Id
    private Long id;

    private String title;
    private String release_date;
    private double rating;
    @Transient
    private List<Integer> genresIds = new ArrayList<>();

    @ManyToMany
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Genre> genres = new HashSet<>();


    public Movie(Long id, String title, String release_date, double rating, Set<Actor> actors, Set<Genre> genres, Director director) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.rating = rating;
        this.actors = actors;
        this.genres = genres;
    }

    public Movie(MovieDTO movieDTO) {
        this.id = movieDTO.getId();
        this.title = movieDTO.getTitle();
        this.release_date = movieDTO.getRelease_date();
        this.rating = movieDTO.getVote_average();
        this.genresIds = movieDTO.getGenre_ids();
    }

    public Movie() {
    }
}