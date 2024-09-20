package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.daos.CastDAO;
import org.example.daos.GenreDAO;
import org.example.daos.MovieDAO;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Actor;
import org.example.entities.Genre;
import org.example.entities.Movie;
import org.example.services.*;

import java.util.List;

public class Main {

    private static EntityManagerFactory emf; // Factory til at skabe EntityManager
    private static GenreDAO genreDAO; // DAO til genrer
    private static MovieDAO movieDAO; // DAO til film
    private static CastDAO castDAO; // DAO til skuespillere
    private static GenreService genreService; // Service til genrer
    private static MovieService movieService; // Service til film
    private static ActorService actorService; // Service til skuespillere

    public static void main(String[] args) {
        emf = HibernateConfig.getEntityManagerFactory("movieRepository"); // Initialiserer EntityManagerFactory
        genreDAO = new GenreDAO(emf); // Opretter GenreDAO
        movieDAO = new MovieDAO(emf); // Opretter MovieDAO
        castDAO = new CastDAO(emf); // Opretter ActorDAO
        genreService = new GenreService(genreDAO); // Opretter GenreService
        movieService = new MovieService(movieDAO); // Opretter MovieService
        actorService = new ActorService(castDAO); // Opretter ActorService

        try {
            persistGenres(); // Gemmer genrer i databasen
            persistMovies(); // Henter og gemmer film i databasen
            persistActors(); // Henter og gemmer skuespillere i databasen

            displayAllMovies(); // Viser alle film
            displayAllGenres(); // Viser alle genrer
            displayMoviesByGenre(1L); // Viser film for en specifik genre ID

            // CRUD operationer
            addNewMovie(); // Tilføjer en ny film
            updateMovie(); // Opdaterer en eksisterende film
            deleteMovie(); // Sletter en film

            // Søgning og statistik
            searchMoviesByTitle("example"); // Søger efter film med en bestemt titel
            displayAverageRating(); // Viser den gennemsnitlige vurdering af alle film
            displayTop10LowestRatedMovies(); // Viser top-10 lavest vurderede film
            displayTop10HighestRatedMovies(); // Viser top-10 højest vurderede film
            displayTop10MostPopularMovies(); // Viser top-10 mest populære film
        } finally {
            emf.close(); // Lukker EntityManagerFactory
        }
    }

