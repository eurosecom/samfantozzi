package com.eusecom.samfantozzi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.StrictMode;
import android.preference.PreferenceManager;
 
@SuppressLint("SimpleDateFormat")
public class NewPoklZahActivitySD extends Activity {
 

    EditText inputKto;
    EditText inputTxp;
    EditText inputIco;
    TextView inputIconaz;
    EditText inputDat;
    EditText inputUce;
    EditText inputFak;
    Button btnSave;
    Button btnIco;
    Button btnPohyb;
    Button btnDat;
    TextView inputEdiServer;
    TextView inputEdiUser;
    TextView inputEdiDruhid;
    TextView inputAll;
    TextView inputDph1;
    TextView inputDph2;
    EditText inputPoh;
    
    EditText inputZk2;
    EditText inputDn2;
    EditText inputZk1;
    EditText inputDn1;
    EditText inputZk0;
    EditText inputCelkom;
    
    TextView textzakl2;
    TextView textdph2;
    TextView textzakl1;
    TextView textdph1;
    
    String pid;
    BufferedReader in;
    String druhid;
    String encrypted;
    String ucp;
 
    // Progress Dialog
    private ProgressDialog pDialog;
    
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
 
    // JSON Node names
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_PAGEX = "page";
    private static final String TAG_CAT = "cat";
    
    private static final String TAG_UCTPOHYB = "uctpohyb";
    private static final String TAG_CPOH = "cpoh";
    private static final String TAG_UZK0 = "uzk0";
    private static final String TAG_UZK1 = "uzk1";
    private static final String TAG_UZK2 = "uzk2";
    private static final String TAG_DZK0 = "dzk0";
    private static final String TAG_DZK1 = "dzk1";
    private static final String TAG_DZK2 = "dzk2";

    private static final String TAG_UDN1 = "udn1";
    private static final String TAG_UDN2 = "udn2";
    private static final String TAG_DDN1 = "ddn1";
    private static final String TAG_DDN2 = "ddn2";
    
    private static final String NODE_CUSTOMER = "customer";
    private static final String NODE_ICO = "ico";
    private static final String NODE_NAI = "nai";
    private static final String NODE_MES = "mes";
    
    private static final String NODE_UCTPOHYB = "uctpohyb";
    private static final String NODE_CPOH = "cpoh";
    private static final String NODE_POHP = "pohp";

    private SQLiteDatabase db=null;
    private Cursor constantsCursor=null;
    String idtitle;
    String idvalue;
    
