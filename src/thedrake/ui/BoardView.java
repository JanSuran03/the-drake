package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import thedrake.*;

import java.util.HashMap;
import java.util.Map;

public class BoardView extends GridPane implements Resettable {
    public int gridSize;

    PositionFactory pf;

    TileView[][] tiles;

    int[] selected = {0, 0};

    private boolean selectedStack = false;

    public BoardView() {
        this.getStyleClass().add("game-board");
    }

    public void setPf(PositionFactory pf) {
        this.pf = pf;
    }

    @Override
    public void reset() {
        this.getChildren().clear();
        this.selectedStack = false;
        this.selected[0] = 0;
        this.selected[1] = 0;
        tiles = null;
    }

    public BoardView setBoard(GameState initialGameState) {
        this.getChildren().clear();
        gridSize = initialGameState.board().dimension();
        tiles = new TileView[gridSize][gridSize];
        EventBus.registerHandler("unset-selected-board", e -> {
            tiles[selected[0]][selected[1]].setBorder(false);
            for (int i = 0; i < gridSize; i++)
                for (int j = 0; j < gridSize; j++)
                    tiles[i][j].setCanMoveOn(false);
            selected[0] = 0;
            selected[1] = 0;
        });
        for (int i = 0; i < gridSize; i++)
            for (int j = 0; j < gridSize; j++) {
                TileView tileView = new TileView();
                tiles[i][j] = tileView;
                if (initialGameState.board().at(pf.pos(i, j)) == BoardTile.MOUNTAIN)
                    tileView.setImage("mountain.png");
                int finalI = i;
                int finalJ = j;
                tileView.setOnMouseClicked(e -> {
                    if (tiles[finalI][finalJ].canMoveOn) { // execute move
                        GameState currentState = (GameState) EventBus.get("gameState", null);
                        PlayingSide side = currentState.sideOnTurn();
                        if (selectedStack) {
                            Troop troop = currentState.armyOnTurn().stack().get(0);
                            GameState newGameState = currentState.placeFromStack(pf.pos(finalI, finalJ));
                            EventBus.fireEvent("set-game-state", new HashMap<>(Map.of("gameState", newGameState)));
                            tiles[finalI][finalJ].setImage(StackView.frontTroopImageName(troop, side));
                            EventBus.fireEvent("set-stack", new HashMap<>(Map.of("side", side)));
                        } else { // can move, but not from stack -> the only other option is from another tile
                            BoardPos origin = pf.pos(selected[0], selected[1]);
                            BoardPos target = pf.pos(finalI, finalJ);
                            for (Move move : currentState.tileAt(origin).movesFrom(origin, currentState)) {
                                if (move.target().equals(target)) {
                                    GameState newGameState = move.execute(currentState);
                                    EventBus.fireEvent("set-game-state", new HashMap<>(Map.of("gameState", newGameState)));
                                    tiles[finalI][finalJ].setImage(Util.flippedImageName(Util.extractImageName(tiles[selected[0]][selected[1]].imageView.getImage().getUrl())));
                                    tiles[selected[0]][selected[1]].removeImage();
                                    break;
                                }
                            }
                        }
                        EventBus.fireEvent("unset-all-selected", null);

                    } else { // select tile, don't execute move
                        EventBus.fireEvent("unset-all-selected", null);
                        tiles[finalI][finalJ].setBorder(true);
                        selected[0] = finalI;
                        selected[1] = finalJ;
                        EventBus.fireEvent("show-possible-moves",
                                new HashMap<>(Map.of("side", ((GameState) EventBus.get("gameState", null)).sideOnTurn(),
                                        "pos", pf.pos(finalI, finalJ).toString())));
                    }
                    EventBus.fireEvent("end-game-if-over", null);
                });
                this.add(tileView, gridSize - 1 - i, gridSize - 1 - j);
            }
        EventBus.registerHandler("set-selected-stack-flag", ev_data -> {
            selectedStack = (boolean) ev_data.get("selected");
        });

        return this;
    }

    static class TileView extends StackPane implements Resettable {
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

        public void removeImage() {
            this.getChildren().clear();
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

        @Override
        public void reset() {
            this.getChildren().clear();
            this.canMoveOn = false;
            this.setBorder(false);
        }
    }
}
