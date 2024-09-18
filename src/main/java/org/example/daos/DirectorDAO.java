package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.example.dtos.DirectorDTO;
import org.example.entities.Director;

import java.util.List;

public class DirectorDAO {

    private static DirectorDAO instance;
    private static EntityManagerFactory emf;

    public DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public static DirectorDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new DirectorDAO(emf);
        }
        return instance;
    }

    // CREATE
    public DirectorDTO createDirector(DirectorDTO directorDTO) {
        Director director = directorDTO.toEntity();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(director);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
        return DirectorDTO.fromEntity(director);
    }

    // READ BY ID
    public DirectorDTO getDirectorById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Director director = em.find(Director.class, id);
            return director != null ? DirectorDTO.fromEntity(director) : null;
        } finally {
            em.close();
        }
    }

    // UPDATE
    public DirectorDTO updateDirector(DirectorDTO directorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Director director = em.find(Director.class, directorDTO.getId());
            if (director == null) {
                throw new IllegalArgumentException("Director with ID " + directorDTO.getId() + " not found.");
            }

            // Update basic fields
            director.setName(directorDTO.getName());

            em.merge(director);
            em.getTransaction().commit();
            return DirectorDTO.fromEntity(director);

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
    public void deleteDirector(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Director director = em.find(Director.class, id);
            if (director != null) {
                em.remove(director);
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

    // GET ALL DIRECTORS
    public List<DirectorDTO> getAllDirectors() {
        EntityManager em = emf.createEntityManager();
        try {
            // Use JPQL to select the Director entity
            TypedQuery<Director> query = em.createQuery("SELECT d FROM Director d", Director.class);
            List<Director> directors = query.getResultList();
            // Convert the list of entities to DTOs
            return directors.stream().map(DirectorDTO::fromEntity).toList();
        } finally {
            em.close();
        }
    }
}
