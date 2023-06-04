package thedrake.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import thedrake.PlayingSide;
import thedrake.Troop;

import java.util.List;

public class CapturedArmyView extends VBox implements Resettable {
    final PlayingSide side;

    static Label styledLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("captured-army-view-label");
        return label;
    }
    public CapturedArmyView(PlayingSide side) {
        this.side = side;
        this.getStyleClass().add("captured-army-view");
        reset();
    }

    public void addSide() {
        Label label = styledLabel("Captured " +side.toString() + ":");
        label.getStyleClass().add("bold");
        this.getChildren().add(label);
    }

    public void update(List<Troop> troops) {
        this.getChildren().clear();
        addSide();
        for (Troop troop : troops) {
            this.getChildren().add(styledLabel(troop.name()));
        }
    }

    @Override
    public void reset() {
        this.getChildren().clear();
        this.addSide();
    }
}
