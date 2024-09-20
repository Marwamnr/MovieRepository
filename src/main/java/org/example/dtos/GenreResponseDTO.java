package org.example.dtos;

import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenreResponseDTO {
    private List<GenreDTO> genres;
}
