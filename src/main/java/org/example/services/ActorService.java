package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.daos.ActorDAO;
import org.example.daos.MovieDAO;
import org.example.dtos.CastDTO;
import org.example.dtos.MovieDTO;
import org.example.dtos.MovieResponseDTO;
import org.example.dtos.MovieCastDTO;
import org.example.entities.Actor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ActorService {

    private static final String apiKey = System.getenv("API_KEY"); // Ensure the API key is correctly set in your environment
    private static final String FETCH_MOVIES = "https://api.themoviedb.org/3/discover/movie";
    private static final String FETCH_MOVIE_CREDITS = "https://api.themoviedb.org/3/movie/";

    private final HttpClient client;
    private final ObjectMapper objectMapper;

    private final ActorDAO actorDao;


    public ActorService(ActorDAO actorDao) {
        this.actorDao = actorDao;
        this.client = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public List<Actor> fetchAllActors() {
        List<CastDTO> allCastDTOs = new ArrayList<>();
        List<Actor> allActors = new ArrayList<>();

        try {

            String movieUrl = FETCH_MOVIES + "?api_key=" + apiKey + "&language=en-US&page=1";

            HttpRequest movieRequest = HttpRequest.newBuilder()
                    .uri(new URI(movieUrl))
                    .GET()
                    .build();

            HttpResponse<String> movieResponse = client.send(movieRequest, HttpResponse.BodyHandlers.ofString());

            if (movieResponse.statusCode() == 200) {
                System.out.println("Movies JSON Response: " + movieResponse.body());

                MovieResponseDTO movieResponseDTO = objectMapper.readValue(movieResponse.body(), MovieResponseDTO.class);
                List<MovieDTO> movies = movieResponseDTO.getResults();


                for (MovieDTO movie : movies) {
                    String creditsUrl = FETCH_MOVIE_CREDITS + movie.getId() + "/credits?api_key=" + apiKey + "&language=en-US";

                    HttpRequest creditsRequest = HttpRequest.newBuilder()
                            .uri(new URI(creditsUrl))
                            .GET()
                            .build();

                    HttpResponse<String> creditsResponse = client.send(creditsRequest, HttpResponse.BodyHandlers.ofString());

                    if (creditsResponse.statusCode() == 200) {
                        MovieCastDTO creditsResponseDTO = objectMapper.readValue(creditsResponse.body(), MovieCastDTO.class);


                        if (creditsResponseDTO.getCast() != null) {
                            allCastDTOs.addAll(creditsResponseDTO.getCast());
                        } else {
                            System.out.println("No cast data for movie ID " + movie.getId());
                        }
                    } else {
                        System.out.println("Error fetching credits for movie ID " + movie.getId());
                    }
                }


                if (!allCastDTOs.isEmpty()) {
                    allActors = ActorMapper.mapToActorList(allCastDTOs);
                    System.out.println("All Actors List: " + allActors);
                } else {
                    System.out.println("No actors found to map.");
                }

            } else {
                System.out.println("Error fetching movies. Status code: " + movieResponse.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return allActors;
    }
}

