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

    public List<FeatureGetAllDTO> findAll() {
        final List<Feature> features = featureRepository.findAll(Sort.by("id"));
        return features.stream()
                .map(feature -> mapToDTO(feature, new FeatureGetAllDTO()))
                .toList();
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

//    public Integer create(final FeatureDTO featureDTO) {
//        final Feature feature = new Feature();
//        mapToEntity(featureDTO, feature);
//        return featureRepository.save(feature).getId();
//    }

    public void update(final Integer id, final FeatureDTO featureDTO) {
        final Feature feature = featureRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(featureDTO, feature);
        featureRepository.save(feature);
    }

    public void delete(final Integer id) {
        featureRepository.deleteById(id);
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

    private Feature mapToEntity(final FeatureDTO featureDTO, final Feature feature) {
        feature.setType(featureDTO.getType());
        feature.setExternalId(featureDTO.getExternalId());
        feature.setMagnitude(featureDTO.getMagnitude());
        feature.setPlace(featureDTO.getPlace());
        feature.setTime(featureDTO.getTime());
        feature.setTsunami(featureDTO.getTsunami());
        feature.setMagType(featureDTO.getMagType());
        feature.setTitle(featureDTO.getTitle());
        feature.setLongitude(featureDTO.getLongitude());
        feature.setLatitude(featureDTO.getLatitude());
        feature.setExternalUrl(featureDTO.getExternalUrl());
        return feature;
    }

    public ReferencedWarning getReferencedWarning(final Integer id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Feature feature = featureRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Comment featureComment = commentRepository.findFirstByFeature(feature);
        if (featureComment != null) {
            referencedWarning.setKey("feature.comment.feature.referenced");
            referencedWarning.addParam(featureComment.getId());
            return referencedWarning;
        }
        return null;
    }

}
