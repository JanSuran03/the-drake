package thedrake;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thedrake.ui.GameView;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GameView gameView = new GameView(stage);
        stage.setTitle("The Drake");
        Scene scene = new Scene(gameView, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
