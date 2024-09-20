package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.DirectorDTO;
import org.example.entities.Director;

import java.util.List;

public class DirectorDAO {

    private final EntityManagerFactory emf;

    public DirectorDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // CREATE
    public DirectorDTO createDirector(DirectorDTO directorDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Director director = directorDTO.toEntity();
            em.persist(director);
            tx.commit();
            return DirectorDTO.fromEntity(director);
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
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Director director = em.find(Director.class, directorDTO.getId());
            if (director == null) {
                throw new IllegalArgumentException("Director with ID " + directorDTO.getId() + " not found.");
            }
            director.setName(directorDTO.getName());
            em.merge(director);
            tx.commit();
            return DirectorDTO.fromEntity(director);
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
    public void deleteDirector(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Director director = em.find(Director.class, id);
            if (director != null) {
                em.remove(director);
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

    // GET ALL DIRECTORS
    public List<DirectorDTO> getAllDirectors() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Director> query = em.createQuery("SELECT d FROM Director d", Director.class);
            List<Director> directors = query.getResultList();
            return directors.stream().map(DirectorDTO::fromEntity).toList();
        } finally {
            em.close();
        }
    }
}
