package io.frogmi.earthquakes.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Coordinates {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

}
