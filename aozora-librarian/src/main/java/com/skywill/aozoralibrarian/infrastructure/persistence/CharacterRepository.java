package com.skywill.aozoralibrarian.infrastructure.persistence;

import com.skywill.aozoralibrarian.domain.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    Optional<Character> findByPagePath(String pagePath);
}