package org.example.dtos;

import lombok.Data;
import org.example.entities.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
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
    private List<CastDTO> actors = new ArrayList<>();   // Initialize with an empty list
    private DirectorDTO director;


    // Standard konstruktør
    public MovieDTO() {
    }

    // Konstruktør der konverterer Movie entitet til MovieDTO
    public MovieDTO(Movie movie) {
        this.id = movie.getId(); // Henter ID
        this.title = movie.getTitle(); // Henter titel
        this.release_date = movie.getRelease_date(); // Henter udgivelsesdato
        this.vote_average = movie.getRating(); // Henter gennemsnitlig bedømmelse
        this.vote_count = movie.getVoteCount(); // Henter antal stemmer
        this.popularity = movie.getPopularity(); // Henter popularitetsværdi
        this.genres = movie.getGenres().stream().map(GenreDTO::fromEntity).collect(Collectors.toList()); // Henter genrer
        this.actors = movie.getActors().stream().map(CastDTO::fromEntity).collect(Collectors.toList()); // Henter skuespillere
        this.director = movie.getDirector() != null ? DirectorDTO.fromEntity(movie.getDirector()) : null; // Henter instruktør, hvis tilgængelig
    }

    // Statisk metode til at konvertere Movie entitet til MovieDTO
    public static MovieDTO fromEntity(Movie movie) {
        return new MovieDTO(movie); // Returnerer ny MovieDTO
    }

    // Konverterer MovieDTO til Movie entitet
    public Movie toEntity() {
        Movie movie = new Movie(); // Opretter en ny Movie instans
        movie.setId(this.id); // Sætter ID
        movie.setTitle(this.title); // Sætter titel
        movie.setRelease_date(this.release_date); // Sætter udgivelsesdato
        movie.setRating(this.vote_average); // Sætter gennemsnitlig bedømmelse
        movie.setPopularity(this.popularity); // Sætter popularitetsværdi
        movie.setVoteCount(this.vote_count); // Sætter antal stemmer
        movie.setGenres(this.genres.stream().map(GenreDTO::toEntity).collect(Collectors.toSet())); // Sætter genrer
        movie.setActors(this.actors.stream().map(CastDTO::toEntity).collect(Collectors.toSet())); // Sætter skuespillere
        movie.setDirector(this.director != null ? this.director.toEntity() : null); // Sætter instruktør, hvis tilgængelig
        return movie; // Returnerer Movie entitet
    }
}