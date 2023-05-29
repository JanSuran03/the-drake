package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardView extends GridPane {
    public static final int GRID_SIZE = 4;

    public BoardView() {
        this.getStyleClass().add("game-board");
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row == 1 || row == 2) && row == col)
                    add(new TileView("mountain.png"), row, col);
                else
                    add(new TileView(), col, row);
            }
        }
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
