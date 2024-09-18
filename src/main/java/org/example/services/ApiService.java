package org.example.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

public class ApiService {

    private static final String API_KEY = System.getenv("API_KEY") != null ? System.getenv("API_KEY") : "your_default_api_key";
    private static final HttpClient CLIENT = HttpClient.newBuilder().build();
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final Logger LOGGER = Logger.getLogger(ApiService.class.getName());

    public static String getApiResponse(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint + "&api_key=" + API_KEY))
                .GET()
                .build();

        LOGGER.info("Sending request to: " + request.uri());

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        LOGGER.info("Received response with status code: " + response.statusCode());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new IOException("GET request failed. Status code: " + response.statusCode());
        }
    }

    // Use JsonService to convert JSON response to a list of MovieDTOs
    public static <T> List<T> parseJsonResponse(String json, Class<T> clazz) throws IOException {
        return JsonService.convertJsonToList(json, clazz);
    }
}
