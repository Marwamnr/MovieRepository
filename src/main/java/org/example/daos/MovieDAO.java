package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.dtos.MovieDTO;
import org.example.entities.Director;
import org.example.entities.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieDAO {

    private static MovieDAO instance;
    private static EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static MovieDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new MovieDAO(emf);
        }
        return instance;
    }

    // CREATE
    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = movieDTO.toEntity();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Handle Director
            if (movieDTO.getDirector() != null) {
                Director director = em.find(Director.class, movieDTO.getDirector().getId());
                if (director != null) {
                    movie.setDirector(director);
                } else {
                    throw new IllegalArgumentException("Director with ID " + movieDTO.getDirector().getId() + " not found.");
                }
            }

            em.persist(movie);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return MovieDTO.fromEntity(movie);
    }

    // READ BY ID
    public MovieDTO getMovieById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Movie movie = em.find(Movie.class, id);
            return movie != null ? MovieDTO.fromEntity(movie) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Movie movie = em.find(Movie.class, movieDTO.getId());
            if (movie == null) {
                throw new IllegalArgumentException("Movie with ID " + movieDTO.getId() + " not found.");
            }

            // Update basic fields
            movie.setTitle(movieDTO.getTitle());
            movie.setYear(movieDTO.getYear());
            movie.setRating(movieDTO.getRating());

            // Handle Director
            if (movieDTO.getDirector() != null) {
                Director director = em.find(Director.class, movieDTO.getDirector().getId());
                if (director != null) {
                    movie.setDirector(director);
                } else {
                    throw new IllegalArgumentException("Director with ID " + movieDTO.getDirector().getId() + " not found.");
                }
            }

            // Merge changes
            em.merge(movie);
            em.getTransaction().commit();

            return MovieDTO.fromEntity(movie);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void deleteMovie(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) {
                em.remove(movie);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // GET ALL MOVIES
    public List<MovieDTO> getAllMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
            List<Movie> movies = query.getResultList();
            return movies.stream()
                    .map(MovieDTO::fromEntity)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}
