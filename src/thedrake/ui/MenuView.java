package thedrake.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MenuView extends VBox {
    final Stage stage;

    public MenuView(Stage stage) {
        this.getStyleClass().add("menu-view");
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.getChildren().add(new Button("Play"));
        this.getChildren().add(new Button("Play vs AI"));
        try {
            this.getChildren().add(new FXMLLoader().load(Files.newInputStream(Paths.get("resources/fxml/credits.fxml"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> stage.close());
        this.getChildren().add(quitButton);
        this.stage = stage;
    }
}
