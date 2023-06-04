package thedrake.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import thedrake.*;

import java.util.HashMap;
import java.util.Map;

public class GameView extends AnchorPane {

    GameState gameState;
    TroopFieldsView troopFieldsView;
    StateView stateView;

    public boolean started = false;

    private BoardView boardView() {
        return troopFieldsView.boardView;
    }

    private StackView blueStack() {
        return troopFieldsView.blueStack;
    }

    private StackView orangeStack() {
        return troopFieldsView.orangeStack;
    }

    public GameView() {
        TroopFieldsView troopFieldsView = new TroopFieldsView();
        AnchorPane.setLeftAnchor(troopFieldsView, 0.0);
        this.stateView = new StateView();
        AnchorPane.setRightAnchor(stateView, 0.0);
        this.troopFieldsView = troopFieldsView;
        this.getChildren().add(troopFieldsView);
        this.getChildren().add(stateView);
        this.getStyleClass().add("game-view");
    }

    public void startGame() {
        Board board = new Board(4);
        PositionFactory pf = board.positionFactory();
        board = board.withTiles(new Board.TileAt(pf.pos("b2"), BoardTile.MOUNTAIN),
                new Board.TileAt(pf.pos("c3"), BoardTile.MOUNTAIN));
        this.gameState = new StandardDrakeSetup().startState(board);
        this.boardView().setPf(pf);
        this.boardView().setBoard(gameState);
        this.troopFieldsView.pf = pf;
        this.blueStack().setStack(this.gameState.army(PlayingSide.BLUE).stack());
        this.orangeStack().setStack(this.gameState.army(PlayingSide.ORANGE).stack());
        this.blueStack().setHighlighted(true);
        EventBus.registerHandler("unset-all-selected", e -> {
            EventBus.fireEvent("unset-selected-board", null);
            EventBus.fireEvent("unset-selected-stack-1", null);
            EventBus.fireEvent("unset-selected-stack-2", null);
            EventBus.fireEvent("set-selected-stack-flag", new HashMap<>(Map.of("selected", false)));
        });

        EventBus.registerHandler("show-possible-moves", ev_data -> {
            if (ev_data.get("side") == this.gameState.sideOnTurn()) {
                for (int i = 0; i < this.gameState.board().dimension(); i++)
                    for (int j = 0; j < this.gameState.board().dimension(); j++) {
                        BoardPos pos = pf.pos(i, j);
                        if (this.gameState.canPlaceFromStack(pos)) {
                            boardView().tiles[i][j].setCanMoveOn(true);
                        }
                    }
            }
        });
        EventBus.registerHandler("set-stack", ev_data -> {
            PlayingSide side = (PlayingSide) ev_data.get("side");
            if (side == PlayingSide.BLUE)
                blueStack().setStack(this.gameState.army(side).stack());
            else
                orangeStack().setStack(this.gameState.army(side).stack());
        });
        EventBus.registerHandler("set-game-state", ev_data -> {
            this.gameState = (GameState) ev_data.get("gameState");
            this.stateView.setState(this.gameState.sideOnTurn() == PlayingSide.BLUE
                    ? StateView.State.BLUE_ON_TURN : StateView.State.ORANGE_ON_TURN);
        });
        EventBus.registerGetter("gameState", ev_data -> this.gameState);
        // on pressing escape, fire "unset-all-selected" event
        this.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ESCAPE")) {
                EventBus.fireEvent("unset-all-selected", null);
            }
        });
        started = true;
    }

    static class TroopFieldsView extends VBox {
        public final StackView blueStack;
        public final StackView orangeStack;
        public final BoardView boardView;
        public PositionFactory pf;

        public TroopFieldsView() {
            StackView blueStack = new StackView(PlayingSide.BLUE);
            blueStack.getStyleClass().add("stack");
            StackView orangeStack = new StackView(PlayingSide.ORANGE);
            orangeStack.getStyleClass().add("stack");
            BoardView boardView = new BoardView();
            this.blueStack = blueStack;
            this.orangeStack = orangeStack;
            this.boardView = boardView;
            this.getStyleClass().add("troops-view");
            this.getChildren().add(blueStack);
            this.getChildren().add(boardView);
            this.getChildren().add(orangeStack);
        }
    }

    static class StateView extends VBox {
        static class StateLabelView extends StackPane {
            public Label label = new Label();

            public StateLabelView() {
                label.getStyleClass().add("state-label");
                this.getChildren().add(label);
                this.getStyleClass().add("label-pane");
            }

            public void setText(String text) {
                label.setText(text);
            }
        }
        public StateLabelView stateLabel;
        public State state;

        public StateView() {
            stateLabel = new StateLabelView();
            state = State.BLUE_ON_TURN;
            stateLabel.setText(state.toString());
            this.getChildren().add(stateLabel);
            this.getStyleClass().add("state-view");
        }

        public void setState(State state) {
            this.state = state;
            stateLabel.setText(state.toString());
        }

        public enum State {
            BLUE_ON_TURN, ORANGE_ON_TURN, BLUE_VICTORY, ORANGE_VICTORY;

            public String toString() {
                switch (this) {
                    case BLUE_ON_TURN:
                        return "Blue on turn";
                    case ORANGE_ON_TURN:
                        return "Orange on turn";
                    case BLUE_VICTORY:
                        return "Blue won";
                    default:
                        return "Orange won";
                }
            }
        }
    }
}
