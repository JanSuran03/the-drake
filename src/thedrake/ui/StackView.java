package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import thedrake.PlayingSide;
import thedrake.Troop;

import java.util.List;

public class StackView extends HBox {
    private static final String side = "front";
    List<Troop> stack;

    PlayingSide playingSide;

    public StackView(PlayingSide playingSide, List<Troop> stack) {
        this.stack = stack;
        this.playingSide = playingSide;
    }

    public void update() {
        this.getChildren().clear();
        for (var troop : stack) {
            this.getChildren().add(new ImageView(getClass().getResource("/images/" + side + troop.name()
                    + (playingSide == PlayingSide.BLUE ? 'B' : 'O') + ".png").toExternalForm()));
        }
    }

    static class TroopView {

    }
}
