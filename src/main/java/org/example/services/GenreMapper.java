package org.example.services;

import org.example.dtos.GenreDTO;
import org.example.entities.Genre;

public class GenreMapper {

    // Konverterer GenreDTO til Genre entitet
    public Genre toEntity(GenreDTO genreDTO) {
        Genre genre = new Genre(); // Opretter en ny Genre entitet
        genre.setId(genreDTO.getId()); // Sætter ID fra DTO'en
        genre.setName(genreDTO.getName()); // Sætter navn fra DTO'en
        return genre; // Returnerer den oprettede entitet
    }

    // Konverterer Genre entitet til GenreDTO
    public GenreDTO toDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO(); // Opretter en ny GenreDTO
        genreDTO.setId(genre.getId()); // Sætter ID fra entiteten
        genreDTO.setName(genre.getName()); // Sætter navn fra entiteten
        return genreDTO; // Returnerer den oprettede DTO
    }
}
