package org.example.daos;

import org.example.config.HibernateConfig;
import org.example.dtos.GenreDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreDAOTest {

    private static final EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private static final GenreDAO genreDAO = new GenreDAO(emfTest);
    private GenreDTO genre1; // Første genre som GenreDTO
    private GenreDTO genre2; // Anden genre som GenreDTO

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Genre").executeUpdate(); // Tømmer Genre tabellen
            em.createNativeQuery("ALTER SEQUENCE genre_id_seq RESTART WITH 1").executeUpdate(); // Nulstiller sekvens
            em.getTransaction().commit();

            // Opretter initiale genrer
            genre1 = new GenreDTO(null, "Action"); // Opretter genre1
            genre2 = new GenreDTO(null, "Comedy"); // Opretter genre2

            genre1 = genreDAO.createGenre(genre1); // Gemmer genre1
            genre2 = genreDAO.createGenre(genre2); // Gemmer genre2
        }
    }

    @Test
    void createGenre() {
        GenreDTO genre3 = new GenreDTO(null, "Drama"); // Opretter genre3
        genre3 = genreDAO.createGenre(genre3); // Gemmer genre3
        assertNotNull(genre3.getId()); // Sikrer at genre3 har en ID
        assertEquals("Drama", genre3.getName()); // Validerer navn
    }

    @Test
    void getGenreById() {
        GenreDTO foundGenre = genreDAO.getGenreById(genre1.getId()); // Henter genre1
        assertNotNull(foundGenre); // Sikrer at fundet genre ikke er null
        assertEquals(genre1.getName(), foundGenre.getName()); // Validerer navn
    }

    @Test
    void updateGenre() {
        genre1.setName("Action Updated"); // Opdaterer navn
        GenreDTO updatedGenre = genreDAO.updateGenre(genre1); // Gemmer ændringer
        assertEquals("Action Updated", updatedGenre.getName()); // Validerer opdatering
    }

    @Test
    void deleteGenre() {
        genreDAO.deleteGenre(genre2.getId()); // Sletter genre2
        assertNull(genreDAO.getGenreById(genre2.getId())); // Sikrer at genre2 er slettet
    }

    @Test
    void getAllGenres() {
        List<GenreDTO> genres = genreDAO.getAllGenres(); // Henter alle genrer
        assertEquals(2, genres.size()); // Validerer antal
    }
}
