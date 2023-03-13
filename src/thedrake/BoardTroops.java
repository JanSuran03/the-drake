package thedrake;

import java.util.*;

public class BoardTroops {
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

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (troopMap.containsKey(target)) {
            throw new IllegalArgumentException("Cannot place troop, the position is taken.");
        }

        TroopTile tile = new TroopTile(troop, playingSide, TroopFace.AVERS);
        BoardTroops newBoardTroops;
        if (!isLeaderPlaced()) {
            newBoardTroops = new BoardTroops(playingSide, new HashMap<>(troopMap), target, 0);
            newBoardTroops.troopMap.put(target, tile);
        } else if (isPlacingGuards()) {
            newBoardTroops = new BoardTroops(playingSide, new HashMap<>(troopMap), leaderPosition, guards + 1);
            newBoardTroops.troopMap.put(target, tile);
        } else {
            newBoardTroops = clone();
            newBoardTroops.troopMap.put(target, tile);
        }
        return newBoardTroops;
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException("Cannot move troop, place the leader and guards first.");

        if (origin == BoardPos.OFF_BOARD)
            throw new IllegalArgumentException("Cannot move troop off the board.");

        TroopTile tile = troopMap.get(origin);
        if (tile == null)
            throw new IllegalArgumentException("Cannot move troop, the troop tile is empty.");

        if (troopMap.containsKey(target))
            throw new IllegalArgumentException("Cannot move troop, the position is taken.");

        if (leaderPosition.i() == origin.i() && leaderPosition.j() == origin.j()) {
            BoardTroops newBoardTroops = new BoardTroops(playingSide, new HashMap<>(troopMap),
                    leaderPosition.step(target.i() - origin.i(), target.j() - origin.j()), guards);
            newBoardTroops.troopMap.put(target, tile.flipped());
            newBoardTroops.troopMap.remove(origin);
            return newBoardTroops;
        }

        BoardTroops newBoardTroops = clone();
        troopMap.put(target, tile.flipped());
        troopMap.remove(origin);
        return newBoardTroops;
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced())
            throw new IllegalStateException("Cannot flip troop before the leader is placed.");

        if (isPlacingGuards())
            throw new IllegalStateException("Cannot flip troop before guards are placed.");

        if (at(origin).isEmpty())
            throw new IllegalArgumentException("Cannot flip troop, the position is empty.");

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced())
            throw new IllegalStateException("Cannot remove troop before the leader is placed.");

        if (isPlacingGuards())
            throw new IllegalStateException("Cannot remove troop before guards are placed.");

        Optional<TroopTile> troopTile = at(target);
        if (troopTile.isEmpty())
            throw new IllegalArgumentException("Cannot remove troop, the position is empty.");

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        newTroops.remove(target);

        if (target.i() == leaderPosition.i() && target.j() == leaderPosition.j())
            return new BoardTroops(playingSide, newTroops, TilePos.OFF_BOARD, guards);

        return new BoardTroops(playingSide, newTroops, leaderPosition, guards);
    }
}
