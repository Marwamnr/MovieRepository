package org.example.entities;

import jakarta.persistence.*; // Importerer JPA-annotationer
import lombok.Data; // Importerer Data annotation fra Lombok
import java.util.ArrayList; // Importerer ArrayList
import java.util.HashSet; // Importerer HashSet
import java.util.List; // Importerer List
import java.util.Set; // Importerer Set

@Entity // Angiver at klassen er en JPA-entitet
@Data // Genererer getter, setter, toString, equals, og hashCode metoder
public class Movie {

    @Id // Angiver ID-feltet som primær nøgle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genererer ID automatisk
    private Long id;

    private String title; // Filmens titel
    private String release_date; // Udgivelsesdato for filmen
    private double rating;  // filmens rating
    private double popularity;  // filmens popularitet
    private int voteCount;  // antallet af stemmer

    @Transient // Angiver at dette felt ikke skal gemmes i databasen
    private List<Integer> genreIds = new ArrayList<>(); // Liste til genre-ID'er

    @ManyToMany // Mange-til-mange relation til aktører
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Mange-til-mange relation til genrer
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne // Mange-til-en relation til instruktør
    @JoinColumn(name = "director_id") // Angiver kolonnenavn i databasen
    private Director director;

    // Tom konstruktør kræves af JPA for at kunne oprette objekter automatisk.
    public Movie() {
    }

    // Konstruktør med voteCount
    public Movie(Long id, String title, String release_date, double rating, double popularity, int voteCount, Set<Actor> actors, Set<Genre> genres, Director director) {
        this.id = id; // Initialisere ID
        this.title = title; // Initialisere titlen
        this.release_date = release_date; // Initialisere udgivelsesdato
        this.rating = rating;  // Initialiserer rating
        this.popularity = popularity;  // Initialiserer popularitet
        this.voteCount = voteCount;  // Initialiserer stemmeantal
        this.actors = actors; // Initialiserer aktører
        this.genres = genres; // Initialiserer genrer
        this.director = director; // Initialiserer instruktør
    }
}
