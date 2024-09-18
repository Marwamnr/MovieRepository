package org.example;

import jakarta.persistence.EntityManager;
import org.example.config.HibernateConfig;
import org.example.daos.*;
import org.example.dtos.*;
import org.example.entities.Movie;
import org.example.services.*;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
        // Set up Hibernate
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieRepository");

        // Create DAO objects using Hibernate EntityManagerFactory
        MovieDAO movieDAO = MovieDAO.getInstance(emf);
        GenreDAO genreDAO = GenreDAO.getInstance(emf);
        ActorDAO actorDAO = ActorDAO.getInstance(emf);
        DirectorDAO directorDAO = DirectorDAO.getInstance(emf);

        // Create service objects
        MovieService movieService = new MovieService(movieDAO);
        GenreService genreService = new GenreService(genreDAO);
        ActorService actorService = new ActorService(actorDAO);
        DirectorService directorService = new DirectorService(directorDAO);

        EntityTransaction transaction = emf.createEntityManager().getTransaction();
        try {
            transaction.begin();

            // Step 1: Fetch and store data
            System.out.println("Fetching and storing movies...");
            List<MovieDTO> movies = movieService.fetchMovies(); // Fetches and stores movies

            System.out.println("Fetching and storing genres...");
            List<GenreDTO> genres = genreService.fetchGenres(); // Fetches and stores genres

            System.out.println("Fetching and storing actors...");
            List<ActorDTO> actors = actorService.fetchActors(); // Fetches and stores actors

            System.out.println("Fetching and storing directors...");
            List<DirectorDTO> directors = directorService.fetchDirectors(); // Fetches and stores directors

            transaction.commit();

            // Step 2: Retrieve and display data
            System.out.println("\nAll Movies:");
            List<MovieDTO> allMovies = movieService.getAllMovies();
            allMovies.forEach(System.out::println);

            System.out.println("\nAll Genres:");
            List<GenreDTO> allGenres = genreService.getAllGenres();
            allGenres.forEach(System.out::println);

            System.out.println("\nAll Actors:");
            List<ActorDTO> allActors = actorService.getAllActors();
            allActors.forEach(System.out::println);

            System.out.println("\nAll Directors:");
            List<DirectorDTO> allDirectors = directorService.getAllDirectors();
            allDirectors.forEach(System.out::println);

            // Example search for a movie by title
            String searchTitle = "Inception";
            System.out.println("\nSearch Results for title containing \"" + searchTitle + "\":");
            List<MovieDTO> searchResults = movieService.searchMoviesByTitle(searchTitle);
            searchResults.forEach(System.out::println);

            // Example of calculating average rating and top movies
            System.out.println("\nAverage Movie Rating: " + movieService.getAverageRating());

            System.out.println("\nTop 10 Highest Rated Movies:");
            List<MovieDTO> top10HighestRated = movieService.getTop10HighestRated();
            top10HighestRated.forEach(System.out::println);

            System.out.println("\nTop 10 Lowest Rated Movies:");
            List<MovieDTO> top10LowestRated = movieService.getTop10LowestRated();
            top10LowestRated.forEach(System.out::println);

            System.out.println("\nTop 10 Most Popular Movies:");
            List<MovieDTO> top10MostPopular = movieService.getTop10MostPopular();
            top10MostPopular.forEach(System.out::println);

            // Example of adding, updating, and deleting a movie
            MovieDTO newMovie = new MovieDTO();
            newMovie.setTitle("New Movie");
            newMovie.setYear(2024); // Use `year` instead of `releaseDate`
            movieService.addMovie(newMovie);
            System.out.println("\nAdded new movie: " + newMovie);

            // Assume you have a movie ID to update and delete
            Long movieIdToUpdate = 1L; // Example ID
            MovieDTO movieToUpdate = movieService.getMovieById(movieIdToUpdate);
            if (movieToUpdate != null) {
                movieToUpdate.setTitle("Updated Movie Title");
                movieService.updateMovie(movieToUpdate);
                System.out.println("\nUpdated movie: " + movieToUpdate);
            }

            Long movieIdToDelete = 2L; // Example ID
            movieService.deleteMovie(movieIdToDelete);
            System.out.println("\nDeleted movie with ID: " + movieIdToDelete);

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

         */

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieRepository");



        MovieService movieService = new MovieService();


        List<MovieDTO> movies = movieService.fetchMovies();


        MovieMapper movieMapper = new MovieMapper();


        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();


            for (MovieDTO movieDTO : movies) {

                Movie movieEntity = movieMapper.toEntity(movieDTO);

                em.persist(movieEntity);
            }
            em.getTransaction().commit();

            System.out.println("All movies persisted successfully.");
        } finally {

            em.close();
            emf.close();
        }
    }
}
