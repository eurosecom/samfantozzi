package com.eusecom.samfantozzi;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.Toast;
import com.eusecom.samfantozzi.MCrypt;

import com.eusecom.samfantozzi.SimpleGestureFilter.SimpleGestureListener;
 
public class DlhyActivity extends ListActivity implements SimpleGestureListener{
	
	private SimpleGestureFilter detector;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
    TextView inputAll;
    TextView inputAllServer;
    TextView inputAllUser;
    TextView obsahKosika;
    TextView aktualplu;
    Button btnUce;
    Button btnNext;
    Button btnPDF;
    
    EditText txtTitle;
    EditText txtValue;
    TextView hladaj1;
    TextView hladaj2;
    TextView hladaj3;
    TextView vybraneico;
    
    ArrayList<HashMap<String, String>> productsList;
 
    
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "name";
    private static final String TAG_PRICE = "price";
    private static final String TAG_POH = "poh";
    private static final String TAG_DAT = "dat";
    private static final String TAG_TEL = "tel";
    private static final String TAG_CAT = "cat";
    private static final String TAG_MINPLU = "minplu";
    private static final String TAG_DCEX = "dcex";
    
    private static final String TAG_PAGEX = "page";
    private static final String TAG_POHX = "pohx";
    private static final String TAG_ODKADE = "odkade";
    private static final String TAG_ICOX = "icox";
    
