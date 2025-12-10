package com.quietcorner.backend.service;

import com.quietcorner.backend.dto.PlaceDto;
import com.quietcorner.backend.model.NoiseLevel;
import com.quietcorner.backend.model.Place;
import com.quietcorner.backend.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository repo;

    public PlaceService(PlaceRepository repo) {
        this.repo = repo;
    }

    public PlaceDto convertToDto(Place place) {
        return PlaceDto.builder()
                .id(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .address(place.getAddress())
                .category(place.getCategory())
                .rating(place.getRating())
                .cost(place.getCost())
                .image(place.getImage())
                .wifi(place.isWifi())
                .sockets(place.isSockets())
                .noiseLevel(place.getNoiseLevel())
                .build();
    }

    public Optional<PlaceDto> findById(Long id) {
        return repo.findById(id).map(this::convertToDto);
    }

    public List<PlaceDto> findFilteredPlaces(
            String category,
            Boolean wifi,
            Boolean sockets,
            Boolean freeAccess,
            NoiseLevel noiseLevel
    ) {
        List<Place> places = repo.findAll();

        return places.stream()
                .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
                .filter(p -> wifi == null || p.isWifi() == wifi)
                .filter(p -> sockets == null || p.isSockets() == sockets)
                .filter(p -> freeAccess == null || p.getCost().equalsIgnoreCase("free") == freeAccess)
                .filter(p -> noiseLevel == null || p.getNoiseLevel() == noiseLevel)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 3. МЕТОДЫ ДЛЯ ВНУТРЕННЕГО CRUD (ЕСЛИ ОСТАВИЛ)
    public Optional<Place> findEntityById(Long id) {
        return repo.findById(id);
    }
    public Place save(Place place) {
        return repo.save(place);
    }
    public void delete(Long id) {
        repo.deleteById(id);
    }
    public List<PlaceDto> findAll() {
        return repo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}