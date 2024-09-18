package org.example;

import jakarta.persistence.EntityManager;
import org.example.config.HibernateConfig;
import org.example.dtos.*;
import org.example.entities.Genre;
import org.example.entities.Movie;
import org.example.services.*;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //Movie

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieRepository");

        // Hent og persister film
        MovieService movieService = new MovieService();
        List<MovieDTO> movies = movieService.fetchMovies();
        MovieMapper movieMapper = new MovieMapper();

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            for (MovieDTO movieDTO : movies) {
                Movie movieEntity = movieMapper.toEntity(movieDTO);
                em.merge(movieEntity); // Brug merge i stedet for persist
            }
            em.getTransaction().commit();
            System.out.println("All movies persisted successfully.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Rollback ved fejl
            }
            e.printStackTrace();
        } finally {
            em.close();
        }



        //Genre

        GenreService genreService = new GenreService();
        List<GenreDTO> genres = genreService.fetchGenres();
        GenreMapper genreMapper = new GenreMapper();

        EntityManager em1 = emf.createEntityManager();
        try {
            em1.getTransaction().begin();

            for (GenreDTO genreDTO : genres) {
                Genre genreEntity = genreMapper.toEntity(genreDTO);
                em1.merge(genreEntity); // Brug merge i stedet for persist
            }

            em1.getTransaction().commit();
            System.out.println("All genres persisted successfully.");
        } catch (Exception e) {
            if (em1.getTransaction().isActive()) {
                em1.getTransaction().rollback();  // Rollback ved fejl
            }
            e.printStackTrace();
        } finally {
            em1.close();
            emf.close();  // Luk EntityManagerFactory, når du er færdig med at bruge det
        }
    }
}

