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

    /**
     * replaces *B.png with *O.png and vice versa
     */
    static String flippedImageName(String imageName) {
        return imageName.startsWith("front")
                ? imageName.replace("front", "back")
                : imageName.replace("back", "front");
    }

    /**
     * .../the-drake/target/classes/images/frontClubmanB.png -> frontClubmanB
     */
    static String extractImageName(String imagePath) {
        return imagePath.substring(imagePath.lastIndexOf('/') + 1);
    }
}
