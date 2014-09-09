package com.eusecom.samfantozzi;

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
 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.StrictMode;
import com.eusecom.samfantozzi.MCrypt;
 
public class ZmazPoklZahActivity extends Activity {
 

    EditText inputKto;
    EditText inputTxp;
    EditText inputIco;
    TextView inputIconaz;
    EditText inputDat;
    EditText inputUce;
    Button btnSave;
    Button btnIco;
    TextView inputEdiServer;
    TextView inputEdiUser;
    TextView inputAll;
    TextView inputEdiDruhid;
    TextView inputDph;
    
    String pid;
    BufferedReader in;
    String druhid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
    
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
 
    // JSON Node names
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_CAT = "cat";
 
    String mno;
    String hod;
    String mnozs;
    int mnozi;
    String dcex;
    String pozx;
    String fakx;
    String cat;
    String encrypted;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zmaz_poklzah);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pozx = extras.getString(TAG_POZX);
        fakx = extras.getString(TAG_FAKX);
        cat = extras.getString(TAG_CAT);
        
        druhid = SettingsActivity.getDruhId(this);
        
        this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx));
 
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
        
        //dialog
        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.okzmazdokl))
        .setMessage(getString(R.string.okzmazdoklmes))
        .setPositiveButton(getString(R.string.textyes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with click
            	new ZmazDoklad().execute();
            }
         })
        .setNegativeButton(getString(R.string.textno), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // do nothing
            	finish();
            }
         })
         .show();
        
        
        
    }
    //koniec oncreate
 
    /**
     * Background Async Task to  Save product Details
     * */
    class ZmazDoklad extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ZmazPoklZahActivity.this);
            pDialog.setMessage(getString(R.string.progzmazdokl));
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
            HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/zmaz_poklzah.php?sid=" + String.valueOf(Math.random()));
            request.getParams().setParameter("http.socket.timeout", 5000);

        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	postParameters.add(new BasicNameValuePair("serverx", serverx));
        	//postParameters.add(new BasicNameValuePair("userx", userx));
        	postParameters.add(new BasicNameValuePair("userhash", encrypted));
        	postParameters.add(new BasicNameValuePair("fakx", fakx));
        	postParameters.add(new BasicNameValuePair("prmall", prmall));
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