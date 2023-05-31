package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import thedrake.BoardTile;
import thedrake.GameState;
import thedrake.PositionFactory;

public class BoardView extends GridPane {
    public static final int GRID_SIZE = 4;

    PositionFactory pf;

    TileView[][] tiles = new TileView[GRID_SIZE][GRID_SIZE];

    int[] selected = {0, 0};

    public BoardView() {
        this.getStyleClass().add("game-board");
    }

    public void setPf(PositionFactory pf) {
        this.pf = pf;
    }

    public BoardView setBoard(GameState gameState) {
        this.getChildren().clear();
        EventBus.registerHandler("unset-selected-board", e -> {
            tiles[selected[0]][selected[1]].setBorder(false);
            selected[0] = 0;
            selected[1] = 0;
        });
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                TileView tileView = new TileView();
                tiles[i][j] = tileView;
                tileView.setImage(gameState.board().at(pf.pos(i, j)) == BoardTile.MOUNTAIN
                        ? "mountain.png" : "move.png");
                int finalI = i;
                int finalJ = j;
                tileView.setOnMouseClicked(e -> {
                    EventBus.fireEvent("unset-selected-board", null);
                    EventBus.fireEvent("unset-selected-stack-1", null);
                    EventBus.fireEvent("unset-selected-stack-2", null);
                    tiles[finalI][finalJ].setBorder(true);
                    selected[0] = finalI;
                    selected[1] = finalJ;
                });
                this.add(tileView, i, j);
            }
        return this;
    }

    static class TileView extends StackPane {
        ImageView imageView;

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
