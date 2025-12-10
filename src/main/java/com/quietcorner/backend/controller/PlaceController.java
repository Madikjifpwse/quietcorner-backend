package com.quietcorner.backend.controller;

import com.quietcorner.backend.dto.PlaceDto;
import com.quietcorner.backend.model.NoiseLevel;
import com.quietcorner.backend.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@CrossOrigin
public class PlaceController {

    private final PlaceService service;

    public PlaceController(PlaceService service) {
        this.service = service;
    }
    @GetMapping
    public ResponseEntity<List<PlaceDto>> getFilteredPlaces(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean wifi,
            @RequestParam(required = false) Boolean sockets,
            @RequestParam(required = false) Boolean freeAccess,
            @RequestParam(required = false) NoiseLevel noiseLevel
    ) {
        List<PlaceDto> filteredPlaces = service.findFilteredPlaces(
                category,
                wifi,
                sockets,
                freeAccess,
                noiseLevel
        );
        return ResponseEntity.ok(filteredPlaces);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getById(@PathVariable Long id) {
        // service.findById возвращает PlaceDto
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}