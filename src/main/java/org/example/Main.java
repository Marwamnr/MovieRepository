package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.daos.GenreDAO;
import org.example.daos.MovieDAO;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Genre;
import org.example.entities.Movie;
import org.example.services.GenreService;
import org.example.services.MovieService;
import org.example.services.GenreMapper;

import java.util.List;

public class Main {

    private static EntityManagerFactory emf;
    private static GenreDAO genreDAO;
    private static MovieDAO movieDAO;
    private static GenreService genreService;
    private static MovieService movieService;

    public static void main(String[] args) {
        emf = HibernateConfig.getEntityManagerFactory("movieRepository");
        genreDAO = new GenreDAO(emf);
        movieDAO = new MovieDAO(emf);
        genreService = new GenreService(genreDAO);
        movieService = new MovieService(movieDAO);

        try {
            // Persist genres
            persistGenres();

            // Fetch and persist movies
            persistMovies();

            // Example usage of DAO methods
            displayAllMovies();
            displayAllGenres();
            displayMoviesByGenre(1L); // Example genre ID

            // CRUD operations
            addNewMovie();
            updateMovie();
            deleteMovie();

            // Search and statistics
            searchMoviesByTitle("example");
            displayAverageRating();
            displayTop10LowestRatedMovies();
            displayTop10HighestRatedMovies();
            displayTop10MostPopularMovies();
        } finally {
            emf.close();
        }
    }

    private static void persistGenres() {
        List<GenreDTO> genres = genreService.fetchGenres();
        GenreMapper genreMapper = new GenreMapper();

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (GenreDTO genreDTO : genres) {
                Genre genreEntity = genreMapper.toEntity(genreDTO);
                em.merge(genreEntity); // Use merge to persist or update
            }
            em.getTransaction().commit();
            System.out.println("All genres persisted successfully.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void persistMovies() {
        List<MovieDTO> movies = movieService.fetchMovies();

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (MovieDTO movieDTO : movies) {
                Movie movieEntity = movieDTO.toEntity();  // Convert DTO to entity
                if (movieDTO.getGenre_ids() != null && !movieDTO.getGenre_ids().isEmpty()) {
                    for (Long genreId : movieDTO.getGenre_ids()) {
                        Genre genre = em.find(Genre.class, genreId);
                        if (genre != null) {
                            movieEntity.getGenres().add(genre);
                        }
                    }
                }
                em.merge(movieEntity); // Use merge to persist or update
            }
            em.getTransaction().commit();
            System.out.println("All movies persisted successfully.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Rollback in case of error
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void displayAllMovies() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies();
        System.out.println("All Movies:");
        allMovies.forEach(System.out::println);
    }

    private static void displayAllGenres() {
        List<GenreDTO> allGenres = genreDAO.getAllGenres();
        System.out.println("All Genres:");
        allGenres.forEach(System.out::println);
    }

    private static void displayMoviesByGenre(Long genreId) {
        List<MovieDTO> moviesByGenre = genreDAO.getMoviesByGenre(genreId);
        System.out.println("Movies in Genre ID " + genreId + ":");
        moviesByGenre.forEach(System.out::println);
    }


    private static void addNewMovie() {
        MovieDTO newMovie = new MovieDTO();
        newMovie.setTitle("New Movie");
        newMovie.setRelease_date("2024-01-01");
        movieService.createMovie(newMovie);
        System.out.println("Added new movie: " + newMovie);
    }

    private static void updateMovie() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies();
        if (!allMovies.isEmpty()) {
            MovieDTO movieToUpdate = allMovies.get(0); // Example: update the first movie
            movieToUpdate.setTitle("Updated Title");
            movieToUpdate.setRelease_date("2024-02-01"); // Update release date if needed
            movieService.updateMovie(movieToUpdate);
            System.out.println("Updated movie: " + movieToUpdate);
        }
    }

    private static void deleteMovie() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies();
        if (!allMovies.isEmpty()) {
            Long movieIdToDelete = allMovies.get(0).getId(); // Example: delete the first movie
            movieService.deleteMovie(movieIdToDelete);
            System.out.println("Deleted movie with ID: " + movieIdToDelete);
        }
    }

    private static void searchMoviesByTitle(String title) {
        List<MovieDTO> searchedMovies = movieService.searchMoviesByTitle(title);
        System.out.println("Movies containing '" + title + "':");
        searchedMovies.forEach(System.out::println);
    }

    private static void displayAverageRating() {
        double averageRating = movieService.getTotalAverageRating();
        System.out.println("Total Average Rating: " + averageRating);
    }

    private static void displayTop10LowestRatedMovies() {
        List<MovieDTO> top10LowestRated = movieService.getTop10LowestRatedMovies();
        System.out.println("Top-10 Lowest Rated Movies:");
        top10LowestRated.forEach(System.out::println);
    }

    private static void displayTop10HighestRatedMovies() {
        List<MovieDTO> top10HighestRated = movieService.getTop10HighestRatedMovies();
        System.out.println("Top-10 Highest Rated Movies:");
        top10HighestRated.forEach(System.out::println);
    }

    private static void displayTop10MostPopularMovies() {
        List<MovieDTO> top10MostPopular = movieService.getTop10MostPopularMovies();
        System.out.println("Top-10 Most Popular Movies:");
        top10MostPopular.forEach(System.out::println);
    }
}
