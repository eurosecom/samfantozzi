package com.eusecom.samfantozzi;

/**
 * To lock user of MySql database.
 * Called from ZostavyPageFragment.java
 * Call ../androidfanti/zamkni.php at WEB server
 */
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

 
public class ZamkniActivity extends ListActivity{
	
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView obsahKosika;
    TextView aktualplu;
    Button btnUce;
    Button btnNext;
    Button btnPDF;
    
    EditText txtTitle;
    EditText txtValue;
    TextView hladaj1;
    TextView hladaj2;
    TextView hladaj3;
    TextView vybraneico;
    TextView kli_uzidx;
    
    ArrayList<HashMap<String, String>> productsList;
 
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_POH = "poh";
    private static final String TAG_DAT = "dat";
    private static final String TAG_CAT = "cat";
    private static final String TAG_MINPLU = "minplu";
    private static final String TAG_KLIUZID = "kliuzid";
    private static final String TAG_DCEX = "dcex";
    
    private static final String TAG_PAGEX = "page";
    private static final String TAG_ICOX = "icox";

    
    // products JSONArray
    JSONArray products = null;
    String cat;
    String dcex;
    String pagex;
    String ucex;
    String umex;
	String encrypted;
	String icox;
	String zamknute="0";
	String device_id;
	String gpslat;
	String gpslon;

