package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import thedrake.BoardPos;
import thedrake.BoardTile;
import thedrake.GameState;
import thedrake.PositionFactory;

public class BoardView extends GridPane {
    public static final int GRID_SIZE = 4;

    PositionFactory pf;

    public BoardView() {
        this.getStyleClass().add("game-board");
    }

    public void setPf(PositionFactory pf) {
        this.pf = pf;
    }

    public BoardView setBoard(GameState gameState) {
        this.getChildren().clear();
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                // mountain?
                if (gameState.board().at(pf.pos(i, j)) == BoardTile.MOUNTAIN) {
                    this.add(new TileView("mountain.png"), i, j);
                } else {
                    this.add(new TileView("move.png"), i, j);
                }
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

        public TileView() {
            this(null);
        }

        private ImageView createImageView(String name) {
            ImageView imageView = new ImageView(getClass().getResource("/images/" + name).toExternalForm());

            imageView.getStyleClass().add("board-tile-image");
            return imageView;
        }
    }
}
