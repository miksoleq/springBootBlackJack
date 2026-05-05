package com.codewithmiki.springbootblackjack.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {
    protected List<Card> hand = new ArrayList<>();
    protected int score;

    public List<Card> getHand() {
        return hand;
    }
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void addCard(Card card) {
        hand.add(card);
        this.count();
    }

    public int count() {
        var sum = 0;
        var ace_count = 0;
        for (Card card : hand) {
            if (card.value() == Value.ACE)
                ace_count++;
            sum += card.value().getNumericValue();
        }

        var pom = ace_count;
        while (sum > 21 && pom > 0) {
            sum -= 10;
            pom--;
        }
        this.score = sum;
        return sum;
    }

    public int getScore() {
        return score;
    }
}
