package thedrake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import thedrake.ui.AppView;
import thedrake.ui.EventBus;
import thedrake.ui.GameView;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("The Drake");
            Parent menu = new FXMLLoader().load(getClass().getResource("/fxml/menu.fxml"));
            GameView gameView = new GameView();
            Scene scene = new Scene(new AppView().setRoot(menu), 900, 700);
            scene.getStylesheets().add(getClass().getResource("/css/the-drake.css").toExternalForm());
            stage.setScene(scene);
            EventBus.registerHandler("quitApplication", e -> stage.close());
            EventBus.registerHandler("startGame", e -> scene.setRoot(new AppView().setRoot(gameView)));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: " + e.getMessage(), e);
        }
    }
}
