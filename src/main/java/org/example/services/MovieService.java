package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.MovieDTO;
import org.example.daos.MovieDAO;
import org.example.dtos.MovieResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class MovieService {

    /*
    private static final String MOVIES_ENDPOINT = "/discover/movie?api_key=f0cae1f38d73242e49780b68affbaf65&language=da&region=DK&primary_release_date.gte=2019-09-17&primary_release_date.lte=2024-09-17";
    private final MovieDAO movieDAO;

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public List<MovieDTO> fetchMovies() throws IOException, InterruptedException {
        String jsonResponse = ApiService.getApiResponse(MOVIES_ENDPOINT);

        // Convert JSON response to List<MovieDTO>
        List<MovieDTO> movieDTOs = JsonService.convertJsonToList(jsonResponse, MovieDTO.class);

        // Persist each MovieDTO
        movieDTOs.forEach(dto -> movieDAO.createMovie(dto));

        return movieDTOs;
    }

    public List<MovieDTO> getAllMovies() {
        return movieDAO.getAllMovies();
    }

    public MovieDTO getMovieById(Long id) {
        return movieDAO.getMovieById(id);
    }

    public void addMovie(MovieDTO movieDTO) {
        movieDAO.createMovie(movieDTO);
    }

    public void updateMovie(MovieDTO movieDTO) {
        movieDAO.updateMovie(movieDTO);
    }

    public void deleteMovie(Long id) {
        movieDAO.deleteMovie(id);
    }

    public List<MovieDTO> searchMoviesByTitle(String title) {
        return movieDAO.getAllMovies().stream()
                .filter(movieDTO -> movieDTO.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public double getAverageRating() {
        return movieDAO.getAllMovies().stream()
                .mapToDouble(MovieDTO::getRating)
                .average()
                .orElse(0.0);
    }

    public List<MovieDTO> getTop10HighestRated() {
        return movieDAO.getAllMovies().stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getTop10LowestRated() {
        return movieDAO.getAllMovies().stream()
                .sorted((m1, m2) -> Double.compare(m1.getRating(), m2.getRating()))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<MovieDTO> getTop10MostPopular() {
        return movieDAO.getAllMovies().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getPopularity(), m1.getPopularity()))
                .limit(10)
                .collect(Collectors.toList());
    }

     */

    public List<MovieDTO> fetchMovies() {
        try {

            HttpClient client = HttpClient.newHttpClient();


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.themoviedb.org/3/discover/movie?api_key=f0cae1f38d73242e49780b68affbaf65&language=en-US&region=DK&primary_release_date.gte=2019-09-17&primary_release_date.lte=2024-09-17"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 200) {
                System.out.println(response.body());


                String json = response.body();

                ObjectMapper objectMapper = new ObjectMapper();

                try {

                    MovieResponseDTO movieResponse = objectMapper.readValue(json, MovieResponseDTO.class);


                    return movieResponse.getResults();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to get response from the server. Status code: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
