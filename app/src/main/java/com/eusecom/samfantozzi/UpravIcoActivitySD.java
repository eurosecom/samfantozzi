package com.eusecom.samfantozzi;

/**
 * Edit SD info about commpany
 * Called from VyberIcoActivitySD.java
 * File SD ico"+ companyx + ".xml
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.StrictMode;

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
    String firmax;
    String adresarx;

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

        firmax = SettingsActivity.getFir(this);
        adresarx = SettingsActivity.getServerName(this);
        String delims = "[/]+";
        String[] serverxxx = adresarx.split(delims);
        adresarx = serverxxx[1];
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

        if (newx.equals("0")) {
            new GetProductDetailsSD().execute();

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

                if (jetel) {
                    EditText num = (EditText) findViewById(R.id.inputTel);
                    String number = "tel:" + num.getText().toString().trim();
                    number = number.replace('/', ' ');
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    if (ActivityCompat.checkSelfPermission(UpravIcoActivitySD.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
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
                    	
                    	fileName = "/eusecom/" + adresarx + "/ico"+ firmax + ".xml";
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

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                              public void run() {

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
 
            
            // write on SD card file data in the text box
            try {
            	
            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            	String fileName = "/eusecom/" + adresarx + "/iconew"+ firmax + ".csv";

            	File myFile = new File(baseDir + File.separator + fileName);

        		if(!myFile.exists()){
        			myFile.createNewFile();
        		}
                //myFile.createNewFile();
                //to true znamena pridat append ked tam nie je prepise
        		FileOutputStream fOut = new FileOutputStream(myFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                
                String datatxt = ico + " ;" + dic + " ;" + icd + " ;" + nai + " ;" + uli + " ;" + psc 
                		+ " ;" + mes + " ;" + tel +  " ;" + mail + " ;" + www + "\n";
                myOutWriter.append(datatxt);
                myOutWriter.close();
                fOut.close();

                	// successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                
                
                //Toast.makeText(getBaseContext(),"Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }

                              }//end run
            });//end runOnUiThread
 
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