package com.codewithmiki.springbootblackjack.model.deckStrategies;

import com.codewithmiki.springbootblackjack.model.Card;

import java.util.List;

public interface DeckGenerationStrategy {
    List<Card> generateDeck();
}
