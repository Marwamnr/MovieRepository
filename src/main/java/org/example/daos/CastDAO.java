package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.CastDTO; // Using CastDTO
import org.example.entities.Actor;

import java.util.List;

public class CastDAO {

    private final EntityManagerFactory emf; // Initialiserer EntityManagerFactory

    public CastDAO(EntityManagerFactory emf) {
        this.emf = emf; // Gemmer EntityManagerFactory
    }

    // Opretter en ny skuespiller
    public CastDTO createActor(CastDTO actorDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Actor actor = actorDTO.toEntity(); // Konverterer DTO til entitet
            em.persist(actor); // Gemmer skuespilleren
            tx.commit(); // Bekræfter transaktionen
            return CastDTO.fromEntity(actor); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter skuespiller efter ID
    public CastDTO getActorById(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            Actor actor = em.find(Actor.class, id); // Finder skuespilleren
            return actor != null ? CastDTO.fromEntity(actor) : null; // Returnerer DTO eller null
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Opdaterer en eksisterende skuespiller
    public CastDTO updateActor(CastDTO actorDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Actor actor = em.find(Actor.class, actorDTO.getId()); // Finder skuespilleren
            if (actor == null) {
                throw new IllegalArgumentException("Actor with ID " + actorDTO.getId() + " not found."); // Kaster fejl hvis ikke fundet
            }
            actor.setName(actorDTO.getName()); // Opdaterer navn
            actor.setCharacter(actorDTO.getCharacter()); // Opdaterer karakter
            actor.setDepartment(actorDTO.getKnown_for_department()); // Opdaterer afdeling
            em.merge(actor); // Merges ændringer
            tx.commit(); // Bekræfter transaktionen
            return CastDTO.fromEntity(actor); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Sletter en skuespiller
    public void deleteActor(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Actor actor = em.find(Actor.class, id); // Finder skuespilleren
            if (actor != null) {
                em.remove(actor); // Fjerner skuespilleren
                tx.commit(); // Bekræfter transaktionen
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter alle skuespillere
    public List<CastDTO> getAllActors() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Actor> query = em.createQuery("SELECT a FROM Actor a", Actor.class); // Opretter forespørgsel
            List<Actor> actors = query.getResultList(); // Henter resultater
            return actors.stream().map(CastDTO::fromEntity).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }
}
