package com.eusecom.samfantozzi;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
    TextView inputAllKosik;
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
    private static final String TAG_NEWX = "newx";
    
    private static final String TAG_PAGEXR = "pagr";

    
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
        
        
        ucex = SettingsActivity.getPokluce(this);
        this.setTitle(getResources().getString(R.string.popisbtnpoklsd) + " " + ucex );        
        firmax=SettingsActivity.getFir(this);
        adresarx=SettingsActivity.getServerName(this);
        String delims = "[/]+";
    	String[] serverxxx = adresarx.split(delims);
    	adresarx=serverxxx[1];
        
     
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAllKosik = (TextView) findViewById(R.id.inputAllKosik);
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
    	String fileName5 = "/eusecom/" + adresarx + "/autopohyby" + firmax + ".xml";
    	File myFile5 = new File(baseDir2 + File.separator + fileName5);
    	if (myFile5.exists()) { } else { incomplet = "1"; }
    	String fileName6 = "/eusecom/" + adresarx + "/uctosnova" + firmax + ".xml";
    	File myFile6 = new File(baseDir2 + File.separator + fileName6);
    	if (myFile6.exists()) { } else { incomplet = "1"; }
    	String fileName7 = "/eusecom/" + adresarx + "/ico" + firmax + ".xml";
    	File myFile7 = new File(baseDir2 + File.separator + fileName7);
    	if (myFile7.exists()) { } else { incomplet = "1"; }
    	
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
	
		   str = getString(R.string.popisbtnpokl); 
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_LEFT :  

		   str = getString(R.string.popisbtnpokl);
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
			   
				   	Intent slideactivityr = new Intent(PokladnicaActivitySD.this, ReklamaPagerActivity.class);
	          	   
	          	   	Bundle extras = new Bundle();
	          	   	extras.putString(TAG_PAGEXR, "0");
	                slideactivityr.putExtras(extras);
        	   
               Bundle bndlanimation =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toright,R.anim.animation_toright2).toBundle();
               startActivity(slideactivityr, bndlanimation);
               finish();
               
			   }


		   break;
		   case SimpleGestureFilter.SWIPE_LEFT :
			   
			   if (Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN) { } else {
              
				   	Intent slideactivityl = new Intent(PokladnicaActivitySD.this, ReklamaPagerActivity.class);
	          	   
	          	   	Bundle extras = new Bundle();
	          	   	extras.putString(TAG_PAGEXR, "0");
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
            
            final String dokladm = productsList.get(inpom).get(TAG_PID);
    		//dialog
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.okzmazdokl) + " " + dokladm)
            .setMessage(getString(R.string.okzmazdoklmes))
            .setPositiveButton(getString(R.string.textyes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	
                    new ZmazDoklad().execute(dokladm);
                    
                    Intent i = new Intent(getApplicationContext(), PokladnicaActivitySD.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_CAT, "1");
                    extras.putString(TAG_DCEX, "0");
                    extras.putString(TAG_PAGEX, "1");
                    i.putExtras(extras);
                    startActivity(i);
                    finish();
                }
             })
            .setNegativeButton(getString(R.string.textno), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	
                }
             })
             .show();
    		

            break;
    
        
        case R.id.kontexttlacsd:
            //String pidx = String.valueOf(info.id);
            //int inpos = Integer.parseInt(pidx);
            //String dokladx = productsList.get(inpos).get(TAG_PID);
        	
        	if (android.os.Build.VERSION.SDK_INT>=16) {
        		
                Intent slideactivity = new Intent(PokladnicaActivitySD.this, ReklamaPagerActivity.class);
          	   
          	   	Bundle extras = new Bundle();
          	   	extras.putString(TAG_PAGEXR, "0");
                slideactivity.putExtras(extras);

  				Bundle bndlanimation =
  						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toleft,R.anim.animation_toleft2).toBundle();
  				startActivity(slideactivity, bndlanimation);
            	}else{
            	
            		Intent i = new Intent(getApplicationContext(), ReklamaPagerActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_PAGEXR, "0");
                    i.putExtras(extras);
                    startActivity(i);
            	}
            
        	
            break;
            
        case R.id.kontuprdoksd:
            String pidz = String.valueOf(info.id);
            int inpoz = Integer.parseInt(pidz);
            
            String dokladz = productsList.get(inpoz).get(TAG_PID);
            String pozz = productsList.get(inpoz).get(TAG_POH);
            
            Intent iu = new Intent(getApplicationContext(), NewPoklZahActivitySD.class);
            Bundle extrasu = new Bundle();
            extrasu.putString(TAG_POZX, pozz);
            extrasu.putString(TAG_FAKX, dokladz);
            extrasu.putString(TAG_NEWX, "0");
            extrasu.putString(TAG_CAT, cat);
            iu.putExtras(extrasu);
            startActivityForResult(iu, 100);
            break;
            

        case R.id.kontextsyncdoksd:
        	
        	String pidd = String.valueOf(info.id);
            int inpod = Integer.parseInt(pidd);
            
            final String dokladd = productsList.get(inpod).get(TAG_PID);
        	
        	if (isOnline()) 
		     {
				Intent i = new Intent(this, SynchroDokActivitySD.class);
				Bundle extras = new Bundle();
	            extras.putString("odkade", "100");
	            extras.putString("page", dokladd);
	            i.putExtras(extras);
				startActivity(i);
				//finish();
		     }else{
		    	 new AlertDialog.Builder(this)
		         .setTitle(getString(R.string.niejeinternet))
		         .setMessage(getString(R.string.potrebujeteinternetsync))
		         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) { 
		               
		             	//finish();
		             }
		          })

		          .show();
		     }
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
            	String pohx =  polozkyx[2].trim();
            	
            	String hodmx =  polozkyx[15];
            	if( pohx.equals("2")) {
            		
            		Float hodmf = 0f;
                	if (hodmx.trim().length() > 0) { hodmf = Float.parseFloat(hodmx); }
                	hodmf = -1 * hodmf;
                	
                	DecimalFormat df = new DecimalFormat("0.00");
                	
                	String hodmxs = df.format(hodmf);
                	hodmxs = hodmxs.replace(',','.');
                	hodx=hodmxs;
            		
            	}


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
    
    /**
     * Background Async Task to Delete doklad
     * */
    class ZmazDoklad extends AsyncTask<String, String, String> {
    	
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
 

        protected String doInBackground(String... args) {
        	
        	String zmazdoklad=args[0].trim();
        	String pozx ="0";
        	
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";
            	File myFile = new File(baseDir + File.separator + fileName);

                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                String testBuffer = "";
            	
                while ((aDataRow = myReader.readLine()) != null) {

                	testBuffer = aDataRow + "\n";
                	
                	String indexx = testBuffer;
                	String delims2 = "[;]+";
                	String[] riadokxxx = indexx.split(delims2);
                	String cplzmaz =  riadokxxx[3].trim();

                	if( cplzmaz.equals(zmazdoklad)) { 
                		
                		pozx =  riadokxxx[2].trim();
                	
                	}else{
                		aBuffer += aDataRow + "\n";
                	}
               

                }

            	inputAllKosik = (TextView) findViewById(R.id.inputAllKosik);                
            	inputAllKosik.setText(aBuffer);
                myReader.close();
                

                	String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath();
                	String fileName2 = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";

                	File myFile2 = new File(baseDir2 + File.separator + fileName2);
                	
                    myFile2.createNewFile();
                    FileOutputStream fOut2 = new FileOutputStream(myFile2);
                    OutputStreamWriter myOutWriter2 = 
                                            new OutputStreamWriter(fOut2);
                    myOutWriter2.append(inputAllKosik.getText());
                    myOutWriter2.close();
                    fOut2.close();

                	String fileName3 = "/eusecom/" + adresarx + "/poklpol"+ firmax + ".csv";
                	File myFile3 = new File(baseDir + File.separator + fileName3);

                    FileInputStream fIn3 = new FileInputStream(myFile3);
                    BufferedReader myReader3 = new BufferedReader(
                            new InputStreamReader(fIn3));
                    String aDataRow3 = "";
                    String aBuffer3 = "";
                    String testBuffer3 = "";
                	
                    while ((aDataRow3 = myReader3.readLine()) != null) {

                    	testBuffer3 = aDataRow3 + "\n";
                    	
                    	String indexx = testBuffer3;
                    	String delims2 = "[;]+";
                    	String[] riadokxxx = indexx.split(delims2);
                    	String cplzmaz =  riadokxxx[1].trim();

                    	if( cplzmaz.equals(zmazdoklad)) { 
                    	
                    	}else{
                    		aBuffer3 += aDataRow3 + "\n";
                    	}
                   

                    }

                	inputAllKosik = (TextView) findViewById(R.id.inputAllKosik);                
                	inputAllKosik.setText(aBuffer3);
                    myReader3.close();
                    

                    	String baseDir4 = Environment.getExternalStorageDirectory().getAbsolutePath();
                    	String fileName4 = "/eusecom/" + adresarx + "/poklpol"+ firmax + ".csv";

                    	File myFile4 = new File(baseDir4 + File.separator + fileName4);
                    	
                        myFile4.createNewFile();
                        FileOutputStream fOut4 = new FileOutputStream(myFile4);
                        OutputStreamWriter myOutWriter4 = 
                                                new OutputStreamWriter(fOut4);
                        myOutWriter4.append(inputAllKosik.getText());
                        myOutWriter4.close();
                        fOut4.close();
                        
                        //toto uklada preference
                     	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                     	Editor editor = prefs.edit();

                     	if(pozx.equals("1")) { editor.putString("pokldok", zmazdoklad.trim()).apply(); }
                     	if(pozx.equals("2")) { editor.putString("pokldov", zmazdoklad.trim()).apply(); }
                     	
                     	editor.commit();
                    
                    
                
                } catch (Exception e) {
                    
                }
        	

            return null;
        }
 

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            

 
        }
 
    }
    //koniec zmazdoklad
    
    
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
			
		case R.id.kontextzmazall:
			
			new AlertDialog.Builder(this)
            .setTitle(getString(R.string.okzmazdoklall))
            .setMessage(getString(R.string.okzmazdoklallmes))
            .setPositiveButton(getString(R.string.textyes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	
 
                	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                	String fileName4 = "/eusecom/" + adresarx + "/poklzah" + firmax + ".csv";
                	File myFile4 = new File(baseDir + File.separator + fileName4);                	
                	if(myFile4.exists()){myFile4.delete();}
                	String fileName5 = "/eusecom/" + adresarx + "/poklpol" + firmax + ".csv";
                	File myFile5 = new File(baseDir + File.separator + fileName5);                	
                	if(myFile5.exists()){myFile5.delete();}
                    
                    Intent i = new Intent(getApplicationContext(), PokladnicaActivitySD.class);
                    Bundle extras = new Bundle();
                    extras.putString(TAG_CAT, "1");
                    extras.putString(TAG_DCEX, "0");
                    extras.putString(TAG_PAGEX, "1");
                    i.putExtras(extras);
                    startActivity(i);
                    finish();
                }
             })
            .setNegativeButton(getString(R.string.textno), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) { 
                	
                }
             })
             .show();
			

	
			return true;
			
		case R.id.kontextsynpokl:
			
			if (isOnline()) 
		     {
				Intent i = new Intent(this, SynchroDokActivitySD.class);
				Bundle extras = new Bundle();
	            extras.putString("odkade", "100");
	            extras.putString("page", "0");
	            i.putExtras(extras);
				startActivity(i);
				//finish();
		     }else{
		    	 new AlertDialog.Builder(this)
		         .setTitle(getString(R.string.niejeinternet))
		         .setMessage(getString(R.string.potrebujeteinternetsync))
		         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
		             public void onClick(DialogInterface dialog, int which) { 
		               
		             	//finish();
		             }
		          })

		          .show();
		     }
			

			
			return true;
			
		case R.id.kontextsynpoh:
			
			if (isOnline()) 
			     {
					Intent i = new Intent(this, SynchroPohActivitySD.class);
					Bundle extras = new Bundle();
		            extras.putString("odkade", "100");
		            extras.putString("page", "1");
		            i.putExtras(extras);
					startActivity(i);
					//finish();
			     }else{
			    	 new AlertDialog.Builder(this)
			         .setTitle(getString(R.string.niejeinternet))
			         .setMessage(getString(R.string.potrebujeteinternetsync))
			         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
			             public void onClick(DialogInterface dialog, int which) { 
			               
			             	//finish();
			             }
			          })

			          .show();
			     }
			

			
			return true;
		

		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//koniec optionsmenu
	
	//test ci je internet pripojeny
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
    //koniec test ci je internet pripojeny
	
 
}