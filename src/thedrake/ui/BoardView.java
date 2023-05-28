package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardView extends GridPane {
    public static final int GRID_SIZE = 4;

    public BoardView() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if ((row == 1 || row == 2) && row == col)
                    add(createImageView("mountain.png"), row, col);
                else
                    add(createImageView("move.png"), col, row);
            }
        }
    }

    private ImageView createImageView(String name) {
        ImageView imageView = new ImageView(getClass().getResource("/images/" + name).toExternalForm());

        imageView.getStyleClass().add("board-tile-image");
        return imageView;
    }
}
