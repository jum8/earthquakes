package io.frogmi.earthquakes.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FeatureDTO {

    private Integer id;

    @Size(max = 255)
    private String type;

    @NotNull
    @Size(max = 255)
    private String externalId;

    private Double magnitude;

    @NotNull
    @Size(max = 255)
    private String place;

    @Size(max = 255)
    private String time;

    private Boolean tsunami;

    @NotNull
    @Size(max = 255)
    private String magType;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private Double longitude;

    private Double latitude;

    @NotNull
    @Size(max = 255)
    private String externalUrl;

}
