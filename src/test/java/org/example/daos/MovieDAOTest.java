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
    private MovieDTO m1;
    private MovieDTO m2;
    private MovieDTO m3;
    private MovieDTO m4;
    private MovieDTO m5;
    private MovieDTO m6;
    private MovieDTO m7;
    private MovieDTO m8;
    private MovieDTO m9;
    private MovieDTO m10;

    @BeforeEach
    void setUp() {
        // Clear the database and reset sequences
        try (var em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Movie").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE movie_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }

        // Create sample movies
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
        movie.setTitle(title);
        movie.setRelease_date(releaseDate);
        movie.setVote_average(voteAverage);
        movie.setPopularity(popularity);
        return movieDAO.createMovie(movie);
    }

    @Test
    void createMovie() {
        MovieDTO m11 = new MovieDTO();
        m11.setTitle("Movie 11");
        m11.setRelease_date("2024-11-01");
        m11.setVote_average(8.9);
        m11.setPopularity(300.0);
        m11 = movieDAO.createMovie(m11);

        assertNotNull(m11.getId());
        assertEquals("Movie 11", m11.getTitle());
    }

    @Test
    void getMovieById() {
        MovieDTO fetchedMovie = movieDAO.getMovieById(m1.getId());
        assertNotNull(fetchedMovie);
        assertEquals(m1.getTitle(), fetchedMovie.getTitle());
    }

    @Test
    void updateMovie() {
        m1.setVote_average(8.5);
        m1 = movieDAO.updateMovie(m1);
        MovieDTO updatedMovie = movieDAO.getMovieById(m1.getId());

        assertEquals(8.5, updatedMovie.getVote_average());
    }

    @Test
    void deleteMovie() {
        movieDAO.deleteMovie(m10.getId());
        MovieDTO deletedMovie = movieDAO.getMovieById(m10.getId());
        assertNull(deletedMovie);
    }

    @Test
    void getAllMovies() {
        List<MovieDTO> allMovies = movieDAO.getAllMovies();
        assertEquals(10, allMovies.size());
    }

    @Test
    void searchMoviesByTitle() {
        List<MovieDTO> results = movieDAO.searchMoviesByTitle("Movie 1");
        assertEquals(1, results.size());
        assertEquals("Movie 1", results.get(0).getTitle());
    }

    @Test
    void getAverageRating() {
        double averageRating = movieDAO.getAverageRating();
        assertEquals(7.0, averageRating, 0.1);
    }

    @Test
    void getTop10LowestRatedMovies() {
        List<MovieDTO> lowestRated = movieDAO.getTop10LowestRatedMovies();
        assertEquals(10, lowestRated.size());
        assertEquals("Movie 10", lowestRated.get(0).getTitle());
    }

    @Test
    void getTop10HighestRatedMovies() {
        List<MovieDTO> highestRated = movieDAO.getTop10HighestRatedMovies();
        assertEquals(10, highestRated.size());
        assertEquals("Movie 6", highestRated.get(0).getTitle());
    }

    @Test
    void getTop10MostPopularMovies() {
        List<MovieDTO> mostPopular = movieDAO.getTop10MostPopularMovies();
        assertEquals(10, mostPopular.size());
        assertEquals("Movie 10", mostPopular.get(0).getTitle());
    }
}
