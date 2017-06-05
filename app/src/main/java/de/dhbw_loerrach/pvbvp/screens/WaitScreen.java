package de.dhbw_loerrach.pvbvp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.dhbw_loerrach.pvbvp.Main;
import de.dhbw_loerrach.pvbvp.Network.Protocol;
import de.dhbw_loerrach.pvbvp.R;

/**
 *
 * Transparent overlay over the Main activity
 * shows the users that the application is waiting for  a connection (server / client) or waiting for the START Signal from the server (client)
 * Created by renat on 03.06.17.
 */
public class WaitScreen extends Activity {

    private TextView message;

    public static Screen screen;

    public static String TEXT = "";

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.waitscreen);
        Protocol.waitscreen = this;

        message = (TextView) findViewById(R.id.msg_ws);


        message.setText(TEXT);
    }

    //test how this will work
    public void killme(){
        if(screen != null)
            screen.finish();
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }


    public void clt_connected(){
        message.setText("Connected! Wait until the server starts the game");
    }

    public void srv_connected(){

        message.setText("Connected! Press to start the game");

        Button definitely_starting = new Button(this);
        definitely_starting.setText("Start");
        definitely_starting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Protocol.send_msg(Protocol.SRV_MSG_START,null);
                killme();
            }
        });

        //end the screen activity
        //display button for "start"
        //show message that we have connection and pressing start, will run the game
    }

}
