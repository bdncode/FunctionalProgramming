package com.bdncode.functionalProgramming;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.*;

import static com.bdncode.functionalProgramming.CoinSide.HEADS;
import static com.bdncode.functionalProgramming.CoinSide.TAILS;

public class FunctionalProgramming {

    public static void main(String[] args) {

        final int timesToFlipCoin = 2000;

        Supplier<CoinSide> flipCoin = () -> new Random().nextInt(2) == 1 ? HEADS : TAILS;

        BiPredicate<Integer, Integer> lowerThan = (firstNumber, secondNumber) -> firstNumber < secondNumber;

        Function<Integer, Integer> addOne = number -> number + 1;

        Function<Integer, List<Coin>> generateCoinsList =
                size -> {
                    int index = 0;
                    List<Coin> coins = new ArrayList<>(size);
                    while (lowerThan.test(index, size)) {
                        coins.add(new Coin(flipCoin.get()));
                        index = addOne.apply(index);
                    }
                    return coins;
                };

        List<Coin> flippedCoinsList = generateCoinsList.apply(timesToFlipCoin);

        Predicate<Coin> isHeads = coin -> coin.getCoinSide().equals(HEADS);

        Function<Coin,CoinSide> getSide = Coin::getCoinSide;// coin -> coin.getCoinSide()

        Function<CoinSide,Integer> sideValue = coinSide -> 1;

        BinaryOperator<Integer> sumSide = Integer::sum;// (numberOne, numberTwo) -> numberOne + numberTwo

        Function<List<Coin>, Integer> countHeads =
                coinsList -> coinsList.stream()
                        .filter(isHeads)
                        .map(getSide)
                        .map(sideValue)
                        .reduce(sumSide)
                        .orElse(0);

        int numberOfHeads = countHeads.apply(flippedCoinsList);

        BiFunction<Integer, Integer, BigDecimal> calculatePercentage =
                (numberOfSide, timesToFlip) -> BigDecimal.valueOf((double) numberOfSide * 100 / (double) timesToFlip)
                        .setScale(2, RoundingMode.HALF_UP);

        BigDecimal percentageOfHeads = calculatePercentage.apply(numberOfHeads, timesToFlipCoin);
        BigDecimal percentageOfTails = calculatePercentage.apply(timesToFlipCoin - numberOfHeads, timesToFlipCoin);

        BiFunction<BigDecimal, BigDecimal, Map<CoinSide,BigDecimal>> percentageToMap =
                (percentageOfHeadsToMap, percentageOfTailsToMap) -> new HashMap<>() {{
                        put(HEADS,percentageOfHeadsToMap);
                        put(TAILS,percentageOfTailsToMap);
                    }};

        Map<CoinSide, BigDecimal> coinsMap = percentageToMap.apply(percentageOfHeads, percentageOfTails);

        Consumer<CoinSide> showMap = coinSide -> System.out.println(coinSide+"  "+coinsMap.get(coinSide));

        for (CoinSide coinSide : coinsMap.keySet()) {
            showMap.accept(coinSide);
        }
    }
}
