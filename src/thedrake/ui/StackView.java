package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import thedrake.PlayingSide;
import thedrake.Troop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StackView extends HBox {
    private static final String side = "front";
    List<Troop> stack;

    PlayingSide playingSide;

    public StackView(PlayingSide playingSide) {
        this.playingSide = playingSide;
    }

    public void setStack(List<Troop> stack) {
        this.getChildren().clear();
        this.stack = stack;
        EventBus.registerHandler("unset-selected-stack-" + (playingSide == PlayingSide.BLUE ? 1 : 2), e -> {
            for (var child : this.getChildren()) {
                child.getStyleClass().remove("selected");
            }
        });
        for (var troop : stack) {
            TroopView troopView = new TroopView(side + troop.name() + (playingSide == PlayingSide.BLUE ? 'B' : 'O') + ".png");
            troopView.setOnMouseClicked(e -> {
                EventBus.fireEvent("unset-all-selected", null);
                if (troop == stack.get(0)) {
                    if (playingSide == ((GameView) Util.getParentOfClass(this, GameView.class)).gameState.sideOnTurn()) {
                        troopView.setBorder(true);
                        EventBus.fireEvent("show-possible-moves", new HashMap<>(
                                Map.of("side", PlayingSide.BLUE)));
                    }
                }
            });
            this.getChildren().add(troopView);
        }
    }

    public void setHighlighted(boolean isHighlighted) {
        if (isHighlighted)
            this.getStyleClass().add("stack-highlighted");
        else
            this.getStyleClass().remove("stack-highlighted");
    }

    static class TroopView extends StackPane {
        ImageView imageView;

        public TroopView(String name) {
            this.getStyleClass().add("stack-tile");
            imageView = createImageView(name);
            this.getChildren().add(imageView);
        }

        public void setBorder(boolean isBorder) {
            if (isBorder)
                this.getStyleClass().add("selected");
            else
                this.getStyleClass().remove("selected");
        }

        private ImageView createImageView(String name) {
            ImageView imageView = new ImageView(getClass().getResource("/images/" + name).toExternalForm());

            imageView.getStyleClass().add("tile-image");
            return imageView;
        }
    }
}
