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

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieRepository");

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

        }

        //Movie


        // Hent og persister film
        MovieService movieService = new MovieService();
        List<MovieDTO> movies = movieService.fetchMovies();

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Genre genre;
            for (MovieDTO movieDTO : movies) {
                Movie movieEntity = new Movie(movieDTO);  // konverter dto til entitet
                if (movieDTO.getGenre_ids().size() > 0) {
                    genre = em.find(Genre.class, movieDTO.getGenre_ids().get(0));
                    System.out.println(genre);
                    movieEntity.getGenres().add(genre);
                }
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
    }
}

