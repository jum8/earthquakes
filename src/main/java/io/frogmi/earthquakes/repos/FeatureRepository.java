package io.frogmi.earthquakes.repos;

import io.frogmi.earthquakes.domain.Feature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FeatureRepository extends JpaRepository<Feature, Integer> {
    public Boolean existsByExternalId(String externalId);

    @Query("SELECT f FROM Feature f WHERE " +
            "(COALESCE(:magType, '') = '' OR f.magType IN (:magType))")

    Page<Feature> findAllFeaturesbyMagType(@Param("magType") String magType, Pageable pageable);
}
