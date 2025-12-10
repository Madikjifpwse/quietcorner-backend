package com.quietcorner.backend.dto;

import com.quietcorner.backend.model.NoiseLevel;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PlaceDto {
    private Long id;
    private String name;
    private String description;

    private double latitude;
    private double longitude;

    private String address;
    private String category;

    private double rating;
    private String cost;
    private String image;

    private boolean wifi;
    private boolean sockets;
    private NoiseLevel noiseLevel;
}