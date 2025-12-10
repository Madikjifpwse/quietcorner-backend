package com.quietcorner.backend.controller;

import com.quietcorner.backend.dto.PlaceDto;
import com.quietcorner.backend.service.favorite.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getFavorites() {
        List<PlaceDto> favorites = favoriteService.getFavorites();
        return ResponseEntity.ok(favorites);
    }


    @PostMapping("/{placeId}")
    public ResponseEntity<Void> addFavorite(@PathVariable Long placeId) {
        boolean added = favoriteService.addFavorite(placeId);

        // 200 OK
        // 404 NOT FOUND
        return added ? ResponseEntity.ok().build() : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long placeId) {
        boolean removed = favoriteService.removeFavorite(placeId);

        //204 No Content
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}