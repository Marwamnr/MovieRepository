package org.example.daos;

import org.example.entities.Director;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DirectorDAO implements GenericDAO<Director> {

    private static DirectorDAO instance;
    private static EntityManagerFactory emf;

    // Privat constructor for at sikre singleton pattern
    private DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Returnerer en singleton-instans af DirectorDAO
    public static DirectorDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new DirectorDAO(emf);
        }
        return instance;
    }

    // Opretter en ny Director i databasen (CREATE)
    @Override
    public Director create(Director director) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();  // Starter en ny transaktion
            em.persist(director);  // Gemmer den nye director i databasen
            em.getTransaction().commit();  // Committer transaktionen
        }
        return director;  // Returnerer den oprettede director
    }

    // Opdaterer en eksisterende Director i databasen (UPDATE)
    @Override
    public Director update(Director director) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Director existingDirector = em.find(Director.class, director.getName());  // Finder Director ved navn (ID)
            if (existingDirector == null) {
                throw new IllegalArgumentException("Director med navn " + director.getName() + " blev ikke fundet.");
            }

            existingDirector.setName(director.getName());  // Opdaterer directorens navn

            em.merge(existingDirector);  // Slår opdateringerne sammen med eksisterende data
            em.getTransaction().commit();  // Committer transaktionen
            return existingDirector;  // Returnerer den opdaterede director

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Ruller tilbage, hvis der er en fejl
            }
            throw e;  // Kaster undtagelsen videre
        } finally {
            em.close();  // Lukker EntityManager
        }
    }

    // Finder en Director ved navn (READ)
    @Override
    public Director findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Director.class, id);  // Finder og returnerer Director ved navn (ID)
        }
    }

    // Henter alle Director-objekter fra databasen (READ ALL)
    @Override
    public List<Director> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT d FROM Director d", Director.class).getResultList();  // Henter alle Director-objekter
        }
    }

    // Sletter en Director ved navn (DELETE)
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Director director = em.find(Director.class, id);  // Finder Director ved navn (ID)
            if (director != null) {
                em.remove(director);  // Sletter director, hvis den findes
            }
            em.getTransaction().commit();  // Committer transaktionen
        } finally {
            em.close();  // Lukker EntityManager
        }
    }
}
