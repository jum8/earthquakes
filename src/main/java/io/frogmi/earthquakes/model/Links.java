package io.frogmi.earthquakes.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Links {

    @Size(max = 255)
    private String externalUrl;

}
