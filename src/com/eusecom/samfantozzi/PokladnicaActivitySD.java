package com.eusecom.samfantozzi;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import android.app.ActivityOptions;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.samfantozzi.MCrypt;
import com.eusecom.samfantozzi.defaultXML;

import com.eusecom.samfantozzi.SimpleGestureFilter.SimpleGestureListener;
 
public class PokladnicaActivitySD extends ListActivity implements SimpleGestureListener{
	
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
    
    ArrayList<HashMap<String, String>> productsList;
 
    
    // JSON Node names
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_POH = "poh";
    private static final String TAG_CAT = "cat";
    private static final String TAG_MINPLU = "minplu";
    private static final String TAG_DCEX = "dcex";
    
    private static final String TAG_POZX = "pozx";
    private static final String TAG_FAKX = "fakx";
    private static final String TAG_PAGEX = "page";
    private static final String TAG_POHX = "pohx";
    private static final String TAG_NEWX = "newx";

    
    // products JSONArray
    JSONArray products = null;
    String cat;
    String dcex;
    String pagex;
    String ucex;
	String encrypted;
	String incomplet;
	String firmax;
    String adresarx;
    String ipx="0";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.pokladnicasd);
         
        detector = new SimpleGestureFilter(this,this);
 
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        cat = extras.getString(TAG_CAT);
        dcex = extras.getString(TAG_DCEX);
        pagex = extras.getString(TAG_PAGEX);
        
        
        this.setTitle(getResources().getString(R.string.popisbtnpoklsd));
        ucex = SettingsActivity.getPokluce(this);
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
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this) + "/Kateg/" + cat);

        incomplet = "0";
    	
    	String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
    	String fileName2 = "/eusecom/" + adresarx + "/poklzah" + firmax + ".csv";
    	File myFile2 = new File(baseDir2 + File.separator + fileName2);
    	if (myFile2.exists()) { } else { incomplet = "1"; }
    	String fileName4 = "/eusecom/" + adresarx + "/poklpol" + firmax + ".csv";
    	File myFile4 = new File(baseDir2 + File.separator + fileName4);
    	if (myFile4.exists()) { } else { incomplet = "1"; }
    	
    	if( incomplet.equals("1")) {
    		//int xxx=1;
    		//defaultXML defxml = new defaultXML(xxx);
    		
    		int xxx2=1;
    		String adresar=adresarx;
    		String firma=firmax;
    		int defxmlint;
    		defxmlint = defaultXML.createdefaultXML(xxx2, adresar, firma);
    		if( defxmlint == 1 ){Toast.makeText(this, getResources().getString(R.string.setdefaultxml), Toast.LENGTH_LONG).show();}

    	}
        
        
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
                Intent in = new Intent(getApplicationContext(),SettingsActivity.class);

                Bundle extras = new Bundle();
                extras.putString(TAG_PID, pid);
                extras.putString(TAG_DCEX, dcex);
                in.putExtras(extras);

                // starting new activity and expecting some response back
                // startActivityForResult(in, 100);
            }
        });
        

 
    }
    //koniec oncreate
    
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
        //naveat z vyberucet
        if (resultCode == 101) {
        	
            Intent intent = getIntent();
            Bundle extras = new Bundle();
            extras.putString(TAG_CAT, cat);
            extras.putString(TAG_DCEX, "0");
            extras.putString(TAG_PAGEX, "1");
            intent.putExtras(extras);
            finish();
            startActivity(intent);
        }
        if (resultCode == 102) {
        	
            Intent intent = getIntent();
            Bundle extras = new Bundle();
            extras.putString(TAG_CAT, cat);
            extras.putString(TAG_DCEX, "0");
            extras.putString(TAG_PAGEX, "1");
            intent.putExtras(extras);
            finish();
            startActivity(intent);
        }
 
    }
    
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
	
		   if( cat.equals("1")) { str = getString(R.string.popisbtndod1); }
		   if( cat.equals("4")) { str = getString(R.string.popisbtnpokl); }
		   if( cat.equals("8")) { str = getString(R.string.popisbtnbanka); }
		   if( cat.equals("9")) { str = getString(R.string.popisbtnodb1); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_LEFT :  

		   if( cat.equals("1")) { str = getString(R.string.popisbtnbanka); }
		   if( cat.equals("4")) { str = getString(R.string.popisbtnodb1); }
		   if( cat.equals("8")) { str = getString(R.string.popisbtndod1); }
		   if( cat.equals("9")) { str = getString(R.string.popisbtnpokl); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_DOWN :
		   str = getString(R.string.kontprevpage);
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	   break;
	   case SimpleGestureFilter.SWIPE_UP :
		   str = getString(R.string.kontnextpage);
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
     break;
	                                            
	   } 
	    
	    
	    switch (direction) {
		   
		   case SimpleGestureFilter.SWIPE_RIGHT :
			   
			   if (Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) { } else {
			   
			   Intent slideactivityr = new Intent(PokladnicaActivitySD.this, PokladnicaActivitySD.class);
        	   
        	   Bundle extrasr = new Bundle();
        	   extrasr.putString(TAG_CAT, "1");
               extrasr.putString(TAG_DCEX, "0");
               extrasr.putString(TAG_PAGEX, "1");
               slideactivityr.putExtras(extrasr);
        	   
               Bundle bndlanimation =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toright,R.anim.animation_toright2).toBundle();
               startActivity(slideactivityr, bndlanimation);
               finish();
               
			   }


		   break;
		   case SimpleGestureFilter.SWIPE_LEFT :
			   
			   if (Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) { } else {
              
			   Intent slideactivityl = new Intent(PokladnicaActivitySD.this, PokladnicaActivitySD.class);
        	   
        	   Bundle extras = new Bundle();
        	   extras.putString(TAG_CAT, "1"); 
               extras.putString(TAG_DCEX, "0");
               extras.putString(TAG_PAGEX, "1");
               slideactivityl.putExtras(extras);
        	   
               Bundle bndlanimationl =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toleft,R.anim.animation_toleft2).toBundle();
               startActivity(slideactivityl, bndlanimationl);
               finish();
               
			   }

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
    
    //oncontextmenu
    @Override 
    public void onCreateContextMenu(ContextMenu menu, View v,
    ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
   
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
    int position = info.position;
	 	
	 	String name2 = productsList.get(position).get(TAG_NAME);

	 	String mnox3 = productsList.get(position).get(TAG_PID);
	 	
	 	String price4 = productsList.get(position).get(TAG_PRICE);

    menu.setHeaderTitle(mnox3 + " | " + name2 + " | " + price4);
    
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.kontext_poklsd, menu);
    

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    
    	case R.id.kontextzmazsd:
    		String pidm = String.valueOf(info.id);
            int inpom = Integer.parseInt(pidm);
            
            String dokladm = productsList.get(inpom).get(TAG_PID);
            
            Intent im = new Intent(getApplicationContext(), ZmazPoklZahActivity.class);
            Bundle extrasm = new Bundle();
            extrasm.putString(TAG_CAT, cat);
            extrasm.putString(TAG_POZX, "1");
            extrasm.putString(TAG_FAKX, dokladm);
            im.putExtras(extrasm);
            startActivityForResult(im, 100);
            break;
    
        
        case R.id.kontexttlacsd:
            String pidx = String.valueOf(info.id);
            int inpos = Integer.parseInt(pidx);
            
            String dokladx = productsList.get(inpos).get(TAG_PID);
            
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputAllUser.getText().toString();
        	
        	String allx = inputAll.getText().toString();
        	String[] allxxx = allx.split(delims);
        	
        	String drupoh = "1";
        	if( cat.equals("1")) { drupoh="1"; }
        	if( cat.equals("4")) { drupoh="4"; }
        	
        	String userxplus = userx + "/" + dokladx;
        	
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
            
        	Uri uri = null;
        	
        	if( cat.equals("1")) {
        	uri = Uri.parse("http://" + serverxxx[0] + 
            		"/ucto/vspk_pdf.php?cislo_dok=" + dokladx + "&hladaj_dok=" + dokladx 
            		+ "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	}
        	if( cat.equals("4")) {
        	uri = Uri.parse("http://" + serverxxx[0] + 
            		"/ucto/vspk_pdf.php?cislo_dok=" + dokladx + "&hladaj_dok=" + dokladx 
            		+ "&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&copern=20&drupoh="+ drupoh + "&page=1&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	}
        	if( cat.equals("8")) {
            uri = Uri.parse("http://" + serverxxx[0] + 
            		"/faktury/vstf_pdf.php?cislo_dok=" + dokladx + "&mini=1&tlacitR=1&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=1&page=1&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	}
        	if( cat.equals("9")) {
                uri = Uri.parse("http://" + serverxxx[0] + 
                		"/faktury/vstf_pdf.php?cislo_dok=" + dokladx + "&mini=1&tlacitR=1&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=2&page=1&serverx=" 
                		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
            	}
            
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            break;
            
        case R.id.kontuprzahsd:
            String pidz = String.valueOf(info.id);
            int inpoz = Integer.parseInt(pidz);
            
            String dokladz = productsList.get(inpoz).get(TAG_PID);
            
            //Intent iz = new Intent(getApplicationContext(), UpravPoklZahActivity.class);
            Intent iz = new Intent();
            if( cat.equals("1")) {iz = new Intent(getApplicationContext(), UpravPoklZahActivity.class);}
            if( cat.equals("4")) {iz = new Intent(getApplicationContext(), UpravPoklZahActivity.class);}
            if( cat.equals("8")) {iz = new Intent(getApplicationContext(), UpravFakZahActivity.class);}
            if( cat.equals("9")) {iz = new Intent(getApplicationContext(), UpravFakZahActivity.class);}
            Bundle extrasz = new Bundle();
            extrasz.putString(TAG_CAT, cat);
            extrasz.putString(TAG_POZX, "1");
            extrasz.putString(TAG_FAKX, dokladz);
            extrasz.putString(TAG_NEWX, "0");
            iz.putExtras(extrasz);
            //startActivity(iz);
            //finish();
            //starting new activity and expecting some response back
            startActivityForResult(iz, 100);
            break;
            
        case R.id.kontuprpolsd:
        	String pidp = String.valueOf(info.id);
            int inpop = Integer.parseInt(pidp);
            
            String dokladp = productsList.get(inpop).get(TAG_PID);
            String pohp = productsList.get(inpop).get(TAG_POH);
            
            Intent ip = new Intent(getApplicationContext(), UpravPoklPolActivity.class);
            Bundle extrasp = new Bundle();
            extrasp.putString(TAG_POZX, "1");
            extrasp.putString(TAG_FAKX, dokladp);
            extrasp.putString(TAG_POHX, pohp);
            extrasp.putString(TAG_CAT, cat);
            ip.putExtras(extrasp);
            startActivity(ip);
            finish();
    		
    		break;

        case R.id.kontextnicsd:
            break;

        }

        return super.onContextItemSelected(item);
    }
  
    
    //koniec oncontextmenu
 

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
            pDialog = new ProgressDialog(PokladnicaActivitySD.this);
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
            
        	// read from SD card file data in the listview
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";
            	File myFile = new File(baseDir + File.separator + fileName);

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                //do ip napocitam kolko riadkov mam
                int ip=0;
            	
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow + "\n";
                    ip = ip+1;
                
                }
                
                //inputAll = (TextView) findViewById(R.id.inputAll);
                //inputAll.setText(aBuffer);

                String kosikx = aBuffer;
                myReader.close();

            	String delims = "[\n]+";
            	String delims2 = "[;]+";
            	
            	String[] riadokxxx = kosikx.split(delims);
            	ipx = ip + "";
            	ip=ip-10;
            	
                for (int i = 0; i < riadokxxx.length; i++) {
            	String riadok1 =  riadokxxx[i];

            	String[] polozkyx = riadok1.split(delims2);
            	//String cplx =  polozkyx[0];
            	String dokx =  polozkyx[3];
            	String txpx =  polozkyx[8];
            	String hodx =  polozkyx[15];
            	String hodmx =  polozkyx[15];
            	String pohx =  polozkyx[14];


                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_PID, dokx);
                map.put(TAG_NAME, txpx);
                map.put(TAG_PRICE, hodx);
                map.put(TAG_MINPLU, hodmx);
                map.put(TAG_POH, pohx);

                // adding HashList to ArrayList 
                productsList.add(map); 

                }
                
                
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
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
                            PokladnicaActivitySD.this, productsList,
                            R.layout.list_pokl, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_POH},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.poh });
                    // updating listview
                    setListAdapter(adapter);
                 
                }
            });
 
        }
 
    }
    //koniec loadall
    
    
	//optionsmenu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

	inflater.inflate(R.menu.options_poklsd, menu);


		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.kontextnewprisd:
  
            Intent ip = new Intent(getApplicationContext(), NewPoklZahActivitySD.class);
            Bundle extrasp = new Bundle();
            extrasp.putString(TAG_POZX, "1");
            extrasp.putString(TAG_FAKX, "0");
            extrasp.putString(TAG_NEWX, "1");
            extrasp.putString(TAG_CAT, cat);
            ip.putExtras(extrasp);
            startActivityForResult(ip, 100);
            
			return true;
			
		case R.id.kontextnewvydsd:
			
			Intent iv = new Intent(getApplicationContext(), NewPoklZahActivitySD.class);
            Bundle extrasv = new Bundle();
            extrasv.putString(TAG_POZX, "2");
            extrasv.putString(TAG_FAKX, "0");
            extrasv.putString(TAG_NEWX, "1");
            extrasv.putString(TAG_CAT, cat);
            iv.putExtras(extrasv);
            startActivityForResult(iv, 100);
	
			return true;
			
		case R.id.kontextsynpokl:
			

	
			return true;
		

		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//koniec optionsmenu
	
 
}