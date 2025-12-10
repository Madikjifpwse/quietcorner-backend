package com.quietcorner.backend.service.favorite;

import com.quietcorner.backend.dto.PlaceDto;
import com.quietcorner.backend.model.Place;
import com.quietcorner.backend.repository.PlaceRepository;
import com.quietcorner.backend.service.PlaceService;
import com.quietcorner.backend.user.User;
import com.quietcorner.backend.user.UserRepository;
import com.quietcorner.backend.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final PlaceService placeService;
    private final SecurityUtils securityUtils;

    /**
     * Получение всех избранных мест.
     */
    @Transactional(readOnly = true)
    public List<PlaceDto> getFavorites() {
        // 1. Берем имя пользователя из токена
        String username = securityUtils.getCurrentUser().getUsername();

        // 2. ВАЖНО: Загружаем пользователя заново в текущей транзакции!
        // Это привязывает его к сессии Hibernate.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Теперь getFavoritePlaces() сработает без ошибки
        return user.getFavoritePlaces().stream()
                .map(placeService::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавление места в избранное.
     */
    @Transactional
    public boolean addFavorite(Long placeId) {
        String username = securityUtils.getCurrentUser().getUsername();

        // Перезагружаем пользователя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new RuntimeException("Place not found"));

        Set<Place> favorites = user.getFavoritePlaces(); // Инициализация коллекции
        boolean added = favorites.add(place);

        if (added) {
            userRepository.save(user);
        }

        return added;
    }

    /**
     * Удаление места из избранного.
     */
    @Transactional
    public boolean removeFavorite(Long placeId) {
        String username = securityUtils.getCurrentUser().getUsername();

        // Перезагружаем пользователя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean removed = user.getFavoritePlaces()
                .removeIf(p -> p.getId().equals(placeId));

        if (removed) {
            userRepository.save(user);
        }

        return removed;
    }
}