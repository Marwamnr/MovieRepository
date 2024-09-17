package org.example.daos;

import org.example.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class MovieDAO implements GenericDAO<Movie> {

    private static MovieDAO instance;
    private static EntityManagerFactory emf;

    // Privat constructor for at sikre singleton pattern
    private MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Returnerer en singleton-instans af MovieDAO
    public static MovieDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new MovieDAO(emf);
        }
        return instance;
    }

    // Opretter en ny Movie i databasen (CREATE)
    @Override
    public Movie create(Movie movie) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();  // Starter en ny transaktion
            em.persist(movie);  // Gemmer den nye movie i databasen
            em.getTransaction().commit();  // Committer transaktionen
        }
        return movie;  // Returnerer den oprettede movie
    }

    // Opdaterer en eksisterende Movie i databasen (UPDATE)
    @Override
    public Movie update(Movie movie) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Movie existingMovie = em.find(Movie.class, movie.getId());  // Finder Movie ved ID
            if (existingMovie == null) {
                throw new IllegalArgumentException("Movie med ID " + movie.getId() + " blev ikke fundet.");
            }

            // Opdaterer Movie-objektets felter
            existingMovie.setTitle(movie.getTitle());
            existingMovie.setYear(movie.getYear());
            existingMovie.setRating(movie.getRating());
            existingMovie.setActors(movie.getActors());
            existingMovie.setGenres(movie.getGenres());
            existingMovie.setDirector(movie.getDirector());

            em.merge(existingMovie);  // Slår opdateringerne sammen med eksisterende data
            em.getTransaction().commit();  // Committer transaktionen
            return existingMovie;  // Returnerer den opdaterede movie

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Ruller tilbage, hvis der er en fejl
            }
            throw e;  // Kaster undtagelsen videre
        } finally {
            em.close();  // Lukker EntityManager
        }
    }

    // Finder en Movie ved ID (READ)
    @Override
    public Movie findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Movie.class, id);  // Finder og returnerer Movie ved ID
        }
    }

    // Henter alle Movie-objekter fra databasen (READ ALL)
    @Override
    public List<Movie> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();  // Henter alle Movie-objekter
        }
    }

    // Sletter en Movie ved ID fra databasen (DELETE)
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);  // Finder Movie ved ID
            if (movie != null) {
                em.remove(movie);  // Sletter movie, hvis den findes
            }
            em.getTransaction().commit();  // Committer transaktionen
        } finally {
            em.close();  // Lukker EntityManager
        }
    }
}
