package org.example.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.DirectorDTO;
import org.example.entities.Movie;
import org.example.daos.DirectorDAO;

import java.io.IOException;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DirectorService {

    private static final String DIRECTORS_ENDPOINT = "/person/popular?language=da";
    private static final String MOVIES_ENDPOINT = "/discover/movie?api_key=f0cae1f38d73242e49780b68affbaf65&language=da&region=DK&primary_release_date.gte=2019-09-17&primary_release_date.lte=2024-09-17";

    private final DirectorDAO directorDAO;

    public DirectorService(DirectorDAO directorDAO) {
        this.directorDAO = directorDAO;
    }

    public List<DirectorDTO> fetchDirectors() throws IOException, InterruptedException {
        int currentYear = Year.now().getValue();
        int fiveYearsAgo = currentYear - 5;

        // Fetch recent Danish movies
        String jsonResponse = ApiService.getApiResponse(MOVIES_ENDPOINT);
        List<Movie> movies = JsonService.convertJsonToList(jsonResponse, Movie.class);

        // Extract director IDs from the recent Danish movies
        Set<Long> directorIds = movies.stream()
                .filter(movie -> movie.getYear() >= fiveYearsAgo)  // Ensure movie is from the last 5 years
                .map(movie -> movie.getDirector() != null ? movie.getDirector().getId() : null)
                .filter(id -> id != null)
                .collect(Collectors.toSet());

        // Fetch director data from the API
        jsonResponse = ApiService.getApiResponse(DIRECTORS_ENDPOINT);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode directorsNode = rootNode.path("results");
        List<DirectorDTO> directorDTOs = mapper.readerForListOf(DirectorDTO.class).readValue(directorsNode.toString());

        // Filter and save directors
        List<DirectorDTO> filteredDirectorDTOs = directorDTOs.stream()
                .filter(dto -> directorIds.contains(dto.getId()))
                .collect(Collectors.toList());

        filteredDirectorDTOs.forEach(directorDAO::createDirector);

        return filteredDirectorDTOs;
    }

    public List<DirectorDTO> getAllDirectors() {
        return directorDAO.getAllDirectors();
    }

    public DirectorDTO getDirectorById(Long id) {
        return directorDAO.getDirectorById(id);
    }

    public void addDirector(DirectorDTO directorDTO) {
        directorDAO.createDirector(directorDTO);
    }

    public void updateDirector(DirectorDTO directorDTO) {
        directorDAO.updateDirector(directorDTO);
    }

    public void deleteDirector(Long id) {
        directorDAO.deleteDirector(id);
    }
}
