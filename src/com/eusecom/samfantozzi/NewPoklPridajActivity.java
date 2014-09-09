package com.eusecom.samfantozzi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.StrictMode;
import com.eusecom.samfantozzi.MCrypt;
 
public class NewPoklPridajActivity extends Activity {
 

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
 
    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_DAT = "dat";
    private static final String TAG_ICONAZ = "iconaz";
    private static final String TAG_UCE = "uce";
    private static final String TAG_KTO = "kto";
    private static final String TAG_TXP = "txp";
    private static final String TAG_ICO = "ico";
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_PAGEX = "page";
    private static final String TAG_CAT = "cat";

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

        db=(new DatabaseHelper(this)).getWritableDatabase();
        
        if( cat.equals("1")) {
            if( pozx.equals("1")) { 
            	String akypokl=getResources().getString(R.string.popisprijmovy);
                this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
            	}
            if( pozx.equals("2")) { 
            	String akypokl=getResources().getString(R.string.popisvydavkovy);
                this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
            	}
           }
           if( cat.equals("4")) {

               	String akypokl=getResources().getString(R.string.popisbankovy);
                   this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
              }
           if( cat.equals("8")) {

             	String akypokl=getResources().getString(R.string.popisodberatelska);
                 this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
            }
          if( cat.equals("9")) {

            	String akypokl=getResources().getString(R.string.popisdodavatelska);
                this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
           }
           
        

        inputDat = (EditText) findViewById(R.id.inputDat);
        inputKto = (EditText) findViewById(R.id.inputKto);
        inputTxp = (EditText) findViewById(R.id.inputTxp);
        
        inputKto.setEnabled(false);
    	inputKto.setFocusable(false);
    	inputKto.setFocusableInTouchMode(false);
    	inputTxp.setEnabled(false);
    	inputTxp.setFocusable(false);
    	inputTxp.setFocusableInTouchMode(false);
    	inputDat.setEnabled(false);
    	inputDat.setFocusable(false);
    	inputDat.setFocusableInTouchMode(false);
          
          textzakl2 = (TextView) findViewById(R.id.textzakl2);
          textzakl2.setText(String.format(getResources().getString(R.string.popzakl2), SettingsActivity.getFirdph2(this)) + "%");
          textdph2 = (TextView) findViewById(R.id.textdph2);
          textdph2.setText(String.format(getResources().getString(R.string.popdph2), SettingsActivity.getFirdph2(this)) + "%");
          textzakl1 = (TextView) findViewById(R.id.textzakl1);
          textzakl1.setText(String.format(getResources().getString(R.string.popzakl1), SettingsActivity.getFirdph1(this)) + "%");
          textdph1 = (TextView) findViewById(R.id.textdph1);
          textdph1.setText(String.format(getResources().getString(R.string.popdph1), SettingsActivity.getFirdph1(this)) + "%");
          
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
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
                
                Intent iz = new Intent(getApplicationContext(), VyberIcoActivity.class);
                Bundle extrasz = new Bundle();
                //extrasz.putString(TAG_POZX, "1");
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
                
