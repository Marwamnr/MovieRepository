package org.example.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieCastDTO {
    private int id;
    private List<CastDTO> cast;


}
