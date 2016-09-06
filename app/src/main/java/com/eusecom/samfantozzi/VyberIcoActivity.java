package com.eusecom.samfantozzi;

/**
 * Get ID of company....
 * Called from NewFakZahActivity.java, newPoklZahActivity.java....
 * After choose Return to Activity that was caaling her
 * Call ../androidfanti/get_all_icos.php
 */

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
import android.content.ContentValues;
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.eusecom.samfantozzi.DatabaseHelper;

public class VyberIcoActivity extends ListActivity {

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
    EditText inputBank;
    EditText inputOdbm;
    TextView inputIconaz;
    TextView inputOdbmnaz;
    
    private SQLiteDatabase db=null;
    private Cursor constantsCursor=null;
    String idtitle;
    String idvalue;
    String odkade;
    String pagex;
    
    ArrayList<HashMap<String, String>> productsList;
 
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_ICOX = "icox";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_PAGEX = "page";
    
    // products JSONArray
    JSONArray products = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.vyber_ico);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pagex = extras.getString(TAG_PAGEX);
        odkade = extras.getString(TAG_ODKADE);
        //odkade 1=prijmovy, 2=vydvkov, 100=z mainscreen
        
        this.setTitle(getResources().getString(R.string.popisIco) + " " + getResources().getString(R.string.page) + " " + pagex);

        
        db=(new DatabaseHelper(this)).getWritableDatabase();
       
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this));
    
        if( odkade.equals("1")) {
        	inputOdbmnaz = (TextView) findViewById(R.id.inputOdbmnaz);
        	inputOdbmnaz.setText("");
            inputOdbmnaz.setVisibility(View.GONE);
            btnhladajodbm = (Button) findViewById(R.id.btnhladajodbm);
            btnhladajodbm.setVisibility(View.GONE);
            inputOdbm = (EditText) findViewById(R.id.inputOdbm);
            inputOdbm.setText("0");
            inputOdbm.setVisibility(View.GONE);
        }
        if( odkade.equals("2")) {
        	inputOdbmnaz = (TextView) findViewById(R.id.inputOdbmnaz);
        	inputOdbmnaz.setText("");
            inputOdbmnaz.setVisibility(View.GONE);
            btnhladajodbm = (Button) findViewById(R.id.btnhladajodbm);
            btnhladajodbm.setVisibility(View.GONE);
            inputOdbm = (EditText) findViewById(R.id.inputOdbm);
            inputOdbm.setText("0");
            inputOdbm.setVisibility(View.GONE);
        }
        if( odkade.equals("100")) {
        	inputOdbmnaz = (TextView) findViewById(R.id.inputOdbmnaz);
        	inputOdbmnaz.setText("");
            inputOdbmnaz.setVisibility(View.GONE);
            btnhladajodbm = (Button) findViewById(R.id.btnhladajodbm);
            btnhladajodbm.setVisibility(View.GONE);
            inputOdbm = (EditText) findViewById(R.id.inputOdbm);
            inputOdbm.setText("0");
            inputOdbm.setVisibility(View.GONE);
        }
        //inputIconaz = (TextView) findViewById(R.id.inputIconaz);
        //inputIconaz.setVisibility(View.GONE);
        
        
        // Buttons uloz
        btnUlozNewObj = (Button) findViewById(R.id.btnUlozNewObj);
        btnUlozNewObj.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
 
                inputBank = (EditText) findViewById(R.id.inputBank);
            	String cisloicox = inputBank.getText().toString();
                inputOdbm = (EditText) findViewById(R.id.inputOdbm);
            	String cisloodbmx = inputOdbm.getText().toString();
            	
            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            	Editor editor = prefs.edit();
            	editor.putString("cisloico", cisloicox).apply();
            	editor.putString("cisloodbm", cisloodbmx).apply();
            	editor.commit();
            	
            	ContentValues values=new ContentValues(2);
            	
            	inputIconaz = (TextView) findViewById(R.id.inputIconaz);
            	String iconaz = inputIconaz.getText().toString();
            	inputOdbmnaz = (TextView) findViewById(R.id.inputOdbmnaz);
            	String odbmnaz = inputOdbmnaz.getText().toString();
        		
        		values.put("titleico", iconaz);
        		values.put("titleodbm", odbmnaz);
        		
        		//db.insert("constants", "title", values);
        		db.update("constants", values, "_ID=1", null);
                
            	// successfully updated
                Intent i = getIntent();
                // send result code 100 to notify about product update
                if( odkade.equals("1")) { setResult(201, i); }
                if( odkade.equals("2")) { setResult(202, i); }
            	// successfully created product
                //Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                //startActivity(i);
                finish();
 
 
            }
        });
        
        // Buttons hladajico
        btnhladajico = (Button) findViewById(R.id.btnhladajico);
        btnhladajico.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
            	
            	cohlada="1";
 
                // Hashmap for ListView
                productsList = new ArrayList<HashMap<String, String>>();
         
                // Loading products in Background Thread
                new LoadAllBanks().execute();
 
 
            }
        });
        
        // Buttons hladajico
        btnhladajodbm = (Button) findViewById(R.id.btnhladajodbm);
        btnhladajodbm.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
            	
            	cohlada="2";
            	inputOdbm = (EditText) findViewById(R.id.inputOdbm);
            	inputOdbm.setText("0");
            	
            	
                // Hashmap for ListView
                productsList = new ArrayList<HashMap<String, String>>();
         
                // Loading products in Background Thread
                new LoadAllOdbm().execute();
 

 
 
            }
        });
        
        //ak uzivatel 0 nepripojeny
        if( SettingsActivity.getUserId(this).equals("0")) {
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
        new LoadAllBanks().execute();
 
        // Get listview
        ListView lv = getListView();
        
        registerForContextMenu(getListView());
        
        if( odkade.equals("100")) {
        	
        } else{
        if( SettingsActivity.getCisloico(this).equals("0")) {
        }else{
        	inputBank = (EditText) findViewById(R.id.inputBank);
            inputBank.setText(SettingsActivity.getCisloico(this));	
        }
        if( SettingsActivity.getCisloodbm(this).equals("0")) {
        }else{
        	inputOdbm = (EditText) findViewById(R.id.inputOdbm);
            inputOdbm.setText(SettingsActivity.getCisloodbm(this));	
        }
        	}
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                if( cohlada.equals("1")) {
                    inputBank = (EditText) findViewById(R.id.inputBank);
                    inputBank.setText(pid);
                    String iconaz = productsList.get(position).get(TAG_NAME);
                    inputIconaz = (TextView) findViewById(R.id.inputIconaz);
                    inputIconaz.setText(iconaz);
                    }
                    
                    if( cohlada.equals("2")) {
                    inputOdbm = (EditText) findViewById(R.id.inputOdbm);
                    inputOdbm.setText(pid);
                    String odbmnaz = productsList.get(position).get(TAG_NAME);
                    inputOdbmnaz = (TextView) findViewById(R.id.inputOdbmnaz);
                    inputOdbmnaz.setText(odbmnaz);
                    }

            }
        });

        if( odkade.equals("100")) {
        	
        						} else { vylistuj(); }
        
        }
        //ak je pripojeny
        
    }
 //koniec oncreate
    
	private void vylistuj() {
		constantsCursor=db.rawQuery("SELECT _ID, title, titleico, titleodbm, value "+
				"FROM constants WHERE _id = 1 ORDER BY title",
				null);
		
		  //getString(2) znamena ze beriem 3tiu 0,1,2 premennu teda titleico
	      if (constantsCursor.moveToFirst()) {
	    	  idtitle=constantsCursor.getString(2);
	    	  idvalue=constantsCursor.getString(3);
	      }
		
	      inputIconaz = (TextView) findViewById(R.id.inputIconaz);
          inputIconaz.setText(idtitle);
	      inputOdbmnaz = (TextView) findViewById(R.id.inputOdbmnaz);
          inputOdbmnaz.setText(idvalue);
	}
	//koniec vylistuj
	
	// Response from UpravPoklZahActivity a vyberucetactivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        //navrat z upravico

    }

	//oncontextmenu
    @Override 
    public void onCreateContextMenu(ContextMenu menu, View v,
    ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
   
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    int position = info.position;
	 	
	 	String name2 = productsList.get(position).get(TAG_NAME);

	 	String mnox3 = productsList.get(position).get(TAG_PID);
	 	
	 	//String price4 = productsList.get(position).get(TAG_PRICE);

    menu.setHeaderTitle(mnox3 + " | " + name2 );
    
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.kontext_ico, menu);
    

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    
    	case R.id.kontextnewico:
    		String pidm = String.valueOf(info.id);
            int inpom = Integer.parseInt(pidm);
            
            String icom = productsList.get(inpom).get(TAG_PID);
            
            Intent im = new Intent(getApplicationContext(), UpravIcoActivity.class);
            Bundle extrasm = new Bundle();
            extrasm.putString(TAG_ICOX, icom);
            extrasm.putString(TAG_NEWX, "1");
            im.putExtras(extrasm);
            startActivityForResult(im, 100);
            break;
            
    	case R.id.kontextuprico:
    		String pidu = String.valueOf(info.id);
            int inpou = Integer.parseInt(pidu);
            
            String icou = productsList.get(inpou).get(TAG_PID);
            
            Intent iu = new Intent(getApplicationContext(), UpravIcoActivity.class);
            Bundle extrasu = new Bundle();
            extrasu.putString(TAG_ICOX, icou);
            extrasu.putString(TAG_NEWX, "0");
            iu.putExtras(extrasu);
            startActivityForResult(iu, 100);

            break;
            
    	case R.id.kontextnextipage:
    		int dpagei = Integer.parseInt(pagex);
            int dpageim = dpagei + 1;
            if( dpageim == 0 ) { dpageim = 1; }
            String dpageis = dpageim + "";
			   
			Intent id = new Intent(getApplicationContext(), VyberIcoActivity.class);
            Bundle extrasd = new Bundle();
            extrasd.putString(TAG_ODKADE, odkade);
            extrasd.putString(TAG_PAGEX, dpageis);
            id.putExtras(extrasd);
            startActivity(id);
            finish();

            break;
            
    	case R.id.kontextprevipage:
    		int dpagep = Integer.parseInt(pagex);
            int dpagepm = dpagep - 1;
            if( dpagepm == 0 ) { dpagepm = 1; }
            String dpageps = dpagepm + "";
			   
			Intent ip = new Intent(getApplicationContext(), VyberIcoActivity.class);
            Bundle extrasp = new Bundle();
            extrasp.putString(TAG_ODKADE, odkade);
            extrasp.putString(TAG_PAGEX, dpageps);
            ip.putExtras(extrasp);
            startActivity(ip);
            finish();

            break;
            
           
    
        
        
        }

        return super.onContextItemSelected(item);
    }
  
    
    //koniec oncontextmenu
 
