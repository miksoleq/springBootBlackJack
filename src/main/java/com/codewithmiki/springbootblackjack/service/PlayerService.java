package com.codewithmiki.springbootblackjack.service;

import com.codewithmiki.springbootblackjack.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerService {
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public Player createNewPlayer() {
        Player player = new Player();
        players.put(player.getId(), player);
        return player;
    }

    public Player getPlayer(String playerId) {
        Player player = players.get(playerId);
        if (player == null) {
            throw new IllegalArgumentException("Nie znaleziono gracza o ID: " + playerId);
        }
        return player;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }
}
