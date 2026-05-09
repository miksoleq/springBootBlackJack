package com.codewithmiki.springbootblackjack.model;

import com.codewithmiki.springbootblackjack.model.deckStrategies.DeckGenerationStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    List<Card> cards = new ArrayList<>();
    public Deck(DeckGenerationStrategy strategy) {
        cards = strategy.generateDeck();
    }
    public Card drawCard() {
        if(cards.isEmpty()) {
            return null;
        }
        Card card = cards.getLast();
        cards.removeLast();
        return card;
    }
    public List<Card> getCards() {
        return cards;
    }
}