    String mno;
    String hod;
    String mnozs;
    int mnozi;
    String dcex;
    String pozx;
    String fakx;
    String newx;
    String cat;
    String firmax;
    String adresarx;
    String dokladx;
    String icoxy;
    String pohxy;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_poklzah);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pozx = extras.getString(TAG_POZX);
        fakx = extras.getString(TAG_FAKX);
        newx = extras.getString(TAG_NEWX);
        cat = extras.getString(TAG_CAT);
        
        druhid = SettingsActivity.getDruhId(this);
        firmax=SettingsActivity.getFir(this);
        dokladx=SettingsActivity.getPokldok(this);
        if(pozx.equals("2")) { dokladx=SettingsActivity.getPokldov(this); }
        
        if(newx.equals("0")) { dokladx=fakx; }
        
        adresarx=SettingsActivity.getServerName(this);
        String delims = "[/]+";
    	String[] serverxxx = adresarx.split(delims);
    	adresarx=serverxxx[1];
    	ucp=SettingsActivity.getPokluce(this);
    	
    	inputPoh = (EditText) findViewById(R.id.inputPoh);
        inputPoh.setText("0");
        
        db=(new DatabaseHelper(this)).getWritableDatabase();
        
        if(cat.equals("1")) {
        if(pozx.equals("1")) { this.setTitle(SettingsActivity.getPokluce(this) + "/" + dokladx + " " + getResources().getString(R.string.kontnewpri)); }
        if(pozx.equals("2")) { this.setTitle(SettingsActivity.getPokluce(this) + "/" + dokladx + " " + getResources().getString(R.string.kontnewvyd)); }
        inputUce = (EditText) findViewById(R.id.inputUce);
        inputUce.setText(SettingsActivity.getPokluce(this));
        }
        if(newx.equals("0")) {
            if(pozx.equals("1")) { this.setTitle(SettingsActivity.getPokluce(this) + "/" + dokladx + " " 
        + getResources().getString(R.string.popisprijmovy) + " " + getResources().getString(R.string.kontuprzah)); }
            if(pozx.equals("2")) { this.setTitle(SettingsActivity.getPokluce(this) + "/" + dokladx + " " 
        + getResources().getString(R.string.popisvydavkovy) + " " + getResources().getString(R.string.kontuprzah)); }
            }
        

        
        
        
        inputIco = (EditText) findViewById(R.id.inputIco);
        inputIco.setText(SettingsActivity.getCisloico(this));
        
        constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      inputIconaz = (TextView) findViewById(R.id.inputIconaz);
          inputIconaz.setText(idtitle);
          
          textzakl2 = (TextView) findViewById(R.id.textzakl2);
          textzakl2.setText(String.format(getResources().getString(R.string.popzakl2), SettingsActivity.getFirdph2(this)) + "%");
          textdph2 = (TextView) findViewById(R.id.textdph2);
          textdph2.setText(String.format(getResources().getString(R.string.popdph2), SettingsActivity.getFirdph2(this)) + "%");
          textzakl1 = (TextView) findViewById(R.id.textzakl1);
          textzakl1.setText(String.format(getResources().getString(R.string.popzakl1), SettingsActivity.getFirdph1(this)) + "%");
          textdph1 = (TextView) findViewById(R.id.textdph1);
          textdph1.setText(String.format(getResources().getString(R.string.popdph1), SettingsActivity.getFirdph1(this)) + "%");
          
          inputDat = (EditText) findViewById( R.id.inputDat );
          
          Calendar c = Calendar.getInstance();   

          SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
          String formattedDate = df.format(c.getTime());
          inputDat.setText(formattedDate);
          
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputEdiServer = (TextView) findViewById(R.id.inputEdiServer);
        inputEdiServer.setText(SettingsActivity.getServerName(this));
        inputEdiUser = (TextView) findViewById(R.id.inputEdiUser);
        inputEdiUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));     

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
        inputZk0 = (EditText) findViewById( R.id.inputZk0 );
        inputZk1 = (EditText) findViewById( R.id.inputZk1 );
        inputZk2 = (EditText) findViewById( R.id.inputZk2 );
        inputDn1 = (EditText) findViewById( R.id.inputDn1 );
        inputDn2 = (EditText) findViewById( R.id.inputDn2 );
        inputCelkom = (EditText) findViewById( R.id.inputCelkom );
        inputDph1 = (TextView) findViewById( R.id.inputDph1 );
        inputDph2 = (TextView) findViewById( R.id.inputDph2 );
        
        inputDph2.setText(SettingsActivity.getFirdph2(this));
        inputDph1.setText(SettingsActivity.getFirdph1(this));
        
        
        inputZk2.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                	String zk2x = inputZk2.getText().toString();
                	String zk1x = inputZk1.getText().toString();
                	String zk0x = inputZk0.getText().toString();
                	String dn2x = inputDn2.getText().toString();
                	String dn1x = inputDn1.getText().toString();

                	Float zk2xf = 0f;
                	Float zk1xf = 0f;
                	Float zk0xf = 0f;
                	Float dn2xf = 0f;
                	Float dn1xf = 0f;
                	
                	if (inputZk2.getText().toString().trim().length() > 0) { zk2xf = Float.parseFloat(zk2x); }
                	if (inputZk1.getText().toString().trim().length() > 0) { zk1xf = Float.parseFloat(zk1x); }
                	if (inputZk0.getText().toString().trim().length() > 0) { zk0xf = Float.parseFloat(zk0x); }
                	if (inputDn2.getText().toString().trim().length() > 0) { dn2xf = Float.parseFloat(dn2x); }
                	if (inputDn1.getText().toString().trim().length() > 0) { dn1xf = Float.parseFloat(dn1x); }
                	
                	String dph2x = "0." + inputDph2.getText().toString();
                	Float dph2f = Float.parseFloat(dph2x);
                	dn2xf = zk2xf * ( dph2f );
                	
                	DecimalFormat df = new DecimalFormat("0.00");
                	
                	String dn2s = df.format(dn2xf);
                	dn2s = dn2s.replace(',','.');
                	inputDn2.setText(dn2s);
                	
                	Float celkomf = zk2xf + zk1xf + zk0xf + dn2xf + dn1xf;
                	
                	String celkoms = df.format(celkomf);
                	celkoms = celkoms.replace(',','.');
                	inputCelkom.setText(celkoms);

                	
                }
                 
            }
        });
        
        inputDn2.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                	String zk2x = inputZk2.getText().toString();
                	String zk1x = inputZk1.getText().toString();
                	String zk0x = inputZk0.getText().toString();
                	String dn2x = inputDn2.getText().toString();
                	String dn1x = inputDn1.getText().toString();

                	Float zk2xf = 0f;
                	Float zk1xf = 0f;
                	Float zk0xf = 0f;
                	Float dn2xf = 0f;
                	Float dn1xf = 0f;
                	
                	if (inputZk2.getText().toString().trim().length() > 0) { zk2xf = Float.parseFloat(zk2x); }
                	if (inputZk1.getText().toString().trim().length() > 0) { zk1xf = Float.parseFloat(zk1x); }
                	if (inputZk0.getText().toString().trim().length() > 0) { zk0xf = Float.parseFloat(zk0x); }
                	if (inputDn2.getText().toString().trim().length() > 0) { dn2xf = Float.parseFloat(dn2x); }
                	if (inputDn1.getText().toString().trim().length() > 0) { dn1xf = Float.parseFloat(dn1x); }
                	
                	Float celkomf = zk2xf + zk1xf + zk0xf + dn2xf + dn1xf;
                	
                	DecimalFormat df = new DecimalFormat("0.00");
                	
                	String celkoms = df.format(celkomf);
                	celkoms = celkoms.replace(',','.');
                	inputCelkom.setText(celkoms);

                	
                }
                 
            }
        });
        
        inputZk1.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                	String zk2x = inputZk2.getText().toString();
                	String zk1x = inputZk1.getText().toString();
                	String zk0x = inputZk0.getText().toString();
                	String dn2x = inputDn2.getText().toString();
                	String dn1x = inputDn1.getText().toString();

                	Float zk2xf = 0f;
                	Float zk1xf = 0f;
                	Float zk0xf = 0f;
                	Float dn2xf = 0f;
                	Float dn1xf = 0f;
                	
                	if (inputZk2.getText().toString().trim().length() > 0) { zk2xf = Float.parseFloat(zk2x); }
                	if (inputZk1.getText().toString().trim().length() > 0) { zk1xf = Float.parseFloat(zk1x); }
                	if (inputZk0.getText().toString().trim().length() > 0) { zk0xf = Float.parseFloat(zk0x); }
                	if (inputDn2.getText().toString().trim().length() > 0) { dn2xf = Float.parseFloat(dn2x); }
                	if (inputDn1.getText().toString().trim().length() > 0) { dn1xf = Float.parseFloat(dn1x); }
                	
                	String dph1x = "0." + inputDph1.getText().toString();
                	Float dph1f = Float.parseFloat(dph1x);
                	dn1xf = zk1xf * ( dph1f );
                	
                	DecimalFormat df = new DecimalFormat("0.00");
                	
                	String dn1s = df.format(dn1xf);
                	dn1s = dn1s.replace(',','.');
                	inputDn1.setText(dn1s);
                	
                	Float celkomf = zk2xf + zk1xf + zk0xf + dn2xf + dn1xf;
                	
                	String celkoms = df.format(celkomf);
                	celkoms = celkoms.replace(',','.');
                	inputCelkom.setText(celkoms);

                	
                }
                 
            }
        });
        
        inputDn1.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                	String zk2x = inputZk2.getText().toString();
                	String zk1x = inputZk1.getText().toString();
                	String zk0x = inputZk0.getText().toString();
                	String dn2x = inputDn2.getText().toString();
                	String dn1x = inputDn1.getText().toString();

                	Float zk2xf = 0f;
                	Float zk1xf = 0f;
                	Float zk0xf = 0f;
                	Float dn2xf = 0f;
                	Float dn1xf = 0f;
                	
                	if (inputZk2.getText().toString().trim().length() > 0) { zk2xf = Float.parseFloat(zk2x); }
                	if (inputZk1.getText().toString().trim().length() > 0) { zk1xf = Float.parseFloat(zk1x); }
                	if (inputZk0.getText().toString().trim().length() > 0) { zk0xf = Float.parseFloat(zk0x); }
                	if (inputDn2.getText().toString().trim().length() > 0) { dn2xf = Float.parseFloat(dn2x); }
                	if (inputDn1.getText().toString().trim().length() > 0) { dn1xf = Float.parseFloat(dn1x); }
                	
                	Float celkomf = zk2xf + zk1xf + zk0xf + dn2xf + dn1xf;
                	
                	DecimalFormat df = new DecimalFormat("0.00");
                	
                	String celkoms = df.format(celkomf);
                	celkoms = celkoms.replace(',','.');
                	inputCelkom.setText(celkoms);

                	
                }
                 
            }
        });
        
        inputZk0.setOnFocusChangeListener(new OnFocusChangeListener() {          
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                	String zk2x = inputZk2.getText().toString();
                	String zk1x = inputZk1.getText().toString();
                	String zk0x = inputZk0.getText().toString();
                	String dn2x = inputDn2.getText().toString();
                	String dn1x = inputDn1.getText().toString();

                	Float zk2xf = 0f;
                	Float zk1xf = 0f;
                	Float zk0xf = 0f;
                	Float dn2xf = 0f;
                	Float dn1xf = 0f;
                	
                	if (inputZk2.getText().toString().trim().length() > 0) { zk2xf = Float.parseFloat(zk2x); }
                	if (inputZk1.getText().toString().trim().length() > 0) { zk1xf = Float.parseFloat(zk1x); }
                	if (inputZk0.getText().toString().trim().length() > 0) { zk0xf = Float.parseFloat(zk0x); }
                	if (inputDn2.getText().toString().trim().length() > 0) { dn2xf = Float.parseFloat(dn2x); }
                	if (inputDn1.getText().toString().trim().length() > 0) { dn1xf = Float.parseFloat(dn1x); }
                	
                	Float celkomf = zk2xf + zk1xf + zk0xf + dn2xf + dn1xf;
                	
                	DecimalFormat df = new DecimalFormat("0.00");
                	
                	String celkoms = df.format(celkomf);
                	celkoms = celkoms.replace(',','.');
                	inputCelkom.setText(celkoms);

                	
                }
                 
            }
        });
        
        //ico button
        btnIco = (Button) findViewById(R.id.btnIco);
        btnIco.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                
                Intent iz = new Intent(getApplicationContext(), VyberIcoActivitySD.class);
                Bundle extrasz = new Bundle();
                extrasz.putString(TAG_PAGEX, "1");
                extrasz.putString(TAG_ODKADE, "1");
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);

            }
        });
        
      //pohyb button
        btnPohyb = (Button) findViewById(R.id.btnPohyb);
        btnPohyb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                
                Intent iz = new Intent(getApplicationContext(), VyberPohybActivitySD.class);
                Bundle extrasz = new Bundle();
                if(cat.equals("1")) {
                if(pozx.equals("1")) { extrasz.putString(TAG_ODKADE, "1"); }
                if(pozx.equals("2")) { extrasz.putString(TAG_ODKADE, "2"); }
                }
                if(cat.equals("4")) {
                extrasz.putString(TAG_ODKADE, "3"); 
                }
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);

            }
        });
        
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);

        final Builder aaa = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.nopohyb))
        .setMessage(getString(R.string.musitepohyb))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
              
            	//finish();
            }
         });
        
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
            	String inppoh = inputPoh.getText().toString();
            	if(inppoh.equals("0")) {            		
            		aaa.show();	
            	}else{
                new SaveProductDetails().execute();
            	}
            }
        });
        
     // dat button
        btnDat = (Button) findViewById(R.id.btnDat);
        btnDat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
            	
            	inputDat = (EditText) findViewById(R.id.inputDat);
            	String dat = inputDat.getText().toString();
                
                Intent iz = new Intent(getApplicationContext(), NastavDatum.class);
                Bundle extrasz = new Bundle();
                extrasz.putString("datx", dat);
                extrasz.putString(TAG_ODKADE, "2");
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);

            }
        });
        
        
        if(newx.equals("0")) {
        	new GetProductDetails().execute();
        }
        
    }
    //koniec oncreate
    
 // Response from VyberIcoActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        //navrat z vyberico prij aj vyd
        if (resultCode == 201) {vylistuj201();}
        //navrat z vyber pohyb 601prij 602vyd
        if (resultCode == 601) {vylistuj601();}
        if (resultCode == 602) {vylistuj602();}
        if (resultCode == 603) {vylistuj603();}
        // if result code 102 nastavdatum
        if (resultCode == 102) {

        	String datumsetx = data.getStringExtra("datumset"); 

            inputDat = (EditText) findViewById(R.id.inputDat);
            inputDat.setText(datumsetx);

        }
 
    }
    //koniec onactivityresult
    
    private void vylistuj201() {
    	
    	if( SettingsActivity.getCisloico(this).equals("0")) {
        }else{
        	inputIco = (EditText) findViewById(R.id.inputIco);
            inputIco.setText(SettingsActivity.getCisloico(this));	
        }
    	
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      inputIconaz = (TextView) findViewById(R.id.inputIconaz);
          inputIconaz.setText(idtitle); 

	}
	//koniec vylistuj
    
    private void vylistuj601() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titlepoh, valuepoh, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      btnPohyb = (Button) findViewById(R.id.btnPohyb);
	      btnPohyb.setText(idtitle);
	      inputPoh = (EditText) findViewById(R.id.inputPoh);
          inputPoh.setText(idvalue); 

	}
	//koniec vylistuj
    
    private void vylistuj602() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titlepoh, valuepoh, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      btnPohyb = (Button) findViewById(R.id.btnPohyb);
	      btnPohyb.setText(idtitle);
	      inputPoh = (EditText) findViewById(R.id.inputPoh);
          inputPoh.setText(idvalue); 

	}
	//koniec vylistuj
    
    private void vylistuj603() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titlepoh, valuepoh, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      btnPohyb = (Button) findViewById(R.id.btnPohyb);
	      btnPohyb.setText(idtitle);
	      inputPoh = (EditText) findViewById(R.id.inputPoh);
          inputPoh.setText(idvalue); 

	}
	//koniec vylistuj
 
    
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPoklZahActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
        	
        	try {
        	
        	String zmazdoklad = dokladx.trim(); 
        	//najprv zmazat
        	String baseDirz1 = Environment.getExternalStorageDirectory().getAbsolutePath();
        	String fileNamez1 = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";
        	File myFilez1 = new File(baseDirz1 + File.separator + fileNamez1);

            FileInputStream fInz1 = new FileInputStream(myFilez1);
            BufferedReader myReaderz1 = new BufferedReader(
                    new InputStreamReader(fInz1));
            String aDataRowz1 = "";
            String aBufferz1 = "";
            String testBufferz1 = "";
        	
            while ((aDataRowz1 = myReaderz1.readLine()) != null) {

            	testBufferz1 = aDataRowz1 + "\n";
            	
            	String indexxz1 = testBufferz1;
            	String delims2z1 = "[;]+";
            	String[] riadokxxxz1 = indexxz1.split(delims2z1);
            	String cplzmazz1 =  riadokxxxz1[3].trim();

            	if( cplzmazz1.equals(zmazdoklad)) { 
            		
            	
            	}else{
            		aBufferz1 += aDataRowz1 + "\n";
            	}
           

            }

        	inputAll = (TextView) findViewById(R.id.inputAll);                
        	inputAll.setText(aBufferz1);
            myReaderz1.close();
            

            	String baseDirz2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileNamez2 = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";

            	File myFilez2 = new File(baseDirz2 + File.separator + fileNamez2);
            	
                myFilez2.createNewFile();
                FileOutputStream fOutz2 = new FileOutputStream(myFilez2);
                OutputStreamWriter myOutWriterz2 = 
                                        new OutputStreamWriter(fOutz2);
                myOutWriterz2.append(inputAll.getText());
                myOutWriterz2.close();
                fOutz2.close();

                String baseDir3 = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName3 = "/eusecom/" + adresarx + "/poklpol"+ firmax + ".csv";
            	File myFile3 = new File(baseDir3 + File.separator + fileName3);

                FileInputStream fIn3 = new FileInputStream(myFile3);
                BufferedReader myReader3 = new BufferedReader(
                        new InputStreamReader(fIn3));
                String aDataRow3 = "";
                String aBuffer3 = "";
                String testBuffer3 = "";
            	
                while ((aDataRow3 = myReader3.readLine()) != null) {

                	testBuffer3 = aDataRow3 + "\n";
                	
                	String indexx = testBuffer3;
                	String delims2 = "[;]+";
                	String[] riadokxxx = indexx.split(delims2);
                	String cplzmaz =  riadokxxx[1].trim();

                	if( cplzmaz.equals(zmazdoklad)) { 
                	
                	}else{
                		aBuffer3 += aDataRow3 + "\n";
                	}
               

                }

            	inputAll = (TextView) findViewById(R.id.inputAll);                
            	inputAll.setText(aBuffer3);
                myReader3.close();
                

                	String baseDir4 = Environment.getExternalStorageDirectory().getAbsolutePath();
                	String fileName4 = "/eusecom/" + adresarx + "/poklpol"+ firmax + ".csv";

                	File myFile4 = new File(baseDir4 + File.separator + fileName4);
                	
                    myFile4.createNewFile();
                    FileOutputStream fOut4 = new FileOutputStream(myFile4);
                    OutputStreamWriter myOutWriter4 = 
                                            new OutputStreamWriter(fOut4);
                    myOutWriter4.append(inputAll.getText());
                    myOutWriter4.close();
                    fOut4.close();
 
        	//teraz ulozit novy
        	inputZk0 = (EditText) findViewById( R.id.inputZk0 );
            inputZk1 = (EditText) findViewById( R.id.inputZk1 );
            inputZk2 = (EditText) findViewById( R.id.inputZk2 );
            inputDn1 = (EditText) findViewById( R.id.inputDn1 );
            inputDn2 = (EditText) findViewById( R.id.inputDn2 );
            inputCelkom = (EditText) findViewById( R.id.inputCelkom );
            inputPoh = (EditText) findViewById( R.id.inputPoh );
            inputDat = (EditText) findViewById( R.id.inputDat );
            inputFak = (EditText) findViewById( R.id.inputFak );
            inputUce = (EditText) findViewById( R.id.inputUce );
            inputKto = (EditText) findViewById( R.id.inputKto );
            inputTxp = (EditText) findViewById( R.id.inputTxp );
            inputIco = (EditText) findViewById( R.id.inputIco );
            
            // getting updated data from EditTexts
        	String uce = inputUce.getText().toString();
        	String dat = inputDat.getText().toString();
        	String fak = inputFak.getText().toString();
            String kto = inputKto.getText().toString();
            String txp = inputTxp.getText().toString();
            String ico = inputIco.getText().toString();
            String poh = inputPoh.getText().toString();
            String zk0 = inputZk0.getText().toString();
            String zk1 = inputZk1.getText().toString();
            String zk2 = inputZk2.getText().toString();
            String dn1 = inputDn1.getText().toString();
            String dn2 = inputDn2.getText().toString();
            String clk = inputCelkom.getText().toString();
            
            Float zk2xf = 0f;
        	Float zk1xf = 0f;
        	Float zk0xf = 0f;
        	Float dn2xf = 0f;
        	Float dn1xf = 0f;
        	
        	if (inputZk2.getText().toString().trim().length() > 0) { zk2xf = Float.parseFloat(zk2); }
        	if (inputZk1.getText().toString().trim().length() > 0) { zk1xf = Float.parseFloat(zk1); }
        	if (inputZk0.getText().toString().trim().length() > 0) { zk0xf = Float.parseFloat(zk0); }
        	if (inputDn2.getText().toString().trim().length() > 0) { dn2xf = Float.parseFloat(dn2); }
        	if (inputDn1.getText().toString().trim().length() > 0) { dn1xf = Float.parseFloat(dn1); }
            

            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";
            	File myFile = new File(baseDir + File.separator + fileName);

            	//i get random number between 10 and 5000
                Random r = new Random();
                int i1=r.nextInt(15000-5000) + 5000;
                int i2 = i1;
                		
        		if(!myFile.exists()){ myFile.createNewFile();}
        		
                //to true znamena pridat append ked tam nie je prepise
        		FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);                
                
                String datatxt = i2 + " ;" + uce + " ;" + pozx + " ;" + dokladx + " ;" + dat + " ;" + ico + " ;" + fak + " ;" + kto
                		 + " ;" + txp + " ;" + zk0 + " ;" + zk1 + " ;" + zk2 + " ;" + dn1 + " ;" + dn2 + " ;" + poh + " ;" + clk + " \n";
                myOutWriter.append(datatxt);
                myOutWriter.close();
                fOut.close();
                
                //read autopohyby
                XMLDOMParser parser = new XMLDOMParser();
                String fileNamep = "/eusecom/" + adresarx + "/autopohyby"+ firmax + ".xml";
            	File myFilep = new File(baseDir + File.separator + fileNamep);
                
                Document doc = parser.getDocument(new FileInputStream(myFilep));
                
                // Get elements by name employee
                NodeList nodeList = doc.getElementsByTagName(TAG_UCTPOHYB);
                
                String uzk0="";String uzk1="";String uzk2="";
                String dzk0="";String dzk1="";String dzk2="";
                String udn1="";String udn2="";
                String ddn1="";String ddn2="";
                
                // Here, we have only one <employee> element
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element e = (Element) nodeList.item(i);
                    String cpoh = parser.getValue(e, TAG_CPOH);
                    
                    if(cpoh.equals(poh)) {
                    		uzk0 = parser.getValue(e, TAG_UZK0);
                            uzk1 = parser.getValue(e, TAG_UZK1);
                            uzk2 = parser.getValue(e, TAG_UZK2);
                            dzk0 = parser.getValue(e, TAG_DZK0);
                            dzk1 = parser.getValue(e, TAG_DZK1);
                            dzk2 = parser.getValue(e, TAG_DZK2);

                            udn1 = parser.getValue(e, TAG_UDN1);
                            udn2 = parser.getValue(e, TAG_UDN2);
                            ddn1 = parser.getValue(e, TAG_DDN1);
                            ddn2 = parser.getValue(e, TAG_DDN2);
                    }
                    
                }//koniec for
                
                String fileName1 = "/eusecom/" + adresarx + "/poklpol"+ firmax + ".csv";
            	File myFile1 = new File(baseDir + File.separator + fileName1);

            	//i get random number between 10 and 5000
                Random r1 = new Random();
                int i11=r1.nextInt(15000-5000) + 5000;
                		
        		if(!myFile1.exists()){ myFile1.createNewFile();}
        		
                //to true znamena pridat append ked tam nie je prepise
        		FileOutputStream fOut1 = new FileOutputStream(myFile1, true);
                OutputStreamWriter myOutWriter1 = new OutputStreamWriter(fOut1);
                
                String datatxt1="";
                if( zk0xf != 0 ){
                	if(pozx.equals("1")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + ucp + " ;" + uzk0 
                		 + " ;" + dzk0 + " ;" + ico + " ;" + fak + " ;" + zk0 + " \n";
                	}
                	if(pozx.equals("2")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + uzk0 + " ;" + ucp 
                		 + " ;" + dzk0 + " ;" + ico + " ;" + fak + " ;" + zk0 + " \n";
                	}
                myOutWriter1.append(datatxt1);
                }
                datatxt1="";
                i11=r1.nextInt(15000-5000) + 5000;
                if( zk2xf != 0 ){
                	if(pozx.equals("1")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + ucp + " ;" + uzk2 
                		 + " ;" + dzk2 + " ;" + ico + " ;" + fak + " ;" + zk2 + " \n";
                	}
                	if(pozx.equals("2")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + uzk2 + " ;" + ucp 
                		 + " ;" + dzk2 + " ;" + ico + " ;" + fak + " ;" + zk2 + " \n";
                	}
                myOutWriter1.append(datatxt1);
                }
                datatxt1="";
                i11=r1.nextInt(15000-5000) + 5000;
                if( dn2xf != 0 ){
                	if(pozx.equals("1")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + ucp + " ;" + udn2 
                		 + " ;" + ddn2 + " ;" + ico + " ;" + fak + " ;" + dn2 + " \n";
                	}
                	if(pozx.equals("2")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + udn2 + " ;" + ucp 
                		 + " ;" + ddn2 + " ;" + ico + " ;" + fak + " ;" + dn2 + " \n";
                	}
                myOutWriter1.append(datatxt1);
                }
                datatxt1="";
                i11=r1.nextInt(15000-5000) + 5000;
                if( zk1xf != 0 ){
                	if(pozx.equals("1")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + ucp + " ;" + uzk1 
                		 + " ;" + dzk1 + " ;" + ico + " ;" + fak + " ;" + zk1 + " \n";
                	}
                	if(pozx.equals("2")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + uzk1 + " ;" + ucp 
                		 + " ;" + dzk1 + " ;" + ico + " ;" + fak + " ;" + zk1 + " \n";
                	}
                myOutWriter1.append(datatxt1);
                }
                datatxt1="";
                i11=r1.nextInt(15000-5000) + 5000;
                if( dn1xf != 0 ){
                	if(pozx.equals("1")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + ucp + " ;" + udn1 
                		 + " ;" + ddn1 + " ;" + ico + " ;" + fak + " ;" + dn1 + " \n";
                	}
                	if(pozx.equals("2")) {
                datatxt1 = i11 + " ;" + dokladx + " ;" + udn1 + " ;" + ucp 
                		 + " ;" + ddn1 + " ;" + ico + " ;" + fak + " ;" + dn1 + " \n";
                	}
                myOutWriter1.append(datatxt1);
                }
                
                
                
                myOutWriter1.close();
                fOut1.close();
                
                int dokladn = Integer.parseInt(dokladx.trim());
                dokladn = dokladn + 1;
                String doklads = dokladn + "";
                
                //toto uklada preference
             	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
             	Editor editor = prefs.edit();

             	if(pozx.equals("1")) { editor.putString("pokldok", doklads).apply(); }
             	if(pozx.equals("2")) { editor.putString("pokldov", doklads).apply(); }
             	
             	editor.commit();
            	
                // successfully saved
                Intent i = getIntent();
                // send result code 100 to notify about product update
                setResult(100, i);
                finish();
                
                
                //Toast.makeText(getBaseContext(),"Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }

 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
 //koniec save
    

    class GetProductDetails extends AsyncTask<String, String, String> {
 

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewPoklZahActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 

        protected String doInBackground(String... args) {
        	
        	// updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
        	
                String upravdoklad = dokladx.trim();
        	try {
        	
        	inputZk0 = (EditText) findViewById( R.id.inputZk0 );
            inputZk1 = (EditText) findViewById( R.id.inputZk1 );
            inputZk2 = (EditText) findViewById( R.id.inputZk2 );
            inputDn1 = (EditText) findViewById( R.id.inputDn1 );
            inputDn2 = (EditText) findViewById( R.id.inputDn2 );
            inputCelkom = (EditText) findViewById( R.id.inputCelkom );
            inputPoh = (EditText) findViewById( R.id.inputPoh );
            inputDat = (EditText) findViewById( R.id.inputDat );
            inputFak = (EditText) findViewById( R.id.inputFak );
            inputUce = (EditText) findViewById( R.id.inputUce );
            inputKto = (EditText) findViewById( R.id.inputKto );
            inputTxp = (EditText) findViewById( R.id.inputTxp );
            inputIco = (EditText) findViewById( R.id.inputIco );
            inputIconaz = (TextView) findViewById(R.id.inputIconaz);
            btnPohyb = (Button) findViewById(R.id.btnPohyb);

            String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        	String fileName = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";
        	File myFile = new File(baseDir + File.separator + fileName);

            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            String datx="";String fakx="";String ktox="";String txpx=""; icoxy=""; pohxy="";
            String zk0x="";String zk1x="";String zk2x="";String dn1x="";String dn2x="";String clkx="";
            
            //String datatxt = i2 + " ;" + uce + " ;" + pozx + " ;" + dokladx + " ;" + dat + " ;" + ico + " ;" + fak + " ;" + kto
           	//	 + " ;" + txp + " ;" + zk0 + " ;" + zk1 + " ;" + zk2 + " ;" + dn1 + " ;" + dn2 + " ;" + poh + " ;" + clk + " 

        	String delimsx2 = "[;]+";

            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer = aDataRow;
                String[] riadokxxx = aBuffer.split(delimsx2);
                String dokx =  riadokxxx[3].trim();
                if( dokx.equals(upravdoklad)) {
                datx =  riadokxxx[4].trim();
                icoxy =  riadokxxx[5].trim();
                fakx =  riadokxxx[6].trim();
                ktox =  riadokxxx[7].trim();
                txpx =  riadokxxx[8].trim();
                zk0x =  riadokxxx[9].trim();
                zk1x =  riadokxxx[10].trim();
                zk2x =  riadokxxx[11].trim();
                dn1x =  riadokxxx[12].trim();
                dn2x =  riadokxxx[13].trim();
                pohxy =  riadokxxx[14].trim();
                clkx =  riadokxxx[15].trim();
                }
            
            }

            myReader.close();
            inputDat.setText(datx);
            inputIco.setText(icoxy);
            inputFak.setText(fakx);
            inputKto.setText(ktox);
            inputTxp.setText(txpx);
            inputPoh.setText(pohxy);
            inputZk0.setText(zk0x);
            inputZk1.setText(zk1x);
            inputZk2.setText(zk2x);
            inputDn1.setText(dn1x);
            inputDn2.setText(dn2x);
            inputCelkom.setText(clkx);

        	} catch (Exception e) {
            
        	}
        
        	XMLDOMParser parser = new XMLDOMParser();
        	try {
        		
        		String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName2 = "/eusecom/" + adresarx + "/ico"+ firmax + ".xml";
            	File myFile2 = new File(baseDir2 + File.separator + fileName2);
            
            	Document doc = parser.getDocument(new FileInputStream(myFile2));
            
            	// Get elements by name employee
            	NodeList nodeList = doc.getElementsByTagName(NODE_CUSTOMER);
            

            	for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);

                String icoy = parser.getValue(e, NODE_ICO);
                		if( icoy.equals(icoxy)) {
                			String naiy = parser.getValue(e, NODE_NAI);
                			String mesy = parser.getValue(e, NODE_MES);
                			inputIconaz.setText(naiy + "" + mesy);
                		}

            		}
            		//koniec for

            	} catch (Exception e) {

            	}
        	

        	try {
        		
        		String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName2 = "/eusecom/" + adresarx + "/autopohyby"+ firmax + ".xml";
            	File myFile2 = new File(baseDir2 + File.separator + fileName2);
            
            	Document doc = parser.getDocument(new FileInputStream(myFile2));
            
            	// Get elements by name employee
            	NodeList nodeList = doc.getElementsByTagName(NODE_UCTPOHYB);
            

            	for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);

                String cpohy = parser.getValue(e, NODE_CPOH);
                		if( cpohy.equals(pohxy)) {
                			String pohpy = parser.getValue(e, NODE_POHP);
                			btnPohyb.setText(pohpy);

                		}

            		}
            		//koniec for

            	} catch (Exception e) {

            	}

 
                }//koniec run   
            });//koniec runable
            
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
    //koniec get

}