package com.codewithmiki.springbootblackjack.controller;

import com.codewithmiki.springbootblackjack.model.Player;
import com.codewithmiki.springbootblackjack.service.GameService;
import com.codewithmiki.springbootblackjack.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerService playerService;
    private final GameService gameService;

    public PlayerController(PlayerService playerService, GameService gameService) {
        this.playerService = playerService;
        this.gameService = gameService;
    }
    @PostMapping
    public Player createPlayer() {
        return playerService.createNewPlayer();
    }
    @DeleteMapping("/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayer(@PathVariable String playerId) {
        gameService.getActiveGames().remove(playerId);
        playerService.getPlayers().remove(playerId);
    }

}
