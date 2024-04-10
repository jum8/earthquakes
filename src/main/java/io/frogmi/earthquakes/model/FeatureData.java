package io.frogmi.earthquakes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureData {
    private String id;
    private String type;
    private FeatureDataProperties properties;
    private FeatureDataGeometry geometry;

}
