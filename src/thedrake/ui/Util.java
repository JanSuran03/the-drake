package thedrake.ui;

import javafx.scene.Node;

public class Util {
    static Object getParentOfClass(Node node, Class<?> parentClass) {
        if (node == null)
            return null;
        if (parentClass.isInstance(node))
            return node;
        return getParentOfClass(node.getParent(), parentClass);
    }
}
