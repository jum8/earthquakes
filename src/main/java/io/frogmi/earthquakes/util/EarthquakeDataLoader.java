package io.frogmi.earthquakes.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.frogmi.earthquakes.domain.Feature;
import io.frogmi.earthquakes.model.FeatureData;
import io.frogmi.earthquakes.repos.FeatureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class EarthquakeDataLoader implements CommandLineRunner {

    private final FeatureRepository featureRepository;

    public EarthquakeDataLoader(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

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
                                featureData.getProperties().getTitle() != null &&
                                featureData.getProperties().getUrl() != null &&
                                featureData.getProperties().getPlace() != null &&
                                featureData.getProperties().getMagType() != null &&
                                featureData.getGeometry().getCoordinates()[0] != null &&
                                featureData.getGeometry().getCoordinates()[1] != null)
                        .map(featureData -> Feature.builder()
                            .type(featureData.getType())
                            .externalId(featureData.getId())
                            .magnitude(featureData.getProperties().getMag())
                            .place(featureData.getProperties().getPlace())
                            .time(featureData.getProperties().getTime())
                            .tsunami(featureData.getProperties().getTsunami())
                            .magType(featureData.getProperties().getMagType())
                            .title(featureData.getProperties().getTitle())
                            .longitude(featureData.getGeometry().getCoordinates()[0])
                            .latitude(featureData.getGeometry().getCoordinates()[1])
                            .externalUrl(featureData.getProperties().getUrl())
                            .build())
                        .forEach(featureRepository::save);
                System.out.println(featureList.get(0));
            }
        }
    }
}
