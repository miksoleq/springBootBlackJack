package com.codewithmiki.springbootblackjack.controller;

import com.codewithmiki.springbootblackjack.dto.GameResponceRecord;
import com.codewithmiki.springbootblackjack.model.Game;
import com.codewithmiki.springbootblackjack.service.GameService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/games")
public class GameController {
    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public GameResponceRecord startGame(
        @RequestParam String playerId, @RequestParam BigDecimal betAmount){
        var game = gameService.startNewGame(playerId, betAmount);
        return new GameResponceRecord(game);
    }

    @PostMapping("/{gameId}/hit")
    public GameResponceRecord hitGame(@PathVariable String gameId){
        var game = gameService.hit(gameId);
        return new GameResponceRecord(game);
    }

    @PostMapping("/{gameId}/stand")
    public GameResponceRecord standGame(@PathVariable String gameId){
        var game = gameService.stand(gameId);
        return new GameResponceRecord(game);
    }

    @PostMapping("/{gameId}/double")
    public GameResponceRecord doubleGame(@PathVariable String gameId){
        var game = gameService.doubleDown(gameId);
        return new GameResponceRecord(game);
    }


}
