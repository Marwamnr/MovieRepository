package org.example.services;

import org.example.dtos.GenreDTO;
import org.example.entities.Genre;

public class GenreMapper {

    // Konverterer GenreDTO til Genre entitet
    public Genre toEntity(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setId(genreDTO.getId());
        genre.setName(genreDTO.getName());
        return genre;
    }

    public GenreDTO toDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        return genreDTO;
    }
}