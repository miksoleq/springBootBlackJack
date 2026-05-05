package com.codewithmiki.springbootblackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    List<Card> cards = new ArrayList<>();
    public Deck() {
        for(Colour colour : Colour.values()) {
            for (Value value : Value.values()) {
                cards.add(new Card(value, colour));
            }
        }
    }
    public void shuffle() {
        if(cards.isEmpty()) {
            return;
        }
        Random rand = new Random();
        int randomInt;
        for(int i = cards.size() - 1; i > 0; i--) {
            randomInt = rand.nextInt(i+1);
            Collections.swap(cards, i, randomInt);
        }
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
