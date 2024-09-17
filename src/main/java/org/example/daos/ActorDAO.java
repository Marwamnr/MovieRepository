package org.example.daos;

import org.example.entities.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ActorDAO implements GenericDAO<Actor> {

    private static ActorDAO instance;
    private static EntityManagerFactory emf;

    // Privat constructor for at sikre singleton pattern
    private ActorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Returnerer en singleton-instans af ActorDAO
    public static ActorDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new ActorDAO(emf);
        }
        return instance;
    }

    // Opretter en ny Actor i databasen (CREATE)
    @Override
    public Actor create(Actor actor) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();  // Starter en ny transaktion
            em.persist(actor);  // Gemmer den nye actor i databasen
            em.getTransaction().commit();  // Committer transaktionen
        }
        return actor;  // Returnerer den oprettede actor
    }

    // Opdaterer en eksisterende Actor i databasen (UPDATE)
    @Override
    public Actor update(Actor actor) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Actor existingActor = em.find(Actor.class, actor.getId());  // Finder Actor ved ID
            if (existingActor == null) {
                throw new IllegalArgumentException("Actor med ID " + actor.getId() + " blev ikke fundet.");
            }

            existingActor.setName(actor.getName());  // Opdaterer actorens navn

            em.merge(existingActor);  // Slår opdateringerne sammen med eksisterende data
            em.getTransaction().commit();  // Committer transaktionen
            return existingActor;  // Returnerer den opdaterede actor

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Ruller tilbage, hvis der er en fejl
            }
            throw e;  // Kaster undtagelsen videre
        } finally {
            em.close();  // Lukker EntityManager
        }
    }

    // Finder en Actor ved ID (READ)
    @Override
    public Actor findById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Actor.class, id);  // Finder og returnerer Actor ved ID
        }
    }

    // Henter alle Actor-objekter fra databasen (READ ALL)
    @Override
    public List<Actor> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT a FROM Actor a", Actor.class).getResultList();  // Henter alle Actor-objekter
        }
    }

    // Sletter en Actor ved ID fra databasen (DELETE)
    @Override
    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, id);  // Finder Actor ved ID
            if (actor != null) {
                em.remove(actor);  // Sletter actor, hvis den findes
            }
            em.getTransaction().commit();  // Committer transaktionen
        } finally {
            em.close();  // Lukker EntityManager
        }
    }
}

