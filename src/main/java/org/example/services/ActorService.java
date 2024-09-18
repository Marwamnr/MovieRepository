package org.example.services;

import org.example.dtos.ActorDTO;
import org.example.entities.Actor;
import org.example.entities.Movie;
import org.example.daos.ActorDAO;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActorService {

    private static final String MOVIES_ENDPOINT = "/discover/movie?api_key=f0cae1f38d73242e49780b68affbaf65&language=da&region=DK&primary_release_date.gte=2019-09-17&primary_release_date.lte=2024-09-17";
    private static final String ACTORS_ENDPOINT = "/person/popular?language=da";

    private final ActorDAO actorDAO;

    public ActorService(ActorDAO actorDAO) {
        this.actorDAO = actorDAO;
    }

    public List<ActorDTO> fetchActors() throws IOException, InterruptedException {
        // Fetch movie data
        String jsonResponse = ApiService.getApiResponse(MOVIES_ENDPOINT);
        List<Movie> movies = JsonService.convertJsonToList(jsonResponse, Movie.class);

        // Extract actor IDs from the filtered Danish movies
        Set<Long> actorIds = movies.stream()
                .flatMap(movie -> movie.getActors().stream())
                .map(Actor::getId)
                .collect(Collectors.toSet());

        // Fetch actor data
        jsonResponse = ApiService.getApiResponse(ACTORS_ENDPOINT);
        List<ActorDTO> actorDTOs = JsonService.convertJsonToList(jsonResponse, ActorDTO.class);

        // Filter and save actors
        List<ActorDTO> filteredActorDTOs = actorDTOs.stream()
                .filter(dto -> actorIds.contains(dto.getId()))
                .collect(Collectors.toList());

        // Save filtered actors to the database
        filteredActorDTOs.forEach(actorDAO::createActor);

        return filteredActorDTOs;
    }

    public List<ActorDTO> getAllActors() {
        return actorDAO.getAllActors();
    }

    public ActorDTO getActorById(Long id) {
        return actorDAO.getActorById(id);
    }

    public void addActor(ActorDTO actorDTO) {
        actorDAO.createActor(actorDTO);
    }

    public void updateActor(ActorDTO actorDTO) {
        actorDAO.updateActor(actorDTO);
    }

    public void deleteActor(Long id) {
        actorDAO.deleteActor(id);
    }
}
