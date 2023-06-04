package thedrake.ui;

import java.util.HashMap;

public class EventBus {
    public interface EventHandler {
        void handleEvent(HashMap<String, Object> args);
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

    private static final HashMap<String, Getter> getters = new HashMap<>();

    public interface Getter {
        Object get(HashMap<String, Object> args);
    }

    public static void registerGetter(String getterID, Getter getter) {
        getters.put(getterID, getter);
    }

    public static Object get(String getterID, HashMap<String, Object> args) {
        if (getters.containsKey(getterID)) {
            return getters.get(getterID).get(args);
        } else {
            System.err.println("No getter for event " + getterID);
            return null;
        }
    }

    public static void reset() {
        handlers.clear();
        getters.clear();
    }
}
