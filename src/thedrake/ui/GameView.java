package thedrake.ui;

import javafx.scene.layout.VBox;
import thedrake.*;

public class GameView extends VBox {
    GameState gameState;

    public GameView() {
        gameState = createGameState();
        StackView stackView = new StackView(PlayingSide.BLUE, gameState.army(PlayingSide.BLUE).stack());
        stackView.update();
        StackView stackView2 = new StackView(PlayingSide.ORANGE, gameState.army(PlayingSide.ORANGE).stack());
        stackView2.update();
        this.getChildren().add(stackView);
        this.getChildren().add(new BoardView());
        this.getChildren().add(stackView2);
    }

    public GameState createGameState() {
        Board board = new Board(4);
        PositionFactory pf = board.positionFactory();
        board = board.withTiles(new Board.TileAt(pf.pos("a2"), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }
}
