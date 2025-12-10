package com.quietcorner.backend.loader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quietcorner.backend.model.NoiseLevel;
import com.quietcorner.backend.model.Place;
import com.quietcorner.backend.repository.PlaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private final PlaceRepository placeRepository;
    private final ResourceLoader resourceLoader;

    public DataLoader(PlaceRepository placeRepository, ResourceLoader resourceLoader) {
        this.placeRepository = placeRepository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void run(String... args) throws Exception {
        if (placeRepository.count() == 0) {
            loadPlacesFromResource("classpath:places.json");
        } else {
            System.out.println("База данных 'places' уже содержит данные. Загрузка JSON пропущена.");
        }
    }

    private void loadPlacesFromResource(String resourcePath) {
        ObjectMapper mapper = new ObjectMapper();
        int count = 0;

        try {
            Resource resource = resourceLoader.getResource(resourcePath);
            try (InputStream is = resource.getInputStream()) {

                JsonNode rootNode = mapper.readTree(is);

                JsonNode categoriesNode = rootNode.get(0);

                Iterator<Map.Entry<String, JsonNode>> fields = categoriesNode.fields();

                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String category = entry.getKey();
                    JsonNode placesArray = entry.getValue();

                    NoiseLevel noiseLevel = determineNoiseLevel(category);

                    if (placesArray.isArray()) {
                        for (JsonNode placeNode : placesArray) {

                            Place place = Place.builder()
                                    .name(placeNode.get("name").asText())
                                    .address(placeNode.get("address").asText())
                                    .latitude(placeNode.get("latitude").asDouble())
                                    .longitude(placeNode.get("longitude").asDouble())
                                    .description(placeNode.get("description").asText())
                                    .category(category)
                                    .wifi(placeNode.get("wifi").asBoolean())
                                    .sockets(placeNode.get("sockets").asBoolean())
                                    .cost(placeNode.get("cost").asText())
                                    .image(placeNode.has("image") && !placeNode.get("image").isNull() ?
                                            placeNode.get("image").asText() : null)
                                    .rating(placeNode.get("rating").asDouble())
                                    .noiseLevel(noiseLevel)
                                    .build();

                            placeRepository.save(place);
                            count++;
                        }
                    }
                }
            }
            System.out.println("✅ Успешно загружено " + count + " мест из JSON в БД.");

        } catch (Exception e) {
            System.err.println("❌ Ошибка при загрузке данных из JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private NoiseLevel determineNoiseLevel(String category) {
        return switch (category.toLowerCase()) {
            case "library", "coworking" -> NoiseLevel.QUIET;
            case "cafe" -> NoiseLevel.MODERATE;
            default -> NoiseLevel.LOUD;
        };
    }
}