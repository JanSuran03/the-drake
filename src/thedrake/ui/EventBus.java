package thedrake.ui;

import javafx.event.Event;

import java.util.HashMap;

public class EventBus {
    public static interface EventHandler {
        void handleEvent(Event event);
    }

    private static final HashMap<String, EventHandler> handlers = new HashMap<>();

    public static void registerHandler(String eventID, EventHandler handler) {
        handlers.put(eventID, handler);
    }

    public static void unregisterHandler(String eventID) {
        handlers.remove(eventID);
    }

    public static void fireEvent(String eventID, Event event) {
        if (handlers.containsKey(eventID)) {
            handlers.get(eventID).handleEvent(event);
        }
    }
}
