package org.example;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.example.config.HibernateConfig;
import org.example.services.MovieService;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("movieRepository");
        EntityManager em = emf.createEntityManager();

        MovieService movieService = new MovieService();

        movieService.fetchMovies();
    }
}