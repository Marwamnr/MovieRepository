package org.example.daos;

import org.example.config.HibernateConfig;
import org.example.dtos.DirectorDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorDAOTest {

    private static final EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private static final DirectorDAO directorDAO = new DirectorDAO(emfTest);
    private DirectorDTO director1;
    private DirectorDTO director2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Director").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE director_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            // Creating initial directors
            director1 = new DirectorDTO(null, "Christopher Nolan", null);
            director2 = new DirectorDTO(null, "Steven Spielberg", null);

            director1 = directorDAO.createDirector(director1);
            director2 = directorDAO.createDirector(director2);
        }
    }

    @Test
    void createDirector() {
        DirectorDTO director3 = new DirectorDTO(null, "Quentin Tarantino", null);
        director3 = directorDAO.createDirector(director3);
        assertNotNull(director3.getId());
        assertEquals("Quentin Tarantino", director3.getName());
    }

    @Test
    void getDirectorById() {
        DirectorDTO foundDirector = directorDAO.getDirectorById(director1.getId());
        assertNotNull(foundDirector);
        assertEquals(director1.getName(), foundDirector.getName());
    }

    @Test
    void updateDirector() {
        director1.setName("Christopher Nolan Updated");
        DirectorDTO updatedDirector = directorDAO.updateDirector(director1);
        assertEquals("Christopher Nolan Updated", updatedDirector.getName());
    }

    @Test
    void deleteDirector() {
        directorDAO.deleteDirector(director2.getId());
        assertNull(directorDAO.getDirectorById(director2.getId()));
    }

    @Test
    void getAllDirectors() {
        List<DirectorDTO> directors = directorDAO.getAllDirectors();
        assertEquals(2, directors.size());
    }
}
