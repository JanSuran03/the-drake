package thedrake;

import static thedrake.TroopFace.*;

public class Troop {
    private final String name;
    private final Offset2D aversPivot, reversPivot;

    /**
     * The main constructor
     */
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
    }

    /**
     * A construcor which sets both pivots to the same value
     */
    public Troop(String name, Offset2D pivot) {
        this(name, pivot, pivot);
    }

    /**
     * A constructor which sets both pivots offsets [1, 1]
     */
    public Troop(String name) {
        this(name, new Offset2D(1, 1));
    }

    public String name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face) {
        return face == AVERS ? aversPivot : reversPivot;
    }
}
