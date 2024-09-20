package org.example.services;

import org.example.dtos.GenreDTO;
import org.example.dtos.MovieDTO;
import org.example.entities.Genre;
import org.example.entities.Movie;
import org.example.entities.Movie;

public class MovieMapper {

    public Movie toEntity(MovieDTO  movieDTO ) {
        if (movieDTO== null) {
            return null;
        }

        Movie movieEntity = new Movie();

        movieEntity.setTitle(movieDTO.getTitle());
        //movieEntity.setYear(movieDTO.getRelease_date());
        movieEntity.setRating(movieDTO.getVote_average());

        return movieEntity;
    }
}