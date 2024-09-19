package org.example.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.GenreDTO;
import org.example.dtos.GenreResponseDTO;

public class GenreService {

    // Hent API-nøgle fra miljøvariabel
    public static String apiKey = System.getenv("API_KEY");

    // API-endpoint til at hente genrer, sprog er sat til dansk (da)
    private static final String GENRE_ENDPOINT = "https://api.themoviedb.org/3/genre/movie/list?language=da";

    // Metode til at hente genrer fra API
    public List<GenreDTO> fetchGenres() {
        try {
            // Kontroller om API-nøglen er korrekt indsat
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalStateException("API_KEY miljøvariablen er ikke sat.");
            }

            // Dynamisk opbygning af URL med API-nøgle
            String urlWithApiKey = GENRE_ENDPOINT + "&api_key=" + apiKey;

            // Opret HttpClient og HttpRequest
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlWithApiKey))  // Brug den dynamisk genererede URL
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
