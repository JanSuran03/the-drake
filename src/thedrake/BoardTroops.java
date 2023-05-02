package thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap,
                       TilePos leaderPosition, int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    @Override
    protected BoardTroops clone() {
        return new BoardTroops(playingSide, new HashMap<>(troopMap), leaderPosition, guards);
    }

    public Optional<TroopTile> at(TilePos pos) {
        return Optional.ofNullable(troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public Set<Map.Entry<BoardPos, TroopTile>> troopEntries() {
        return troopMap.entrySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (at(target).isPresent()) {
            throw new IllegalArgumentException("Cannot place troop, the position is taken.");
        }

        BoardTroops newBoardTroops;
        if (!isLeaderPlaced()) {
            newBoardTroops = new BoardTroops(playingSide, new HashMap<>(troopMap), target, 0);
        } else if (isPlacingGuards()) {
            newBoardTroops = new BoardTroops(playingSide, new HashMap<>(troopMap), leaderPosition, guards + 1);
        } else {
            newBoardTroops = clone();
        }
        newBoardTroops.troopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
        return newBoardTroops;
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException("Cannot move troop, place the leader and guards first.");

        if (at(origin).isEmpty())
            throw new IllegalArgumentException("Cannot move troop, the troop tile is empty.");

        if (at(target).isPresent())
            throw new IllegalArgumentException("Cannot move troop, the position is taken.");

        HashMap<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile tile = newTroopMap.remove(origin);
        newTroopMap.put(target, tile.flipped());
        return new BoardTroops(playingSide,
                newTroopMap,
                leaderPosition.i() == origin.i() && leaderPosition.j() == origin.j() ? target : leaderPosition,
                guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException("Cannot flip troop before the leader and both guards are placed.");

        if (at(origin).isEmpty())
            throw new IllegalArgumentException("Cannot flip troop, the position is empty.");

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException("Cannot remove troop before the leader and both guards is placed.");

        Optional<TroopTile> troopTile = at(target);
        if (troopTile.isEmpty())
            throw new IllegalArgumentException("Cannot remove troop, the position is empty.");

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        return new BoardTroops(playingSide,
                newTroops,
                target.i() == leaderPosition.i() && target.j() == leaderPosition.j() ? TilePos.OFF_BOARD : leaderPosition,
                guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.printf("{side:\"%s\"", playingSide.name());
        writer.printf(",\"leaderPosition\":\"%s\"", leaderPosition.toString());
        writer.printf(",\"guards\":%d,\"troopMap\":{", guards);
        List<Map.Entry<BoardPos, TroopTile>> troopMapEntries = new ArrayList<>(troopMap.entrySet());
        troopMapEntries.sort(Comparator.comparing(e -> e.getKey().toString()));
        for (int i = 0; i < troopMapEntries.size(); i++) {
            if (i != 0)
                writer.print(",");
            Map.Entry<BoardPos, TroopTile> entry = troopMapEntries.get(i);
            writer.printf("\"%s\":", entry.getKey().toString());
            entry.getValue().toJSON(writer);
        }
        writer.print("}}");
    }
}
