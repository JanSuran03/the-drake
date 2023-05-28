package thedrake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thedrake.ui.AppView;
import thedrake.ui.EventBus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("The Drake");
            Parent menu = new FXMLLoader().load(Files.newInputStream(Paths.get("resources/fxml/menu.fxml")));
            Parent game = new FXMLLoader().load(Files.newInputStream(Paths.get("resources/fxml/game.fxml")));
            Scene scene = new Scene(new AppView().setRoot(menu), 800, 600);
            scene.getStylesheets().add(Paths.get("resources/css/the-drake.css").toUri().toString());
            stage.setScene(scene);
            EventBus.registerHandler("quitApplication", e -> stage.close());
            EventBus.registerHandler("startGame", e -> scene.setRoot(new AppView().setRoot(game)));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: " + e.getMessage(), e);
        }
    }
}
