package thedrake.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import thedrake.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends AnchorPane implements Resettable {

    GameState gameState;
    TroopFieldsViewWrapper troopFieldsViewWrapper;
    StateView stateView;

    public boolean started = false;

    private BoardView boardView() {
        return troopFieldsViewWrapper.troopFieldsView().boardView;
    }

    private StackView blueStack() {
        return troopFieldsViewWrapper.troopFieldsView().blueStack;
    }

    private StackView orangeStack() {
        return troopFieldsViewWrapper.troopFieldsView().orangeStack;
    }

    public GameView() {
        TroopFieldsViewWrapper troopFieldsViewWrapper = new TroopFieldsViewWrapper();
        AnchorPane.setLeftAnchor(troopFieldsViewWrapper, 0.0);
        this.stateView = new StateView();
        AnchorPane.setRightAnchor(stateView, 0.0);
        this.troopFieldsViewWrapper = troopFieldsViewWrapper;
        this.getChildren().add(troopFieldsViewWrapper);
        this.getChildren().add(stateView);
        this.getStyleClass().add("game-view");
    }

    @Override
    public void reset() {
        this.started = false;
        this.blueStack().reset();
        this.orangeStack().reset();
        this.boardView().reset();
        this.stateView.reset();
    }

    public void startGame() {
        int dimension = 4;
        Board board = new Board(dimension);
        PositionFactory pf = board.positionFactory();
        EventBus.registerGetter("pf", e -> pf);
        // generate for each row from 2 to dimension - 1, pick a random letter from a
        // to dimension - 1 and generate a mountain there
        for (int i = 2; i < dimension; i++) {
            board = board.withTiles(new Board.TileAt(pf.pos(
                    String.valueOf((char) ('a' + (int) (Math.random() * dimension))) + i),
                    BoardTile.MOUNTAIN));
        }
        this.gameState = new StandardDrakeSetup().startState(board);
        this.boardView().setPf(pf);
        this.boardView().setBoard(gameState);
        this.troopFieldsViewWrapper.troopFieldsView().pf = pf;
        this.blueStack().setStack(this.gameState.army(PlayingSide.BLUE).stack());
        this.orangeStack().setStack(this.gameState.army(PlayingSide.ORANGE).stack());
        EventBus.registerHandler("unset-all-selected", e -> {
            EventBus.fireEvent("unset-selected-board", null);
            EventBus.fireEvent("unset-selected-stack-1", null);
            EventBus.fireEvent("unset-selected-stack-2", null);
            EventBus.fireEvent("set-selected-stack-flag", new HashMap<>(Map.of("selected", false)));
        });

        EventBus.registerHandler("show-possible-moves", ev_data -> {
            if (ev_data.get("side") == this.gameState.sideOnTurn()) {
                if (ev_data.get("pos") == "stack") {
                    for (int i = 0; i < this.gameState.board().dimension(); i++) {
                        for (int j = 0; j < this.gameState.board().dimension(); j++) {
                            BoardPos pos = pf.pos(i, j);
                            if (this.gameState.canPlaceFromStack(pos)) {
                                boardView().tiles[i][j].setCanMoveOn(true);
                            }
                        }
                    }
                } else {
                    BoardPos pos = pf.pos((String) ev_data.get("pos"));
                    for (Move move : this.gameState.tileAt(pos).movesFrom(pos, this.gameState)) {
                        boardView().tiles[move.target().i()][move.target().j()].setCanMoveOn(true);
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
            this.getChildren().add(orangeStack);
            this.getChildren().add(boardView);
            this.getChildren().add(blueStack);
        }
    }

    static class StateView extends VBox implements Resettable {
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
        public Button goToMainMenuButton;
        public Button restartGameButton;

        public CapturedArmyView capturedBlueArmy;
        public CapturedArmyView capturedOrangeArmy;

        @Override
        public void reset() {
            this.setState(State.BLUE_ON_TURN);
            this.capturedBlueArmy.reset();
            this.capturedOrangeArmy.reset();
            EventBus.fireEvent("game-over-overlay", new HashMap<>(Map.of("over", false)));
        }

        public StateView() {
            stateLabel = new StateLabelView();
            state = State.BLUE_ON_TURN;
            stateLabel.setText(state.toString());
            this.getChildren().add(stateLabel);
            this.getStyleClass().add("state-view");
            EventBus.registerHandler("end-game-if-over", e ->
            {
                GameState currentState = (GameState) EventBus.get("gameState", null);
                if (currentState.result() != GameResult.VICTORY) {
                    // can place from stack somewhere
                    for (int i = 0; i < currentState.board().dimension(); i++)
                        for (int j = 0; j < currentState.board().dimension(); j++) {
                            BoardPos pos = ((PositionFactory) EventBus.get("pf", null)).pos(i, j);
                            if (currentState.canPlaceFromStack(pos) || currentState.tileAt(pos).movesFrom(pos, currentState).size() > 0)
                                return;
                        }
                }
                setState(currentState.sideOnTurn() == PlayingSide.BLUE ? State.ORANGE_VICTORY : State.BLUE_VICTORY);
                EventBus.fireEvent("game-over-overlay", new HashMap<>(Map.of("over", true)));
            });

            this.goToMainMenuButton = new Button("Go to main menu");
            this.goToMainMenuButton.setOnMouseClicked(e -> EventBus.fireEvent("go-to-main-menu", null));

            this.restartGameButton = new Button("Restart game");
            this.restartGameButton.setOnMouseClicked(e -> EventBus.fireEvent("restart-game", null));

            this.getChildren().addAll(goToMainMenuButton, restartGameButton);

            this.capturedBlueArmy = new CapturedArmyView(PlayingSide.BLUE);
            this.capturedOrangeArmy = new CapturedArmyView(PlayingSide.ORANGE);
            this.getChildren().addAll(capturedBlueArmy, capturedOrangeArmy);

            EventBus.registerHandler("setCapturedArmy", ev_data -> {
                PlayingSide side = (PlayingSide) ev_data.get("side");
                List<Troop> capturedArmy = ((GameState)(EventBus.get("gameState", null))).army(side).captured();
                if (side == PlayingSide.BLUE)
                    this.capturedBlueArmy.update(capturedArmy);
                else
                    this.capturedOrangeArmy.update(capturedArmy);
            });
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
