package com.eusecom.samfantozzi;

/**
 * Synchronize account ID number from WEB to SD.
 * Called from options menuu of PokladnicaActivitySD.java
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;


public class SynchroPohActivitySD extends ListActivity {

	Button btnUlozNewObj;
	Button btnhladajico;
	Button btnhladajodbm;
	String cohlada="1";
	
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView nacitanexml;
    
    String idtitle;
    String idvalue;
    String odkade;
    String pagex;
    BufferedReader in;
    String encrypted;
    
    ArrayList<HashMap<String, String>> productsList;
 
    
    // JSON Node names
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_PAGEX = "page";
    
    // XML node names
    static final String NODE_PID = "ico";
    static final String NODE_NAME = "nai";
    static final String NODE_PRICE = "mes";
    
    
    // products JSONArray
    JSONArray products = null;
    
    String incomplet;
    String firmax;
    String adresarx;
    String obsahcsv;

	String prmall, serverx, userx;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.vyber_icosync);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pagex = extras.getString(TAG_PAGEX);
        odkade = extras.getString(TAG_ODKADE);
        //odkade 1=prijmovy, 2=vydvkov, 100=z mainscreen
        
        this.setTitle(getResources().getString(R.string.synchropoh));

       
        firmax=SettingsActivity.getFir(this);
        adresarx=SettingsActivity.getServerName(this);
        String delims = "[/]+";
    	String[] serverxxx = adresarx.split(delims);
    	adresarx=serverxxx[1];
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) + 
                "/Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
   		StrictMode.setThreadPolicy(policy);

		prmall = inputAll.getText().toString();
		serverx = inputAllServer.getText().toString();
		userx = inputAllUser.getText().toString();

        new LoadAllPohSD().execute(prmall, serverx, userx);
 
  
    }
 //koniec oncreate
    

 
    //nacitaj autopohyby144.csv

    class LoadAllPohSD extends AsyncTask<String, String, String> {
    	
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SynchroPohActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 

        protected String doInBackground(String... args) {

			String prmall  = args[0];
			String serverx = args[1];
			String userx  = args[2];

        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	
        	String userxplus = userx;
        	
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
        	
        
        		try {
            			
            			HttpParams httpParameters = new BasicHttpParams();

                        HttpClient client = new DefaultHttpClient(httpParameters);
                        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
                        client.getParams().setParameter("http.socket.timeout", 2000);
                        client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                        httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
                        HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/synchro_poh.php?sid=5");
                        request.getParams().setParameter("http.socket.timeout", 5000);

                    	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    	postParameters.add(new BasicNameValuePair("prmall", prmall));
                    	postParameters.add(new BasicNameValuePair("serverx", serverx));
                    	postParameters.add(new BasicNameValuePair("userhash", encrypted));


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
                     		//Intent i = new Intent(getApplicationContext(), VyberIcoActivitySD.class);
                			//extras.putString("odkade", "100");
                            //extras.putString("page", "1");
                            //i.putExtras(extras);
                			//startActivity(i);
                            //finish();
                        }else {

                        }


        			} catch (ClientProtocolException e) {
        				e.printStackTrace();
        			} catch (IOException e) {
             		   	e.printStackTrace();
             		}
            

            return null;
        }
 

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                	
                	String urls = "http://www.eshoptest.sk/tmp/autopohyby" + firmax + ".xml";
            	   	String strret = LoadPohXmlFromWebOperations(urls);
            	   	nacitanexml = (TextView) findViewById(R.id.nacitanexml);
            	    nacitanexml.setText(strret);
            	   	String urlu = "http://www.eshoptest.sk/tmp/uctosnova" + firmax + ".xml";
            	   	String strreu = LoadUceXmlFromWebOperations(urlu);
            	   	nacitanexml = (TextView) findViewById(R.id.nacitanexml);
            	    nacitanexml.setText(strreu);
            	    finish();
            	    

                }
            });
 
        }
 
    }
    //koniec nacitaj autopohyby.csv
    
	
    public String LoadUceXmlFromWebOperations(String urls) {
    	
    	String strret="";
    	
    	try {
    	    // Create a URL for the desired page
    		
    	    URL url = new URL(urls);

    	    // Read all the text returned by the server
    	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    	    String aBuffer="";
    	    String str="";
    	    
    	    while ((str = in.readLine()) != null) {
    	        // str is one line of text; readLine() strips the newline character(s)
    	    	aBuffer += str + "\n";
    	    }
    	    strret = aBuffer;
    	    in.close();
    	    
    	    
    	} catch (MalformedURLException e) {
    	} catch (IOException e) {
    	}

    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();  
    	String fileName1 = "/eusecom/" + adresarx + "/uctosnova" + firmax + ".xml";
    	File myFile1 = new File(baseDir + File.separator + fileName1);
    	
    	if(myFile1.exists()){ myFile1.delete(); }
    		
			try {
				myFile1.createNewFile();

				FileOutputStream fOut1 = null;
				fOut1 = new FileOutputStream(myFile1, true);
				OutputStreamWriter myOutWriter1 = new OutputStreamWriter(fOut1);
				
					String datatxt1 = strret;


				myOutWriter1.append(datatxt1);
				myOutWriter1.close();
				fOut1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

    	
		return strret;
    	
    }
    //koniecloadfile
    
public String LoadPohXmlFromWebOperations(String urls) {
    	
    	String strret="";
    	
    	try {
    	    // Create a URL for the desired page
    		
    	    URL url = new URL(urls);

    	    // Read all the text returned by the server
    	    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    	    String aBuffer="";
    	    String str="";
    	    
    	    while ((str = in.readLine()) != null) {
    	        // str is one line of text; readLine() strips the newline character(s)
    	    	aBuffer += str + "\n";
    	    }
    	    strret = aBuffer;
    	    in.close();
    	    
    	    
    	} catch (MalformedURLException e) {
    	} catch (IOException e) {
    	}

    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();  
    	String fileName1 = "/eusecom/" + adresarx + "/autopohyby" + firmax + ".xml";
    	File myFile1 = new File(baseDir + File.separator + fileName1);
    	
    	if(myFile1.exists()){ myFile1.delete(); }
    		
			try {
				myFile1.createNewFile();

				FileOutputStream fOut1 = null;
				fOut1 = new FileOutputStream(myFile1, true);
				OutputStreamWriter myOutWriter1 = new OutputStreamWriter(fOut1);
				
					String datatxt1 = strret;


				myOutWriter1.append(datatxt1);
				myOutWriter1.close();
				fOut1.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

    	
		return strret;
    	
    }
    //koniecloadfile
	
  
}
//koniec activity