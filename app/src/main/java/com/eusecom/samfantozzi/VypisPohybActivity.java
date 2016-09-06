package com.eusecom.samfantozzi;

/**
 * List accounting items....
 * Called from ZostavyPageFragment.java
 * Call /androidfanti/get_vypis_pohyby.php
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;
 
public class VypisPohybActivity extends ListActivity {
 
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
    private static final String TAG_NAZX = "nazx";
    private static final String TAG_PAGEX = "pagex";
    
    // products JSONArray
    JSONArray products = null;
    
    String odkade;
    String idtitle;
    String idvalue;
    String encrypted;
    String pohx;
    String nazx;
    String pagex;

    String prmall, serverx, userx;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.vypis_pohyby);
        
     // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        //odkade=1-newprijmovy,2=newvydavkovy
        odkade = extras.getString(TAG_ODKADE);
        pohx = extras.getString(TAG_POHX);
        pagex = extras.getString(TAG_PAGEX);
       
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));


        if( pohx.equals("0")) {
        	this.setTitle(getResources().getString(R.string.popisUcd));	
        }else {
        	nazx = extras.getString(TAG_NAZX);
        	this.setTitle(pagex + ". - " + pohx + " " + nazx);	
        }


        prmall = inputAll.getText().toString();
        serverx = inputAllServer.getText().toString();
        userx = inputAllUser.getText().toString();

        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();

        // Loading products in Background Thread
        new LoadAllProducts().execute(prmall, serverx, userx);
 
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
            	
                Intent iz = new Intent(getApplicationContext(), VypisPohybActivity.class);
                Bundle extrasz = new Bundle();
                extrasz.putString("odkade", "99");
                extrasz.putString("pohx", pid);
                extrasz.putString("nazx", name);
                extrasz.putString("pagex", "1");
                iz.putExtras(extrasz);
                if( pohx.equals("0")) {startActivity(iz);}
 
            }
        });
        

    }
 //koniec oncreate
    
  //optionsmenu
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) {
  		MenuInflater inflater = getMenuInflater();

  	inflater.inflate(R.menu.options_vypispoh, menu);

  		return true;
  	}

  	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {

  		switch (item.getItemId()) {

  		
  		case R.id.kontextprevpagev:
  			int dpagei = Integer.parseInt(pagex);
              int dpageim = dpagei - 1;
              if( dpageim == 0 ) { dpageim = 1; }
              String dpageis = dpageim + "";
  			   
              Intent iz = new Intent(getApplicationContext(), VypisPohybActivity.class);
              Bundle extrasz = new Bundle();
              extrasz.putString("odkade", "99");
              extrasz.putString("pohx", pohx);
              extrasz.putString("nazx", nazx);
              extrasz.putString("pagex", dpageis);
              iz.putExtras(extrasz);
              if( pohx.equals("0")){}else {startActivity(iz); finish();}
              
              return true;
              
  		case R.id.kontextnextpagev:
  			int upagei = Integer.parseInt(pagex);
              int upageim = upagei + 1;
              if( upageim == 0 ) { upageim = 1; }
              String upageis = upageim + "";
  			   
              Intent iu = new Intent(getApplicationContext(), VypisPohybActivity.class);
              Bundle extrasu = new Bundle();
              extrasu.putString("odkade", "99");
              extrasu.putString("pohx", pohx);
              extrasu.putString("nazx", nazx);
              extrasu.putString("pagex", upageis);
              iu.putExtras(extrasu);
              if( pohx.equals("0")){}else {startActivity(iu); finish();}
              return true;
  		
  		default:
  			return super.onOptionsItemSelected(item);
  		}
  	}
  	//koniec optionsmenu
 

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
            pDialog = new ProgressDialog(VypisPohybActivity.this);
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
        	String userx =  args[2];
        	
        	String userxplus = userx + "/" + odkade;
        	
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
            params.add(new BasicNameValuePair("odkade", odkade));
            params.add(new BasicNameValuePair("pohx", pohx));
            params.add(new BasicNameValuePair("pagex", pagex));
            
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_vypis_pohyby.php", "GET", params);
            
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
                            VypisPohybActivity.this, productsList,
                            R.layout.list_vypis_pohyby, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec loadallproducts
}