package thedrake.ui;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameView extends StackPane {
    final Stage stage;
    public GameView(Stage stage) {
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.getChildren().add(new MenuView(stage));
        this.stage = stage;
    }
}
