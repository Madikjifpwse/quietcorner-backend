package com.quietcorner.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String category;

    private String address;

    private double latitude;
    private double longitude;

    private boolean wifi;
    private boolean sockets;

    private double rating;
    private String cost;

    private String image;

    @Enumerated(EnumType.STRING)
    private NoiseLevel noiseLevel;
}