//nacitanie ico
    /**
     * Background Async Task to Load all banks by making HTTP Request
     * */
    class LoadAllBanks extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VyberIcoActivity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All banks from url
         * */
        protected String doInBackground(String... args) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                              public void run() {

        	String prmall = inputAll.getText().toString();
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	//String userx = inputAllUser.getText().toString();
            inputBank = (EditText) findViewById(R.id.inputBank);
        	String icox = inputBank.getText().toString();
        	
        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("triedenie", "22"));
            //params.add(new BasicNameValuePair("orderbyx", orderbyx));         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            //params.add(new BasicNameValuePair("userx", userx));
            params.add(new BasicNameValuePair("icox", icox));
            params.add(new BasicNameValuePair("pagex", pagex));
            
            // getting JSON string from URL
            //JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_all_icos.php", "GET", params);
            
            // Check your log cat for JSON reponse
            try {
            Log.d("All Products: ", json.toString());
            } catch (NullPointerException e) {
                //e.printStackTrace();
            	Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
 
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

                              }//end run
            });//end runonthread

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
                            VyberIcoActivity.this, productsList,
                            R.layout.list_item_ico, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec nacitanie ico

  //nacitanie odbm
    /**
     * Background Async Task to Load all banks by making HTTP Request
     * */
    class LoadAllOdbm extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VyberIcoActivity.this);
            pDialog.setMessage(getString(R.string.prognewodbm));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All banks from url
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
            inputBank = (EditText) findViewById(R.id.inputBank);
        	String icox = inputBank.getText().toString();
        	
        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //params.add(new BasicNameValuePair("triedenie", "22"));
            //params.add(new BasicNameValuePair("orderbyx", orderbyx));         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            params.add(new BasicNameValuePair("userx", userx));
            params.add(new BasicNameValuePair("icox", icox));
            
            // getting JSON string from URL
            //JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androiducto/get_all_icos.php", "GET", params);
            
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

                              }//end run
            });//end runonthread

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
                            VyberIcoActivity.this, productsList,
                            R.layout.list_item_ico, new String[] { TAG_PID, TAG_NAME, TAG_PRICE},
                            new int[] { R.id.pid, R.id.name, R.id.price });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
    //koniec nacitanie odbm
    
    //optionsmenu
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) {
  		MenuInflater inflater = getMenuInflater();

  		inflater.inflate(R.menu.options_ico, menu);

  		return true;
  	}

  	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {

  		switch (item.getItemId()) {

  		case R.id.optionsnewico:
            
            String icom = "0";
            
            Intent im = new Intent(getApplicationContext(), UpravIcoActivity.class);
            Bundle extrasm = new Bundle();
            extrasm.putString(TAG_ICOX, icom);
            extrasm.putString(TAG_NEWX, "1");
            im.putExtras(extrasm);
            startActivityForResult(im, 100);
  			return true;
  		default:
  			return super.onOptionsItemSelected(item);
  		}
  	}
  	//koniec optionsmenu
    
}
//koniec activity