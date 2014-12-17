package com.eusecom.samfantozzi;

//zaciatokprojektu 23.10.2013, na GooglePlay 01.12.2013

import java.io.File;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
// ak jednorazove spustenie import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.MenuInflater;

 
public class MainScreenActivity extends Activity implements
SharedPreferences.OnSharedPreferenceChangeListener{
	
	protected static final int REQUEST_ADD_BOOK = 0;
	
	Button btnPokl;
    Button btnSetPreferences;
    Button btnBanka;
    Button btnMydemo;
    Button btnDod1;
    Button btnOdb1;
    Button btnZostavy;
    Button btnDane;
    Button btnRozne;
    Button btnFirma;
    
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    
    String incomplet;
    
    private static final String TAG_PAGEX = "page";
    private static final String TAG_POKLX = "pokl";
    private static final String TAG_CAT = "cat";
    private static final String TAG_DCEX = "dcex";

    private SQLiteDatabase db2=null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		PreferenceManager.getDefaultSharedPreferences(this)
		.registerOnSharedPreferenceChangeListener(this);
        
        setContentView(R.layout.main_screen);
        
        btnFirma = (Button) findViewById(R.id.btnFirma);
        btnFirma.setText(getResources().getString(R.string.popisbtnfirma) + " " + SettingsActivity.getFir(this) + " " + SettingsActivity.getFirnaz(this));
        
        String serverxx = SettingsActivity.getServerName(this);
        String useridxx = SettingsActivity.getUserId(this);
        int useridxxi = 0;
        try {
        	useridxxi= Integer.parseInt(useridxx);
        } catch(NumberFormatException nfe) {
           System.out.println("Could not parse " + nfe);
        } 
        if(serverxx != null){
        	if(useridxxi < 1002 && useridxxi != 1001){
        	serverxx="www.eshoptest.sk/androiducto";
        	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        	Editor editor = prefs.edit();
        	editor.putString("mydemo", "0").apply();
            editor.putString("nickname", "test2345").apply();
            editor.putString("userpsw", "cp41cs").apply();
         	editor.putString("username", "epxeas").apply();
         	editor.putString("userid", "1001").apply();
         	editor.putString("servername", serverxx).apply();
         	editor.putString("mojmail", "andrejsg4@gmail.com").apply();
         	editor.putString("druhid", "99").apply();
         	editor.putString("pokluce", "21100").apply();
         	editor.putString("pokldok", "1001").apply();
         	editor.putString("pokldov", "2001").apply();
         	editor.putString("fir", "144").apply();
         	editor.putString("firnaz", "DEMO FUCTO 2014").apply();
         	editor.putString("firduct", "9").apply();
         	editor.putString("cisloico", "31213124").apply();
         	editor.putString("firdph", "1").apply();
         	editor.putString("firdph1", "10").apply();
         	editor.putString("firdph2", "20").apply();
         	editor.putString("firrok", "2014").apply();
         	editor.putString("bankuce", "22100").apply();
         	editor.putString("bankdok", "3001").apply();
         	editor.putString("doduce", "32100").apply();
         	editor.putString("doddok", "84001").apply();
         	editor.putString("odbuce", "31100").apply();
         	editor.putString("odbdok", "74001").apply();
        	editor.commit();
        	
        	ContentValues values=new ContentValues(2);

    		values.put("server2", serverxx);
    		values.put("nick2", "test2345");
    		values.put("mail2", "andrejsg4@gmail.com");
    		values.put("uzid2", "1001");
    		values.put("name2", "epxeas");
    		values.put("pswd2", "cp41cs");
    		values.put("uzid2", "1001");
    		
    		String[] argsx={serverxx};
    		db2=(new DatabaseDomeny(this)).getWritableDatabase();
    		db2.delete("mojedomeny", "server2=?", argsx);
    		db2.insert("mojedomeny", "server2", values);
    		db2.close();
    		
            btnFirma = (Button) findViewById(R.id.btnFirma);
            btnFirma.setText(getResources().getString(R.string.popisbtnfirma) + " " + SettingsActivity.getFir(this) + " " + SettingsActivity.getFirnaz(this));

        	}
        }
        
        if( SettingsActivity.getSDkarta(this).equals("1")) {
    	this.setTitle(getResources().getString(R.string.app_namesd));
        ((TextView) findViewById(R.id.inputOrder)).setText(getString(R.string.lokaldata));
        }else
        {
        this.setTitle(getResources().getString(R.string.app_nameweb));        	       	
        ((TextView) findViewById(R.id.inputOrder)).setText(getString(R.string.webdata) + " " + SettingsActivity.getServerName(this));
        }
        
        ImageView onlinex = (ImageView) findViewById(R.id.onlinex);
        onlinex.setOnClickListener(new View.OnClickListener() {
  
            @Override
            public void onClick(View arg0) {
            	
            	websd();
 
            }
        });
        
//ak sdkarta = 1
if( SettingsActivity.getSDkarta(this).equals("1")) {
	
	incomplet = "0";
	String serverx = SettingsActivity.getServerName(this);
	String delims = "[/]+";
	String[] serverxxx = serverx.split(delims);
	String firmax = SettingsActivity.getFir(this);
	
	String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
	String fileName2 = "/eusecom/" + serverxxx[1] + "/ico" + firmax + ".xml";
	File myFile2 = new File(baseDir2 + File.separator + fileName2);
	if (myFile2.exists()) { } else { incomplet = "1"; }
	String fileName4 = "/eusecom/" + serverxxx[1] + "/uctosnova" + firmax + ".xml";
	File myFile4 = new File(baseDir2 + File.separator + fileName4);
	if (myFile4.exists()) { } else { incomplet = "1"; }
	String fileName5 = "/eusecom/" + serverxxx[1] + "/autopohyby" + firmax + ".xml";
	File myFile5 = new File(baseDir2 + File.separator + fileName5);
	if (myFile5.exists()) { } else { incomplet = "1"; }

	
		if( incomplet.equals("1")) {
		new AlertDialog.Builder(this)
        .setTitle(getString(R.string.niejelocaldata))
        .setMessage(getString(R.string.potrebujetelocaldata))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
              
            	//finish();
            }
         })

         .show();
		}


	  ImageView myImgView = (ImageView) findViewById(R.id.casnicka);
	  myImgView.setImageResource(R.drawable.fantozzi3);
	  onlinex.setImageResource(R.drawable.stop);
   
	  
	  // pokl button
      btnPokl = (Button) findViewById(R.id.btnPokl);
      btnPokl.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              // Launching All products Activity
        	  Intent i = new Intent(getApplicationContext(), PokladnicaActivitySD.class);
              Bundle extras = new Bundle();
              extras.putString(TAG_CAT, "1");
              extras.putString(TAG_DCEX, "0");
              extras.putString(TAG_PAGEX, "1");
              i.putExtras(extras);
              startActivity(i);

          }
      });
     
  
      final Builder aaa = new AlertDialog.Builder(this)
      .setTitle(getString(R.string.lokalnyrezim))
      .setMessage(getString(R.string.lenpokladnicu))
      .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) { 
            
          	//finish();
          }
       });

  
      // banka button
      btnBanka = (Button) findViewById(R.id.btnBanka);
      btnBanka.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              aaa.show();
          }
      });
      
      // dodav button
      btnDod1 = (Button) findViewById(R.id.btnDod1);
      btnDod1.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              aaa.show();
          }
      });
      
      // Odber button
      btnOdb1 = (Button) findViewById(R.id.btnOdb1);
      btnOdb1.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              aaa.show();
          }
      });
      
	
	
}else //ak sdkarta=0 tj z webu
{            
        
     //ak je pripojenie do internetu
     if (isOnline()) 
     {
        
    	 // Button firma
         btnFirma = (Button) findViewById(R.id.btnFirma);
         // new obj click event
         btnFirma.setOnClickListener(new View.OnClickListener() {
  
             @Override
             public void onClick(View view) {
                 // Launching All products Activity
                 Intent i = new Intent(getApplicationContext(), VyberFirmuActivity.class);
                 Bundle extras = new Bundle();
                 extras.putString("odkade", "2");
                 extras.putString("pohx", "1");
                 i.putExtras(extras);
                 startActivityForResult(i, 100);
  
             }
         });
    	 
    	 // Buttons
        btnPokl = (Button) findViewById(R.id.btnPokl);
        // new obj click event
        btnPokl.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), PokladnicaActivity.class);
                Bundle extras = new Bundle();
                extras.putString(TAG_CAT, "1");
                extras.putString(TAG_DCEX, "0");
                extras.putString(TAG_PAGEX, "1");
                i.putExtras(extras);
                startActivity(i);
 
            }
        });
        
        btnBanka = (Button) findViewById(R.id.btnBanka);
        // new obj click event
        btnBanka.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {

            	// Launching All products Activity
            	Intent i = new Intent(getApplicationContext(), PokladnicaActivity.class);
                Bundle extras = new Bundle();
                extras.putString(TAG_CAT, "4");
                extras.putString(TAG_DCEX, "0");
                extras.putString(TAG_PAGEX, "1");
                i.putExtras(extras);
                startActivity(i);
                
 
            }
        });
        
     // dodav button
        btnDod1 = (Button) findViewById(R.id.btnDod1);
        btnDod1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            	// Launching All products Activity
            	Intent i = new Intent(getApplicationContext(), PokladnicaActivity.class);
                Bundle extras = new Bundle();
                extras.putString(TAG_CAT, "9");
                extras.putString(TAG_DCEX, "0");
                extras.putString(TAG_PAGEX, "1");
                i.putExtras(extras);
                startActivity(i);
                

            }
        });
        
     // odber button
        btnOdb1 = (Button) findViewById(R.id.btnOdb1);
        btnOdb1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            	// Launching All products Activity
            	Intent i = new Intent(getApplicationContext(), PokladnicaActivity.class);
                Bundle extras = new Bundle();
                extras.putString(TAG_CAT, "8");
                extras.putString(TAG_DCEX, "0");
                extras.putString(TAG_PAGEX, "1");
                i.putExtras(extras);
                startActivity(i);
                

            }
        });

 
        btnZostavy = (Button) findViewById(R.id.btnZostavy);
        // new obj click event
        btnZostavy.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
            	
            	String akedruhid = SettingsActivity.getDruhId(getApplicationContext());
        		if( akedruhid.equals("99")) {
        			Intent i = new Intent(getApplicationContext(), ZostavyActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_PAGEX, "0");
                    extras.putString(TAG_POKLX, "0");
                    i.putExtras(extras);
                    startActivity(i);
        		}
 
            }
        });
        
        btnDane = (Button) findViewById(R.id.btnDane);    
        // new obj click event
        btnDane.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
            	
            	String akedruhid = SettingsActivity.getDruhId(getApplicationContext());
        		if( akedruhid.equals("99")) {
        			Intent i = new Intent(getApplicationContext(), ZostavyActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_PAGEX, "1");
                    extras.putString(TAG_POKLX, "0");
                    i.putExtras(extras);
                    startActivity(i);
        		}
 
            }
        });
        
        btnRozne = (Button) findViewById(R.id.btnRozne);    
        // new obj click event
        btnRozne.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
            	
            	String akedruhid = SettingsActivity.getDruhId(getApplicationContext());
        		if( akedruhid.equals("99")) {
        			Intent i = new Intent(getApplicationContext(), ZostavyActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_PAGEX, "2");
                    extras.putString(TAG_POKLX, "0");
                    i.putExtras(extras);
                    startActivity(i);
        		}
 
            }
        });
       
      }
     //ak nie je pripojenie do internetu
     else{
     	 

         new AlertDialog.Builder(this)
         .setTitle(getString(R.string.niejeinternet))
         .setMessage(getString(R.string.potrebujeteinternet))
         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int which) { 
               
             	//finish();
             }
          })

          .show();
         

      }
      //koniec ak nie je Internet
  
}
//koniec ak sdkarta=0 tj z webu

		btnMydemo = (Button) findViewById(R.id.btnMydemo);    
		btnMydemo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

					Intent i = new Intent(getApplicationContext(), MyDemoActivity.class);
					startActivityForResult(i, 190);

			}
		});
		
		String mydemox = SettingsActivity.getMydemo(this);
		if( mydemox.equals("1")) {

			btnMydemo.setVisibility(View.GONE);
	        }else{
	        	//btnMydemo.setVisibility(View.GONE);	
	        }
     
    }
    //koniec oncreate
    
 // Response from vyberfirmu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        if (resultCode == 190) {

        	Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
     
    
    
