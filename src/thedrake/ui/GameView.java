package thedrake.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameView extends VBox {
    public GameView() {
        HBox stack1 = new HBox();
        stack1.getChildren().add(new Label("Blue:"));
        stack1.getChildren().add(new Label("Foo"));

        HBox stack2 = new HBox();
        stack2.getChildren().add(new Label("Orange:"));
        stack2.getChildren().add(new Label("Bar"));

        BoardView boardView = new BoardView();
        boardView.getStyleClass().add("game-board");

        getChildren().add(stack1);
        getChildren().add(boardView);
        getChildren().add(stack2);
    }
}
