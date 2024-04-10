package io.frogmi.earthquakes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeatureDataProperties {

    private Double mag;

    private String place;

    private String time;

    private String url;

    private Boolean tsunami;

    private String magType;

    private String title;

}
