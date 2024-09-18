package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.example.config.HibernateConfig;
import org.example.dtos.MovieDTO;
import org.example.entities.Movie;
import org.example.services.MovieMapper;
import org.example.services.MovieService;

import java.util.List;

public class Main {

    public static void main(String[] args) {




        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieRepository");



        MovieService movieService = new MovieService();


        List<MovieDTO> movies = movieService.fetchMovies();


        MovieMapper movieMapper = new MovieMapper();


        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();


            for (MovieDTO movieDTO : movies) {

                Movie movieEntity = movieMapper.toEntity(movieDTO);

                em.persist(movieEntity);
            }
            em.getTransaction().commit();

            System.out.println("All movies persisted successfully.");
        } finally {

            em.close();
            emf.close();
        }
    }
}
