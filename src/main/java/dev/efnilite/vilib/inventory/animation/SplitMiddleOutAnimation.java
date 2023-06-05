package dev.efnilite.vilib.inventory.animation;

/**
 * An animation which goes from the middle to the sides.
 * <p>
 * ---------   ----x----   ---xxx---   --xxxxx--
 * --------- > ----x---- > ---xxx--- > --xxxxx-- > etc.
 * ---------   ----x----   ---xxx---   --xxxxx--
 * <p>
 * Expected duration: 4 ticks
 * Expected time: 0.2s
 *
 * @author Efnilite
 */
public final class SplitMiddleOutAnimation extends MenuAnimation {

    @Override
    public void init(int rows) {
        // middle row first since uneven
        add(0, getVertical(4, rows));

        add(1, getVertical(5, rows));
        add(1, getVertical(3, rows));

        add(2, getVertical(6, rows));
        add(2, getVertical(2, rows));

        add(3, getVertical(7, rows));
        add(3, getVertical(1, rows));

        add(4, getVertical(8, rows));
        add(4, getVertical(0, rows));
    }
}
