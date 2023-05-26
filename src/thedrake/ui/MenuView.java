package thedrake.ui;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView extends VBox {
    final Stage stage ;
    public MenuView(Stage stage) {
        this.getStyleClass().add("menu-view");
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.getChildren().add(new Button("Play"));
        this.getChildren().add(new Button("Play vs AI"));
        this.getChildren().add(new Button("Play online"));
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> stage.close());
        this.getChildren().add(quitButton);
        this.stage = stage;
    }
}
