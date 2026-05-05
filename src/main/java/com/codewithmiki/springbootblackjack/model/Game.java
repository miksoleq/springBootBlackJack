package com.codewithmiki.springbootblackjack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Game {
    private final String id;
    @JsonIgnore
    private final Deck deck;
    private final Player player;
    private final Dealer dealer;
    private GameStatus status;

    public Game(Player player){
        this.id = UUID.randomUUID().toString();
        this.deck = new Deck();
        this.player = player;
        this.dealer = new Dealer();
        this.status = GameStatus.IN_PROGRESS;
        this.player.resetHand();
    }

    public String getId() {
        return id;
    }
    public Deck getDeck() {
        return deck;
    }
    public Player getPlayer() {
        return player;
    }
    public Dealer getDealer() {
        return dealer;
    }
    public GameStatus getStatus() {
        return status;
    }
    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
