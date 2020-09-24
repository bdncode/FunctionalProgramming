package com.bdncode.functionalProgramming;

public class Coin {
    private final CoinSide coinSide;

    public Coin(CoinSide coinSide) {
        this.coinSide = coinSide;
    }

    public CoinSide getCoinSide() {
        return coinSide;
    }
}
