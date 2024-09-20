package org.example.dtos;

import lombok.*; // Importerer alle lombok-annotationer
import java.util.List; // Importerer List

@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@AllArgsConstructor // Genererer en konstruktør med alle felter
@NoArgsConstructor // Genererer en standard konstruktør
public class GenreResponseDTO {
    private List<GenreDTO> genres; // Liste over genrer i DTO-format
}