    String prmall, serverx, userx;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.zamkni);
         
 
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        cat = extras.getString(TAG_CAT);
        dcex = extras.getString(TAG_DCEX);
        pagex = extras.getString(TAG_PAGEX);
        icox = extras.getString(TAG_ICOX);
        
        obsahKosika = (TextView) findViewById(R.id.obsahKosika);
        btnPDF = (Button) findViewById(R.id.btnPDF);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnUce = (Button) findViewById(R.id.btnUce);
        
        if( cat.equals("8")) {
            ucex = SettingsActivity.getOdbuce(this);
            umex = SettingsActivity.getUme(this);
            obsahKosika.setVisibility(View.GONE);
            btnUce.setText(SettingsActivity.getUme(this));
            //this.setTitle(getResources().getString(R.string.popisbtndph) + " " + umex);
        }
     
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        
        GPSTracker mGPS = new GPSTracker(this);
        gpslat="Lat0"; gpslon="Lon0";
        
        if(mGPS.canGetLocation ){
        mGPS.getLocation();
        gpslat="Lat"+mGPS.getLatitude();
        gpslon="Lon"+mGPS.getLongitude();
        }
        
        kli_uzidx = (TextView) findViewById(R.id.kli_uzidx);
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this) 
        		+ "/Klivume/" + SettingsActivity.getUme(this) + "/fir_uctx01/" + SettingsActivity.getFirdph(this)
        		+ "/kliv_duj/" + SettingsActivity.getFirduct(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/" + device_id + "/" + gpslat + "/" + gpslon);
 
        obsahKosika.setText(" " + "0" + " " + getText(R.string.mnozstvo) 
        		+ "  " + getText(R.string.hodnota) + " = " +  "0" + "  " + getText(R.string.mena));
        
      //ak uzivatel 0 nepripojeny
        if( SettingsActivity.getUserId(this).equals("0") || SettingsActivity.getDruhId(this).equals("0") ) {
        //dialog
        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.oknepripojeny))
        .setMessage(getString(R.string.oknepripojenymes))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with click
            	finish();
            }
        })

        .show();
        } else
        //koniec uzivatel 0 nepripojeny
        //ak je pripojeny
        {

            prmall = inputAll.getText().toString();
            serverx = inputAllServer.getText().toString();
            userx = inputAllUser.getText().toString();

       btnNext = (Button) findViewById(R.id.btnNext);     
       // next button click event
       btnNext.setOnClickListener(new View.OnClickListener() {
 
           @Override
           public void onClick(View arg0) {
 
        	   Intent iz = new Intent(getApplicationContext(), ZamkniActivity.class);
               Bundle extramd = new Bundle();
               extramd.putString("cat", "8");
               if( zamknute.equals("0")) {extramd.putString("dcex", "2"); }
               if( zamknute.equals("1")) {extramd.putString("dcex", "3"); }
               extramd.putString("page", "1");
               extramd.putString("icox", "0");
               iz.putExtras(extramd);
               startActivity(iz);
               finish();
           }
       });

        
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 

       	if( dcex.equals("1")) { 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
       	}
       	if( dcex.equals("2")) {
       		
       		//dialog
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.okzamkni))
            .setMessage(getString(R.string.okzamknimes))
            .setPositiveButton(getString(R.string.textyes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    // continue with click
                	new LoadAllProducts().execute();
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
       	if( dcex.equals("3")) {
       		
       		//dialog
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.okodomkni))
            .setMessage(getString(R.string.okodomknimes))
            .setPositiveButton(getString(R.string.textyes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                    // continue with click
                	new LoadAllProducts().execute();
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
        
 
        // Get listview
        ListView lv = getListView();
        
        registerForContextMenu(getListView());
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
               
            }
        });
        
        //ak je pripojeny
        }
 
    }
    //koniec oncreate
    

 

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ZamkniActivity.this);
            if( cat.equals("8")) {pDialog.setMessage(getString(R.string.progdata)); }
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	
        	Random r = new Random();
        	int i1=r.nextInt(989123-289123) + 289123;
        	String i1s= i1 + "";
        	
        	String userxplus = userx + "/" + i1s;
        	
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
        	
        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            params.add(new BasicNameValuePair("userhash", encrypted));
            params.add(new BasicNameValuePair("copern", dcex));
            params.add(new BasicNameValuePair("datop", i1s));
            
            // getting JSON string from URL
            JSONObject json = null;
            json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/zamkni.php", "GET", params);      	
            
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                //String minplu = json.getString(TAG_MINPLU);
                //aktualplu = (TextView) findViewById(R.id.aktualplu);
                //aktualplu.setText(minplu);
 
                if (success == 1) {
                	
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
 
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String price = c.getString(TAG_PRICE);
                        String minplu = c.getString(TAG_MINPLU);
                        String pohx = c.getString(TAG_POH);
                        String naix = c.getString(TAG_DAT);
                        String kliuzidx = c.getString(TAG_KLIUZID);
                        
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_MINPLU, minplu);
                        map.put(TAG_KLIUZID, kliuzidx);
                        map.put(TAG_POH, pohx);
                        map.put(TAG_DAT, naix);
                        
                        // adding HashList to ArrayList
                        productsList.add(map);
                        
                    }
                    
                } else {
                    // no products found
                    // Launch Add New product Activity
                    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                    // Closing all previous activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
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
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            ZamkniActivity.this, productsList,
                            R.layout.list_zamkni, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_POH},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.poh });
                    // updating listview
                    setListAdapter(adapter);
                    
                    String zostatok = productsList.get(0).get(TAG_MINPLU);
                    zamknute=zostatok;

                    btnNext = (Button) findViewById(R.id.btnNext);
                    
                    if( zostatok.equals("0")) {
                        btnNext.setText(getString(R.string.popiszamknut));
                    	vybraneico = (TextView) findViewById(R.id.vybraneico);
                    	vybraneico.setVisibility(View.GONE);
                    	ImageView myImgView = (ImageView) findViewById(R.id.casnicka);
                  	  	myImgView.setImageResource(R.drawable.go);
                        }else{
                        	btnNext.setText(getString(R.string.popisodomknut));
                        	vybraneico = (TextView) findViewById(R.id.vybraneico);
                        	vybraneico.setVisibility(View.GONE);
                        	ImageView myImgView = (ImageView) findViewById(R.id.casnicka);
                      	  	myImgView.setImageResource(R.drawable.stop);
                            }
                    
                }
            });
 
        }
 
    }
    //koniec loadall
    
    
    
}