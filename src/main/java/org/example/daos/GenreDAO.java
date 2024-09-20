package org.example.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Genre;
import org.example.entities.Movie;

import java.util.List;

public class GenreDAO {

    private final EntityManagerFactory emf; // Initialiserer EntityManagerFactory

    public GenreDAO(EntityManagerFactory emf) {
        this.emf = emf; // Gemmer EntityManagerFactory
    }

    // Opretter en ny genre
    public GenreDTO createGenre(GenreDTO genreDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Genre genre = genreDTO.toEntity(); // Konverterer DTO til entitet
            em.persist(genre); // Gemmer genren
            tx.commit(); // Bekræfter transaktionen
            return GenreDTO.fromEntity(genre); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter genre efter ID
    public GenreDTO getGenreById(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            Genre genre = em.find(Genre.class, id); // Finder genren
            return genre != null ? GenreDTO.fromEntity(genre) : null; // Returnerer DTO eller null
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Opdaterer en eksisterende genre
    public GenreDTO updateGenre(GenreDTO genreDTO) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Genre genre = em.find(Genre.class, genreDTO.getId()); // Finder genren
            if (genre == null) {
                throw new IllegalArgumentException("Genre with ID " + genreDTO.getId() + " not found."); // Kaster fejl hvis ikke fundet
            }
            genre.setName(genreDTO.getName()); // Opdaterer navn
            em.merge(genre); // Merges ændringer
            tx.commit(); // Bekræfter transaktionen
            return GenreDTO.fromEntity(genre); // Returnerer DTO
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Ruller tilbage ved fejl
            }
            throw e; // Kaster fejl videre
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Sletter en genre
    public void deleteGenre(Long id) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        EntityTransaction tx = em.getTransaction(); // Henter transaktion
        try {
            tx.begin(); // Starter transaktionen
            Genre genre = em.find(Genre.class, id); // Finder genren
            if (genre != null) {
                em.remove(genre); // Fjerner genren
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

    // Henter alle genrer
    public List<GenreDTO> getAllGenres() {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class); // Opretter forespørgsel
            List<Genre> genres = query.getResultList(); // Henter resultater
            return genres.stream().map(GenreDTO::fromEntity).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }

    // Henter film efter genre
    public List<MovieDTO> getMoviesByGenre(Long genreId) {
        EntityManager em = emf.createEntityManager(); // Opretter EntityManager
        try {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId", Movie.class); // Opretter forespørgsel med join
            query.setParameter("genreId", genreId); // Sætter parameter for genre ID
            List<Movie> movies = query.getResultList(); // Henter resultater
            return movies.stream().map(MovieDTO::new).toList(); // Konverterer til liste af DTO'er
        } finally {
            em.close(); // Lukker EntityManager
        }
    }
}
