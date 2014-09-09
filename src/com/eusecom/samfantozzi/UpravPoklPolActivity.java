package com.eusecom.samfantozzi;
 
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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;
 
public class UpravPoklPolActivity extends ListActivity {
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView obsahKosika;
    TextView mnozstvox;
    Button btnObjednaj;
    TextView kosikMnoz;
    TextView kosikSdph;
    TextView kosikBdph;    
    TextView kosikIco;
    TextView kosikOdbm;
    Button btnPolozky;
    Button btnFakturuj;
    
    ArrayList<HashMap<String, String>> productsList;
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";   
    private static final String TAG_MNOX = "mnox";
    private static final String TAG_ICONAZ = "iconaz";
    private static final String TAG_ODBMNAZ = "odbmnaz";
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_CELKOM = "celkom";
    private static final String TAG_POL = "pol";
    private static final String TAG_CPLX = "cplx";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_POHX = "pohx";
    private static final String TAG_CAT = "cat";
    
    // products JSONArray
    JSONArray products = null;
    String cat;
    String pidx;
    String pozx;
    String fakx;
    String pohx;
    String encrypted;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.uprav_poklpol);
        
     // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        pozx = extras.getString(TAG_POZX);
        fakx = extras.getString(TAG_FAKX);
        pohx = extras.getString(TAG_POHX);
        cat = extras.getString(TAG_CAT);

       if( cat.equals("1")) {
        if( pohx.equals("1")) { 
        	String akypokl=getResources().getString(R.string.popisprijmovy);
            this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
        	}
        if( pohx.equals("2")) { 
        	String akypokl=getResources().getString(R.string.popisvydavkovy);
            this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
        	}
       }
       if( cat.equals("4")) {

           	String akypokl=getResources().getString(R.string.popisbankovy);
               this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
          }
       if( cat.equals("8")) {

          	String akypokl=getResources().getString(R.string.popisodberatelska);
              this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
         }
       if( cat.equals("9")) {

         	String akypokl=getResources().getString(R.string.popisdodavatelska);
             this.setTitle(String.format(getResources().getString(R.string.title_activity_upravdok), fakx) + " " + akypokl);
        }
        

        
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this) + "/ICO/" + SettingsActivity.getCisloico(this) 
                + "/ODBM/" + SettingsActivity.getCisloodbm(this));
 
        //obsahKosika = (TextView) findViewById(R.id.obsahKosika);
        //obsahKosika.setText(" " + SettingsActivity.getDokmno(this) + " " + getText(R.string.mnozstvo) 
        //		+ "  " + getText(R.string.hodnota) + " = " +  SettingsActivity.getDokhod(this) + "  " + getText(R.string.mena));
        
        kosikMnoz = (TextView) findViewById(R.id.kosikMnoz);
        kosikMnoz.setText(getText(R.string.kosikMnoz) + " " + "0");
        kosikSdph = (TextView) findViewById(R.id.kosikSdph);
        //CharSequence textsdph = getText(R.string.kosikSdph);
        //kosikSdph.setText(textsdph);
        kosikSdph.setText(String.format(getString(R.string.spoluhod), "0"));
        kosikBdph = (TextView) findViewById(R.id.kosikBdph);
        kosikBdph.setText(String.format(getString(R.string.kosikBdph), "0"));
        
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
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
 
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SettingsActivity.class);
                // sending pid to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                //zaremoval som clicklistener startActivityForResult(in, 100);
            }
        });
        
        
        btnPolozky = (Button) findViewById(R.id.btnPolozky);
        
        // poznamka button click event
        btnPolozky.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
            	
            	Intent ip = new Intent();
                if( cat.equals("1")) {ip = new Intent(getApplicationContext(), NewPoklPridajActivity.class);}
                if( cat.equals("4")) {ip = new Intent(getApplicationContext(), NewPoklPridajActivity.class);}
                if( cat.equals("8")) {ip = new Intent(getApplicationContext(), NewPoklPridajActivity.class);}
                if( cat.equals("9")) {ip = new Intent(getApplicationContext(), NewPoklPridajActivity.class);}

                Bundle extrasp = new Bundle();
                extrasp.putString(TAG_POZX, pohx);
                extrasp.putString(TAG_FAKX, fakx);
                extrasp.putString(TAG_NEWX, "0");
                extrasp.putString(TAG_CAT, cat);
                ip.putExtras(extrasp);
                if( cat.equals("1")) { startActivityForResult(ip, 100); }
                if( cat.equals("4")) { startActivityForResult(ip, 100); }
                
            	
            }
        });
        

    }
    //koniec oncreate
    
 // Response from UpravPoklPol1Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100 UpravPoklPol1Activity
        if (resultCode == 101) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        // if result code 100 newpoklzahActivity
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
    
    //oncontextmenu
    @Override 
    public void onCreateContextMenu(ContextMenu menu, View v,
    ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
   
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    int position = info.position;
	 	
	 	String name2 = productsList.get(position).get(TAG_NAME);
	 	
	 	String mnox3 = productsList.get(position).get(TAG_MNOX);

    menu.setHeaderTitle(mnox3 + " " + name2);// if your table name is name
    
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.kontext_poklpol, menu);
    

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
  
    	case R.id.kontextzmazpol:
    		String pidm = String.valueOf(info.id);
    		int inpom = Integer.parseInt(pidm);
        
    		String dokladm = productsList.get(inpom).get(TAG_PID);
        
    		Intent im = new Intent(getApplicationContext(), ZmazPoklPolActivity.class);
    		Bundle extrasm = new Bundle();
    		extrasm.putString(TAG_POZX, "1");
    		extrasm.putString(TAG_FAKX, fakx);
    		extrasm.putString(TAG_CPLX, dokladm);
    		extrasm.putString(TAG_CAT, cat);
    		im.putExtras(extrasm);
    		startActivityForResult(im, 100);
        break;
        
        case R.id.kontextuprpol:
        	 
            String pidz = String.valueOf(info.id);
            int inpoz = Integer.parseInt(pidz);
            
            String dokladz = productsList.get(inpoz).get(TAG_PID);
            
            Intent iz = new Intent(getApplicationContext(), UpravPoklPol1Activity.class);
            Bundle extrasz = new Bundle();
            extrasz.putString(TAG_POZX, "1");
            extrasz.putString(TAG_CPLX, dokladz);
            extrasz.putString(TAG_NEWX, "0");
            extrasz.putString(TAG_FAKX, fakx);
            extrasz.putString(TAG_POHX, pohx);
            extrasz.putString(TAG_CAT, cat);
            iz.putExtras(extrasz);
            startActivityForResult(iz, 101);
    	break;
    	
        case R.id.kontextnic:
        break;

        }

        return super.onContextItemSelected(item);
    }
  
    
    //koniec oncontextmenu
    
  //optionsmenu
  	@Override
  	public boolean onCreateOptionsMenu(Menu menu) {
  		MenuInflater inflater = getMenuInflater();

  	inflater.inflate(R.menu.options_poklpol, menu);

  		return true;
  	}

  	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {
  	
  		switch (item.getItemId()) {
  		case R.id.kontextnewpol:
  			
            Intent iz = new Intent(getApplicationContext(), UpravPoklPol1Activity.class);
            Bundle extrasz = new Bundle();
            extrasz.putString(TAG_POZX, "1");
            extrasz.putString(TAG_CPLX, "0");
            extrasz.putString(TAG_NEWX, "1");
            extrasz.putString(TAG_FAKX, fakx);
            extrasz.putString(TAG_POHX, pohx);
            extrasz.putString(TAG_CAT, cat);
            iz.putExtras(extrasz);
            startActivityForResult(iz, 101);
 
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
            pDialog = new ProgressDialog(UpravPoklPolActivity.this);
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
        	
        	String userxplus = userx + "/" + fakx;
        	
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
            params.add(new BasicNameValuePair("userhash", encrypted));
            //params.add(new BasicNameValuePair("userx", userx));
            params.add(new BasicNameValuePair("fakx", fakx));
            params.add(new BasicNameValuePair("cat", cat));
            
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_poklpol.php", "GET", params);
            
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
                        String mnox = c.getString(TAG_MNOX);
                        String iconazx = c.getString(TAG_ICONAZ);
                        String odbmnazx = c.getString(TAG_ODBMNAZ);
                        String celkomx = c.getString(TAG_CELKOM);
                        String polx = c.getString(TAG_POL);
                        
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_MNOX, mnox);
                        map.put(TAG_ICONAZ, iconazx);
                        map.put(TAG_ODBMNAZ, odbmnazx);
                        map.put(TAG_CELKOM, celkomx);
                        map.put(TAG_POL, polx);
                        
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
                            UpravPoklPolActivity.this, productsList,
                            R.layout.list_item_poklpol, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_MNOX},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.mnozstvox });
                    // updating listview
                    setListAdapter(adapter);
                    
                    //String prodx = productsList.get(0).toString();
        	     	
            	 	//String[] sepiconaz = prodx.split("iconaz=");
            	 	//String iconaz1 = sepiconaz[1];
            	 	
            	 	String iconaz1 = productsList.get(0).get(TAG_ICONAZ);
            	 	
                    kosikIco = (TextView) findViewById(R.id.kosikIco);
                    kosikIco.setText(String.format(getString(R.string.kosikIco), iconaz1));

                    kosikOdbm = (TextView) findViewById(R.id.kosikOdbm);
            	 	String odbmnaz1 = productsList.get(0).get(TAG_ODBMNAZ);
                    //kosikOdbm.setText(odbmnaz1);
                    kosikOdbm.setText(odbmnaz1);
                    
                    String celkom1 = productsList.get(0).get(TAG_CELKOM);
                    String pol1 = productsList.get(0).get(TAG_POL);
                    kosikMnoz = (TextView) findViewById(R.id.kosikMnoz);
                    kosikSdph = (TextView) findViewById(R.id.kosikSdph);
                    kosikMnoz.setText(getString(R.string.kosikMnoz) + " " + pol1);
                    kosikSdph.setText(String.format(getString(R.string.spoluhod), celkom1));
                    
                }
            });
 
        }
 
    }
}