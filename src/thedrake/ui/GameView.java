package thedrake.ui;

import javafx.scene.layout.VBox;
import thedrake.*;

public class GameView extends VBox {
    StackView blueStack;
    StackView orangeStack;
    BoardView boardView;
    GameState gameState;
    PositionFactory pf;

    public GameView() {
        StackView blueStack = new StackView(PlayingSide.BLUE);
        blueStack.getStyleClass().add("stack");
        StackView orangeStack = new StackView(PlayingSide.ORANGE);
        orangeStack.getStyleClass().add("stack");
        BoardView boardView = new BoardView();
        this.blueStack = blueStack;
        this.orangeStack = orangeStack;
        this.boardView = boardView;
        this.getStyleClass().add("game-view");
        this.getChildren().add(blueStack);
        this.getChildren().add(boardView);
        this.getChildren().add(orangeStack);
    }

    public void startGame() {
        Board board = new Board(4);
        PositionFactory pf = board.positionFactory();
        board = board.withTiles(new Board.TileAt(pf.pos("a2"), BoardTile.MOUNTAIN));
        GameState gameState = new StandardDrakeSetup().startState(board);
        this.boardView.setPf(pf);
        this.boardView.setBoard(gameState);
        this.gameState = gameState;
        this.pf = pf;
        this.blueStack.setStack(gameState.army(PlayingSide.BLUE).stack());
        this.orangeStack.setStack(gameState.army(PlayingSide.ORANGE).stack());
    }
}
