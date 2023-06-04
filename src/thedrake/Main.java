package thedrake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
            AppView appView = new AppView();
            Scene scene = new Scene(appView.setRoot(menu), 1000, 800);
            scene.getStylesheets().add(getClass().getResource("/css/the-drake.css").toExternalForm());
            stage.setScene(scene);
            EventBus.registerHandler("quitApplication", e -> stage.close());
            EventBus.registerHandler("startGame", e -> {
                gameView.startGame();
                appView.setRoot(gameView);
            });

            EventBus.registerHandler("go-to-main-menu", e -> {
                gameView.reset();
                appView.setRoot(menu);
            });

            EventBus.registerHandler("restart-game", e -> {
                gameView.reset();
                gameView.startGame();
            });

            scene.setOnKeyPressed(e -> {
                if (gameView.started && e.getCode().toString().equals("ESCAPE")) {
                    EventBus.fireEvent("unset-all-selected", null);
                }
            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: " + e.getMessage(), e);
        }
    }
}
