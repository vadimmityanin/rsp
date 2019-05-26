package com.mityanin.rsp.controller;

import com.mityanin.rsp.domain.dto.GameOutcome;
import com.mityanin.rsp.domain.enums.GameEntity;
import com.mityanin.rsp.service.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/rsp")
    public GameOutcome play(@RequestParam String username, @RequestParam GameEntity entity) {
        return gameService.play(username, entity);
    }
}
