package io.frogmi.earthquakes.rest;

import io.frogmi.earthquakes.model.FeatureDTO;
import io.frogmi.earthquakes.model.FeatureGetAllDTO;
import io.frogmi.earthquakes.service.FeatureService;
import io.frogmi.earthquakes.util.ReferencedException;
import io.frogmi.earthquakes.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/features", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeatureResource {

    private final FeatureService featureService;

    public FeatureResource(final FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    public Map<String, Object> getAllFeatures() {
        List<FeatureGetAllDTO> featureDTOS = featureService.findAll();
        Map<String, Object> map = new HashMap<>();
        map.put("data", featureDTOS);
        return map;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureDTO> getFeature(@PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(featureService.get(id));
    }

//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Integer> createFeature(@RequestBody @Valid final FeatureDTO featureDTO) {
//        final Integer createdId = featureService.create(featureDTO);
//        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateFeature(@PathVariable(name = "id") final Integer id,
            @RequestBody @Valid final FeatureDTO featureDTO) {
        featureService.update(id, featureDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFeature(@PathVariable(name = "id") final Integer id) {
        final ReferencedWarning referencedWarning = featureService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        featureService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
