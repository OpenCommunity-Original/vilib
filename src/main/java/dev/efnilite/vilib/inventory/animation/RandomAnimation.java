package dev.efnilite.vilib.inventory.animation;

import dev.efnilite.vilib.util.Colls;
import dev.efnilite.vilib.util.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * An animation which picks random slots to be set
 * <p>
 * ---------   -----x---   -----x---   -xx--x---
 * --------- > x-------- > x--x---x- > x--x---x- > etc.
 * ---------   ----x----   -x--x----   -x--x--x-
 * <p>
 * Expected duration: 4 ticks
 * Expected time: 0.2s
 *
 * @author Efnilite
 */
public final class RandomAnimation extends MenuAnimation {

    private static final int DURATION_TICKS = 4;

    // the amount of items per tick
    private int amountPerTick;
    private List<Integer> available;

    @Override
    public void init(int rows) {
        available = Numbers.getFromZero(rows * 9 - 1);

        amountPerTick = available.size() / DURATION_TICKS; // 4 = duration

        IntStream.range(0, DURATION_TICKS).forEach(i -> add(i, getRandomSlots()));
    }

    private List<Integer> getRandomSlots() {
        List<Integer> selected = Colls.random(available, amountPerTick);
        available.removeAll(selected);
        return selected;
    }
}