    private static final String TAG_FKX = "fkx";

    
    // products JSONArray
    JSONArray products = null;
    String cat;
    String dcex;
    String pagex;
    String ucex;
	String encrypted;
	String icox;
	String telx;
	String fakx;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.dlhy);
         
        detector = new SimpleGestureFilter(this,this);
 
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        cat = extras.getString(TAG_CAT);
        dcex = extras.getString(TAG_DCEX);
        pagex = extras.getString(TAG_PAGEX);
        icox = extras.getString(TAG_ICOX);
        
        obsahKosika = (TextView) findViewById(R.id.obsahKosika);
        btnPDF = (Button) findViewById(R.id.btnPDF);
        btnNext = (Button) findViewById(R.id.btnNext);
        
        if( cat.equals("8")) {
            this.setTitle(getResources().getString(R.string.popisbtnodb1) + " " + getResources().getString(R.string.page) + " " + pagex);
            ucex = SettingsActivity.getOdbuce(this);
            obsahKosika.setVisibility(View.GONE);
            //btnNext.setText(getText(R.string.popisJadlzim) + " >");
        }
        if( cat.equals("9")) {
            this.setTitle(getResources().getString(R.string.popisbtndod1) + " " + getResources().getString(R.string.page) + " " + pagex);
            ucex = SettingsActivity.getDoduce(this);
            obsahKosika.setVisibility(View.GONE);
            //btnNext.setText(getText(R.string.popisMnedlzia) + " >");
        }
     
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputAllServer = (TextView) findViewById(R.id.inputAllServer);
        inputAllServer.setText(SettingsActivity.getServerName(this));
        inputAllUser = (TextView) findViewById(R.id.inputAllUser);
        inputAllUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this) + "/Kateg/" + cat);
 
        obsahKosika.setText(" " + "0" + " " + getText(R.string.mnozstvo) 
        		+ "  " + getText(R.string.hodnota) + " = " +  "0" + "  " + getText(R.string.mena));
        
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
        
       btnNext = (Button) findViewById(R.id.btnNext);     
       // info button click event
       btnNext.setOnClickListener(new View.OnClickListener() {
 
           @Override
           public void onClick(View arg0) {

        	   Intent slideactivity = new Intent(DlhyActivity.this, DlhyActivity.class);
        	   
        	   Bundle extras = new Bundle();
        	   if( cat.equals("8")) { extras.putString(TAG_CAT, "9"); }
        	   if( cat.equals("9")) { extras.putString(TAG_CAT, "8"); }
               extras.putString(TAG_DCEX, "0");
               extras.putString(TAG_PAGEX, "1");
               extras.putString(TAG_ICOX, icox);
               slideactivity.putExtras(extras);
        	   
				Bundle bndlanimation =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toleft,R.anim.animation_toleft2).toBundle();
				startActivity(slideactivity, bndlanimation);
				finish();
           }
       });
        
        // new obj click event
        btnUce = (Button) findViewById(R.id.btnUce);
        
        if( icox.equals("0")) {
        if( cat.equals("8")) { btnUce.setText(SettingsActivity.getOdbuce(this)); }
        if( cat.equals("9")) { btnUce.setText(SettingsActivity.getDoduce(this)); }
        }else{
        	//btnUce.setText(getText(R.string.popisTel) + " " + telx);
        	btnUce.setText(telx);
        }
        	
        
        btnUce.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
            	
            	if( icox.equals("0")) {
            	Intent iz = new Intent(getApplicationContext(), VyberUceActivity.class);
                Bundle extrasz = new Bundle();
                //extrasz.putString(TAG_POZX, "1");
                //extrasz.putString(TAG_FAKX, "0");
                if( cat.equals("8")) {extrasz.putString(TAG_ODKADE, "108");}
                if( cat.equals("9")) {extrasz.putString(TAG_ODKADE, "109");}
                if( cat.equals("8")) {extrasz.putString(TAG_POHX, "108");}
                if( cat.equals("9")) {extrasz.putString(TAG_POHX, "109");}
                extrasz.putString(TAG_CAT, cat);
                iz.putExtras(extrasz);
                startActivityForResult(iz, 100);
            	}else{
 
                    String number = "tel:" + telx.trim();
                    number = number.replace('/',' ');
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
                    startActivity(callIntent);	
            	}
 
            }
        });
        
        btnPDF = (Button) findViewById(R.id.btnPDF);     
        // info button click event
        btnPDF.setOnClickListener(new View.OnClickListener() {
  
            @Override
            public void onClick(View arg0) {
                
            	String serverx = inputAllServer.getText().toString();
            	String delims = "[/]+";
            	String[] serverxxx = serverx.split(delims);
            	String userx = inputAllUser.getText().toString();
            	
            	String allx = inputAll.getText().toString();
            	String[] allxxx = allx.split(delims);

            	String userxplus = userx + "/" + icox;
            	
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
            	
            	if( icox.equals("0")) {
            		
                    	uri = Uri.parse("http://" + serverxxx[0] + 
                        "/ucto/saldo_pdf.php?copern=11&drupoh=1&page=1&h_uce=" + ucex + "&h_ico=0&zandroidu=1&anduct=1&serverx=" 
                        + serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
            		
            	}else{
            			uri = Uri.parse("http://" + serverxxx[0] + 
                        "/ucto/saldo_pdf.php?copern=14&drupoh=4&page=1&h_uce=" + ucex + "&h_ico=" + icox + "&zandroidu=1&anduct=1&serverx=" 
                        + serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );

            	}
            	//koniec else
                
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
        
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
                Intent in = new Intent(getApplicationContext(),DlhyActivity.class);

                Bundle extras = new Bundle();
                
         	    extras.putString(TAG_CAT, cat); 
                extras.putString(TAG_DCEX, "0");
                extras.putString(TAG_PAGEX, "1");
                extras.putString(TAG_ICOX, pid);
                in.putExtras(extras);

                if( icox.equals("0")) {
                	startActivity(in);	
                }else{
                
                }
            }
        });
        
        //ak je pripojeny
        }
 
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
	
		   if( cat.equals("8")) { str = getString(R.string.popisbtndod1); }
		   if( cat.equals("9")) { str = getString(R.string.popisbtnodb1); }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

	   break;
	   case SimpleGestureFilter.SWIPE_LEFT :  

		   if( cat.equals("8")) { str = getString(R.string.popisbtndod1); }
		   if( cat.equals("9")) { str = getString(R.string.popisbtnodb1); }
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
			   
			   Intent slideactivityr = new Intent(DlhyActivity.this, DlhyActivity.class);
        	   
        	   Bundle extrasr = new Bundle();
        	   if( cat.equals("8")) { extrasr.putString(TAG_CAT, "9"); }
        	   if( cat.equals("9")) { extrasr.putString(TAG_CAT, "8"); }
               extrasr.putString(TAG_DCEX, "0");
               extrasr.putString(TAG_PAGEX, "1");
               extrasr.putString(TAG_ICOX, icox);
               slideactivityr.putExtras(extrasr);
        	   
               Bundle bndlanimation =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toright,R.anim.animation_toright2).toBundle();
               startActivity(slideactivityr, bndlanimation);
               finish();


		   break;
		   case SimpleGestureFilter.SWIPE_LEFT :
              
			   Intent slideactivityl = new Intent(DlhyActivity.this, DlhyActivity.class);
        	   
        	   Bundle extras = new Bundle();
        	   if( cat.equals("8")) { extras.putString(TAG_CAT, "9"); }
        	   if( cat.equals("9")) { extras.putString(TAG_CAT, "8"); }
               extras.putString(TAG_DCEX, "0");
               extras.putString(TAG_PAGEX, "1");
               extras.putString(TAG_ICOX, icox);
               slideactivityl.putExtras(extras);
        	   
               Bundle bndlanimationl =
						ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation_toleft,R.anim.animation_toleft2).toBundle();
               startActivity(slideactivityl, bndlanimationl);
               finish();

		   break;
		   case SimpleGestureFilter.SWIPE_DOWN :
			   
			   int dpagei = Integer.parseInt(pagex);
               int dpageim = dpagei - 1;
               if( dpageim == 0 ) { dpageim = 1; }
               String dpageis = dpageim + "";
			   
			   Intent id = new Intent(getApplicationContext(), DlhyActivity.class);
               Bundle extrasd = new Bundle();
        	   if( cat.equals("8")) { extrasd.putString(TAG_CAT, "8"); }
        	   if( cat.equals("9")) { extrasd.putString(TAG_CAT, "9"); }
               extrasd.putString(TAG_DCEX, "0");
               extrasd.putString(TAG_PAGEX, dpageis);
               extrasd.putString(TAG_ICOX, icox);
               id.putExtras(extrasd);
               startActivity(id);
               finish();
 
		   break;
		   case SimpleGestureFilter.SWIPE_UP :
			   
			   int upagei = Integer.parseInt(pagex);
               int upageim = upagei + 1;
               if( upageim == 0 ) { upageim = 1; }
               String upageis = upageim + "";
			   
			   Intent iu = new Intent(getApplicationContext(), DlhyActivity.class);
               Bundle extrasu = new Bundle();
        	   if( cat.equals("8")) { extrasu.putString(TAG_CAT, "8"); }
        	   if( cat.equals("9")) { extrasu.putString(TAG_CAT, "9"); }
               extrasu.putString(TAG_DCEX, "0");
               extrasu.putString(TAG_PAGEX, upageis);
               extrasu.putString(TAG_ICOX, icox);
               iu.putExtras(extrasu);
               startActivity(iu);
               finish();

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

	 	String mnox3 = productsList.get(position).get(TAG_FKX);
	 	
	 	String price4 = productsList.get(position).get(TAG_PRICE);

    menu.setHeaderTitle(mnox3 + " | " + name2 + " | " + price4);
    
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.kontext_dlhy, menu);
    
    if( cat.equals("9")) {menu.getItem(1).setVisible(false); menu.getItem(2).setVisible(false); menu.getItem(3).setVisible(false); }
    if(icox.equals("0")) {menu.getItem(3).setVisible(false); }
    }
    
    private void sendSMS(String phoneNumber, String message)
    {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);

    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    
    case R.id.kontextupomsmsd:
    	if( icox.equals("0")) {}else{
    		String telxup=telx.replace('/',' ');
    		
    		int position = info.position;
    	 	
    	 	String vsyx = productsList.get(position).get(TAG_FKX);
    	 	String hodx = productsList.get(position).get(TAG_PRICE);
    	 	fakx = vsyx;
    	 	
    	 	String smstextx= getResources().getString(R.string.upomsmstext1) + " " + getResources().getString(R.string.popisVsy) 
    	 			+ " " + vsyx + " " + getResources().getString(R.string.upomneuhradene) 
    	 			+ " " + hodx + getString(R.string.mena);
    	 	
    	
    	PackageManager packageManager = getBaseContext().getPackageManager();
		boolean jetel = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

						if (jetel){
			
			sendSMS(telxup, smstextx);
    	
    	new AlertDialog.Builder(this)
        .setTitle(getString(R.string.kontupomsms))
        .setMessage(getString(R.string.upomodoslana) + " " + telx)
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
                // continue with click
            	//finish();
            }
        }).show();
    	
    	new upomsms().execute();
						}//konic ak jetel

    	}
    	break;
    
    case R.id.kontextupomd:
        String pidxu = String.valueOf(info.id);
        int inposu = Integer.parseInt(pidxu);
        
        String dokladxu = productsList.get(inposu).get(TAG_FKX);
        
    	String serverxu = inputAllServer.getText().toString();
    	String delimsu = "[/]+";
    	String[] serverxxxu = serverxu.split(delimsu);
    	String userxu = inputAllUser.getText().toString();
    	
    	String allxu = inputAll.getText().toString();
    	String[] allxxxu = allxu.split(delimsu);

    	String userxplusu = userxu + "/" + dokladxu;
    	
    	//String userhash = sha1Hash( userx );
    	MCrypt mcryptu = new MCrypt();
    	/* Encrypt */
    	try {
			encrypted = MCrypt.bytesToHex( mcryptu.encrypt(userxplusu) );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/* Decrypt */
    	//String decrypted = new String( mcrypt.decrypt( encrypted ) );
        
    	Uri uriu = null;
    	
    	if( icox.equals("0")) {
    		
    		uriu = Uri.parse("http://" + serverxxxu[0] + 
                    "/ucto/upomienka.php?copern=20&drupoh=1&page=1&h_pen=0&h_ppe=0&h_uce=" + ucex + "&h_ico=" + dokladxu + 
                    "&cislo_ico=" + dokladxu + "&cislo_fak=0&cislo_strana=1&zandroidu=1&anduct=1&serverx=" 
                    + serverxu + "&userhash=" + encrypted + "&rokx=" + allxxxu[3] + "&firx=" + allxxxu[1] );
    		
    	}else{
    	
    		uriu = Uri.parse("http://" + serverxxxu[0] + 
                    "/ucto/upomienka.php?copern=10&drupoh=1&page=1&h_pen=0&h_ppe=0&h_uce=" + ucex + "&h_ico=" + icox + 
                    "&cislo_ico=" + icox + "&cislo_fak=" + dokladxu + "&cislo_strana=1&zandroidu=1&anduct=1&serverx=" 
                    + serverxu + "&userhash=" + encrypted + "&rokx=" + allxxxu[3] + "&firx=" + allxxxu[1] );

    	}
    	//koniec else
        
        Intent intentu = new Intent(Intent.ACTION_VIEW, uriu);
        startActivity(intentu);
        intentu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        break;
        
    case R.id.kontextupomemail:
        String pidxe = String.valueOf(info.id);
        int inpose = Integer.parseInt(pidxe);
        
        String dokladxe = productsList.get(inpose).get(TAG_FKX);
        
    	String serverxe = inputAllServer.getText().toString();
    	String delimse = "[/]+";
    	String[] serverxxxe = serverxe.split(delimse);
    	String userxe = inputAllUser.getText().toString();
    	
    	String allxe = inputAll.getText().toString();
    	String[] allxxxe = allxe.split(delimse);

    	String userxpluse = userxe + "/" + dokladxe;
    	
    	//String userhash = sha1Hash( userx );
    	MCrypt mcrypte = new MCrypt();
    	/* Encrypt */
    	try {
			encrypted = MCrypt.bytesToHex( mcrypte.encrypt(userxpluse) );
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/* Decrypt */
    	//String decrypted = new String( mcrypt.decrypt( encrypted ) );
        
    	Uri urie = null;
    	
    	if( icox.equals("0")) {
    		
    		urie = Uri.parse("http://" + serverxxxe[0] + 
                    "/ucto/upomienka.php?copern=20&drupoh=1&page=1&h_pen=0&h_ppe=0&h_uce=" + ucex + "&h_ico=" + dokladxe + 
                    "&cislo_ico=" + dokladxe + "&cislo_fak=0&cislo_strana=1&zandroidu=1&anduct=1&akoposlatandroid=1&serverx=" 
                    + serverxe + "&userhash=" + encrypted + "&rokx=" + allxxxe[3] + "&firx=" + allxxxe[1] );
    		
    	}else{
    	
    		urie = Uri.parse("http://" + serverxxxe[0] + 
                    "/ucto/upomienka.php?copern=10&drupoh=1&page=1&h_pen=0&h_ppe=0&h_uce=" + ucex + "&h_ico=" + icox + 
                    "&cislo_ico=" + icox + "&cislo_fak=" + dokladxe + "&cislo_strana=1&zandroidu=1&akoposlatandroid=1&anduct=1&serverx=" 
                    + serverxe + "&userhash=" + encrypted + "&rokx=" + allxxxe[3] + "&firx=" + allxxxe[1] );

    	}
    	//koniec else
        
        Intent intente = new Intent(Intent.ACTION_VIEW, urie);
        startActivity(intente);
        intente.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        break;
    
        
        case R.id.kontexttlacd:
            String pidx = String.valueOf(info.id);
            int inpos = Integer.parseInt(pidx);
            
            String dokladx = productsList.get(inpos).get(TAG_PID);
            
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputAllUser.getText().toString();
        	
        	String allx = inputAll.getText().toString();
        	String[] allxxx = allx.split(delims);

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
        	
        	if( icox.equals("0")) {
        		
        		uri = Uri.parse("http://" + serverxxx[0] + 
                        "/ucto/saldo_pdf.php?copern=11&drupoh=1&page=1&h_uce=" + ucex + "&h_ico=" + dokladx + "&zandroidu=1&anduct=1&serverx=" 
                        + serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        		
        	}else{
        	
        	if( cat.equals("8")) {
            uri = Uri.parse("http://" + serverxxx[0] + 
            		"/faktury/vstf_pdf.php?cislo_dok=" + dokladx + "&mini=0&tlacitR=1&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=1&page=1&serverx=" 
            		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
        	}
        	if( cat.equals("9")) {
                uri = Uri.parse("http://" + serverxxx[0] + 
                		"/faktury/vstf_pdf.php?cislo_dok=" + dokladx + "&mini=0&tlacitR=1&sysx=UCT&rozuct=ANO&zandroidu=1&anduct=1&h_razitko=1&copern=20&drupoh=2&page=1&serverx=" 
                		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
            	}
        	
        	}
        	//koniec else
            
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            break;
            

        case R.id.kontextnicd:
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
            pDialog = new ProgressDialog(DlhyActivity.this);
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
            
        	String prmall = inputAll.getText().toString();
        	String serverx = inputAllServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputAllUser.getText().toString();
        	
        	hladaj1 = (TextView) findViewById(R.id.hladaj1);
        	hladaj2 = (TextView) findViewById(R.id.hladaj2);
        	hladaj3 = (TextView) findViewById(R.id.hladaj3);
        	
        	String hladaj1x = hladaj1.getText().toString();
        	String hladaj2x = hladaj2.getText().toString();
        	String hladaj3x = hladaj3.getText().toString();
        	
        	String userxplus = userx + "/" + cat;
        	
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
            params.add(new BasicNameValuePair("pagex", pagex));
            params.add(new BasicNameValuePair("ucex", ucex));
            params.add(new BasicNameValuePair("icox", icox));
            params.add(new BasicNameValuePair("cat", cat));
            params.add(new BasicNameValuePair("hladaj1", hladaj1x));
            params.add(new BasicNameValuePair("hladaj2", hladaj2x));
            params.add(new BasicNameValuePair("hladaj3", hladaj3x));
            
            // getting JSON string from URL
            JSONObject json = null;
            if( icox.equals("0")) {
            json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_saldo.php", "GET", params);
            }else{
                json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/get_saldofak.php", "GET", params);
                }
            	
            
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
                        String tlfx = c.getString(TAG_TEL);
                        String fkx = c.getString(TAG_FKX);
                        
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_PRICE, price);
                        map.put(TAG_MINPLU, minplu);
                        map.put(TAG_POH, pohx);
                        map.put(TAG_DAT, naix);
                        map.put(TAG_TEL, tlfx);
                        map.put(TAG_FKX, fkx);
                        
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
                            DlhyActivity.this, productsList,
                            R.layout.list_dlhy, new String[] { TAG_PID, TAG_NAME, TAG_PRICE, TAG_POH, TAG_FKX},
                            new int[] { R.id.pid, R.id.name, R.id.price, R.id.poh, R.id.fkx });
                    // updating listview
                    setListAdapter(adapter);
                    
                    String zostatok = productsList.get(0).get(TAG_MINPLU);
            	 	
                    //obsahKosika = (TextView) findViewById(R.id.obsahKosika);
                    //obsahKosika.setText(String.format(getString(R.string.kosikZos), zostatok));
                    btnNext = (Button) findViewById(R.id.btnNext);
                    btnNext.setText(getString(R.string.hodnota) + " " + zostatok);
                    
                    if( icox.equals("0")) {
                    	vybraneico = (TextView) findViewById(R.id.vybraneico);
                    	vybraneico.setVisibility(View.GONE);
                        }else{
                        	String vybraneicox = productsList.get(0).get(TAG_DAT);
                        	vybraneico = (TextView) findViewById(R.id.vybraneico);
                        	vybraneico.setText(vybraneicox);
                        	
                        	telx = productsList.get(0).get(TAG_TEL);
                        	btnUce = (Button) findViewById(R.id.btnUce);
                            btnUce.setText(telx);
                        	
                        	
                            }
                   
                    
                }
            });
 
        }
 
    }
    //koniec loadall
    
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class upomsms extends AsyncTask<String, String, String> {
    	
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
        	
        	String userxplus = userx + "/" + cat;
        	
        	//String userhash = sha1Hash( userx );
        	MCrypt mcrypt = new MCrypt();
        	/* Encrypt */
        	try {
				encrypted = MCrypt.bytesToHex( mcrypt.encrypt(userxplus) );
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        	// Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();         
            params.add(new BasicNameValuePair("prmall", prmall));
            params.add(new BasicNameValuePair("serverx", serverx));
            params.add(new BasicNameValuePair("userhash", encrypted));
            params.add(new BasicNameValuePair("icox", icox));
            params.add(new BasicNameValuePair("fakx", fakx));
            params.add(new BasicNameValuePair("telx", telx));

            // getting JSON string from URL
            JSONObject json = null;

            json = jParser.makeHttpRequest("http://" + serverxxx[0] + "/androidfanti/upom_sms.php", "GET", params);

            	
            
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                	

                    
                } else {

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

            
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                   
                   
                    
                }
            });
 
        }
 
    }
    //koniec upomsms
    
    
	//optionsmenu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

	inflater.inflate(R.menu.options_dlhy, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case R.id.optionsvsetkyd:
			hladaj1 = (TextView) findViewById(R.id.hladaj1);
			hladaj1.setText("");
			hladaj2 = (TextView) findViewById(R.id.hladaj2);
			hladaj2.setText("");
			hladaj3 = (TextView) findViewById(R.id.hladaj3);
			hladaj3.setText("");
			
			// Hashmap for ListView
	        productsList = new ArrayList<HashMap<String, String>>();
			new LoadAllProducts().execute();
			
			return true;
		
		case R.id.optionshladajd:

    		hladaj();
    		return true;
    		
		
		case R.id.optionsprevpaged:
			int dpagei = Integer.parseInt(pagex);
            int dpageim = dpagei - 1;
            if( dpageim == 0 ) { dpageim = 1; }
            String dpageis = dpageim + "";
			   
			   Intent id = new Intent(getApplicationContext(), DlhyActivity.class);
            Bundle extrasd = new Bundle();
     	    if( cat.equals("8")) { extrasd.putString(TAG_CAT, "8"); }
     	    if( cat.equals("9")) { extrasd.putString(TAG_CAT, "9"); }
            extrasd.putString(TAG_DCEX, "0");
            extrasd.putString(TAG_PAGEX, dpageis);
            extrasd.putString(TAG_ICOX, icox);
            id.putExtras(extrasd);
            startActivity(id);
            finish();
            return true;
            
		case R.id.optionsnextpaged:
			int upagei = Integer.parseInt(pagex);
            int upageim = upagei + 1;
            if( upageim == 0 ) { upageim = 1; }
            String upageis = upageim + "";
			   
			   Intent iu = new Intent(getApplicationContext(), DlhyActivity.class);
            Bundle extrasu = new Bundle();
     	    if( cat.equals("8")) { extrasu.putString(TAG_CAT, "8"); }
     	    if( cat.equals("9")) { extrasu.putString(TAG_CAT, "9"); }
            extrasu.putString(TAG_DCEX, "0");
            extrasu.putString(TAG_PAGEX, upageis);
            extrasu.putString(TAG_ICOX, icox);
            iu.putExtras(extrasu);
            startActivity(iu);
            finish();
            return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	//koniec optionsmenu
	
	@SuppressLint("InflateParams")
	private void hladaj() {
		LayoutInflater inflater=LayoutInflater.from(this);
		View addView=inflater.inflate(R.layout.add_edit, null);
		final DialogWrapper wrapper=new DialogWrapper(addView);
		
		new AlertDialog.Builder(this)
			.setTitle(R.string.konthladaj)
			.setView(addView)
			.setPositiveButton(R.string.textyes,
													new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
															int whichButton) {
					processHladaj(wrapper);
				}
			})
			.setNegativeButton(R.string.textno,
													new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
															int whichButton) {
					// ignore, just dismiss
					
				}
			})
			.show();
	}
	//koniec hladaj
	
	private void processHladaj(DialogWrapper wrapper) {
		
		hladaj1 = (TextView) findViewById(R.id.hladaj1);
		hladaj1.setText(wrapper.getTitle());
		String valuex = wrapper.getValue() + "";
		hladaj2 = (TextView) findViewById(R.id.hladaj2);
		hladaj2.setText(valuex);
		hladaj3 = (TextView) findViewById(R.id.hladaj3);
		hladaj3.setText(wrapper.getText());
	
		// Hashmap for ListView
        productsList = new ArrayList<HashMap<String, String>>();
		new LoadAllProducts().execute();
	}
	//koniec processhladaj
	
	class DialogWrapper {
		EditText titleField=null;
		EditText valueField=null;
		EditText textField=null;
		View base=null;
		
		DialogWrapper(View base) {
			this.base=base;
			valueField=(EditText)base.findViewById(R.id.value);
		}
		
		String getTitle() {
			return(getTitleField().getText().toString());
		}
		
		float getValuefloat() {
			//return(new Float(getValueField().getText().toString()).floatValue());
			return(Float.valueOf(getValueField().getText().toString()));
		}
		
		String getValue() {
			return(getValueField().getText().toString());
		}
		
		String getText() {
			return(getTextField().getText().toString());
		}
		
		private EditText getTitleField() {
			if (titleField==null) {
				titleField=(EditText)base.findViewById(R.id.title);
			}
			
			return(titleField);
		}
		
		private EditText getValueField() {
			if (valueField==null) {
				valueField=(EditText)base.findViewById(R.id.value);
			}
			
			return(valueField);
		}
		
		private EditText getTextField() {
			if (textField==null) {
				textField=(EditText)base.findViewById(R.id.retazec);
			}
			
			return(textField);
		}
	}
	//koniec wrapper
    
    
}