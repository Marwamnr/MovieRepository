package org.example.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.GenreDTO;
import org.example.entities.Genre;
import org.example.entities.Movie;
import org.example.daos.GenreDAO;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreService {

    private static final String GENRES_ENDPOINT = "/genre/movie/list?language=da";
    private static final String MOVIES_ENDPOINT = "/discover/movie?api_key=f0cae1f38d73242e49780b68affbaf65&language=da&region=DK&primary_release_date.gte=2019-09-17&primary_release_date.lte=2024-09-17";

    private final GenreDAO genreDAO;

    public GenreService(GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    public List<GenreDTO> fetchGenres() throws IOException, InterruptedException {
        // Fetch movie data
        String jsonResponse = ApiService.getApiResponse(MOVIES_ENDPOINT);
        List<Movie> movies = JsonService.convertJsonToList(jsonResponse, Movie.class);

        // Extract genre IDs from the filtered Danish movies
        Set<Long> genreIds = movies.stream()
                .flatMap(movie -> movie.getGenres().stream())
                .map(Genre::getId)
                .collect(Collectors.toSet());

        // Fetch genre data from the API
        jsonResponse = ApiService.getApiResponse(GENRES_ENDPOINT);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode genresNode = rootNode.path("genres");
        List<GenreDTO> genreDTOs = mapper.readerForListOf(GenreDTO.class).readValue(genresNode.toString());

        // Filter genres based on extracted genre IDs
        List<GenreDTO> filteredGenreDTOs = genreDTOs.stream()
                .filter(dto -> genreIds.contains(dto.getId()))
                .collect(Collectors.toList());

        // Save filtered genres to the database
        filteredGenreDTOs.forEach(genreDAO::createGenre);

        return filteredGenreDTOs;
    }

    public List<GenreDTO> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    public GenreDTO getGenreById(Long id) {
        return genreDAO.getGenreById(id);
    }

    public List<Movie> getMoviesByGenre(Long genreId) throws IOException, InterruptedException {
        // Fetch recent Danish movies
        String jsonResponse = ApiService.getApiResponse(MOVIES_ENDPOINT);
        List<Movie> movies = JsonService.convertJsonToList(jsonResponse, Movie.class);

        // Filter movies by genre
        return movies.stream()
                .filter(movie -> movie.getGenres().stream().anyMatch(genre -> genre.getId().equals(genreId)))
                .collect(Collectors.toList());
    }

    public void addGenre(GenreDTO genreDTO) {
        genreDAO.createGenre(genreDTO);
    }

    public void updateGenre(GenreDTO genreDTO) {
        genreDAO.updateGenre(genreDTO);
    }

    public void deleteGenre(Long id) {
        genreDAO.deleteGenre(id);
    }
}
