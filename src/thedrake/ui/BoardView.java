package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import thedrake.*;

import java.util.HashMap;
import java.util.Map;

public class BoardView extends GridPane {
    public static final int GRID_SIZE = 4;

    PositionFactory pf;

    TileView[][] tiles = new TileView[GRID_SIZE][GRID_SIZE];

    int[] selected = {0, 0};

    private boolean selectedStack = false;

    public BoardView() {
        this.getStyleClass().add("game-board");
    }

    public void setPf(PositionFactory pf) {
        this.pf = pf;
    }

    public BoardView setBoard(GameState initialGameState) {
        this.getChildren().clear();
        EventBus.registerHandler("unset-selected-board", e -> {
            tiles[selected[0]][selected[1]].setBorder(false);
            for (int i = 0; i < GRID_SIZE; i++)
                for (int j = 0; j < GRID_SIZE; j++)
                    tiles[i][j].setCanMoveOn(false);
            selected[0] = 0;
            selected[1] = 0;
        });
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                TileView tileView = new TileView();
                tiles[i][j] = tileView;
                if (initialGameState.board().at(pf.pos(i, j)) == BoardTile.MOUNTAIN)
                    tileView.setImage("mountain.png");
                int finalI = i;
                int finalJ = j;
                tileView.setOnMouseClicked(e -> {
                    if (tiles[finalI][finalJ].canMoveOn) {
                        if (selectedStack) {
                            GameState currentState = (GameState) EventBus.get("gameState", null);
                            PlayingSide side = currentState.sideOnTurn();
                            Troop troop = currentState.armyOnTurn().stack().get(0);
                            GameState newGameState = currentState.placeFromStack(pf.pos(finalI, finalJ));
                            EventBus.fireEvent("set-game-state", new HashMap<>(Map.of("gameState", newGameState)));
                            tiles[finalI][finalJ].setImage(StackView.frontTroopImageName(troop, side));
                            EventBus.fireEvent("set-stack", new HashMap<>(Map.of("side", side)));
                            EventBus.fireEvent("unset-all-selected", null);
                        }
                    } else {
                        EventBus.fireEvent("unset-all-selected", null);
                        tiles[finalI][finalJ].setBorder(true);
                        selected[0] = finalI;
                        selected[1] = finalJ;
                    }
                });
                this.add(tileView, i, j);
            }
        EventBus.registerHandler("set-selected-stack-flag", ev_data -> {
            selectedStack = (boolean) ev_data.get("selected");
        });

        return this;
    }

    static class TileView extends StackPane {
        ImageView imageView;

        ImageView moveImageView;

        boolean canMoveOn = false;

        void createMoveImageView() {
            moveImageView = new ImageView("/images/move.png");
            moveImageView.setOpacity(0.5);
        }

        public TileView(String name) {
            this.getStyleClass().add("board-tile");
            if (name == null)
                return;
            imageView = createImageView(name);
            this.getChildren().add(imageView);
        }

        public void setImage(String name) {
            this.getChildren().clear();
            imageView = createImageView(name);
            this.getChildren().add(imageView);
        }

        public void setBorder(boolean isBorder) {
            if (isBorder)
                this.getStyleClass().add("selected");
            else
                this.getStyleClass().remove("selected");
        }

        public void setCanMoveOn(boolean canMoveOn) {
            if (canMoveOn) {
                if (moveImageView == null)
                    createMoveImageView();
                if (!this.getChildren().contains(moveImageView))
                    this.getChildren().add(moveImageView);
                this.canMoveOn = true;
            } else {
                if (moveImageView != null)
                    this.getChildren().remove(moveImageView);
                this.canMoveOn = false;
            }
        }

        public TileView() {
            this(null);
        }

        private ImageView createImageView(String name) {
            ImageView imageView = new ImageView(getClass().getResource("/images/" + name).toExternalForm());

            imageView.getStyleClass().add("tile-image");
            return imageView;
        }
    }
}
