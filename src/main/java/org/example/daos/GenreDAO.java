package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.dtos.GenreDTO;
import org.example.entities.Genre;

import java.util.List;

public class GenreDAO {

    private static GenreDAO instance;
    private EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static GenreDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new GenreDAO(emf);
        }
        return instance;
    }

    // CREATE
    public GenreDTO createGenre(GenreDTO genreDTO) {
        Genre genre = genreDTO.toEntity();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return GenreDTO.fromEntity(genre);
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
        try {
            em.getTransaction().begin();

            Genre genre = em.find(Genre.class, genreDTO.getId());
            if (genre == null) {
                throw new IllegalArgumentException("Genre with ID " + genreDTO.getId() + " not found.");
            }

            // Update basic fields
            genre.setName(genreDTO.getName());

            em.merge(genre);
            em.getTransaction().commit();
            return GenreDTO.fromEntity(genre);

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
    public void deleteGenre(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Genre genre = em.find(Genre.class, id);
            if (genre != null) {
                em.remove(genre);
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

    // GET ALL GENRES
    public List<GenreDTO> getAllGenres() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
            List<Genre> genres = query.getResultList();
            return genres.stream()
                    .map(GenreDTO::fromEntity)
                    .toList();
        } finally {
            em.close();
        }
    }
}
