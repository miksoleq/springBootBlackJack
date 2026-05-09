package com.codewithmiki.springbootblackjack.model.deckStrategies;

import com.codewithmiki.springbootblackjack.model.Card;
import com.codewithmiki.springbootblackjack.model.Colour;
import com.codewithmiki.springbootblackjack.model.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Component
public class StandardDeckStrategy implements DeckGenerationStrategy{
    @Override
    public List<Card> generateDeck() {
        List<Card> cards = new ArrayList<>();
        for(Colour colour : Colour.values()) {
            for (Value value : Value.values()) {
                cards.add(new Card(value, colour));
            }
        }
        Collections.shuffle(cards);
        return cards;
    }
}
