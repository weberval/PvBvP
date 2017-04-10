package de.dhbw_loerrach.pvbvp.gui;

import de.dhbw_loerrach.pvbvp.function.GameController;

/**
 * Created by Renat on 05.04.2017.
 * This class connects the TouchHandler to the actual game.
 */
public class UserAction {

    private static final String TAG = "UserAction";
    private static GameController gameController;

    public static void init(GameController gameController_){
        gameController = gameController_;
    }

}
