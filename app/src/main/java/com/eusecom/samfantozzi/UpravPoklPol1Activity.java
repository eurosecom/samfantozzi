package com.eusecom.samfantozzi;

/**
 * Edit one row of accounting item at accounting documents.
 * Called from UpravPoklPolActivity.java
 * Call ../androidfanti/get_poklpol1.php and /androidfanti/uloz_poklpol1.php
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.StrictMode;
import com.eusecom.samfantozzi.MCrypt;
 
public class UpravPoklPol1Activity extends Activity {
 

    EditText inputFak;
    EditText inputPop;
    EditText inputIco;
    TextView inputIconaz;
    TextView inputUcmnaz;
    TextView inputUcdnaz;
    TextView inputRdpnaz;
    EditText inputRdp;
    EditText inputHod;
    EditText inputUcm;
    EditText inputUcd;
    Button btnSave;
    Button btnIco;
    Button btnUcm;
    Button btnUcd;
    Button btnRdp;
    TextView inputAll;
    TextView inputEdiServer;
    TextView inputEdiUser;
    TextView inputDrpnaz;
    TextView inputDph;
    
    String pid;
    BufferedReader in;
    String druhid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_RDP = "rdp";
    private static final String TAG_HOD = "hod";
    private static final String TAG_FAK = "fak";
    private static final String TAG_POP = "pop";
    private static final String TAG_ICO = "ico";
    private static final String TAG_ICONAZ = "iconaz";
    private static final String TAG_UCENAZ = "ucenaz";
    private static final String TAG_UCENA2 = "ucena2";
    private static final String TAG_RDPNAZ = "rdpnaz";
    private static final String TAG_UCM = "ucm";
    private static final String TAG_UCD = "ucd";
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_CPLX = "cplx";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_POHX = "pohx";
    private static final String TAG_PAGEX = "page";
    private static final String TAG_CAT = "cat";
    
    private SQLiteDatabase db=null;
    private Cursor constantsCursor=null;
    String idtitle;
    String idvalue;
    String iduce;
    String iducenaz;
    String idrdp;
    String idrdpnaz;

    String mno;
    String hod;
    String mnozs;
    int mnozi;
    String dcex;
    String pozx;
    String fakx;
    String cplx;
    String newx;
    String pohx;
    String ucmclick;
    String ucdclick;
    String cat;
    String encrypted;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uprav_poklpol1);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pozx = extras.getString(TAG_POZX);
        fakx = extras.getString(TAG_FAKX);
        cplx = extras.getString(TAG_CPLX);
        newx = extras.getString(TAG_NEWX);
        pohx = extras.getString(TAG_POHX);
        cat = extras.getString(TAG_CAT);
        
        druhid = SettingsActivity.getDruhId(this);
        
        db=(new DatabaseHelper(this)).getWritableDatabase();
        
        btnUcm = (Button) findViewById(R.id.btnUcm);
        btnUcd = (Button) findViewById(R.id.btnUcd);
        inputUcm = (EditText) findViewById(R.id.inputUcm);
        inputUcd = (EditText) findViewById(R.id.inputUcd);
        
        if( cat.equals("1")) 
        		{
        if( pohx.equals("1")) { 
        	String akypokl=getResources().getString(R.string.popisprijmovy);
            this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
            inputUcm.setEnabled(false);
        	inputUcm.setFocusable(false);
        	inputUcm.setFocusableInTouchMode(false);
        	ucmclick="0"; ucdclick="1";
        }
        if( pohx.equals("2")) { 
        	String akypokl=getResources().getString(R.string.popisvydavkovy);
            this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
            btnUcm.setText(getResources().getString(R.string.popisUcd));
            btnUcd.setText(getResources().getString(R.string.popisUcm));
            inputUcd.setEnabled(false);
        	inputUcd.setFocusable(false);
        	inputUcd.setFocusableInTouchMode(false);
        	ucmclick="1"; ucdclick="0";
        }
        		}
        
        if( cat.equals("4")) 
				{
        		String akypokl=getResources().getString(R.string.popisbankovy);
        		this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
        		btnUcm.setText(getResources().getString(R.string.popisUcmbank));
                btnUcd.setText(getResources().getString(R.string.popisUcdbank));
                ucmclick="1"; ucdclick="1";
				}
        if( cat.equals("8")) 
		{
		String akypokl=getResources().getString(R.string.popisodberatelska);
		this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
		btnUcm.setText(getResources().getString(R.string.popisUcmodb));
        btnUcd.setText(getResources().getString(R.string.popisUcdodb));
        ucmclick="1"; ucdclick="1";
		}
        if( cat.equals("9")) 
		{
		String akypokl=getResources().getString(R.string.popisdodavatelska);
		this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
		btnUcm.setText(getResources().getString(R.string.popisUcmdod));
        btnUcd.setText(getResources().getString(R.string.popisUcddod));
        ucmclick="1"; ucdclick="1";
		}

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
        
        // pokl button
        btnIco = (Button) findViewById(R.id.btnIco);
        btnIco.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent iz = new Intent(getApplicationContext(), VyberIcoActivity.class);
                Bundle extrasz = new Bundle();
                //extrasz.putString(TAG_POZX, "1");
                extrasz.putString(TAG_PAGEX, "1");
                extrasz.putString(TAG_ODKADE, "2");
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);

            }
        });
        
        // ucm button 
        btnUcm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
            	if( ucmclick.equals("1")) {
                Intent iz = new Intent(getApplicationContext(), VyberUceActivity.class);
                Bundle extrasz = new Bundle();
                //extrasz.putString(TAG_POZX, "1");
                //extrasz.putString(TAG_FAKX, "0");
                extrasz.putString(TAG_ODKADE, "2");
                extrasz.putString(TAG_POHX, pohx);
                extrasz.putString(TAG_CAT, cat);
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);
            	}

            }
        });
        
        // ucd button
        btnUcd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
            	if( ucdclick.equals("1")) {
                Intent iz = new Intent(getApplicationContext(), VyberUceActivity.class);
                Bundle extrasz = new Bundle();
                //extrasz.putString(TAG_POZX, "1");
                //extrasz.putString(TAG_FAKX, "0");
                extrasz.putString(TAG_ODKADE, "3");
                extrasz.putString(TAG_POHX, pohx);
                extrasz.putString(TAG_CAT, cat);
                iz.putExtras(extrasz);
                startActivityForResult(iz, 101);
            	}

            }
        });
        
        // rdp button
        btnRdp = (Button) findViewById(R.id.btnRdp);
        btnRdp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                
                Intent iz = new Intent(getApplicationContext(), VyberRdpActivity.class);
                Bundle extrasz = new Bundle();
                //extrasz.putString(TAG_POZX, "1");
                //extrasz.putString(TAG_FAKX, "0");
                extrasz.putString(TAG_ODKADE, "4");
                iz.putExtras(extrasz);
                startActivityForResult(iz, 104);

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
        
        
        dcex = "0";
        pid = "1";		    

        new GetProductDetails().execute();
        
    }
    //koniec oncreate
    
 // Response from VyberIcoActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 202) {
        	
        	if( SettingsActivity.getCisloico(this).equals("0")) {
            }else{
            	inputIco = (EditText) findViewById(R.id.inputIco);
                inputIco.setText(SettingsActivity.getCisloico(this));	
            }
        	vylistuj202();
        }
        if (resultCode == 302) {
        	
        	vylistuj302();
        }
        if (resultCode == 402) {
        	
        	vylistuj402();
        }
        if (resultCode == 502) {
        	
        	vylistuj502();
        }
 
    }
//koniec onactivityresult
    
    private void vylistuj202() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, valueuce, titleuce, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	    	  iduce=constantsCursor.getString(4);
	    	  iducenaz=constantsCursor.getString(5);
	      }
		
	      inputIconaz = (TextView) findViewById(R.id.inputIconaz);
          inputIconaz.setText(idtitle);

	}
	//koniec vylistuj
    
    private void vylistuj302() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, valueuce, titleuce, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	    	  iduce=constantsCursor.getString(4);
	    	  iducenaz=constantsCursor.getString(5);
	      }
		
          inputUcm = (EditText) findViewById(R.id.inputUcm);
          inputUcm.setText(iduce);
          inputUcmnaz = (TextView) findViewById(R.id.inputUcmnaz);
          inputUcmnaz.setText(iducenaz);

	}
	//koniec vylistuj
    
    private void vylistuj402() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, valueuce, titleuce, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	    	  iduce=constantsCursor.getString(4);
	    	  iducenaz=constantsCursor.getString(5);
	      }
		
          inputUcd = (EditText) findViewById(R.id.inputUcd);
          inputUcd.setText(iduce);
          inputUcdnaz = (TextView) findViewById(R.id.inputUcdnaz);
          inputUcdnaz.setText(iducenaz);

	}
	//koniec vylistuj
    
    private void vylistuj502() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, valueuce, titleuce, valuerdp, titlerdp, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	    	  iduce=constantsCursor.getString(4);
	    	  iducenaz=constantsCursor.getString(5);
	    	  idrdp=constantsCursor.getString(6);
	    	  idrdpnaz=constantsCursor.getString(7);
	      }
		
          inputRdp = (EditText) findViewById(R.id.inputRdp);
          inputRdp.setText(idrdp);
          inputRdpnaz = (TextView) findViewById(R.id.inputRdpnaz);
          inputRdpnaz.setText(idrdpnaz);

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
            pDialog2 = new ProgressDialog(UpravPoklPol1Activity.this);
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
                        params.add(new BasicNameValuePair("cplx", cplx));
                        params.add(new BasicNameValuePair("serverx", serverx));
                        //params.add(new BasicNameValuePair("userx", userx));
                        params.add(new BasicNameValuePair("userhash", encrypted));
                        params.add(new BasicNameValuePair("newx", newx));
                        params.add(new BasicNameValuePair("cat", cat));
                        
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                        		"http://" + serverxxx[0] + "/androidfanti/get_poklpol1.php", "GET", params);
 
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
                            inputUcm = (EditText) findViewById(R.id.inputUcm);
                            inputUcd = (EditText) findViewById(R.id.inputUcd);
                            inputRdp = (EditText) findViewById(R.id.inputRdp);
                            inputHod = (EditText) findViewById(R.id.inputHod);
                            inputFak = (EditText) findViewById(R.id.inputFak);
                            inputPop = (EditText) findViewById(R.id.inputPop);
                            inputIco = (EditText) findViewById(R.id.inputIco);
                            inputIconaz = (TextView) findViewById(R.id.inputIconaz);
                            inputUcmnaz = (TextView) findViewById(R.id.inputUcmnaz);
                            inputUcdnaz = (TextView) findViewById(R.id.inputUcdnaz);
                            inputRdpnaz = (TextView) findViewById(R.id.inputRdpnaz);

                            
                            
                           // display product data in EditText
                            inputUcm.setText(product.getString(TAG_UCM));
                            inputUcd.setText(product.getString(TAG_UCD));
                            inputRdp.setText(product.getString(TAG_RDP));
                            inputHod.setText(product.getString(TAG_HOD));
                            inputFak.setText(product.getString(TAG_FAK));
                            inputIco.setText(product.getString(TAG_ICO));
                            inputIconaz.setText(product.getString(TAG_ICONAZ));
                            inputUcmnaz.setText(product.getString(TAG_UCENAZ));
                            inputUcdnaz.setText(product.getString(TAG_UCENA2));
                            inputRdpnaz.setText(product.getString(TAG_RDPNAZ));
                            inputPop.setText(product.getString(TAG_POP));

                            
                       
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
            pDialog = new ProgressDialog(UpravPoklPol1Activity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                              public void run() {
 
        	String prmall = inputAll.getText().toString();
            // getting updated data from EditTexts
        	String ucm = inputUcm.getText().toString();
        	String ucd = inputUcd.getText().toString();
        	String rdp = inputRdp.getText().toString();
            String hod = inputHod.getText().toString();
            String fak = inputFak.getText().toString();
            String pop = inputPop.getText().toString();
            String ico = inputIco.getText().toString();
            
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
            HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/uloz_poklpol1.php?sid=" + String.valueOf(Math.random()));
            request.getParams().setParameter("http.socket.timeout", 5000);

        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	postParameters.add(new BasicNameValuePair("prmall", prmall));
        	postParameters.add(new BasicNameValuePair(TAG_PID, pid));
        	postParameters.add(new BasicNameValuePair(TAG_UCM, ucm));
        	postParameters.add(new BasicNameValuePair(TAG_UCD, ucd));
            postParameters.add(new BasicNameValuePair(TAG_RDP, rdp));
            postParameters.add(new BasicNameValuePair(TAG_HOD, hod));
            postParameters.add(new BasicNameValuePair(TAG_FAK, fak));
            postParameters.add(new BasicNameValuePair(TAG_POP, pop));
            postParameters.add(new BasicNameValuePair(TAG_ICO, ico));
        	postParameters.add(new BasicNameValuePair("serverx", serverx));
        	//postParameters.add(new BasicNameValuePair("userx", userx));
        	postParameters.add(new BasicNameValuePair("userhash", encrypted));
        	postParameters.add(new BasicNameValuePair("fakx", fakx));
        	postParameters.add(new BasicNameValuePair("cplx", cplx));
        	postParameters.add(new BasicNameValuePair("newx", newx));
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
                setResult(101, i);
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

                              }//end run
            });//end runOnUiThread

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