package thedrake.ui;

import java.util.HashMap;

public class EventBus {
    public interface EventHandler {
        void handleEvent(HashMap<String, Object> event);
    }

    private static final HashMap<String, EventHandler> handlers = new HashMap<>();

    public static void registerHandler(String eventID, EventHandler handler) {
        handlers.put(eventID, handler);
    }

    public static void unregisterHandler(String eventID) {
        handlers.remove(eventID);
    }

    public static void fireEvent(String eventID, HashMap<String, Object> event) {
        if (handlers.containsKey(eventID)) {
            handlers.get(eventID).handleEvent(event);
        } else {
            System.err.println("No handler for event " + eventID);
        }
    }

    public static void reset() {
        handlers.clear();
    }
}
