package thedrake.ui.controllers;

import thedrake.ui.EventBus;

public class MenuController {
    public void quit() {
        EventBus.fireEvent("quitApplication", null);
    }

    public void startGame() {
        EventBus.fireEvent("startGame", null);
    }
}
