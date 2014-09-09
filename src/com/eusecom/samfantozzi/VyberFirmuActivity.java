package com.eusecom.samfantozzi;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;
 
public class VyberFirmuActivity extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    
    Button btnNext;
    
    ArrayList<HashMap<String, String>> productsList;
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";   
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_POHX = "pohx";
    
    // products JSONArray
    JSONArray products = null;
    
    String odkade;
    String idtitle;
    String idvalue;
    String pohx;
    String ductx;
    String encrypted;
    
    private SQLiteDatabase db2=null;
    private Cursor constantsCursor2=null;
    String idserver2;
    String idpswd2;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.all_firmy);
        
        
        //ak uzivatel 0 nepripojeny
        if( SettingsActivity.getUserId(this).equals("0") || SettingsActivity.getDruhId(this).equals("0") ) {
        //dialog
        new AlertDialog.Builder(this)
        .setTitle(getString(R.string.oknepripojeny))
        .setMessage(getString(R.string.oknepripojenymes))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with click
            	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            	finish();
            }
        })

        .show();
        } else
        //koniec uzivatel 0 nepripojeny
        //ak je pripojeny
        {
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        odkade = extras.getString(TAG_ODKADE);
        pohx = extras.getString(TAG_POHX);
        
        inputAll = (TextView) findViewById(R.id.inputAll);
        //((TextView) findViewById(R.id.inputAll)).setText("Nastavenie " + SettingsActivity.getFormTitle(this));
        inputAll.setText("Triedenie/" + "0" + "/DESC/" + "0");
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));
        
        ductx = SettingsActivity.getFirduct(this);
        
        String servername = SettingsActivity.getServerName(this);
        String delimss = "[/]+";
    	String[] servernamex = servername.split(delimss);
    	btnNext = (Button) findViewById(R.id.btnNext);
    	btnNext.setText(servernamex[0]);
    	
    	db2=(new DatabaseDomeny(this)).getWritableDatabase();
        
        constantsCursor2=db2.rawQuery("SELECT _ID, server2, pswd2, name2 "+
				"FROM  mojedomeny WHERE _id = 2 ORDER BY server2",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor2.moveToFirst()) {
	    	  idserver2=constantsCursor2.getString(1);
	    	  idpswd2=constantsCursor2.getString(2);
	      }

	      btnNext.setText(SettingsActivity.getServerName(this));
	      
	      btnNext.setOnClickListener(new View.OnClickListener() {

	          @Override
	          public void onClick(View view) {
	              // Launching All products Activity
	              Intent i = new Intent(getApplicationContext(), VyberDomenuActivity.class);
	              startActivity(i);
	              finish();

	          }
	      });
        
        
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String firxy = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                String firnazxy = ((TextView) view.findViewById(R.id.name)).getText().toString();
                
                String result = ((TextView) view.findViewById(R.id.price)).getText().toString();
                
             	String delimso = "[;]+";
             	String[] resultxxx = result.split(delimso);

                 	String icoodb = resultxxx[9];
                 	String pokluce = resultxxx[10];
                 	String pokldok = resultxxx[11];
                 	String pokldov = resultxxx[12];

                 	String firductxy = resultxxx[14];
                 	String firdphxy = resultxxx[15];
                 	String firdph1xy = resultxxx[16];
                 	String firdph2xy = resultxxx[17];
                 	String firrokxy = resultxxx[18];
                 	
                 	String bankucexy = resultxxx[19];
                 	String bankdokxy = resultxxx[20];
                 	String doducexy = resultxxx[21];
                 	String doddokxy = resultxxx[22];
                 	String odbucexy = resultxxx[23];
                 	String odbdokxy = resultxxx[24];

            	
            	//toto uklada preference
             	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
             	Editor editor = prefs.edit();


             	editor.putString("pokluce", pokluce).apply();
             	editor.putString("pokldok", pokldok).apply();
             	editor.putString("pokldov", pokldov).apply();
             	editor.putString("fir", firxy).apply();
             	editor.putString("firnaz", firnazxy).apply();
             	editor.putString("firduct", firductxy).apply();
             	editor.putString("cisloico", icoodb).apply();
             	editor.putString("firdph", firdphxy).apply();
             	editor.putString("firdph1", firdph1xy).apply();
             	editor.putString("firdph2", firdph2xy).apply();
             	editor.putString("firrok", firrokxy).apply();
             	
             	editor.putString("bankuce", bankucexy).apply();
             	editor.putString("bankdok", bankdokxy).apply();
             	editor.putString("doduce", doducexy).apply();
             	editor.putString("doddok", doddokxy).apply();
             	editor.putString("odbuce", odbucexy).apply();
             	editor.putString("odbdok", odbdokxy).apply();
             	
             	editor.commit();
            	
            	
                Intent i = getIntent();
                // send result code 100 to notify about product update
                if( odkade.equals("2")) { setResult(100, i); }
                finish();
 
            }
        });
        
        }
        //koniec ak je pripojeny
        

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
            pDialog = new ProgressDialog(VyberFirmuActivity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            
        	String prmall = inputAll.getText().toString();
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputAllUser.getText().toString();
        	
        	String userxplus = userx + "/" + pohx;
        	
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
            //params.add(new BasicNameValuePair("triedenie", "22"));
            //params.add(new BasicNameValuePair("orderbyx", orderbyx));         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            //params.add(new BasicNameValuePair("userx", userx));
            params.add(new BasicNameValuePair("userhash", encrypted));
            params.add(new BasicNameValuePair("odkade", odkade));
            params.add(new BasicNameValuePair("pohx", pohx));
            params.add(new BasicNameValuePair("ductx", ductx));
            
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_all_firmy.php", "GET", params);
            
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
                            VyberFirmuActivity.this, productsList,
                            R.layout.list_item_firmy, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec loadallproducts
}