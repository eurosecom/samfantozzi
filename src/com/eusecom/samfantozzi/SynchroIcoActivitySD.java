package com.eusecom.samfantozzi;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;


public class SynchroIcoActivitySD extends ListActivity {

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
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
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
        
        this.setTitle(getResources().getString(R.string.synchroIcoSD));

       
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
    
        	
    	
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        //new LoadAllIcosSD().execute();
        new SynchroAllIcosSD().execute();
 
        // Get listview
        //ListView lv = getListView();
        
        registerForContextMenu(getListView());
        
        

        
    }
 //koniec oncreate
    

    //prenos iconew.csv na server
    /**
     * Background Async Task to Load all banks by making HTTP Request
     * */
    class SynchroAllIcosSD extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SynchroIcoActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All banks from url
         * */
        protected String doInBackground(String... args) {
        	
        	String prmall = inputAll.getText().toString();
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputAllUser.getText().toString();
        	
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
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarx + "/iconew" + firmax + ".csv";
            	File myFile = new File(baseDir + File.separator + fileName);

            	if(myFile.exists()){
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                //do ip napocitam kolko riadkov mam
                int ip=0;
            	
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + ";endLinexx;";
                    ip = ip+1;
                
                }

                obsahcsv = aBuffer;
                myReader.close();
                if(myFile.exists()){ myFile.delete(); }

            	}
            	//koniec ak iconew.csv existuje
            			
           	} catch (Exception e) {
               
           	}
        
        		try {
            			
            			HttpParams httpParameters = new BasicHttpParams();

                        HttpClient client = new DefaultHttpClient(httpParameters);
                        client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
                        client.getParams().setParameter("http.socket.timeout", 2000);
                        client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
                        httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
                        HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/synchro_ico.php?sid=5");
                        request.getParams().setParameter("http.socket.timeout", 5000);

                    	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    	postParameters.add(new BasicNameValuePair("prmall", prmall));
                    	postParameters.add(new BasicNameValuePair("obsahcsv", obsahcsv));
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
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                	
                	String urls = "http://www.eshoptest.sk/tmp/ico" + firmax + ".xml";
            	   	String strret = LoadIcoXmlFromWebOperations(urls);
            	   	nacitanexml = (TextView) findViewById(R.id.nacitanexml);
            	    nacitanexml.setText(strret);

                }
            });
 
        }
 
    }
    //koniec prenos iconew.csv na server

 
    //nacitanie ico
    /**
     * Background Async Task to Load all banks by making HTTP Request
     * */
    class LoadAllIcosSD extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SynchroIcoActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All banks from url
         * */
        protected String doInBackground(String... args) {
            
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarx + "/iconew" + firmax + ".csv";
            	File myFile = new File(baseDir + File.separator + fileName);

            	if( pagex.equals("1")) {
            			if(myFile.exists()){
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                //do ip napocitam kolko riadkov mam
                int ip=0;
            	
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                    ip = ip+1;
                
                }

                String kosikx = aBuffer;
                myReader.close();

            	String delims = "[\n]+";
            	String delims2 = "[;]+";
            	
            	String[] riadokxxx = kosikx.split(delims);
            	
                for (int i = 0; i < riadokxxx.length; i++) {
            	String riadok1 =  riadokxxx[i];

            	String[] polozkyx = riadok1.split(delims2);
            	String icox =  polozkyx[0];
            	String naix =  polozkyx[3] + " " + polozkyx[6];

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_PID, icox);
                map.put(TAG_NAME, naix);
                map.put(TAG_PRICE, "!!");

                // adding HashList to ArrayList dam tam len poslednych 10 i >= ip
                productsList.add(map);
                }
                //koniec for
            			}
            			//koniec ak iconew.csv existuje
            	}
            	//koniec ak pagex=1
            	
               
            } catch (Exception e) {
               
            }
            
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            SynchroIcoActivitySD.this, productsList,
                            R.layout.list_item_icosd, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec nacitanie ico
    
	
    public String LoadIcoXmlFromWebOperations(String urls) {
    	
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
		return strret;
    	
    }
    //koniecloadfile
	
  
}
//koniec activity