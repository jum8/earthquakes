package io.frogmi.earthquakes.repos;

import io.frogmi.earthquakes.domain.Feature;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FeatureRepository extends JpaRepository<Feature, Integer> {
}
