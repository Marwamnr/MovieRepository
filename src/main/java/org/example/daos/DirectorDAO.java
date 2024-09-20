package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.DirectorDTO;
import org.example.entities.Director;

import java.util.List;

public class DirectorDAO {

    private final EntityManagerFactory emf; // Initialiserer EntityManagerFactory

    public DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf; // Gemmer EntityManagerFactory
    }

    // Opretter en ny instruktør
    public DirectorDTO createDirector(DirectorDTO directorDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Director director = directorDTO.toEntity(); // Konverterer DTO til entitet
            em.persist(director); // Gemmer instruktøren
            tx.commit(); // Bekræfter transaktionen
            return DirectorDTO.fromEntity(director); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter instruktør efter ID
    public DirectorDTO getDirectorById(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            Director director = em.find(Director.class, id); // Finder instruktøren
            return director != null ? DirectorDTO.fromEntity(director) : null; // Returnerer DTO eller null
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Opdaterer en eksisterende instruktør
    public DirectorDTO updateDirector(DirectorDTO directorDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Director director = em.find(Director.class, directorDTO.getId()); // Finder instruktøren
            if (director == null) {
                throw new IllegalArgumentException("Director with ID " + directorDTO.getId() + " not found."); // Kaster fejl hvis ikke fundet
            }
            director.setName(directorDTO.getName()); // Opdaterer navn
            em.merge(director); // Merges ændringer
            tx.commit(); // Bekræfter transaktionen
            return DirectorDTO.fromEntity(director); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Sletter en instruktør
    public void deleteDirector(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Director director = em.find(Director.class, id); // Finder instruktøren
            if (director != null) {
                em.remove(director); // Fjerner instruktøren
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

    // Henter alle instruktører
    public List<DirectorDTO> getAllDirectors() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Director> query = em.createQuery("SELECT d FROM Director d", Director.class); // Opretter forespørgsel
            List<Director> directors = query.getResultList(); // Henter resultater
            return directors.stream().map(DirectorDTO::fromEntity).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }
}
