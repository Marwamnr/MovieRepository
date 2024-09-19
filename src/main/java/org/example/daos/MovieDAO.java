package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.MovieDTO;
import org.example.entities.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieDAO {

    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // CREATE
    public MovieDTO createMovie(MovieDTO movieDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Movie movie = movieDTO.toEntity();
            em.persist(movie);
            tx.commit();
            return new MovieDTO(movie);  // Use the constructor to convert the entity back to DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // READ BY ID
    public MovieDTO getMovieById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Movie movie = em.find(Movie.class, id);
            return movie != null ? new MovieDTO(movie) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Movie movie = em.find(Movie.class, movieDTO.getId());
            if (movie == null) {
                throw new IllegalArgumentException("Movie with ID " + movieDTO.getId() + " not found.");
            }
            // Update fields
            movie.setTitle(movieDTO.getTitle());
            movie.setRelease_date(movieDTO.getRelease_date());
            movie.setRating(movieDTO.getVote_average());
            movie.setPopularity(movieDTO.getPopularity());
            // Optionally update other fields or relationships

            em.merge(movie);
            tx.commit();
            return new MovieDTO(movie);
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // DELETE
    public void deleteMovie(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) {
                em.remove(movie);
                tx.commit();
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
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
            return em.createQuery("SELECT m FROM Movie m JOIN FETCH m.genres JOIN FETCH m.actors JOIN FETCH m.director", Movie.class)
                    .getResultList()
                    .stream()
                    .map(MovieDTO::fromEntity)
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    // SEARCH MOVIES BY TITLE
    public List<MovieDTO> searchMoviesByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:title)", Movie.class);
            query.setParameter("title", "%" + title + "%");
            List<Movie> movies = query.getResultList();
            return movies.stream().map(MovieDTO::new).toList();
        } finally {
            em.close();
        }
    }

    // GET AVERAGE RATING
    public double getAverageRating() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(m.rating) FROM Movie m", Double.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // GET TOP-10 LOWEST RATED MOVIES
    public List<MovieDTO> getTop10LowestRatedMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m ORDER BY m.rating ASC", Movie.class);
            query.setMaxResults(10);
            List<Movie> movies = query.getResultList();
            return movies.stream().map(MovieDTO::new).toList();
        } finally {
            em.close();
        }
    }

    // GET TOP-10 HIGHEST RATED MOVIES
    public List<MovieDTO> getTop10HighestRatedMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m ORDER BY m.rating DESC", Movie.class);
            query.setMaxResults(10);
            List<Movie> movies = query.getResultList();
            return movies.stream().map(MovieDTO::new).toList();
        } finally {
            em.close();
        }
    }

    // GET TOP-10 MOST POPULAR MOVIES
    public List<MovieDTO> getTop10MostPopularMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m ORDER BY m.popularity DESC", Movie.class);
            query.setMaxResults(10);
            List<Movie> movies = query.getResultList();
            return movies.stream().map(MovieDTO::new).toList();
        } finally {
            em.close();
        }
    }
}

