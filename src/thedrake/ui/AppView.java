package thedrake.ui;

import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class AppView extends StackPane {
    public AppView() {
        this.getStyleClass().add("app-view");
    }

    public AppView setRoot(Parent root) {
        this.getChildren().clear();
        this.getChildren().add(root);
        return this;
    }
}
