package com.codewithmiki.springbootblackjack.service;

import com.codewithmiki.springbootblackjack.model.Game;
import com.codewithmiki.springbootblackjack.model.GameStatus;
import com.codewithmiki.springbootblackjack.model.deckStrategies.DeckGenerationStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, Game> activeGames = new ConcurrentHashMap<>();
    private final PlayerService playerService;
    private final Map<String, String> playerGame =  new ConcurrentHashMap<>();
    private final DeckGenerationStrategy deckGenerationStrategy;

    public GameService(PlayerService playerService, DeckGenerationStrategy deckGenerationStrategy) {
        this.playerService = playerService;
        this.deckGenerationStrategy = deckGenerationStrategy;
    }

    public Game startNewGame(String playerId, BigDecimal betAmount) {
        var player = playerService.getPlayer(playerId);
        if(playerGame.containsKey(playerId))
            throw new IllegalArgumentException("Player with id " + playerId + " is already in an active game.");

        var game = new Game(player, deckGenerationStrategy);
        player.placeBet(betAmount);

        for(var i = 0; i < 2; i++) {
            game.getPlayer().addCard(game.getDeck().drawCard());
            game.getDealer().addCard(game.getDeck().drawCard());
        }
        var playerScore = game.getPlayer().getScore();
        var dealerScore = game.getDealer().getScore();
        if(playerScore == 21) {
            if(dealerScore == 21) {
                game.setStatus(GameStatus.TIE);
                game.getPlayer().returnBet();
                game.getPlayer().setCurrentBet(BigDecimal.ZERO);
            }
            else {
                game.setStatus(GameStatus.PLAYER_WON);
                game.getPlayer().winBet(new BigDecimal("2.50"));
                game.getPlayer().setCurrentBet(BigDecimal.ZERO);
            }
            return game;
        }
        if(dealerScore == 21) {
            game.setStatus(GameStatus.DEALER_WON);
            game.getPlayer().setCurrentBet(BigDecimal.ZERO);
            return game;
        }


        activeGames.put(game.getId(), game);
        playerGame.put(playerId, game.getId());
        return game;
    }

    public Game hit(String gameId){
        var game = activeGames.get(gameId);
        if(game == null)
            throw new IllegalArgumentException("Game with id " + gameId + " not found");
        synchronized (game) {
        if(game.getStatus() != GameStatus.IN_PROGRESS)
            throw new IllegalArgumentException("Game with id " + gameId + " is not in progress");

        game.getPlayer().addCard(game.getDeck().drawCard());

        if(game.getPlayer().count() > 21) {
            game.setStatus(GameStatus.DEALER_WON);
            activeGames.remove(gameId);
            playerGame.remove(game.getPlayer().getId());
            game.getPlayer().setCurrentBet(BigDecimal.ZERO);
        }

        return game;
        }
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
        game.getPlayer().setCurrentBet(BigDecimal.ZERO);
        activeGames.remove(gameId);
        playerGame.remove(game.getPlayer().getId());
        return game;
    }
    public Game doubleDown(String gameId){
        var game = activeGames.get(gameId);
        if(game == null)
            throw new IllegalArgumentException("Game with id " + gameId + " not found");
        if(game.getStatus() != GameStatus.IN_PROGRESS)
            throw new IllegalArgumentException("Game with id " + gameId + " is not in progress");
        if(game.getPlayer().getHand().size() > 2)
            throw new IllegalArgumentException("Cannot double in game:" + gameId +". You already took a hit" );
        if(game.getPlayer().getMoney().compareTo(game.getPlayer().getCurrentBet()) < 0)
            throw new IllegalArgumentException("Cannot double in game:" + gameId +". Not enough money");


        game.getPlayer().placeBet(game.getPlayer().getCurrentBet());
        game.getPlayer().addCard(game.getDeck().drawCard());
        if(game.getPlayer().count() > 21) {
            game.setStatus(GameStatus.DEALER_WON);
            activeGames.remove(gameId);
            playerGame.remove(game.getPlayer().getId());
            game.getPlayer().setCurrentBet(BigDecimal.ZERO);
            return game;
        }
        return stand(gameId);
    }



    public Map<String, Game> getActiveGames() {
        return activeGames;
    }

    public Map<String, String> getPlayerGame() {
        return playerGame;
    }

    public Game getGameForPlayer(String playerId) {
        if(!playerService.getPlayers().containsKey(playerId))
            throw new IllegalArgumentException("Player with id " + playerId + " not found");
        if(!playerGame.containsKey(playerId))
            throw new IllegalArgumentException("Player with id: "+ playerId + " is not in an active game");
        return activeGames.get(playerGame.get(playerId));
    }

}
