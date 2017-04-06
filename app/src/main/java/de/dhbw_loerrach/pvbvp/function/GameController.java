package de.dhbw_loerrach.pvbvp.function;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.gui.GameView;

/**
 * Created by Renat on 06.04.2017.
 * Class that manages the game
 */

public class GameController {

    private Main main;
    private World world;
    private GameView view;

    public GameController(Main main, GameView view){
        this.main = main;
        this.view = view;

        world = new World(this);

        //TEST
        world.init();
        view.update(world.playground);
    }

    public void mainLoop(){

        for(;;){

        }
    }
}
