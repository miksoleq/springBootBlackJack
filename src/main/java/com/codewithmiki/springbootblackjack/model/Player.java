package com.codewithmiki.springbootblackjack.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public class Player extends Person {
    BigDecimal money;
    BigDecimal currentBet;
    String id;


    public Player() {
        this.money = new BigDecimal("1000.00");
        this.currentBet = new BigDecimal("0.00");
        this.id = UUID.randomUUID().toString();
        this.hand = new ArrayList<>();
    }

    public void resetHand() {
        this.hand.clear();
        this.score = 0;
        this.currentBet = BigDecimal.ZERO;
    }

    public void placeBet(BigDecimal betAmount) {
        if (betAmount.compareTo(money) > 0) {
            throw new IllegalArgumentException("Nie masz wystarczających środków!");
        }
        if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Zakład musi być większy niż 0!");
        }
        this.money = this.money.subtract(betAmount);
        this.currentBet = betAmount;
    }

    public void winBet(BigDecimal multiplier) {
        BigDecimal winnings = this.currentBet.multiply(multiplier);
        this.money = this.money.add(winnings);
        this.currentBet = BigDecimal.ZERO;
    }

    public void returnBet() {
        this.money = this.money.add(this.currentBet);
        this.currentBet = BigDecimal.ZERO;
    }


    public void setCurrentBet(BigDecimal currentBet) {
        this.currentBet = currentBet;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }
    public BigDecimal getCurrentBet() {
        return currentBet;
    }

    public String getId() {
        return id;
    }

}