// na ondestroy odregistrovanie zmeny preferences a alarm a broadcast
	@Override
	public void onDestroy() {

		super.onDestroy();

		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);
	}
	//koniec ondestroy

	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(SettingsActivity.NICK_NAME) || key.equals(SettingsActivity.SERVER_NAME)) {
			//cinnost ktoru urobi ak sa zmenili preferences ORDE_BY alebo DESC alebo SERVER_NAME
			((TextView) findViewById(R.id.inputOrder)).setText("Pripojenie k " + SettingsActivity.getServerName(this));
		}

		if( SettingsActivity.getSDkarta(this).equals("0")) {
	       	this.setTitle(getResources().getString(R.string.app_nameweb));
	        ((TextView) findViewById(R.id.inputOrder)).setText(getString(R.string.webdata) + " " + SettingsActivity.getServerName(this));
	       }
	       if( SettingsActivity.getSDkarta(this).equals("1")) {
		       	this.setTitle(getResources().getString(R.string.app_namesd));
		        ((TextView) findViewById(R.id.inputOrder)).setText(getString(R.string.lokaldata));
		       }
	                  
	}
    //koniec onSharedPreferenceChanged

	//optionsmenu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
        String akedruhid = SettingsActivity.getDruhId(this);
        String akesdkarta = SettingsActivity.getSDkarta(this);

	if( akesdkarta.equals("0")) {
		if( akedruhid.equals("99")) {
		inflater.inflate(R.menu.options_menu, menu);
		} else
		{
		inflater.inflate(R.menu.options_menumale, menu);
		}
	} else
	{
	inflater.inflate(R.menu.options_menusd, menu);
	}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i=null;
		switch (item.getItemId()) {
		case R.id.cisico:
			if( SettingsActivity.getSDkarta(this).equals("1")) {
			i = new Intent(this, VyberIcoActivitySD.class);
			Bundle extras = new Bundle();
            extras.putString("odkade", "100");
            extras.putString("page", "1");
            i.putExtras(extras);
			startActivity(i);
			}else
			{
				if (isOnline()) 
			     {
					i = new Intent(this, VyberIcoActivity.class);
					Bundle extras = new Bundle();
		            extras.putString("odkade", "100");
		            extras.putString("page", "1");
		            i.putExtras(extras);
					startActivity(i);
			     }else{
			    	 new AlertDialog.Builder(this)
			         .setTitle(getString(R.string.niejeinternet))
			         .setMessage(getString(R.string.potrebujeteinternet))
			         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
			             public void onClick(DialogInterface dialog, int which) { 
			               
			             	//finish();
			             }
			          })

			          .show();
			     }
			}
			
			return true;
		case R.id.preferences:
			i = new Intent(this, SettingsActivity.class);
			startActivity(i);
			return true;
		case R.id.connectserver:
			i = new Intent(this, Pripojv2Activity.class);
			startActivity(i);
			return true;
			
		case R.id.udajefir:
			if( SettingsActivity.getSDkarta(this).equals("1")) {

			}else
			{
				if (isOnline()) 
			     {
					Intent im = new Intent(getApplicationContext(), EditDemoActivity.class);
		            Bundle extrasm = new Bundle();
		            extrasm.putString("icox", "0");
		            extrasm.putString("newx", "0");
		            im.putExtras(extrasm);
		            startActivityForResult(im, 100);
			     }else{
			    	 new AlertDialog.Builder(this)
			         .setTitle(getString(R.string.niejeinternet))
			         .setMessage(getString(R.string.potrebujeteinternet))
			         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
			             public void onClick(DialogInterface dialog, int which) { 
			               
			             	//finish();
			             }
			          })

			          .show();
			     }
			}
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//koniec optionsmenu
	
    //test ci je internet pripojeny
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    //koniec test ci je internet pripojeny

    //prepni web sd
    public boolean websd() {
    
    	ImageView onlinex = (ImageView) findViewById(R.id.onlinex);
    	String onlinexs = SettingsActivity.getSDkarta(this);
    	if( onlinexs.equals("1")) {
		onlinex.setImageResource(R.drawable.go);
		this.setTitle(getResources().getString(R.string.app_nameweb));
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
     	Editor editor = prefs.edit();
     	editor.putString("sdkarta", "0").apply(); 
     	editor.commit();
     	Intent i = new Intent(this, MainScreenActivity.class);
		startActivity(i);
		finish();
     	
    	}else{
		onlinex.setImageResource(R.drawable.stop);
		this.setTitle(getResources().getString(R.string.app_namesd));
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
     	Editor editor = prefs.edit();
     	editor.putString("sdkarta", "1").apply(); 
     	editor.commit();
     	Intent i = new Intent(this, MainScreenActivity.class);
		startActivity(i);
		finish();
		
    	}
    
    return true;
    }
    //koniec prepni web sd

}
//koniec MainScreenActivity