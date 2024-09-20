package org.example.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.entities.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
public class MovieDTO {
    private Long id;
    private boolean adult;
    private String backdrop_path;
    private List<Long> genre_ids = new ArrayList<>();  // Initialize with an empty list
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;
    private List<GenreDTO> genres = new ArrayList<>();  // Initialize with an empty list
    private List<ActorDTO> actors = new ArrayList<>();   // Initialize with an empty list
    private DirectorDTO director;

    public MovieDTO() {
    }

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.release_date = movie.getRelease_date();
        this.vote_average = movie.getRating();
        this.popularity = movie.getPopularity();
        this.genres = movie.getGenres().stream().map(GenreDTO::fromEntity).collect(Collectors.toList());
        this.actors = movie.getActors().stream().map(ActorDTO::fromEntity).collect(Collectors.toList());
        this.director = movie.getDirector() != null ? DirectorDTO.fromEntity(movie.getDirector()) : null;
    }

    public static MovieDTO fromEntity(Movie movie) {
        return new MovieDTO(movie);
    }

    public Movie toEntity() {
        Movie movie = new Movie();
        movie.setId(this.id);
        movie.setTitle(this.title);
        movie.setRelease_date(this.release_date);
        movie.setRating(this.vote_average);
        movie.setPopularity(this.popularity);
        movie.setGenres(this.genres.stream().map(GenreDTO::toEntity).collect(Collectors.toSet()));
        movie.setActors(this.actors.stream().map(ActorDTO::toEntity).collect(Collectors.toSet()));
        movie.setDirector(this.director != null ? this.director.toEntity() : null);
        return movie;
    }
}
