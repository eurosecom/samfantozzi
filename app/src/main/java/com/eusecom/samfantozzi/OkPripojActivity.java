package com.eusecom.samfantozzi;

/**
 * Get message Fantozzi is OK connected...
 * Called from Pripojv2Activity.java
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
 
public class OkPripojActivity extends Activity {

 
    TextView inputPriServer;
    TextView inputPriUser;
    
      
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okpripoj_pinkla);

               
        inputPriServer = (TextView) findViewById(R.id.inputPriServer);
        inputPriServer.setText(SettingsActivity.getServerName(this));
        inputPriUser = (TextView) findViewById(R.id.inputPriUser);
        inputPriUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
        + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this));
       

        //dialog
        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.okpripojpinkla))
        .setMessage(getString(R.string.okpripojpinklames))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with click
            	finish();
            }
         })

         .show();
        
    }
 //koniec oncreate
    
    

}