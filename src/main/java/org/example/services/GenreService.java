package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.GenreDTO;
import org.example.dtos.GenreResponseDTO;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GenreService {

    // API-endpoint til at hente genrer
    private static final String GENRE_ENDPOINT = "https://api.themoviedb.org/3/genre/movie/list?api_key=f0cae1f38d73242e49780b68affbaf65&language=da";

    // Metode til at hente genrer fra API
    public List<GenreDTO> fetchGenres() {
        try {
            // Opret HttpClient og HttpRequest
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(GENRE_ENDPOINT))
                    .GET()
                    .build();

            // Send forespørgslen
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Kontroller om forespørgslen lykkedes (statuskode 200)
            if (response.statusCode() == 200) {
                System.out.println("Response body: " + response.body());

                // Pars response body til JSON
                String json = response.body();
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    // Konverter JSON response til GenreResponseDTO
                    GenreResponseDTO genreResponse = objectMapper.readValue(json, GenreResponseDTO.class);

                    // Returner listen af GenreDTO'er fra responsen
                    return genreResponse.getGenres();
                } catch (IOException e) {
                    System.err.println("Failed to parse JSON: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to fetch genres. Status code: " + response.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}