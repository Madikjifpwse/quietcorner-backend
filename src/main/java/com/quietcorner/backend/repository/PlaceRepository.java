package com.quietcorner.backend.repository;

import com.quietcorner.backend.model.Place;
import com.quietcorner.backend.model.NoiseLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCategory(String category);
    List<Place> findByWifi(boolean wifi);
    List<Place> findBySockets(boolean sockets);
    List<Place> findByNoiseLevel(NoiseLevel noiseLevel);

}