                Intent iz = new Intent(getApplicationContext(), VyberPohybActivity.class);
                Bundle extrasz = new Bundle();
                if(cat.equals("1")) {
                if(pozx.equals("1")) { extrasz.putString(TAG_ODKADE, "1"); }
                if(pozx.equals("2")) { extrasz.putString(TAG_ODKADE, "2"); }
                }
                if(cat.equals("4")) { extrasz.putString(TAG_ODKADE, "3"); }
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);

            }
        });
        
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveProductDetails().execute();
            }
        });
        
        new GetProductDetails().execute();
        
        
    }
    //koniec oncreate
    
 // Response from VyberIcoActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        //navrat z vyberico prij aj vyd
        if (resultCode == 201) {vylistuj201();}
        //navrat z vyber pohyb 601prij 602vyd, 603banka
        if (resultCode == 601) {vylistuj601();}
        if (resultCode == 602) {vylistuj602();}
        if (resultCode == 603) {vylistuj603();}
 
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
     * Background Async Task to Get complete product details
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(NewPoklPridajActivity.this);
            pDialog2.setMessage(getString(R.string.progdata));
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

        	
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                       
                    	String prmall = inputAll.getText().toString();
                    	String serverx = inputEdiServer.getText().toString();
                    	String delims = "[/]+";
                    	String[] serverxxx = serverx.split(delims);
                    	String userx = inputEdiUser.getText().toString();
                    	
                    	String userxplus = userx + "/" + fakx;
                    	
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
                    	
                    	List<NameValuePair> params = new ArrayList<NameValuePair>();
                    	params.add(new BasicNameValuePair("prmall", prmall));
                    	params.add(new BasicNameValuePair("fakx", fakx));
                        params.add(new BasicNameValuePair("serverx", serverx));
                        //params.add(new BasicNameValuePair("userx", userx));
                        params.add(new BasicNameValuePair("userhash", encrypted));
                        params.add(new BasicNameValuePair("newx", newx));
                        params.add(new BasicNameValuePair("cat", cat));
                        
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                        		"http://" + serverxxx[0] + "/androidfanti/get_poklzah.php", "GET", params);
 
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            // product with this pid found
                            // Edit Text
                            inputUce = (EditText) findViewById(R.id.inputUce);
                            inputDat = (EditText) findViewById(R.id.inputDat);
                            inputKto = (EditText) findViewById(R.id.inputKto);
                            inputTxp = (EditText) findViewById(R.id.inputTxp);
                            inputIco = (EditText) findViewById(R.id.inputIco);
                            inputIconaz = (TextView) findViewById(R.id.inputIconaz);
                            
                            
                            // display product data in EditText
                            inputUce.setText(product.getString(TAG_UCE));
                            inputDat.setText(product.getString(TAG_DAT));
                            inputKto.setText(product.getString(TAG_KTO));
                            inputIco.setText(product.getString(TAG_ICO));
                            inputIconaz.setText(product.getString(TAG_ICONAZ));
                            inputTxp.setText(product.getString(TAG_TXP));
                            
                            
                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog2.dismiss();
        }
    }
 
    
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
            pDialog = new ProgressDialog(NewPoklPridajActivity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
        	
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
            
            String prmall = inputAll.getText().toString();
        	String serverx = inputEdiServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputEdiUser.getText().toString();
        	
        	String userxplus = userx + "/" + fakx;
        	
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
        	
            HttpParams httpParameters = new BasicHttpParams();

            HttpClient client = new DefaultHttpClient(httpParameters);
            client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
            client.getParams().setParameter("http.socket.timeout", 2000);
            client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
            HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/uloz_newpoklzah.php?sid=" + String.valueOf(Math.random()));
            request.getParams().setParameter("http.socket.timeout", 5000);

        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	postParameters.add(new BasicNameValuePair("prmall", prmall));
        	postParameters.add(new BasicNameValuePair("uce", uce));
            postParameters.add(new BasicNameValuePair(TAG_DAT, dat));
            postParameters.add(new BasicNameValuePair(TAG_KTO, kto));
            postParameters.add(new BasicNameValuePair(TAG_TXP, txp));
            postParameters.add(new BasicNameValuePair(TAG_ICO, ico));
            postParameters.add(new BasicNameValuePair("poh", poh));
            postParameters.add(new BasicNameValuePair("fak", fak));
            postParameters.add(new BasicNameValuePair("zk0", zk0));
            postParameters.add(new BasicNameValuePair("zk1", zk1));
            postParameters.add(new BasicNameValuePair("zk2", zk2));
            postParameters.add(new BasicNameValuePair("dn1", dn1));
            postParameters.add(new BasicNameValuePair("dn2", dn2));
            postParameters.add(new BasicNameValuePair("celkom", clk));
            
        	postParameters.add(new BasicNameValuePair("serverx", serverx));
        	//postParameters.add(new BasicNameValuePair("userx", userx));
        	postParameters.add(new BasicNameValuePair("userhash", encrypted));
        	postParameters.add(new BasicNameValuePair("fakx", fakx));
        	postParameters.add(new BasicNameValuePair("newx", newx));
        	postParameters.add(new BasicNameValuePair("pozx", pozx));
        	postParameters.add(new BasicNameValuePair("cat", cat));
            
            try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, HTTP.UTF_8);
            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            //String lineSeparator = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line);
                //sb.append(lineSeparator);
            }
            in.close();
            String result = sb.toString();
  
            String delimso = "[;]+";
         	String[] resultxxx = result.split(delimso);

             if( resultxxx[0].equals("1")) {

            	
            	// successfully updated
                Intent i = getIntent();
                // send result code 100 to notify about product update
                setResult(100, i);
                finish();
            }else {

            }
            
                 } catch (ClientProtocolException e) {
            	   // TODO Auto-generated catch block
            	   e.printStackTrace();
            	   //Toast.makeText(EditProductv2Activity.this, e.toString(), Toast.LENGTH_LONG).show();
            	  } catch (IOException e) {
            		   // TODO Auto-generated catch block
            		   e.printStackTrace();
            		   //Toast.makeText(EditProductv2Activity.this, e.toString(), Toast.LENGTH_LONG).show();
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

}