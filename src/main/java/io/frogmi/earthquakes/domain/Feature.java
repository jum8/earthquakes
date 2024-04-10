package io.frogmi.earthquakes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;

import lombok.*;


@Entity
@Getter
@Setter
@Builder
public class Feature {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer id;

    @Column
    private String type;

    @Column(nullable = false)
    private String externalId;

    @Column
    private Double magnitude;

    @Column(nullable = false)
    private String place;

    @Column
    private String time;

    @Column
    private Boolean tsunami;

    @Column(nullable = false)
    private String magType;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private Double latitude;

    @Column(nullable = false)
    private String externalUrl;

    @OneToMany(mappedBy = "feature")
    private Set<Comment> comments;

}
