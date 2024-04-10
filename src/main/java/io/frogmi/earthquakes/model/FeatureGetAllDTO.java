package io.frogmi.earthquakes.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeatureGetAllDTO {

    private Integer id;

    @Size(max = 255)
    private String type;

    private FeatureAttributes attributes;

    private Links links;
}
