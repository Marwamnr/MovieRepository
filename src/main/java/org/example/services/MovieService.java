package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.MovieDTO;
import org.example.dtos.MovieResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MovieService {


    public MovieDTO fetchMovies () {
        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.themoviedb.org/3/discover/movie?api_key=f0cae1f38d73242e49780b68affbaf65&language=en-US&region=DK&primary_release_date.gte=2019-09-17&primary_release_date.lte=2024-09-17"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the status code and print the response
            if (response.statusCode() == 200) {
                System.out.println(response.body());

                // Example JSON string
                String json = response.body();

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    // Deserialiser JSON til MovieResponseDTO
                    MovieResponseDTO movieResponse = objectMapper.readValue(json, MovieResponseDTO.class);



                    // Arbejd med movieResponse objektet
                    System.out.println("Page: " + movieResponse.getPage());
                    System.out.println("Number of movies: " + movieResponse.getResults().size());

                    for (MovieDTO movie : movieResponse.getResults()) {
                        System.out.println("Movie title: " + movie.getTitle());
                    }
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


