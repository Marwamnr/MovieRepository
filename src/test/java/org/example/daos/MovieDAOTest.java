package org.example.daos;

import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.dtos.MovieDTO; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieDAOTest {

    private static final EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private final MovieDAO movieDAO = new MovieDAO(emfTest);
    private MovieDTO m1; // Første film
    private MovieDTO m2; // Anden film
    private MovieDTO m3; // Tredje film
    private MovieDTO m4; // Fjerde film
    private MovieDTO m5; // Femte film
    private MovieDTO m6; // Sjette film
    private MovieDTO m7; // Syvende film
    private MovieDTO m8; // Ottende film
    private MovieDTO m9; // Niende film
    private MovieDTO m10; // Tiende film

    @BeforeEach
    void setUp() {
        // Tømmer databasen og nulstiller sekvenser
        try (var em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie").executeUpdate(); // Sletter alle film
            em.createNativeQuery("ALTER SEQUENCE movie_id_seq RESTART WITH 1").executeUpdate(); // Nulstiller sekvens
            em.getTransaction().commit();
        }

        // Opretter prøvefilm
        m1 = createMovie("Movie 1", "2024-01-01", 7.0, 100.0);
        m2 = createMovie("Movie 2", "2024-02-01", 6.8, 90.0);
        m3 = createMovie("Movie 3", "2024-03-01", 7.5, 110.0);
        m4 = createMovie("Movie 4", "2024-04-01", 5.0, 80.0);
        m5 = createMovie("Movie 5", "2024-05-01", 8.0, 150.0);
        m6 = createMovie("Movie 6", "2024-06-01", 9.0, 200.0);
        m7 = createMovie("Movie 7", "2024-07-01", 6.5, 70.0);
        m8 = createMovie("Movie 8", "2024-08-01", 7.2, 120.0);
        m9 = createMovie("Movie 9", "2024-09-01", 8.5, 160.0);
        m10 = createMovie("Movie 10", "2024-10-01", 4.5, 60.0);
    }

    private MovieDTO createMovie(String title, String releaseDate, double voteAverage, double popularity) {
        MovieDTO movie = new MovieDTO();
        movie.setTitle(title); // Sætter titel
        movie.setRelease_date(releaseDate); // Sætter udgivelsesdato
        movie.setVote_average(voteAverage); // Sætter gennemsnitlig vurdering
        movie.setPopularity(popularity); // Sætter popularitet
        return movieDAO.createMovie(movie); // Gemmer filmen
    }

    @Test
    void createMovie() {
        MovieDTO m11 = new MovieDTO();
        m11.setTitle("Movie 11"); // Sætter titel for film 11
        m11.setRelease_date("2024-11-01"); // Sætter udgivelsesdato
        m11.setVote_average(8.9); // Sætter vurdering
        m11.setPopularity(300.0); // Sætter popularitet
        m11 = movieDAO.createMovie(m11); // Gemmer film 11

        assertNotNull(m11.getId()); // Sikrer at film 11 har en ID
        assertEquals("Movie 11", m11.getTitle()); // Validerer titlen
    }

    @Test
    void getMovieById() {
        MovieDTO fetchedMovie = movieDAO.getMovieById(m1.getId()); // Henter film 1
        assertNotNull(fetchedMovie); // Sikrer at filmen ikke er null
        assertEquals(m1.getTitle(), fetchedMovie.getTitle()); // Validerer titlen
    }

    @Test
    void updateMovie() {
        m1.setVote_average(8.5); // Opdaterer vurdering for film 1
        m1 = movieDAO.updateMovie(m1); // Gemmer ændringer
        MovieDTO updatedMovie = movieDAO.getMovieById(m1.getId()); // Henter opdateret film

        assertEquals(8.5, updatedMovie.getVote_average()); // Validerer den opdaterede vurdering
    }

    @Test
    void deleteMovie() {
        movieDAO.deleteMovie(m10.getId()); // Sletter film 10
        MovieDTO deletedMovie = movieDAO.getMovieById(m10.getId()); // Forsøger at hente slettet film
        assertNull(deletedMovie); // Sikrer at filmen er slettet
    }

    @Test
    void getAllMovies() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies(); // Henter alle film
        assertEquals(10, allMovies.size()); // Validerer antallet af film
    }

    @Test
    void searchMoviesByTitle() {
        List<MovieDTO> results = movieDAO.searchMoviesByTitle("Movie 1"); // Søger efter film med titel "Movie 1"
        assertEquals(1, results.size()); // Sikrer at der kun er én film
        assertEquals("Movie 1", results.get(0).getTitle()); // Validerer titlen på den fundne film
    }

    @Test
    void getAverageRating() {
        double averageRating = movieDAO.getAverageRating(); // Henter gennemsnitlig vurdering
        assertEquals(7.0, averageRating, 0.1); // Validerer den gennemsnitlige vurdering
    }

    @Test
    void getTop10LowestRatedMovies() {
        List<MovieDTO> lowestRated = movieDAO.getTop10LowestRatedMovies(); // Henter de 10 lavest rated film
        assertEquals(10, lowestRated.size()); // Sikrer at der er 10 film
        assertEquals("Movie 10", lowestRated.get(0).getTitle()); // Validerer titlen på den lavest rated film
    }

    @Test
    void getTop10HighestRatedMovies() {
        List<MovieDTO> highestRated = movieDAO.getTop10HighestRatedMovies(); // Henter de 10 højest rated film
        assertEquals(10, highestRated.size()); // Sikrer at der er 10 film
        assertEquals("Movie 6", highestRated.get(0).getTitle()); // Validerer titlen på den højest rated film
    }

    @Test
    void getTop10MostPopularMovies() {
        List<MovieDTO> mostPopular = movieDAO.getTop10MostPopularMovies(); // Henter de 10 mest populære film
        assertEquals(10, mostPopular.size()); // Sikrer at der er 10 film
        assertEquals("Movie 10", mostPopular.get(0).getTitle()); // Validerer titlen på den mest populære film
    }
}

