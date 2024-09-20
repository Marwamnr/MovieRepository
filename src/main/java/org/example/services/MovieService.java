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
import java.util.List;

public class MovieService {

    // API-konstanter
    public static String apiKey = System.getenv("API_KEY"); // Hent API-nøglen fra miljøvariabler

    public static final String FETCH_DANISH_MOVIES = "https://api.themoviedb.org/3/discover/movie"; // URL til at hente danske film
    public static final int MAX_MOVIES = 1146; // Maksimalt antal film
    public static final String RELEASE_DATE_START = "2019-01-01"; // Startdato for de seneste 5 år

    private final MovieDAO movieDAO; // DAO til filmoperationer

    public MovieService(MovieDAO movieDAO) {
        this.movieDAO = movieDAO; // Initialiser DAO'en
    }

    public List<MovieDTO> fetchMovies() {
        List<MovieDTO> allMovies = new ArrayList<>(); // Liste til at gemme hentede film
        HttpClient client = HttpClient.newHttpClient(); // HTTP-klient til at sende forespørgsler
        ObjectMapper objectMapper = new ObjectMapper(); // JSON-mapper til konvertering
        int currentPage = 1; // Start ved side 1
        int totalMoviesFetched = 0; // Holder styr på hentede film

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

                // Send forespørgsel og modtag svar
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) { // Tjek for succesfuld respons
                    String json = response.body(); // Hent JSON-svaret

                    // Konverter JSON-svar til objekt
                    MovieResponseDTO movieResponse = objectMapper.readValue(json, MovieResponseDTO.class);
                    List<MovieDTO> moviesOnThisPage = movieResponse.getResults(); // Hent film fra svaret

                    // Tjek om der er flere film at hente, hvis ikke, stop loopen
                    if (moviesOnThisPage.isEmpty()) {
                        break; // Stop hvis der ikke er flere film
                    }

                    // Tilføj film til samlet liste
                    allMovies.addAll(moviesOnThisPage);
                    totalMoviesFetched += moviesOnThisPage.size(); // Opdater det samlede antal hentede film

                    // Hvis vi har hentet mere end 1146 film, fjern overskydende
                    if (totalMoviesFetched > MAX_MOVIES) {
                        int excessMovies = totalMoviesFetched - MAX_MOVIES; // Beregn overskydende film
                        allMovies = allMovies.subList(0, allMovies.size() - excessMovies); // Fjern overskydende film
                        break; // Stop loop
                    }

                    currentPage++; // Gå til næste side

                } else {
                    System.out.println("Fejl ved at hente data fra serveren. Statuskode: " + response.statusCode());
                    break; // Stop ved fejl
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace(); // Håndter undtagelser
        }

        return allMovies; // Returner de hentede film
    }

    // Opret en ny film
    public MovieDTO createMovie(MovieDTO movieDTO) {
        return movieDAO.createMovie(movieDTO); // Kalder DAO for at oprette film
    }

    // Hent en film efter ID
    public MovieDTO getMovieById(Long id) {
        return movieDAO.getMovieById(id); // Kalder DAO for at hente film
    }

    // Opdater en eksisterende film
    public MovieDTO updateMovie(MovieDTO movieDTO) {
        return movieDAO.updateMovie(movieDTO); // Kalder DAO for at opdatere film
    }

    // Slet en film efter ID
    public void deleteMovie(Long id) {
        movieDAO.deleteMovie(id); // Kalder DAO for at slette film
    }

    // Hent alle film
    public List<MovieDTO> getAllMovies() {
        return movieDAO.getAllMovies(); // Kalder DAO for at hente alle film
    }

    // Søg efter film efter titel
    public List<MovieDTO> searchMoviesByTitle(String title) {
        return movieDAO.searchMoviesByTitle(title); // Kalder DAO for at søge film
    }

    // Få den samlede gennemsnitsvurdering af alle film
    public double getTotalAverageRating() {
        return movieDAO.getAverageRating(); // Kalder DAO for at hente gennemsnitsvurdering
    }

    // Få top-10 lavest vurderede film
    public List<MovieDTO> getTop10LowestRatedMovies() {
        return movieDAO.getTop10LowestRatedMovies(); // Kalder DAO for at hente lavest vurderede film
    }

    // Få top-10 højest vurderede film
    public List<MovieDTO> getTop10HighestRatedMovies() {
        return movieDAO.getTop10HighestRatedMovies(); // Kalder DAO for at hente højest vurderede film
    }

    // Få top-10 mest populære film
    public List<MovieDTO> getTop10MostPopularMovies() {
        return movieDAO.getTop10MostPopularMovies(); // Kalder DAO for at hente mest populære film
    }
}

