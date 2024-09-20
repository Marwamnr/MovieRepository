package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.CastDTO;
import org.example.entities.Actor;

import java.util.List;

public class ActorDAO {

    private final EntityManagerFactory emf;



    public ActorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // CREATE
    public CastDTO createActor(CastDTO actorDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Actor actor = actorDTO.toEntity();
            em.persist(actor);
            tx.commit();
            return CastDTO.fromEntity(actor);
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
    public CastDTO getActorById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Actor actor = em.find(Actor.class, id);
            return actor != null ? CastDTO.fromEntity(actor) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public CastDTO updateActor(CastDTO actorDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Actor actor = em.find(Actor.class, actorDTO.getId());
            if (actor == null) {
                throw new IllegalArgumentException("Actor with ID " + actorDTO.getId() + " not found.");
            }
            actor.setName(actorDTO.getName());
            em.merge(actor);
            tx.commit();
            return CastDTO.fromEntity(actor);
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
    public void deleteActor(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Actor actor = em.find(Actor.class, id);
            if (actor != null) {
                em.remove(actor);
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

    // GET ALL ACTORS
    public List<CastDTO> getAllActors() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Actor> query = em.createQuery("SELECT a FROM Actor a", Actor.class);
            List<Actor> actors = query.getResultList();
            return actors.stream().map(CastDTO::fromEntity).toList();
        } finally {
            em.close();
        }
    }
}

