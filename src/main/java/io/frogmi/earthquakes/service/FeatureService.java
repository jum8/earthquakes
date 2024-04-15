package io.frogmi.earthquakes.service;

import io.frogmi.earthquakes.domain.Comment;
import io.frogmi.earthquakes.domain.Feature;
import io.frogmi.earthquakes.model.*;
import io.frogmi.earthquakes.repos.CommentRepository;
import io.frogmi.earthquakes.repos.FeatureRepository;
import io.frogmi.earthquakes.util.NotFoundException;
import io.frogmi.earthquakes.util.ReferencedWarning;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class FeatureService {

    private final FeatureRepository featureRepository;
    private final CommentRepository commentRepository;

    public FeatureService(final FeatureRepository featureRepository,
            final CommentRepository commentRepository) {
        this.featureRepository = featureRepository;
        this.commentRepository = commentRepository;
    }



    public Page<FeatureGetAllDTO> findAllFeaturesByMagType(String[] magType, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Feature> features = featureRepository.findAllFeaturesbyMagType(magType, pageRequest);

        List<FeatureGetAllDTO> dtos = features.map(feature -> mapToDTO(feature, new FeatureGetAllDTO()))
                .toList();

        return new PageImpl<>(dtos, features.getPageable(), features.getTotalElements());
    }

    public FeatureDTO get(final Integer id) {
        return featureRepository.findById(id)
                .map(feature -> mapToDTO(feature, new FeatureDTO()))
                .orElseThrow(NotFoundException::new);
    }

    private FeatureDTO mapToDTO(final Feature feature, final FeatureDTO featureDTO) {
        featureDTO.setId(feature.getId());
        featureDTO.setType(feature.getType());
        featureDTO.setExternalId(feature.getExternalId());
        featureDTO.setMagnitude(feature.getMagnitude());
        featureDTO.setPlace(feature.getPlace());
        featureDTO.setTime(feature.getTime());
        featureDTO.setTsunami(feature.getTsunami());
        featureDTO.setMagType(feature.getMagType());
        featureDTO.setTitle(feature.getTitle());
        featureDTO.setLongitude(feature.getLongitude());
        featureDTO.setLatitude(feature.getLatitude());
        featureDTO.setExternalUrl(feature.getExternalUrl());
        return featureDTO;
    }

    private FeatureGetAllDTO mapToDTO(final Feature feature, final FeatureGetAllDTO featureDTO) {
        featureDTO.setId(feature.getId());
        featureDTO.setType(feature.getType());
        FeatureAttributes attributes = FeatureAttributes.builder()
                .externalId(feature.getExternalId())
                .magnitude(feature.getMagnitude())
                .place(feature.getPlace())
                .time(feature.getTime())
                .tsunami(feature.getTsunami())
                .magType(feature.getMagType())
                .title(feature.getTitle())
                .coordinates(
                        Coordinates.builder()
                                .longitude(feature.getLongitude())
                                .latitude(feature.getLatitude())
                                .build()
                )
                .build();

        featureDTO.setAttributes(attributes);
        featureDTO.setLinks(
                Links.builder()
                        .externalUrl(feature.getExternalUrl())
                        .build()
        );
        return featureDTO;
    }

}
