package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Genre;
import org.example.entities.Movie;

import java.util.List;

public class GenreDAO {

    private final EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // CREATE
    public GenreDTO createGenre(GenreDTO genreDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Genre genre = genreDTO.toEntity();
            em.persist(genre);
            tx.commit();
            return GenreDTO.fromEntity(genre);
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
    public GenreDTO getGenreById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Genre genre = em.find(Genre.class, id);
            return genre != null ? GenreDTO.fromEntity(genre) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public GenreDTO updateGenre(GenreDTO genreDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Genre genre = em.find(Genre.class, genreDTO.getId());
            if (genre == null) {
                throw new IllegalArgumentException("Genre with ID " + genreDTO.getId() + " not found.");
            }
            genre.setName(genreDTO.getName());
            em.merge(genre);
            tx.commit();
            return GenreDTO.fromEntity(genre);
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
    public void deleteGenre(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Genre genre = em.find(Genre.class, id);
            if (genre != null) {
                em.remove(genre);
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

    // GET ALL GENRES
    public List<GenreDTO> getAllGenres() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
            List<Genre> genres = query.getResultList();
            return genres.stream().map(GenreDTO::fromEntity).toList();
        } finally {
            em.close();
        }
    }

        // GET MOVIES BY GENRE
        public List<MovieDTO> getMoviesByGenre(Long genreId) {
            EntityManager em = emf.createEntityManager();
            try {
                TypedQuery<Movie> query = em.createQuery(
                        "SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId", Movie.class);
                query.setParameter("genreId", genreId);
                List<Movie> movies = query.getResultList();
                return movies.stream().map(MovieDTO::new).toList();
            } finally {
                em.close();
            }
        }
}
