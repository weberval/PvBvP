package de.dhbw_loerrach.pvbvp.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    public static String TEXT = "";

    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.waitscreen);
        Protocol.waitscreen = this;

        message = (TextView) findViewById(R.id.msg_ws);


        message.setText(TEXT);
    }

    public void killme(){
        Intent intent = new Intent(this,Main.class);
        startActivity(intent);
        finish();
    }


    public void clt_connected(){

    }

    public void srv_connected(){

    }

}
