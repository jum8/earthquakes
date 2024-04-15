package io.frogmi.earthquakes.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.frogmi.earthquakes.domain.Feature;
import io.frogmi.earthquakes.model.FeatureDTO;
import io.frogmi.earthquakes.model.FeatureData;
import io.frogmi.earthquakes.repos.FeatureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class EarthquakeDataLoader {

    private final FeatureRepository featureRepository;

    public EarthquakeDataLoader(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Scheduled(fixedDelay = 30,timeUnit = TimeUnit.DAYS)
    public void loadData() {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("*".repeat(10) + " Running EarthquakeDataLoader " + "*".repeat(10));

        String url = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_month.geojson";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        var responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.containsKey("features")) {
                Object features = responseBody.get("features");
                List<FeatureData> featureList = mapper.convertValue(features, new TypeReference<List<FeatureData>>() {});
                featureList.stream()
                        .filter(featureData ->
                                !featureRepository.existsByExternalId(featureData.getId()) &&
                                areRequiredPropertiesNotNull(featureData))
                        .map(featureData -> mapToEntity(featureData, new Feature()))
                        .forEach(featureRepository::save);
            }
        }
    }

    private boolean areRequiredPropertiesNotNull(FeatureData featureData) {
        return featureData.getProperties().getTitle() != null &&
                featureData.getProperties().getUrl() != null &&
                featureData.getProperties().getPlace() != null &&
                featureData.getProperties().getMagType() != null &&
                featureData.getGeometry().getCoordinates()[0] != null &&
                featureData.getGeometry().getCoordinates()[1] != null;
    }

    private Feature mapToEntity(final FeatureData featureData, final Feature feature) {
        feature.setType(featureData.getType());
        feature.setExternalId(featureData.getId());
        feature.setMagnitude(featureData.getProperties().getMag());
        feature.setPlace(featureData.getProperties().getPlace());
        feature.setTime(featureData.getProperties().getTime());
        feature.setTsunami(featureData.getProperties().getTsunami());
        feature.setMagType(featureData.getProperties().getMagType());
        feature.setTitle(featureData.getProperties().getTitle());
        feature.setLongitude(featureData.getGeometry().getCoordinates()[0]);
        feature.setLatitude(featureData.getGeometry().getCoordinates()[1]);
        feature.setExternalUrl(featureData.getProperties().getUrl());
        return feature;
    }
}
