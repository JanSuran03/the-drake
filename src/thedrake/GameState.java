package thedrake;

import java.io.PrintWriter;
import java.util.Optional;

public class GameState implements JSONSerializable {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(Board board, Army blueArmy, Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(Board board, Army blueArmy, Army orangeArmy, PlayingSide sideOnTurn, GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        return side == PlayingSide.BLUE ? blueArmy : orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        return sideOnTurn == PlayingSide.BLUE ? orangeArmy : blueArmy;
    }

    public Tile tileAt(BoardPos pos) {
        Optional<TroopTile> optTroopTile = blueArmy.boardTroops().at(pos);
        return optTroopTile.isPresent() ? optTroopTile.get()
                : (optTroopTile = orangeArmy.boardTroops().at(pos)).isPresent() ? optTroopTile.get()
                : board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        if (result != GameResult.IN_PLAY
                || origin == TilePos.OFF_BOARD
                || armyOnTurn().boardTroops().guards() < 2)
            return false;

        Tile tile = tileAt((BoardPos) origin);
        return tile.hasTroop() && ((TroopTile) tile).side() == sideOnTurn;
    }

    private boolean canStepTo(TilePos target) {
        return result == GameResult.IN_PLAY
                && target != TilePos.OFF_BOARD
                && tileAt((BoardPos) target).canStepOn();
    }

    private boolean canCaptureOn(TilePos target) {
        if (result != GameResult.IN_PLAY
                || target == TilePos.OFF_BOARD)
            return false;

        Tile tile = tileAt((BoardPos) target);
        return tile.hasTroop() && ((TroopTile) tile).side() != sideOnTurn;
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) { // todo?
        if (result != GameResult.IN_PLAY
                || target == TilePos.OFF_BOARD
                || armyOnTurn().stack().isEmpty()
                || !tileAt((BoardPos) target).canStepOn())
            return false;

        Army army = armyOnTurn();
        if (!army.boardTroops().isLeaderPlaced()) {
            return sideOnTurn == PlayingSide.BLUE
                    ? target.j() == 0
                    : target.j() == board.dimension() - 1;
        }

        return army.boardTroops().isPlacingGuards()
                ? army.boardTroops().leaderPosition().isNextTo(target)
                : army.boardTroops().troopPositions().stream().anyMatch(pos -> pos.isNextTo(target));
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }

    @Override
    public void toJSON(PrintWriter writer){
        writer.print("{\"result:\":");
        result.toJSON(writer);
        writer.print(",\"board\":");
        board.toJSON(writer);
        writer.print(",\"blueArmy\":");
        blueArmy.toJSON(writer);
        writer.print(",\"orangeArmy\":");
        orangeArmy.toJSON(writer);
    }
}
