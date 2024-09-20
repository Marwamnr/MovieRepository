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
    private GenreDTO genre1;
    private GenreDTO genre2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Genre").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE genre_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            // Creating initial genres
            genre1 = new GenreDTO(null, "Action");
            genre2 = new GenreDTO(null, "Comedy");

            genre1 = genreDAO.createGenre(genre1);
            genre2 = genreDAO.createGenre(genre2);
        }
    }

    @Test
    void createGenre() {
        GenreDTO genre3 = new GenreDTO(null, "Drama");
        genre3 = genreDAO.createGenre(genre3);
        assertNotNull(genre3.getId());
        assertEquals("Drama", genre3.getName());
    }

    @Test
    void getGenreById() {
        GenreDTO foundGenre = genreDAO.getGenreById(genre1.getId());
        assertNotNull(foundGenre);
        assertEquals(genre1.getName(), foundGenre.getName());
    }

    @Test
    void updateGenre() {
        genre1.setName("Action Updated");
        GenreDTO updatedGenre = genreDAO.updateGenre(genre1);
        assertEquals("Action Updated", updatedGenre.getName());
    }

    @Test
    void deleteGenre() {
        genreDAO.deleteGenre(genre2.getId());
        assertNull(genreDAO.getGenreById(genre2.getId()));
    }

    @Test
    void getAllGenres() {
        List<GenreDTO> genres = genreDAO.getAllGenres();
        assertEquals(2, genres.size());
    }
}
