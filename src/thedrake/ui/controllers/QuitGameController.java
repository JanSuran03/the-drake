package thedrake.ui.controllers;

import thedrake.ui.EventBus;

public class QuitGameController {
    public void quit() {
        EventBus.fireEvent("quitApplication", null);
    }
}
