package com.codewithmiki.springbootblackjack.dto;

import com.codewithmiki.springbootblackjack.model.Card;
import com.codewithmiki.springbootblackjack.model.Game;
import com.codewithmiki.springbootblackjack.model.GameStatus;
import com.codewithmiki.springbootblackjack.model.Player;

import java.util.List;

public record GameResponceRecord(String id, GameStatus status,
                                 List<Card> playerHand, List<Card> dealerHand, Player player) {

    public GameResponceRecord(Game game){
        this(
        game.getId(),
        game.getStatus(),
        game.getPlayer().getHand(),
        (game.getStatus() == GameStatus.IN_PROGRESS && !game.getDealer().getHand().isEmpty()) ?
                List.of(game.getDealer().getHand().getFirst()) : game.getDealer().getHand(),
                game.getPlayer());
    }
}
