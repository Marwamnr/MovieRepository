package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.MovieDTO;
import org.example.entities.Movie;

import java.util.List;
import java.util.stream.Collectors;

public class MovieDAO {

    private final EntityManagerFactory emf; // Initialiserer EntityManagerFactory

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf; // Gemmer EntityManagerFactory
    }

    // Opretter en ny film
    public MovieDTO createMovie(MovieDTO movieDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Movie movie = movieDTO.toEntity(); // Konverterer DTO til entitet
            em.persist(movie); // Gemmer filmen
            tx.commit(); // Bekræfter transaktionen
            return new MovieDTO(movie); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter film efter ID
    public MovieDTO getMovieById(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            Movie movie = em.find(Movie.class, id); // Finder filmen
            return movie != null ? new MovieDTO(movie) : null; // Returnerer DTO eller null
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Opdaterer en eksisterende film
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Movie movie = em.find(Movie.class, movieDTO.getId()); // Finder filmen
            if (movie == null) {
                throw new IllegalArgumentException("Movie with ID " + movieDTO.getId() + " not found."); // Kaster fejl hvis ikke fundet
            }
            // Opdaterer felter
            movie.setTitle(movieDTO.getTitle());
            movie.setRelease_date(movieDTO.getRelease_date());
            movie.setRating(movieDTO.getVote_average());
            movie.setPopularity(movieDTO.getPopularity());
            // Optionelt opdater andre felter eller relationer

            em.merge(movie); // Merges ændringer
            tx.commit(); // Bekræfter transaktionen
            return new MovieDTO(movie); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Sletter en film
    public void deleteMovie(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Movie movie = em.find(Movie.class, id); // Finder filmen
            if (movie != null) {
                em.remove(movie); // Fjerner filmen
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

    // Henter alle film
    public List<MovieDTO> getAllMovies() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            return em.createQuery("SELECT m FROM Movie m JOIN FETCH m.genres", Movie.class) // Opretter forespørgsel
                    .getResultList() // Henter resultater
                    .stream()
                    .map(MovieDTO::fromEntity) // Konverterer til liste af DTO'er
                    .collect(Collectors.toList());
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Søger film efter titel
    public List<MovieDTO> searchMoviesByTitle(String title) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(:title)", Movie.class); // Opretter forespørgsel
            query.setParameter("title", "%" + title + "%"); // Sætter parameter for titel
            List<Movie> movies = query.getResultList(); // Henter resultater
            return movies.stream().map(MovieDTO::new).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter gennemsnitlig vurdering
    public double getAverageRating() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(m.rating) FROM Movie m", Double.class); // Opretter forespørgsel for gennemsnit
            return query.getSingleResult(); // Returnerer gennemsnit
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter top-10 lavest vurderede film (sorteret efter stemmetal)
    public List<MovieDTO> getTop10LowestRatedMovies() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m ORDER BY m.voteCount ASC, m.rating ASC", Movie.class); // Opretter forespørgsel
            query.setMaxResults(10); // Sætter maksimum resultater
            List<Movie> movies = query.getResultList(); // Henter resultater
            return movies.stream().map(MovieDTO::new).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter top-10 højest vurderede film (sorteret efter stemmetal)
    public List<MovieDTO> getTop10HighestRatedMovies() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m ORDER BY m.voteCount DESC, m.rating DESC", Movie.class); // Opretter forespørgsel
            query.setMaxResults(10); // Sætter maksimum resultater
            List<Movie> movies = query.getResultList(); // Henter resultater
            return movies.stream().map(MovieDTO::new).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter top-10 mest populære film
    public List<MovieDTO> getTop10MostPopularMovies() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m ORDER BY m.popularity DESC", Movie.class); // Opretter forespørgsel
            query.setMaxResults(10); // Sætter maksimum resultater
            List<Movie> movies = query.getResultList(); // Henter resultater
            return movies.stream().map(MovieDTO::new).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }
}


