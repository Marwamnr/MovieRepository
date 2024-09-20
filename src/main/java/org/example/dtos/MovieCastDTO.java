package org.example.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importerer annotation til JSON behandling
import lombok.Data; // Importerer lombok's Data annotation
import java.util.List; // Importerer List

@Data // Genererer getter, setter, toString, equals, og hashCode metoder
@JsonIgnoreProperties(ignoreUnknown = true) // Ignorerer ukendte felter i JSON
public class MovieCastDTO {
    private int id; // Unik ID for filmen
    private List<CastDTO> cast; // Liste over skuespillere i filmen
}
