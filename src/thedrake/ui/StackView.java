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

    public StackView(PlayingSide playingSide) {
        this.playingSide = playingSide;
    }

    public void setStack(List<Troop> stack) {
        this.getChildren().clear();
        this.stack = stack;
        for (var troop : stack) {
            this.getChildren().add(new ImageView(getClass().getResource("/images/" + side + troop.name()
                    + (playingSide == PlayingSide.BLUE ? 'B' : 'O') + ".png").toExternalForm()));
        }
    }

    static class TroopView {

    }
}
