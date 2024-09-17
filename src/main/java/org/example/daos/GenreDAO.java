package org.example.daos;

import org.example.entities.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class GenreDAO implements GenericDAO<Genre> {

    private static GenreDAO instance;
    private static EntityManagerFactory emf;

    // Privat constructor for at sikre singleton pattern
    private GenreDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Returnerer en singleton-instans af GenreDAO
    public static GenreDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new GenreDAO(emf);
        }
        return instance;
    }

    // Opretter en ny Genre i databasen (CREATE)
    @Override
    public Genre create(Genre genre) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();  // Starter en ny transaktion
            em.persist(genre);  // Gemmer den nye genre i databasen
            em.getTransaction().commit();  // Committer transaktionen
        }
        return genre;  // Returnerer den oprettede genre
    }

    // Opdaterer en eksisterende Genre i databasen (UPDATE)
    @Override
    public Genre update(Genre genre) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Genre existingGenre = em.find(Genre.class, genre.getId());  // Finder Genre ved ID
            if (existingGenre == null) {
                throw new IllegalArgumentException("Genre med ID " + genre.getId() + " blev ikke fundet.");
            }

            existingGenre.setName(genre.getName());  // Opdaterer genreens navn

            em.merge(existingGenre);  // Slår opdateringerne sammen med eksisterende data
            em.getTransaction().commit();  // Committer transaktionen
            return existingGenre;  // Returnerer den opdaterede genre

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Ruller tilbage, hvis der er en fejl
            }
            throw e;  // Kaster undtagelsen videre
        } finally {
            em.close();  // Lukker EntityManager
        }
    }

    // Finder en Genre ved ID (READ)
    @Override
    public Genre findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);  // Finder og returnerer Genre ved ID
        }
    }

    // Henter alle Genre-objekter fra databasen (READ ALL)
    @Override
    public List<Genre> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT g FROM Genre g", Genre.class).getResultList();  // Henter alle Genre-objekter
        }
    }

    // Sletter en Genre ved ID fra databasen (DELETE)
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Genre genre = em.find(Genre.class, id);  // Finder Genre ved ID
            if (genre != null) {
                em.remove(genre);  // Sletter genre, hvis den findes
            }
            em.getTransaction().commit();  // Committer transaktionen
        } finally {
            em.close();  // Lukker EntityManager
        }
    }
}
