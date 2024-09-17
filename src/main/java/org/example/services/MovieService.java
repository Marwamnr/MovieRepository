package org.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dtos.MovieDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MovieService {
    public static void main(String[] args) {
        try {


            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request
            HttpRequest request = HttpRequest.newBuilder()
                    //Indsætte API link
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
                // Create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    // Convert JSON string to MovieDTO
                    MovieDTO movieDTO = objectMapper.readValue(json, MovieDTO.class);

                    // Print the MovieDTO object
                    System.out.println(movieDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
