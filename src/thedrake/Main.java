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
    static FXMLLoader loader = new FXMLLoader();
    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("The Drake");
            Parent menu = loader.load(Files.newInputStream(Paths.get("resources/fxml/menu.fxml")));
            Scene scene = new Scene(new AppView().setRoot(menu), 800, 600);
            scene.getStylesheets().add(Paths.get("resources/css/the-drake.css").toUri().toString());
            stage.setScene(scene);
            EventBus.registerHandler("quitApplication", e -> stage.close());
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: " + e.getMessage(), e);
        }
    }
}