    private static void persistGenres() {
        List<GenreDTO> genres = genreService.fetchGenres(); // Henter genrer fra API
        GenreMapper genreMapper = new GenreMapper(); // Opretter GenreMapper

        EntityManager em = emf.createEntityManager(); // Skaber en EntityManager
        try {
            em.getTransaction().begin(); // Starter en transaktion
            for (GenreDTO genreDTO : genres) {
                Genre genreEntity = genreMapper.toEntity(genreDTO); // Mapper GenreDTO til Genre entitet
                em.merge(genreEntity); // Gemmer eller opdaterer genren i databasen
            }
            em.getTransaction().commit(); // Committer transaktionen
            System.out.println("Alle genrer er gemt succesfuldt.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Ruller tilbage ved fejl
            }
            e.printStackTrace(); // Logger fejl
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    private static void persistMovies() {
        List<MovieDTO> movies = movieService.fetchMovies(); // Henter film fra API

        EntityManager em = emf.createEntityManager(); // Skaber en EntityManager
        try {
            em.getTransaction().begin(); // Starter en transaktion
            for (MovieDTO movieDTO : movies) {
                Movie movieEntity = movieDTO.toEntity(); // Mapper MovieDTO til Movie entitet
                if (movieDTO.getGenre_ids() != null && !movieDTO.getGenre_ids().isEmpty()) {
                    for (Long genreId : movieDTO.getGenre_ids()) {
                        Genre genre = em.find(Genre.class, genreId); // Finder genre i databasen
                        if (genre != null) {
                            movieEntity.getGenres().add(genre); // Tilføjer genre til film
                        }
                    }
                }
                em.merge(movieEntity); // Gemmer eller opdaterer filmen i databasen
            }
            em.getTransaction().commit(); // Committer transaktionen
            System.out.println("Alle film er gemt succesfuldt.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Ruller tilbage ved fejl
            }
            e.printStackTrace(); // Logger fejl
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    private static void persistActors() {
        List<Actor> allActors = actorService.fetchAllActors(); // Henter alle skuespillere
        ActorMapper actorMapper = new ActorMapper(); // Opretter ActorMapper

        EntityManager em = emf.createEntityManager(); // Skaber en EntityManager
        try {
            em.getTransaction().begin(); // Starter en transaktion
            for (Actor actor : allActors) {
                Actor actorEntity = actorMapper.toEntity(actor); // Mapper Actor til Actor entitet
                for (Movie movieDTO : actor.getMovies()) {
                    Movie movieEntity = em.find(Movie.class, movieDTO.getId()); // Finder film i databasen
                    if (movieEntity != null) {
                        actorEntity.getMovies().add(movieEntity); // Tilføjer film til skuespiller
                        movieEntity.getActors().add(actorEntity); // Tilføjer skuespiller til film
                    } else {
                        System.out.println("Film ikke fundet for ID: " + movieDTO.getId());
                    }
                }
                em.merge(actorEntity); // Gemmer eller opdaterer skuespilleren i databasen
            }
            em.getTransaction().commit(); // Committer transaktionen
            System.out.println("Alle skuespillere og deres filmrelationer er gemt succesfuldt.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Ruller tilbage ved fejl
            }
            e.printStackTrace(); // Logger fejl
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    private static void displayAllMovies() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies(); // Henter alle film fra DAO
        System.out.println("Alle film:");
        allMovies.forEach(System.out::println); // Viser film
    }

    private static void displayAllGenres() {
        List<GenreDTO> allGenres = genreDAO.getAllGenres(); // Henter alle genrer fra DAO
        System.out.println("Alle genrer:");
        allGenres.forEach(System.out::println); // Viser genrer
    }

    private static void displayMoviesByGenre(Long genreId) {
        List<MovieDTO> moviesByGenre = genreDAO.getMoviesByGenre(genreId); // Henter film efter genre
        System.out.println("Film i Genre ID " + genreId + ":");
        moviesByGenre.forEach(System.out::println); // Viser film i genre
    }

    private static void addNewMovie() {
        MovieDTO newMovie = new MovieDTO(); // Opretter en ny film
        newMovie.setTitle("New Movie"); // Sætter filmens titel
        newMovie.setRelease_date("2024-01-01"); // Sætter udgivelsesdato
        movieService.createMovie(newMovie); // Kalder service for at oprette film
        System.out.println("Tilføjet ny film: " + newMovie);
    }

    private static void updateMovie() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies(); // Henter alle film
        if (!allMovies.isEmpty()) {
            MovieDTO movieToUpdate = allMovies.get(0); // opdaterer den første film
            movieToUpdate.setTitle("Updated Title"); // Opdaterer filmens titel
            movieToUpdate.setRelease_date("2024-02-01"); // Opdaterer udgivelsesdato hvis nødvendigt
            movieService.updateMovie(movieToUpdate); // Kalder service for at opdatere film
            System.out.println("Opdateret film: " + movieToUpdate);
        }
    }

    private static void deleteMovie() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies(); // Henter alle film
        if (!allMovies.isEmpty()) {
            Long movieIdToDelete = allMovies.get(0).getId(); // Eksempel: sletter den første film
            movieService.deleteMovie(movieIdToDelete); // Kalder service for at slette film
            System.out.println("Slettet film med ID: " + movieIdToDelete);
        }
    }

    private static void searchMoviesByTitle(String title) {
        List<MovieDTO> searchedMovies = movieService.searchMoviesByTitle(title); // Søger efter film
        System.out.println("Film der indeholder '" + title + "':");
        searchedMovies.forEach(System.out::println); // Viser fundne film
    }

    private static void displayAverageRating() {
        double averageRating = movieService.getTotalAverageRating(); // Henter gennemsnitlig vurdering
        System.out.println("Samlet gennemsnitsvurdering: " + averageRating);
    }

    private static void displayTop10LowestRatedMovies() {
        List<MovieDTO> top10LowestRated = movieService.getTop10LowestRatedMovies(); // Henter top-10 lavest vurderede film
        System.out.println("Top-10 lavest vurderede film:");
        top10LowestRated.forEach(System.out::println); // Viser film
    }

    private static void displayTop10HighestRatedMovies() {
        List<MovieDTO> top10HighestRated = movieService.getTop10HighestRatedMovies(); // Henter top-10 højest vurderede film
        System.out.println("Top-10 højest vurderede film:");
        top10HighestRated.forEach(System.out::println); // Viser film
    }

    private static void displayTop10MostPopularMovies() {
        List<MovieDTO> top10MostPopular = movieService.getTop10MostPopularMovies(); // Henter top-10 mest populære film
        System.out.println("Top-10 mest populære film:");
        top10MostPopular.forEach(System.out::println); // Viser film
    }
}
