package com.eusecom.samfantozzi;

/**
 * Menu of accounting reports
 * Called from ZostavyPageFragment.java
 */

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;
 
public class UctoZosActivity extends ListActivity {
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;   
    
    // JSON Node names   
    private static final String TAG_COPERN = "copern";

    String copern;
    String encrypted;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.all_ucty);
        
     // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        copern = extras.getString(TAG_COPERN);
        
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));
        

        String serverx = inputAllServer.getText().toString();
    	String delims = "[/]+";
    	String[] serverxxx = serverx.split(delims);
    	String userx = inputAllUser.getText().toString();
    	
    	String allx = inputAll.getText().toString();
    	String[] allxxx = allx.split(delims);
    	
    	String kli_vume = SettingsActivity.getUme(this);
    	String userxplus = userx + "/" + copern;
    	
    	//String userhash = sha1Hash( userx );
    	MCrypt mcrypt = new MCrypt();
    	/* Encrypt */
    	try {
			encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxplus) );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/* Decrypt */
    	//String decrypted = new String( mcrypt.decrypt( encrypted ) );
        
    	Uri uri = null;
    	
    	//penazny dennik v1
    	if( copern.equals("4")) {
    	uri = Uri.parse("http://" + serverxxx[0] + 
        		"/ucto/penden.php?copern=10&drupoh=1&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume=" + kli_vume + "&serverx=" 
        		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
    	
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    	}
    	//penazny dennik v2
    	if( copern.equals("1")) {
    	uri = Uri.parse("http://" + serverxxx[0] + 
        		"/ucto/penden2013.php?copern=10&drupoh=1&page=1&typ=PDF&zandroidu=1&anduct=1&kli_vume=" + kli_vume + "&serverx=" 
        		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
    	
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    	}
    	//vykaz o prijmoch a vydavkoch
    	if( copern.equals("2")) {
        	uri = Uri.parse("http://" + serverxxx[0] + 
            		"/ucto/vprivyd2013.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume=" + kli_vume + "&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	
        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
    	}
    	//vykaz o majetku a zavazkoch
    	if( copern.equals("3")) {
        	uri = Uri.parse("http://" + serverxxx[0] + 
            		"/ucto/vmajzav2013.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume=" + kli_vume + "&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	
        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
    	}
    	//kniha pohladavok
    	if( copern.equals("5")) {
        	uri = Uri.parse("http://" + serverxxx[0] + 
            		"/ucto/kniha_faktur.php?copern=1&drupoh=1&page=1&zandroidu=1&anduct=1&h_drp=3&kli_vume=" + kli_vume + "&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	
        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
    	}
    	//kniha zavazkov
    	if( copern.equals("6")) {
        	uri = Uri.parse("http://" + serverxxx[0] + 
            		"/ucto/kniha_faktur.php?copern=1&drupoh=1&page=1&zandroidu=1&anduct=1&h_drp=4&kli_vume=" + kli_vume + "&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	
        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
    	}
        
        

    }
 //koniec oncreate
 

  
}