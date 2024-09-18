package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.dtos.ActorDTO;
import org.example.entities.Actor;

import java.util.List;

public class ActorDAO {

    private static ActorDAO instance;
    private EntityManagerFactory emf;

    public ActorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static ActorDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new ActorDAO(emf);
        }
        return instance;
    }

    // CREATE
    public ActorDTO createActor(ActorDTO actorDTO) {
        Actor actor = actorDTO.toEntity();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(actor);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return ActorDTO.fromEntity(actor);
    }

    // READ BY ID
    public ActorDTO getActorById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Actor actor = em.find(Actor.class, id);
            return actor != null ? ActorDTO.fromEntity(actor) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public ActorDTO updateActor(ActorDTO actorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Actor actor = em.find(Actor.class, actorDTO.getId());
            if (actor == null) {
                throw new IllegalArgumentException("Actor with ID " + actorDTO.getId() + " not found.");
            }

            // Update basic fields
            actor.setName(actorDTO.getName());

            em.merge(actor);
            em.getTransaction().commit();
            return ActorDTO.fromEntity(actor);

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
    public void deleteActor(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Actor actor = em.find(Actor.class, id);
            if (actor != null) {
                em.remove(actor);
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

    // GET ALL ACTORS
    public List<ActorDTO> getAllActors() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Actor> query = em.createQuery("SELECT a FROM Actor a", Actor.class);
            List<Actor> actors = query.getResultList();
            return actors.stream()
                    .map(ActorDTO::fromEntity)
                    .toList();
        } finally {
            em.close();
        }
    }
}
