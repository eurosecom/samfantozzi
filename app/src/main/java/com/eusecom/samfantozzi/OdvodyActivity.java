package com.eusecom.samfantozzi;

/**
 * Get pay for health and social insurance for next year....
 * Called from ZostavyPageFragment.java
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.MCrypt;

import com.eusecom.samfantozzi.SimpleGestureFilter.SimpleGestureListener;
 
public class OdvodyActivity extends ListActivity implements SimpleGestureListener{
	
	private SimpleGestureFilter detector;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView aktualplu;

    
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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.odvody);
         
        detector = new SimpleGestureFilter(this,this);
 
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        cat = extras.getString(TAG_CAT);
        dcex = extras.getString(TAG_DCEX);
        pagex = extras.getString(TAG_PAGEX);
        icox = extras.getString(TAG_ICOX);
        
        
        if( cat.equals("8")) {
            ucex = SettingsActivity.getOdbuce(this);
            umex = SettingsActivity.getUme(this);
            this.setTitle(getResources().getString(R.string.popisbtnplatbystatu));
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
                + "/Doklad/" + SettingsActivity.getDoklad(this) + "/Kateg/" + cat);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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


        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
        
 
        // Get listview
        ListView lv = getListView();
        
        registerForContextMenu(getListView());
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                //String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
 
                // Starting new intent
                //Intent in = new Intent(getApplicationContext(),DphActivity.class);

                //Bundle extras = new Bundle();
                
         	    //extras.putString(TAG_CAT, cat); 
                //extras.putString(TAG_DCEX, "0");
                //extras.putString(TAG_PAGEX, "1");
                //extras.putString(TAG_ICOX, pid);
                //in.putExtras(extras);

                //startActivity(in);
            }
        });
        
        //ak je pripojeny
        }
 
    }
    //koniec oncreate
    
    
    
    //simplegesture ovladanie
    
    @Override 
	  public boolean dispatchTouchEvent(MotionEvent me){ 
	    this.detector.onTouchEvent(me);
	   return super.dispatchTouchEvent(me); 
	  }

	  @Override
	  public void onSwipe(int direction) {
	   String str = "";
	   
	   switch (direction) {
	   
	   case SimpleGestureFilter.SWIPE_RIGHT : 
	
		   if( cat.equals("8")) { str = getString(R.string.popisbtnplatbystatu); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_LEFT :  

		   if( cat.equals("8")) { str = getString(R.string.popisbtnplatbystatu); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_DOWN :
		   str = getString(R.string.popisbtnplatbystatu);
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	   break;
	   case SimpleGestureFilter.SWIPE_UP :
		   str = getString(R.string.popisbtnplatbystatu);
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
     break;
	                                            
	   } 
	    
	    
	    switch (direction) {
		   
		   case SimpleGestureFilter.SWIPE_RIGHT :
			   


		   break;
		   case SimpleGestureFilter.SWIPE_LEFT :
              


		   break;
		   case SimpleGestureFilter.SWIPE_DOWN :
			   
 
		   break;
		   case SimpleGestureFilter.SWIPE_UP :
			   

		   break;
		                                            
		   }
	  }

	  @Override
	  public void onDoubleTap() {
	     Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show(); 
	  }
	  
	  //koniec simplegesture ovladanie
    
    

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
            pDialog = new ProgressDialog(OdvodyActivity.this);
            if( cat.equals("1")) {pDialog.setMessage(getString(R.string.progpokl)); }
            if( cat.equals("4")) {pDialog.setMessage(getString(R.string.progbank)); }
            if( cat.equals("8")) {pDialog.setMessage(getString(R.string.progdata)); }
            if( cat.equals("9")) {pDialog.setMessage(getString(R.string.progdata)); }
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                              public void run() {

        	String prmall = inputAll.getText().toString();
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputAllUser.getText().toString();
        	String[] prmallxxx = prmall.split(delims);
        	String jazykx = Locale.getDefault().getLanguage();
        	
        	String userxplus = userx + "/" + cat + "/" + jazykx;
        	
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
            
            params.add(new BasicNameValuePair("fir_uctx01", prmallxxx[7]));
            params.add(new BasicNameValuePair("firx", prmallxxx[1]));
            params.add(new BasicNameValuePair("rokx", prmallxxx[3]));
            params.add(new BasicNameValuePair("h_drp", "1"));
            params.add(new BasicNameValuePair("h_arch", "0"));
            params.add(new BasicNameValuePair("copern", "10"));
            params.add(new BasicNameValuePair("drupoh", "1"));
            params.add(new BasicNameValuePair("zandroidu", "1"));
            params.add(new BasicNameValuePair("anduct", "1"));
            params.add(new BasicNameValuePair("pdfand", "0"));
            params.add(new BasicNameValuePair("kli_vume", prmallxxx[5]));
            params.add(new BasicNameValuePair("kli_vduj", prmallxxx[9]));

            //'../ucto/prizdph2014.php?fir_uctx01=' + fir_uctx01 + '&h_drp=' + h_drp + '&h_dap=' + h_dap + '&h_arch=' + h_arch + 
            //'&copern=10&drupoh=1&page=1'
            
            // getting JSON string from URL
            JSONObject json = null;
            json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/ucto/platbyju.php", "GET", params);      	
            
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

                              }
            });//endrunOnUiThread

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
                            OdvodyActivity.this, productsList,
                            R.layout.list_dph, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_POH},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.poh });
                    // updating listview
                    setListAdapter(adapter);
                    
                    String kli_uzid = productsList.get(0).get(TAG_KLIUZID);
            	 	
                    
                    if( icox.equals("0")) {
                    	vybraneico = (TextView) findViewById(R.id.vybraneico);
                    	vybraneico.setVisibility(View.GONE);
                        }else{
                        	String vybraneicox = productsList.get(0).get(TAG_DAT);
                        	vybraneico = (TextView) findViewById(R.id.vybraneico);
                        	vybraneico.setText(vybraneicox);
                            }
                    kli_uzidx = (TextView) findViewById(R.id.kli_uzidx);
                    kli_uzidx.setText(kli_uzid);
                    
                }
            });
 
        }
 
    }
    //koniec loadall
    
    
	
    
    
}