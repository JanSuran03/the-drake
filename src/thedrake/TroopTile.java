package thedrake;

import java.util.ArrayList;
import java.util.List;

import static thedrake.TroopFace.*;

public class TroopTile implements Tile {
    private Troop troop;
    private PlayingSide side;
    private TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side() {
        return side;
    }

    public TroopFace face() {
        return face;
    }

    public Troop troop() {
        return troop;
    }

    public boolean canStepOn() {
        return false;
    }

    // Vrací True
    public boolean hasTroop() {
        return true;
    }

    public TroopTile flipped() {
        return new TroopTile(troop, side, face == AVERS ? REVERS : AVERS);
    }

    public List<Move> movesFrom(BoardPos origin, GameState state) {
        List<Move> result = new ArrayList<>();
        for (TroopAction action : troop.actions(face)) {
            result.addAll(action.movesFrom(origin, side, state));
        }
        return result;
    }
}
