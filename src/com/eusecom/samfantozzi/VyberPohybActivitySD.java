package com.eusecom.samfantozzi;
 
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class VyberPohybActivitySD extends ListActivity {
 
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
    
    private static final String TAG_UCTPOHYB = "uctpohyb";
    private static final String TAG_DRUH = "druh";
    private static final String TAG_CPOH = "cpoh";
    private static final String TAG_POHP = "pohp";
    
    // products JSONArray
    JSONArray products = null;
    
    String odkade;
    private SQLiteDatabase db=null;
    String idtitle;
    String idvalue;
    String firmax;
    String adresarx;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.all_ucty);
        
     // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        //odkade=1-newprijmovy,2=newvydavkovy
        odkade = extras.getString(TAG_ODKADE);
        firmax=SettingsActivity.getFir(this);
        adresarx=SettingsActivity.getServerName(this);
        String delims = "[/]+";
    	String[] serverxxx = adresarx.split(delims);
    	adresarx=serverxxx[1];
        
        db=(new DatabaseHelper(this)).getWritableDatabase();
       
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));
        
        
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllPohybySD().execute();
 
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
            	
            	ContentValues values=new ContentValues(2);
        		
        		values.put("valuepoh", pid);
        		values.put("titlepoh", name);
        		
        		//db.insert("constants", "title", values);
        		db.update("constants", values, "_ID=1", null);
            	
                Intent i = getIntent();
                // send result code 100 to notify about product update
                if( odkade.equals("1")) { setResult(601, i); }
                if( odkade.equals("2")) { setResult(602, i); }
                if( odkade.equals("3")) { setResult(603, i); }
                if( odkade.equals("8")) { setResult(608, i); }
                if( odkade.equals("9")) { setResult(609, i); }
                finish();
 
            }
        });
        

    }
    //koniec oncreate
    
    //nacitanie pohyby sd
    class LoadAllPohybySD extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VyberPohybActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 

        protected String doInBackground(String... args) {
            
        	XMLDOMParser parser = new XMLDOMParser();
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName2 = "/eusecom/" + adresarx + "/autopohyby"+ firmax + ".xml";
            	File myFile2 = new File(baseDir + File.separator + fileName2);
                
                Document doc = parser.getDocument(new FileInputStream(myFile2));
                
                // Get elements by name employee
                NodeList nodeList = doc.getElementsByTagName(TAG_UCTPOHYB);
                
                /*
                 * for each <employee> element get text of name, salary and
                 * designation
                 */
                // Here, we have only one <employee> element
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element e = (Element) nodeList.item(i);
                    String id = parser.getValue(e, TAG_CPOH);
                    String name = parser.getValue(e, TAG_POHP);
                    String price = parser.getValue(e, TAG_DRUH);
                    
                    name = name + " " + price;
                    
                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);
                    map.put(TAG_PRICE, " ");
                    
                    // adding HashList to ArrayList
                    productsList.add(map);
                }
               
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
                            VyberPohybActivitySD.this, productsList,
                            R.layout.list_item_ucty, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec nacitanie icosd
    
 

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
            pDialog = new ProgressDialog(VyberPohybActivitySD.this);
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
        	//String userx = inputAllUser.getText().toString();
        	
        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            //params.add(new BasicNameValuePair("userx", userx));
            params.add(new BasicNameValuePair("odkade", odkade));
            
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_all_pohyby.php", "GET", params);
            
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
                            VyberPohybActivitySD.this, productsList,
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