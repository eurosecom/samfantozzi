package com.eusecom.samfantozzi;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

 
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.eusecom.samfantozzi.MCrypt;

 
public class MyDemoActivity extends Activity {


    // Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputConServer;
    EditText inputConNick;
    EditText inputConMail;
    TextView inputPriServer;
    TextView inputPriUser;
    EditText inputConIco;
    EditText inputConNazov;
    EditText inputConRok;
    
	Button btnPripoj;
	Button btnEDcomsk;
    BufferedReader in;
    String encrypted;
    String name2xx;
    String pswd2xx;
    String uzid2xx;
    String device_id;
    String device_td;
    Builder aabbregok;
    Builder aabbjeuz;
    String akoreg;
   
    private SQLiteDatabase db2=null;
    
    protected ArrayAdapter<CharSequence> mAdapter;
    protected int mPos;
    protected String mSelection;
	  
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydemo);

        db2=(new DatabaseDomeny(this)).getWritableDatabase();
               
        inputPriServer = (TextView) findViewById(R.id.inputPriServer);
        inputPriServer.setText(SettingsActivity.getServerName(this));
        inputPriUser = (TextView) findViewById(R.id.inputPriUser);
        inputPriUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) +
        		"/PSW/" + SettingsActivity.getUserPsw(this));
       
        
        // Edit Text
        inputConServer = (EditText) findViewById(R.id.inputConServer);
        inputConNick = (EditText) findViewById(R.id.inputConNick);
        inputConMail = (EditText) findViewById(R.id.inputConMail);
        inputConServer.setText(SettingsActivity.getServerName(this));
        inputConNick.setText(SettingsActivity.getNickName(this));
        inputConMail.setText(SettingsActivity.getMojMail(this));
        inputConIco = (EditText) findViewById(R.id.inputConIco);
        inputConNazov = (EditText) findViewById(R.id.inputConNazov);
        inputConRok = (EditText) findViewById(R.id.inputConRok);
        inputConRok.setText("2014");
        
        name2xx = SettingsActivity.getUserName(this);
        pswd2xx = SettingsActivity.getUserPsw(this);
        uzid2xx = SettingsActivity.getUserId(this);
        
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        device_id = tm.getDeviceId();
        device_td = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID); //*** use for tablets
        
        aabbregok = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.popisbtnmydemo))
        .setMessage(getString(R.string.popisbtnmydemook))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
              
            	Intent i = getIntent();
                setResult(100, i);
            	finish();
            }
         });
        
        aabbjeuz = new AlertDialog.Builder(this)
        .setTitle(getString(R.string.popisbtnmydemo))
        .setMessage(getString(R.string.popisbtnmydemouzje))
        .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
              
            	finish();
            }
         });

        Button btnEDcomsk = (Button) findViewById(R.id.btnEDcomsk);
        
        // button click event
        btnEDcomsk.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("http://www.edcom.sk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        
        Spinner spinner = (Spinner) findViewById(R.id.Spinner01);
        this.mAdapter = ArrayAdapter.createFromResource(this, R.array.Planets,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(this.mAdapter);
        

        OnItemSelectedListener spinnerListener = new myOnItemSelectedListener(this,this.mAdapter);
        spinner.setOnItemSelectedListener(spinnerListener);



        if (isOnline()) 
	     {
        	
        	// Create button
            Button btnPripoj = (Button) findViewById(R.id.btnPripoj);
     
            // button click event
            btnPripoj.setOnClickListener(new View.OnClickListener() {
     
                @Override
                public void onClick(View view) {
                    // creating new product in background thread
                    new CreateMyDemo().execute();
                }
            });

	     }else{
	    	 new AlertDialog.Builder(this)
	         .setTitle(getString(R.string.niejeinternet))
	         .setMessage(getString(R.string.potrebujeteinternetdemo))
	         .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int which) { 
	               
	             	finish();
	             }
	          })

	          .show();
	     }
        
    }
    //koniec oncreate
    
    public class myOnItemSelectedListener implements OnItemSelectedListener {


        ArrayAdapter<CharSequence> mLocalAdapter;
        Activity mLocalContext;

        public myOnItemSelectedListener(Activity c, ArrayAdapter<CharSequence> ad) {

          this.mLocalContext = c;
          this.mLocalAdapter = ad;

        }


        public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {

            MyDemoActivity.this.mPos = pos;
            MyDemoActivity.this.mSelection = parent.getItemAtPosition(pos).toString();

            inputConRok = (EditText) findViewById(R.id.inputConRok);
            inputConRok.setText(MyDemoActivity.this.mSelection);

        }
        
        public void onNothingSelected(AdapterView<?> parent) {

            // do nothing

        }
    }

    
    
    
    
    class CreateMyDemo extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MyDemoActivity.this);
            pDialog.setMessage(getString(R.string.progpripojpinkla));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
        	String serverx = inputConServer.getText().toString();
        	String mailxx = inputConMail.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputPriUser.getText().toString();
        	String icox = inputConIco.getText().toString();
        	String nazovx = inputConNazov.getText().toString();
        	String rokx = inputConRok.getText().toString();
        	
        	String userxplus = userx + "/" + icox + "/" + nazovx + "/" + rokx + "/" + device_id + "/" + device_td + "/" + mailxx;
        	
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

        	 HttpParams httpParameters = new BasicHttpParams();

             HttpClient client = new DefaultHttpClient(httpParameters);
             client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
             client.getParams().setParameter("http.socket.timeout", 2000);
             client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
             httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
             HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/create_demo.php?sid=" + String.valueOf(Math.random()));
             request.getParams().setParameter("http.socket.timeout", 5000);

         	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
         	postParameters.add(new BasicNameValuePair("serverx", serverx));
         	postParameters.add(new BasicNameValuePair("userhash", encrypted));

             try {
             UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters, HTTP.UTF_8);
             request.setEntity(formEntity);

             HttpResponse response = client.execute(request);

             in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
             StringBuffer sb = new StringBuffer("");
             String line = "";
             //String lineSeparator = System.getProperty("line.separator");
             while ((line = in.readLine()) != null) {
                 sb.append(line);
                 //sb.append(lineSeparator);
             }
             in.close();
             String result = sb.toString();
             Log.d("result",sb.toString());
             
         	String delimso = "[;]+";
         	String[] resultxxx = result.split(delimso);
         	akoreg = resultxxx[0].trim();

         	//0;test2345;mu83vd;eahdfp;;www.eshoptest.sk/androiducto;andrejd@edcom.sk;10;144;0;
         	//21100;1003;2002;xxxxxx;9;1;10;20;2014;22100;3001;32100;84001;31100;74001;0
             if( akoreg.equals("0")) {
             	String nickname = resultxxx[1];
             	String heslo = resultxxx[2];
             	String meno = resultxxx[3];
             	String uzid = resultxxx[4];
             	String servery = resultxxx[5];
             	String email = resultxxx[6];
             	String druhid = resultxxx[7];
             	String firxy = resultxxx[8];
             	String icoodb = resultxxx[9];
             	String pokluce = resultxxx[10];
             	String pokldok = resultxxx[11];
             	String pokldov = resultxxx[12];
             	String firnazxy = resultxxx[13];
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

             	editor.putString("mydemo", "1").apply();
                editor.putString("nickname", nickname).apply();
                editor.putString("userpsw", heslo).apply();
             	editor.putString("username", meno).apply();
             	editor.putString("userid", uzid).apply();
             	editor.putString("servername", servery).apply();
             	editor.putString("mojmail", email).apply();
             	editor.putString("druhid", druhid).apply();
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
             	
             	ContentValues values=new ContentValues(2);

        		values.put("server2", servery);
        		values.put("nick2", nickname);
        		values.put("mail2", email);
        		values.put("uzid2", uzid);
        		values.put("name2", name2xx);
        		values.put("pswd2", pswd2xx);
        		values.put("uzid2", uzid2xx);
        		
        		String[] argsx={servery};
        		db2.delete("mojedomeny", "server2=?", argsx);
        		db2.insert("mojedomeny", "server2", values);
                 
             }else {

            	 
             }
             
                  } catch (ClientProtocolException e) {
             	   // TODO Auto-generated catch block
             	   e.printStackTrace();
             	   //Toast.makeText(Pripojv2Activity.this, e.toString(), Toast.LENGTH_LONG).show();
             	  } catch (IOException e) {
             		   // TODO Auto-generated catch block
             		   e.printStackTrace();
             		   //Toast.makeText(Pripojv2Activity.this, e.toString(), Toast.LENGTH_LONG).show();
             		  }
             
        	
            return akoreg;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final String akoregx) {
            // dismiss the dialog once done
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                	if( akoregx.equals("1")) {
                		aabbjeuz.show();
               	 	}else{aabbregok.show();}
                }
            });
        }
 
    }//end createmydemo
    
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
    
}//endmydemoactivity