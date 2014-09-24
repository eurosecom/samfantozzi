package com.eusecom.samfantozzi;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;


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
    
    String idtitle;
    String idvalue;
    String odkade;
    String pagex;
    
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
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this));
    
        	
    	
        // Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllIcosSD().execute();
 
        // Get listview
        //ListView lv = getListView();
        
        registerForContextMenu(getListView());
        
        

        
    }
 //koniec oncreate
    


 
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


    
}
//koniec activity