package thedrake.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TroopFieldsViewWrapper extends StackPane {
    private final GameView.TroopFieldsView troopFieldsView;
    private Label gameOverOverlay;

    public TroopFieldsViewWrapper() {
        this.troopFieldsView = new GameView.TroopFieldsView();
        this.gameOverOverlay = new Label("Game Over");
        this.gameOverOverlay.getStyleClass().add("game-over-overlay");
        this.gameOverOverlay.setVisible(false);
        this.getChildren().addAll(this.troopFieldsView, this.gameOverOverlay);
        EventBus.registerHandler("game-over-overlay", ev_data -> {
            System.out.println("Setting game over: " + (boolean) ev_data.get("over"));
            this.gameOverOverlay.setVisible((boolean) ev_data.get("over"));
        });
    }

    public GameView.TroopFieldsView troopFieldsView() {
        return troopFieldsView;
    }
}
