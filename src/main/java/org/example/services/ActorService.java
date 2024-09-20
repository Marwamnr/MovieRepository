package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.daos.CastDAO;
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

    // Henter API-nøglen fra miljøvariabler
    private static final String apiKey = System.getenv("API_KEY");
    private static final String FETCH_MOVIES = "https://api.themoviedb.org/3/discover/movie"; // URL til at hente film
    private static final String FETCH_MOVIE_CREDITS = "https://api.themoviedb.org/3/movie/"; // URL til at hente filmens credits

    private final HttpClient client; // HTTP-klient til at foretage forespørgsler
    private final ObjectMapper objectMapper; // Mapper JSON til Java-objekter

    private final CastDAO actorDao; // DAO til at håndtere Actor-entiteter

    public ActorService(CastDAO castDao) {
        this.actorDao = castDao; // Initialiserer DAO'en
        this.client = HttpClient.newHttpClient(); // Opretter en ny HTTP-klient
        this.objectMapper = new ObjectMapper(); // Opretter en ny ObjectMapper
    }

    // Henter alle skuespillere fra API'et
    public List<Actor> fetchAllActors() {
        List<CastDTO> allCastDTOs = new ArrayList<>(); // Liste til at gemme alle CastDTO'er
        List<Actor> allActors = new ArrayList<>(); // Liste til at gemme alle Actor-entiteter

        try {
            // Bygger URL til at hente film
            String movieUrl = FETCH_MOVIES + "?api_key=" + apiKey + "&language=en-US&page=1";

            // Opretter en HTTP-forespørgsel for filmene
            HttpRequest movieRequest = HttpRequest.newBuilder()
                    .uri(new URI(movieUrl))
                    .GET()
                    .build();

            // Sender forespørgslen og får svaret
            HttpResponse<String> movieResponse = client.send(movieRequest, HttpResponse.BodyHandlers.ofString());

            // Tjekker om svaret er OK
            if (movieResponse.statusCode() == 200) {
                System.out.println("Movies JSON Response: " + movieResponse.body());

                // Mapper JSON-svaret til MovieResponseDTO
                MovieResponseDTO movieResponseDTO = objectMapper.readValue(movieResponse.body(), MovieResponseDTO.class);
                List<MovieDTO> movies = movieResponseDTO.getResults(); // Henter filmene fra responsen

                // Går igennem hver film for at hente deres credits
                for (MovieDTO movie : movies) {
                    String creditsUrl = FETCH_MOVIE_CREDITS + movie.getId() + "/credits?api_key=" + apiKey + "&language=en-US";

                    // Opretter en HTTP-forespørgsel for filmens credits
                    HttpRequest creditsRequest = HttpRequest.newBuilder()
                            .uri(new URI(creditsUrl))
                            .GET()
                            .build();

                    // Sender forespørgslen og får svaret
                    HttpResponse<String> creditsResponse = client.send(creditsRequest, HttpResponse.BodyHandlers.ofString());

                    // Tjekker om svaret er OK
                    if (creditsResponse.statusCode() == 200) {
                        MovieCastDTO creditsResponseDTO = objectMapper.readValue(creditsResponse.body(), MovieCastDTO.class);

                        // Tjekker om der er cast-data
                        if (creditsResponseDTO.getCast() != null) {
                            allCastDTOs.addAll(creditsResponseDTO.getCast()); // Tilføjer cast-data til listen
                        } else {
                            System.out.println("No cast data for movie ID " + movie.getId());
                        }
                    } else {
                        System.out.println("Error fetching credits for movie ID " + movie.getId());
                    }
                }

                // Mapper alle CastDTO'er til Actor-entiteter
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
            e.printStackTrace(); // Håndterer undtagelser
        }

        return allActors; // Returnerer listen af skuespillere
    }
}

