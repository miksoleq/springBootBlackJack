package com.codewithmiki.springbootblackjack.service;

import com.codewithmiki.springbootblackjack.model.Game;
import com.codewithmiki.springbootblackjack.model.GameStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, Game> activeGames = new ConcurrentHashMap<>();
    private final PlayerService playerService;
    public GameService(PlayerService playerService) {
        this.playerService = playerService;
    }
    public Game startNewGame(String playerId, BigDecimal betAmount) {
        var player = playerService.getPlayer(playerId);

        var game = new Game(player);
        player.placeBet(betAmount);

        game.getDeck().shuffle();

        for(var i = 0; i < 2; i++) {
            game.getPlayer().addCard(game.getDeck().drawCard());
            game.getDealer().addCard(game.getDeck().drawCard());
        }
        activeGames.put(game.getId(), game);
        return game;
    }

    public Game hit(String gameId){
        var game = activeGames.get(gameId);
        if(game == null)
            throw new IllegalArgumentException("Game with id " + gameId + " not found");
        if(game.getStatus() != GameStatus.IN_PROGRESS)
            throw new IllegalArgumentException("Game with id " + gameId + " is not in progress");

        game.getPlayer().addCard(game.getDeck().drawCard());

        if(game.getPlayer().count() > 21) {
            game.setStatus(GameStatus.DEALER_WON);
            activeGames.remove(gameId);
        }

        return game;
    }

    public Game stand(String gameId){
        var game = activeGames.get(gameId);
        if(game == null)
            throw new IllegalArgumentException("Game with id " + gameId + " not found");
        if(game.getStatus() != GameStatus.IN_PROGRESS)
            throw new IllegalArgumentException("Game with id " + gameId + " is not in progress");

        while(game.getDealer().count() < 17){
            game.getDealer().addCard(game.getDeck().drawCard());
        }

        if(game.getDealer().count() > 21 || game.getDealer().count() < game.getPlayer().count() ) {
            game.setStatus(GameStatus.PLAYER_WON);
            game.getPlayer().winBet(new BigDecimal("2.00"));
        } else if (game.getDealer().count() > game.getPlayer().count()) {
            game.setStatus(GameStatus.DEALER_WON);
        }else{
            game.setStatus(GameStatus.TIE);
            game.getPlayer().returnBet();
        }
        activeGames.remove(gameId);
        return game;
    }

    public Map<String, Game> getActiveGames() {
        return activeGames;
    }
}
