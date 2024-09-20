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
    private DirectorDTO director1; // Første instruktør som DirectorDTO
    private DirectorDTO director2; // Anden instruktør som DirectorDTO

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Director").executeUpdate(); // Tømmer Director tabellen
            em.createNativeQuery("ALTER SEQUENCE director_id_seq RESTART WITH 1").executeUpdate(); // Nulstiller sekvens
            em.getTransaction().commit();

            // Opretter initiale instruktører
            director1 = new DirectorDTO(null, "Christopher Nolan", null); // Opretter director1
            director2 = new DirectorDTO(null, "Steven Spielberg", null); // Opretter director2

            director1 = directorDAO.createDirector(director1); // Gemmer director1
            director2 = directorDAO.createDirector(director2); // Gemmer director2
        }
    }

    @Test
    void createDirector() {
        DirectorDTO director3 = new DirectorDTO(null, "Quentin Tarantino", null); // Opretter director3
        director3 = directorDAO.createDirector(director3); // Gemmer director3
        assertNotNull(director3.getId()); // Sikrer at director3 har en ID
        assertEquals("Quentin Tarantino", director3.getName()); // Validerer navn
    }

    @Test
    void getDirectorById() {
        DirectorDTO foundDirector = directorDAO.getDirectorById(director1.getId()); // Henter director1
        assertNotNull(foundDirector); // Sikrer at fundet instruktør ikke er null
        assertEquals(director1.getName(), foundDirector.getName()); // Validerer navn
    }

    @Test
    void updateDirector() {
        director1.setName("Christopher Nolan Updated"); // Opdaterer navn
        DirectorDTO updatedDirector = directorDAO.updateDirector(director1); // Gemmer ændringer
        assertEquals("Christopher Nolan Updated", updatedDirector.getName()); // Validerer opdatering
    }

    @Test
    void deleteDirector() {
        directorDAO.deleteDirector(director2.getId()); // Sletter director2
        assertNull(directorDAO.getDirectorById(director2.getId())); // Sikrer at director2 er slettet
    }

    @Test
    void getAllDirectors() {
        List<DirectorDTO> directors = directorDAO.getAllDirectors(); // Henter alle instruktører
        assertEquals(2, directors.size()); // Validerer antal
    }
}
