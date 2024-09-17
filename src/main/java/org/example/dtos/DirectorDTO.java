package org.example.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.entities.Movie;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DirectorDTO {
    private String name;
    private List<Movie> movies;
}
