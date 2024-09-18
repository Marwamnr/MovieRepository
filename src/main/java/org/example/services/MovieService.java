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
import java.util.List;

public class MovieService {

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


