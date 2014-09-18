package com.eusecom.samfantozzi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.StrictMode;
import com.eusecom.samfantozzi.MCrypt;
 
public class UpravIcoActivitySD extends Activity {
 

	EditText inputIco;
	EditText inputDic;
	EditText inputIcd;
	EditText inputNai;
	EditText inputUli;
	EditText inputMes;
	EditText inputPsc;
	EditText inputTel;
	EditText inputMail;
	EditText inputWww;
	
    Button btnSave;
    Button btnTel;

    TextView inputEdiServer;
    TextView inputEdiUser;
    TextView inputAll;
    
    BufferedReader in;
 
    // Progress Dialog
    private ProgressDialog pDialog;
    private ProgressDialog pDialog2;
    
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
 
    // JSON Node names
    private static final String TAG_ICO = "ico";
    private static final String TAG_DIC = "dic";
    private static final String TAG_ICD = "icd";
    private static final String TAG_NAI = "nai";
    private static final String TAG_ULI = "uli";
    private static final String TAG_MES = "mes";
    private static final String TAG_PSC = "psc";
    private static final String TAG_TEL = "tel";
    private static final String TAG_MAIL = "mail";
    private static final String TAG_WWW = "www";
    private static final String TAG_NEWX = "newx";
    private static final String TAG_ICOX = "icox";
    
    private static final String TAG_CUSTOMER = "customer";
    
    private static final String NODE_ICO = "ico";
    private static final String NODE_DIC = "dic";
    private static final String NODE_ICD = "icd";
    private static final String NODE_NAI = "nai";
    private static final String NODE_MES = "mes";
    private static final String NODE_ULI = "uli";
    private static final String NODE_PSC = "psc";
    private static final String NODE_TEL = "tel";
    private static final String NODE_MAIL = "mail";
    private static final String NODE_WWW = "www";

