package io.frogmi.earthquakes.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class Coordinates {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

}
