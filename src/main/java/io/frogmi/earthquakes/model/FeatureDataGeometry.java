package io.frogmi.earthquakes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureDataGeometry {

    private String type;
    private Double[] coordinates;

}
