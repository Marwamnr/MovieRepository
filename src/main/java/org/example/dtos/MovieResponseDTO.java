package org.example.dtos;

import java.util.List; // Importerer List
import lombok.Data; // Importerer lombok's Data annotation

@Data // Genererer getter, setter, toString, equals, og hashCode metoder
public class MovieResponseDTO {
    private int page; // Den aktuelle side af resultaterne
    private List<MovieDTO> results; // Liste over filmresultater
    private int total_pages; // Det samlede antal sider
    private int total_results; // Det samlede antal resultater
}
