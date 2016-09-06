package com.eusecom.samfantozzi;

/**
 * Get type of accounting....
 * Called from UpravPoklPol1Activity.java....
 * After choose Return to Activity that was caaling her
 * Call ../androidfanti/get_all_ucty.php
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class VyberUceActivity extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    
    ArrayList<HashMap<String, String>> productsList;
 
    String orderbyx = "77777";
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";   
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_POHX = "pohx";
    private static final String TAG_CAT = "cat";
    
    // products JSONArray
    JSONArray products = null;
    
    String odkade;
    private SQLiteDatabase db=null;
    String idtitle;
    String idvalue;
    String pohx;
    String ductx;
    String cat;

    String prmall, serverx;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.all_ucty);
        
     // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        odkade = extras.getString(TAG_ODKADE);
        pohx = extras.getString(TAG_POHX);
        cat = extras.getString(TAG_CAT);
        
        db=(new DatabaseHelper(this)).getWritableDatabase();
       
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));
        
        ductx = SettingsActivity.getFirduct(this);


        prmall = inputAll.getText().toString();
        serverx = inputAllServer.getText().toString();

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllProducts().execute(prmall, serverx);
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                
                String corobit="0";
                
                if( odkade.equals("2")) { corobit="1"; }
                if( odkade.equals("3")) { corobit="1"; }
                if( odkade.equals("101")) { corobit="2"; }
                if( odkade.equals("102")) { corobit="4"; }
                if( odkade.equals("108")) { corobit="8"; }
                if( odkade.equals("109")) { corobit="9"; }
            	
                if( corobit.equals("1")) {
            	ContentValues values=new ContentValues(2);
        		
        		values.put("valueuce", pid);
        		values.put("titleuce", name);
        		
        		//db.insert("constants", "title", values);
        		db.update("constants", values, "_ID=1", null);
                }
                
                

                if( corobit.equals("2")) {
                	
                	String result = ((TextView) view.findViewById(R.id.price)).getText().toString();
                    
                 	String delimso = "[;]+";
                 	String[] resultxxx = result.split(delimso);

                     	String pokldok = resultxxx[0];
                     	String pokldov = resultxxx[1];
                	
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Editor editor = prefs.edit();
                
             	editor.putString("pokluce", pid).apply();
             	editor.putString("pokldok", pokldok).apply();
             	editor.putString("pokldov", pokldov).apply();
             	
             	editor.commit();
                 }
                if( corobit.equals("4")) {
                	
                	String result = ((TextView) view.findViewById(R.id.price)).getText().toString();
                    
                 	String delimso = "[;]+";
                 	String[] resultxxx = result.split(delimso);

                     	String pokldok = resultxxx[0];
                	
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Editor editor = prefs.edit();
                
             	editor.putString("bankuce", pid).apply();
             	editor.putString("bankdok", pokldok).apply();
             	
             	editor.commit();
                 }
                
                if( corobit.equals("8")) {
                	
                	String result = ((TextView) view.findViewById(R.id.price)).getText().toString();
                    
                 	String delimso = "[;]+";
                 	String[] resultxxx = result.split(delimso);

                     	String pokldok = resultxxx[0];
                	
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Editor editor = prefs.edit();
                
             	editor.putString("odbuce", pid).apply();
             	editor.putString("odbdok", pokldok).apply();
             	
             	editor.commit();
                 }
                
                if( corobit.equals("9")) {
                	
                	String result = ((TextView) view.findViewById(R.id.price)).getText().toString();
                    
                 	String delimso = "[;]+";
                 	String[] resultxxx = result.split(delimso);

                     	String pokldok = resultxxx[0];
                	
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                Editor editor = prefs.edit();
                
             	editor.putString("doduce", pid).apply();
             	editor.putString("doddok", pokldok).apply();
             	
             	editor.commit();
                 }
            	
                Intent i = getIntent();
                // send result code 100 to notify about product update
                if( odkade.equals("2")) { setResult(302, i); }
                if( odkade.equals("3")) { setResult(402, i); }
                if( odkade.equals("101")) { setResult(101, i); }
                if( odkade.equals("102")) { setResult(102, i); }
                if( odkade.equals("108")) { setResult(101, i); }
                if( odkade.equals("109")) { setResult(101, i); }
                finish();
 
            }
        });
        

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
            pDialog = new ProgressDialog(VyberUceActivity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            String prmall =  args[0];
            String serverx =  args[1];
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	//String userx = inputAllUser.getText().toString();
        	
        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("triedenie", "22"));
            //params.add(new BasicNameValuePair("orderbyx", orderbyx));         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            //params.add(new BasicNameValuePair("userx", userx));
            params.add(new BasicNameValuePair("odkade", odkade));
            params.add(new BasicNameValuePair("pohx", pohx));
            params.add(new BasicNameValuePair("ductx", ductx));
            params.add(new BasicNameValuePair("cat", cat));
            
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_all_ucty.php", "GET", params);
            
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
 
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_PID);
                        String name = c.getString(TAG_NAME);
                        String price = c.getString(TAG_PRICE);
                        
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        
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
                            VyberUceActivity.this, productsList,
                            R.layout.list_item_ucty, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec loadallproducts
}