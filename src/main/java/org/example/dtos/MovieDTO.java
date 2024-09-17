package org.example.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.entities.Actor;
import org.example.entities.Genre;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {
    private String title;
    private int year;
    private double rating;
    private List<Genre> genres;
    private List<Actor> actors;
    private String director;


}
