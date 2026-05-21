package com.skywill.aozoralibrarian.service;

import com.skywill.aozoralibrarian.domain.entity.Character;
import com.skywill.aozoralibrarian.infrastructure.persistence.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;

    }
    /* ページパスから司書キャラを取得 */

    public Character findByPagePath(String pagepath){
            return characterRepository.findByPagePath(pagepath)
            .orElseGet(() -> characterRepository.findByPagePath("/")
        .orElseThrow(() -> new IllegalStateException("DBに存在しないpagepathです。")));
        }
}