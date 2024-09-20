package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.daos.MovieDAO;
import org.example.dtos.MovieDTO;
import org.example.dtos.MovieResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    // API-konstanter

    public static String apiKey = System.getenv("API_KEY");


    public static final String FETCH_DANISH_MOVIES = "https://api.themoviedb.org/3/discover/movie";
    public static final int MAX_MOVIES = 1146; // Maksimalt antal film
    public static final String RELEASE_DATE_START = "2019-01-01"; // Startdato for de seneste 5 år

    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public List<MovieDTO> fetchMovies() {
        List<MovieDTO> allMovies = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        int currentPage = 1; // Start ved side 1
        int totalMoviesFetched = 0; // Holder styr på hvor mange film der er hentet

        try {
            // While-loop til pagination (fortsæt så længe vi ikke har hentet 1146 film)
            while (totalMoviesFetched < MAX_MOVIES) {

                // Byg URL'en med den aktuelle side og filtrer på dansk sprog
                String url = FETCH_DANISH_MOVIES + "?api_key=" + apiKey +
                        "&region=DK&sort_by=release_date.desc&primary_release_date.gte=" + RELEASE_DATE_START +
                        "&with_original_language=da&page=" + currentPage;

                // Opret forespørgsel
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(url))
                        .GET()
                        .build();

                // Send forespørgsel
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    String json = response.body();

                    // Konverter JSON-svar til objekt
                    MovieResponseDTO movieResponse = objectMapper.readValue(json, MovieResponseDTO.class);
                    List<MovieDTO> moviesOnThisPage = movieResponse.getResults();

                    // Tjek om der er flere film at hente, hvis ikke, stop loopen
                    if (moviesOnThisPage.isEmpty()) {
                        break;
                    }

                    // Tilføj film til samlet liste
                    allMovies.addAll(moviesOnThisPage);
                    totalMoviesFetched += moviesOnThisPage.size();

                    // Hvis vi har hentet mere end 1146 film, fjern overskydende
                    if (totalMoviesFetched > MAX_MOVIES) {
                        int excessMovies = totalMoviesFetched - MAX_MOVIES;
                        allMovies = allMovies.subList(0, allMovies.size() - excessMovies);
                        break;
                    }

                    currentPage++;

                } else {
                    System.out.println("Fejl ved at hente data fra serveren. Statuskode: " + response.statusCode());
                    break;
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return allMovies; // Returner de hentede film
    }

    // Create a new movie
    public MovieDTO createMovie(MovieDTO movieDTO) {
        return movieDAO.createMovie(movieDTO);
    }

    // Read a movie by ID
    public MovieDTO getMovieById(Long id) {
        return movieDAO.getMovieById(id);
    }

    // Update an existing movie
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        return movieDAO.updateMovie(movieDTO);
    }

    // Delete a movie by ID
    public void deleteMovie(Long id) {
        movieDAO.deleteMovie(id);
    }

    // Get all movies
    public List<MovieDTO> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    // Search movies by title
    public List<MovieDTO> searchMoviesByTitle(String title) {
        return movieDAO.searchMoviesByTitle(title);
    }

    // Get total average rating of all movies
    public double getTotalAverageRating() {
        return movieDAO.getAverageRating();
    }

    // Get top-10 lowest rated movies
    public List<MovieDTO> getTop10LowestRatedMovies() {
        return movieDAO.getTop10LowestRatedMovies();
    }

    // Get top-10 highest rated movies
    public List<MovieDTO> getTop10HighestRatedMovies() {
        return movieDAO.getTop10HighestRatedMovies();
    }

    // Get top-10 most popular movies
    public List<MovieDTO> getTop10MostPopularMovies() {
        return movieDAO.getTop10MostPopularMovies();
    }
}


