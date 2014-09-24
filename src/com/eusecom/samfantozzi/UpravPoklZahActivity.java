package com.eusecom.samfantozzi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
 
public class UpravPoklZahActivity extends Activity {
 

    EditText inputKto;
    EditText inputTxp;
    EditText inputIco;
    EditText inputIconaz;
    EditText inputDat;
    EditText inputUce;
    Button btnSave;
    Button btnIco;
    Button btnDat;
    TextView inputAll;
    TextView inputEdiServer;
    TextView inputEdiUser;
    TextView inputEdiDruhid;
    TextView inputDph;
    TextView textUce;
    
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
    private static final String TAG_PID = "pid";
    private static final String TAG_DAT = "dat";
    private static final String TAG_KTO = "kto";
    private static final String TAG_TXP = "txp";
    private static final String TAG_DPH = "dph";
    private static final String TAG_ICO = "ico";
    private static final String TAG_ICONAZ = "iconaz";
    private static final String TAG_UCE = "uce";
    private static final String TAG_CAT = "cat";
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_PAGEX = "page";

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
        setContentView(R.layout.uprav_poklzah);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pozx = extras.getString(TAG_POZX);
        fakx = extras.getString(TAG_FAKX);
        newx = extras.getString(TAG_NEWX);
        cat = extras.getString(TAG_CAT);
        
        druhid = SettingsActivity.getDruhId(this);
        
        db=(new DatabaseHelper(this)).getWritableDatabase();
        
        this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx));
        
        textUce = (TextView) findViewById(R.id.textUce);
        if( cat.equals("4")) {
        	textUce.setText(getResources().getString(R.string.popisbtnbanka));
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
                extrasz.putString(TAG_PAGEX, "1");
                //extrasz.putString(TAG_FAKX, "0");
                extrasz.putString(TAG_ODKADE, "1");
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
                extrasz.putString(TAG_ODKADE, "1");
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);

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
        // if result code 201 vyberico
        if (resultCode == 201) {
        	
        	if( SettingsActivity.getCisloico(this).equals("0")) {
            }else{
            	inputIco = (EditText) findViewById(R.id.inputIco);
                inputIco.setText(SettingsActivity.getCisloico(this));	
            }
        	vylistuj();

        }
        // if result code 101 nastavdatum
        if (resultCode == 101) {

        	String datumsetx = data.getStringExtra("datumset"); 

            inputDat = (EditText) findViewById(R.id.inputDat);
            inputDat.setText(datumsetx);

        }
 
    }
    //koniec onactivityresult
    
    private void vylistuj() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      inputIconaz = (EditText) findViewById(R.id.inputIconaz);
          inputIconaz.setText(idtitle); 

	}
	//koniec vylistuj
    
    //toto je hashovacia funkcia odkaz na nu je String userhash = sha1Hash( userx );
    //nede sa to decryptovat na druhej strane, sluzi to na $ret1$info$ret2 toto zahashujem a prenesiem s $info a na druhej strane
    //porovnam znovu vyrobeny hash lebo ret1 a ret2 poznam len ja a porovnam ci mi to po ceste niekto nefakuje
    String sha1Hash( String toHash )
    {
        String hash = null;
        try
        {
            MessageDigest digest = MessageDigest.getInstance( "SHA-1" );
            byte[] bytes = toHash.getBytes("UTF-8");
            digest.update(bytes, 0, bytes.length);
            bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for( byte b : bytes )
            {
                sb.append( String.format("%02X", b) );
            }
            hash = sb.toString();
        }
        catch( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        catch( UnsupportedEncodingException e )
        {
            e.printStackTrace();
        }
        return hash;
    }
 
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
            pDialog2 = new ProgressDialog(UpravPoklZahActivity.this);
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
                    	
                    	String userxfax = userx + "/" + fakx;
                    	
                    	//String userhash = sha1Hash( userx );
                    	MCrypt mcrypt = new MCrypt();
                    	/* Encrypt */
                    	encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxfax) );
                    	/* Decrypt */
                    	//String decrypted = new String( mcrypt.decrypt( encrypted ) );
                    	
                    	List<NameValuePair> params = new ArrayList<NameValuePair>();
                    	params.add(new BasicNameValuePair("prmall", prmall));
                    	//params.add(new BasicNameValuePair("fakx", fakx));
                        params.add(new BasicNameValuePair("serverx", serverx));
                        //params.add(new BasicNameValuePair("userx", userx));
                        //params.add(new BasicNameValuePair("userhash", userhash));
                        params.add(new BasicNameValuePair("userhash", encrypted));
                        params.add(new BasicNameValuePair("newx", newx));
                        params.add(new BasicNameValuePair("cat", cat));
                        
                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                        		"http://" + serverxxx[0] + "/androidfanti/get_poklzah.php", "GET", params);
 
                        // check your log for json response
                        Log.d("JSONObject json from parser", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array
                            
                            // check your log for json response
                            Log.d("JSONArray productObj", productObj.toString());
 
                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);
 
                            // product with this pid found
                            // Edit Text
                            inputUce = (EditText) findViewById(R.id.inputUce);
                            inputDat = (EditText) findViewById(R.id.inputDat);
                            inputKto = (EditText) findViewById(R.id.inputKto);
                            inputTxp = (EditText) findViewById(R.id.inputTxp);
                            inputIco = (EditText) findViewById(R.id.inputIco);
                            inputIconaz = (EditText) findViewById(R.id.inputIconaz);
                            inputDph = (TextView) findViewById(R.id.inputDph);
                            inputUce = (EditText) findViewById(R.id.inputUce);
                            
                            
                           // display product data in EditText
                            inputUce.setText(product.getString(TAG_UCE));
                            inputDat.setText(product.getString(TAG_DAT));
                            inputKto.setText(product.getString(TAG_KTO));
                            inputIco.setText(product.getString(TAG_ICO));
                            inputIconaz.setText(product.getString(TAG_ICONAZ));
                            //inputIconaz.setText("xxxxx");
                            inputTxp.setText(product.getString(TAG_TXP));
                            inputDph.setText(product.getString(TAG_DPH));
                            inputUce.setText(product.getString(TAG_UCE));
                            
                            
                        }else{
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
						// TODO Auto-generated catch block
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
            pDialog = new ProgressDialog(UpravPoklZahActivity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
        	String prmall = inputAll.getText().toString();
        	String dat = inputDat.getText().toString();
            String kto = inputKto.getText().toString();
            String txp = inputTxp.getText().toString();
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
            HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/uloz_poklzah.php?sid=" + String.valueOf(Math.random()));
            request.getParams().setParameter("http.socket.timeout", 5000);

        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	postParameters.add(new BasicNameValuePair("prmall", prmall));
        	postParameters.add(new BasicNameValuePair(TAG_PID, pid));
            postParameters.add(new BasicNameValuePair(TAG_DAT, dat));
            postParameters.add(new BasicNameValuePair(TAG_KTO, kto));
            postParameters.add(new BasicNameValuePair(TAG_TXP, txp));
            postParameters.add(new BasicNameValuePair(TAG_ICO, ico));
        	postParameters.add(new BasicNameValuePair("serverx", serverx));
        	//postParameters.add(new BasicNameValuePair("userx", userx));
        	postParameters.add(new BasicNameValuePair("userhash", encrypted));
        	postParameters.add(new BasicNameValuePair("fakx", fakx));
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