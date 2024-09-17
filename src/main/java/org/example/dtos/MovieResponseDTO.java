package org.example.dtos;

import org.example.dtos.MovieDTO;

import java.util.List;

import lombok.Data;
import java.util.List;

@Data
public class MovieResponseDTO {
    private int page;
    private List<MovieDTO> results;
    private int total_pages;
    private int total_results;

}