    String pozx;
    String fakx;
    String newx;
    String icox;
    String encrypted;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uprav_ico);
        
        // getting product details from intent
        Intent i = getIntent();
        
        Bundle extras = i.getExtras();
        icox = extras.getString(TAG_ICOX);
        //pozx = extras.getString(TAG_POZX);
        //fakx = extras.getString(TAG_FAKX);
        newx = extras.getString(TAG_NEWX);
        pozx = "1";
        fakx = "13001";
          
        inputAll = (TextView) findViewById(R.id.inputAll);
        inputAll.setText("Fir/" + SettingsActivity.getFir(this) + "/Firrok/" + SettingsActivity.getFirrok(this));
        inputEdiServer = (TextView) findViewById(R.id.inputEdiServer);
        inputEdiServer.setText(SettingsActivity.getServerName(this));
        inputEdiUser = (TextView) findViewById(R.id.inputEdiUser);
        inputEdiUser.setText("Nick/" + SettingsActivity.getNickName(this) + "/ID/" + SettingsActivity.getUserId(this) + "/PSW/" 
                + SettingsActivity.getUserPsw(this) + "/druhID/" + SettingsActivity.getDruhId(this) 
                + "/Doklad/" + SettingsActivity.getDoklad(this));     

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        
    
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update product
                new SaveProductDetailsSD().execute();
            }
        });
        
        if( newx.equals("0")) { new GetProductDetailsSD().execute(); 

        inputIco = (EditText) findViewById(R.id.inputIco);
        inputIco.setEnabled(false);
    	inputIco.setFocusable(false);
    	inputIco.setFocusableInTouchMode(false);
        }
        
        // tel button
        btnTel = (Button) findViewById(R.id.btnTel);
        

        btnTel.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
            	
            		PackageManager packageManager = getBaseContext().getPackageManager();
            		boolean jetel = packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

            		if (jetel){
            		EditText num=(EditText)findViewById(R.id.inputTel); 
                    String number = "tel:" + num.getText().toString().trim();
                    number = number.replace('/',' ');
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
                    startActivity(callIntent);
            		}else{
            			
            			
            		}

            }
        });
        

        
        
    }
    //koniec oncreate
    

    
    /**
     * Background Async Task to Get complete product details
     * */
    class GetProductDetailsSD extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(UpravIcoActivitySD.this);
            pDialog2.setMessage(getString(R.string.progdata));
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... params) {

        	
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                	
                	
                	XMLDOMParser parser = new XMLDOMParser();
                    try {
                    	
                    	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                    	String fileName = "";
                    	
                    	fileName = "/eusecom/androiducto/ico.xml";
                    	File myFile = new File(baseDir + File.separator + fileName);
                        
                        Document doc = parser.getDocument(new FileInputStream(myFile));
                        
                        // Get elements by name employee
                        NodeList nodeList = doc.getElementsByTagName(TAG_CUSTOMER);
                        
                        inputIco = (EditText) findViewById(R.id.inputIco);
                        inputDic = (EditText) findViewById(R.id.inputDic);
                        inputIcd = (EditText) findViewById(R.id.inputIcd);
                        inputNai = (EditText) findViewById(R.id.inputNai);
                        inputUli = (EditText) findViewById(R.id.inputUli);
                        inputMes = (EditText) findViewById(R.id.inputMes);
                        inputPsc = (EditText) findViewById(R.id.inputPsc);
                        inputTel = (EditText) findViewById(R.id.inputTel);
                        inputMail = (EditText) findViewById(R.id.inputMail);
                        inputWww = (EditText) findViewById(R.id.inputWww);
                        
                        // Here, we have only one <employee> element
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            Element e = (Element) nodeList.item(i);

                            String icoxy = parser.getValue(e, NODE_ICO);
                            String dicx = parser.getValue(e, NODE_DIC);
                            String icdx = parser.getValue(e, NODE_ICD);
                            String naix = parser.getValue(e, NODE_NAI);
                            String ulix = parser.getValue(e, NODE_ULI);
                            String mesx = parser.getValue(e, NODE_MES);
                            String pscx = parser.getValue(e, NODE_PSC);
                            String telx = parser.getValue(e, NODE_TEL);
                            String mailx = parser.getValue(e, NODE_MAIL);
                            String wwwx = parser.getValue(e, NODE_WWW);

                			if( icoxy.equals(icox)) { 
                            inputIco.setText(icoxy);
                            inputDic.setText(dicx);
                            inputIcd.setText(icdx);
                            inputNai.setText(naix);
                            inputUli.setText(ulix);
                            inputPsc.setText(pscx);
                            inputMes.setText(mesx);
                            inputTel.setText(telx);
                            inputMail.setText(mailx);
                            inputWww.setText(wwwx);
                			}

                            			
                            			
                        }
                       //koniec for
                        
 
                    //koniec try
                    } catch (Exception e) {
                       
                    }
                	//koniec catch
                	
                    
                }
            });
            //koniec runonui
            
            return null;
        }
        //koniec doinback
        
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog2.dismiss();
        }
    }
 
    
    /**
     * Background Async Task to  Save product Details
     * */
    class SaveProductDetailsSD extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UpravIcoActivitySD.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {
 
        	
        	inputIco = (EditText) findViewById(R.id.inputIco);
            inputDic = (EditText) findViewById(R.id.inputDic);
            inputIcd = (EditText) findViewById(R.id.inputIcd);
            inputNai = (EditText) findViewById(R.id.inputNai);
            inputUli = (EditText) findViewById(R.id.inputUli);
            inputMes = (EditText) findViewById(R.id.inputMes);
            inputPsc = (EditText) findViewById(R.id.inputPsc);
            inputTel = (EditText) findViewById(R.id.inputTel);
            inputMail = (EditText) findViewById(R.id.inputMail);
            inputWww = (EditText) findViewById(R.id.inputWww);
            
            // getting updated data from EditTexts
            String ico = inputIco.getText().toString();
            String dic = inputDic.getText().toString();
            String icd = inputIcd.getText().toString();
            String nai = inputNai.getText().toString();
            String uli = inputUli.getText().toString();
            String mes = inputMes.getText().toString();
            String psc = inputPsc.getText().toString();
            String tel = inputTel.getText().toString();
            String mail = inputMail.getText().toString();
            String www = inputWww.getText().toString();


            
            String prmall = inputAll.getText().toString();
        	String serverx = inputEdiServer.getText().toString();
        	String delims = "[/]+";
        	String[] serverxxx = serverx.split(delims);
        	String userx = inputEdiUser.getText().toString();
        	
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
        	
            HttpParams httpParameters = new BasicHttpParams();

            HttpClient client = new DefaultHttpClient(httpParameters);
            client.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
            client.getParams().setParameter("http.socket.timeout", 2000);
            client.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
            httpParameters.setBooleanParameter("http.protocol.expect-continue", false);
            HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/uloz_ico1.php?sid=" + String.valueOf(Math.random()));
            request.getParams().setParameter("http.socket.timeout", 5000);

        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        	postParameters.add(new BasicNameValuePair("prmall", prmall));
            postParameters.add(new BasicNameValuePair(TAG_ICO, ico));
            postParameters.add(new BasicNameValuePair(TAG_DIC, dic));
            postParameters.add(new BasicNameValuePair(TAG_ICD, icd));
            postParameters.add(new BasicNameValuePair(TAG_NAI, nai));
            postParameters.add(new BasicNameValuePair(TAG_ULI, uli));
            postParameters.add(new BasicNameValuePair(TAG_MES, mes));
            postParameters.add(new BasicNameValuePair(TAG_PSC, psc));
            postParameters.add(new BasicNameValuePair(TAG_TEL, tel));
            postParameters.add(new BasicNameValuePair(TAG_MAIL, mail));
            postParameters.add(new BasicNameValuePair(TAG_WWW, www));
            
        	postParameters.add(new BasicNameValuePair("serverx", serverx));
        	//postParameters.add(new BasicNameValuePair("userx", userx));
        	postParameters.add(new BasicNameValuePair("userhash", encrypted));
        	postParameters.add(new BasicNameValuePair("fakx", fakx));
        	postParameters.add(new BasicNameValuePair("newx", newx));
        	postParameters.add(new BasicNameValuePair("pozx", pozx));
        	postParameters.add(new BasicNameValuePair("icox", icox));
            
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
  
            String delimso = "[;]+";
         	String[] resultxxx = result.split(delimso);

             if( resultxxx[0].equals("1")) {

            	
            	// successfully updated
                Intent i = getIntent();
                // send result code 100 to notify about product update
                setResult(100, i);
                finish();
            }else {

            }
            
                 } catch (ClientProtocolException e) {
            	   // TODO Auto-generated catch block
            	   e.printStackTrace();
            	   //Toast.makeText(EditProductv2Activity.this, e.toString(), Toast.LENGTH_LONG).show();
            	  } catch (IOException e) {
            		   // TODO Auto-generated catch block
            		   e.printStackTrace();
            		   //Toast.makeText(EditProductv2Activity.this, e.toString(), Toast.LENGTH_LONG).show();
            		  }

 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
 //koniec save

}