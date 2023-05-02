package thedrake;

import java.util.List;

import static thedrake.TroopFace.*;

public class Troop {
    private final String name;
    private final Offset2D aversPivot, reversPivot;

    private final List<TroopAction> aversActions;
    private final List<TroopAction> reversActions;

    /**
     * The main constructor
     */
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    /**
     * A constructor which sets both pivots to the same value
     */
    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this(name, pivot, pivot, aversActions, reversActions);
    }

    /**
     * A constructor which sets both pivots offsets [1, 1]
     */
    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this(name, new Offset2D(1, 1), aversActions, reversActions);
    }

    public String name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face) {
        return face == AVERS ? aversPivot : reversPivot;
    }

    public List<TroopAction> actions(TroopFace face) {
        return face == AVERS ? aversActions : reversActions;
    }
}
