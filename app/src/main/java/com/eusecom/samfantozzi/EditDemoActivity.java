package com.eusecom.samfantozzi;

/**
 * Update info about my company - name, adress, tel....
 * Called from optionsmenu MainScreenActivity.java
 */

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EditDemoActivity extends Activity {


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

    EditText dprie;
    EditText dmeno;
    EditText dtitl;

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
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
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

    private static final String TAG_DPRIE = "dprie";
    private static final String TAG_DMENO = "dmeno";
    private static final String TAG_DTITL = "dtitl";

    String pozx;
    String fakx;
    String newx;
    String icox;
    String encrypted;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uprav_myfir);

        // getting product details from intent
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        icox = extras.getString(TAG_ICOX);
        icox = "44551142";
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
                new SaveProductDetails().execute();
            }
        });

        if (newx.equals("0")) {

            inputIco = (EditText) findViewById(R.id.inputIco);
            //inputIco.setEnabled(false);
            //inputIco.setFocusable(false);
            //inputIco.setFocusableInTouchMode(false);
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
                    if (ActivityCompat.checkSelfPermission(EditDemoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                } else {


                }

            }
        });


        if (newx.equals("0")) {
            new GetProductDetails().execute();
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditDemo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.eusecom.samfantozzi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditDemo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.eusecom.samfantozzi/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    //koniec oncreate


    /**
     * Background Async Task to Get complete product details
     */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog2 = new ProgressDialog(EditDemoActivity.this);
            pDialog2.setMessage(getString(R.string.progdata));
            pDialog2.setIndeterminate(false);
            pDialog2.setCancelable(true);
            pDialog2.show();
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {


            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters

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
                            encrypted = MCrypt.bytesToHex(mcrypt.encrypt(userxplus));
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    	/* Decrypt */
                        //String decrypted = new String( mcrypt.decrypt( encrypted ) );

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("prmall", prmall));
                        params.add(new BasicNameValuePair("fakx", fakx));
                        params.add(new BasicNameValuePair("icox", icox));
                        params.add(new BasicNameValuePair("serverx", serverx));
                        //params.add(new BasicNameValuePair("userx", userx));
                        params.add(new BasicNameValuePair("userhash", encrypted));
                        params.add(new BasicNameValuePair("newx", newx));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                "http://" + serverxxx[0] + "/androidfanti/get_ufir.php", "GET", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PRODUCT); // JSON Array

                            // get first product object from JSON Array
                            JSONObject product = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
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

                            dprie = (EditText) findViewById(R.id.dprie);
                            dmeno = (EditText) findViewById(R.id.dmeno);
                            dtitl = (EditText) findViewById(R.id.dtitl);

                            // display product data in EditText
                            inputIco.setText(product.getString(TAG_ICO));
                            inputDic.setText(product.getString(TAG_DIC));
                            inputIcd.setText(product.getString(TAG_ICD));
                            inputNai.setText(product.getString(TAG_NAI));
                            inputUli.setText(product.getString(TAG_ULI));
                            inputMes.setText(product.getString(TAG_MES));
                            inputPsc.setText(product.getString(TAG_PSC));
                            inputTel.setText(product.getString(TAG_TEL));
                            inputMail.setText(product.getString(TAG_MAIL));
                            inputWww.setText(product.getString(TAG_WWW));

                            dprie.setText(product.getString(TAG_DPRIE));
                            dmeno.setText(product.getString(TAG_DMENO));
                            dtitl.setText(product.getString(TAG_DTITL));


                        } else {
                            // product with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog2.dismiss();
        }
    }


    /**
     * Background Async Task to  Save product Details
     */
    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditDemoActivity.this);
            pDialog.setMessage(getString(R.string.progdata));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         */
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

            dprie = (EditText) findViewById(R.id.dprie);
            dmeno = (EditText) findViewById(R.id.dmeno);
            dtitl = (EditText) findViewById(R.id.dtitl);

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

            String dpriex = dprie.getText().toString();
            String dmenox = dmeno.getText().toString();
            String dtitlx = dtitl.getText().toString();

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
                encrypted = MCrypt.bytesToHex(mcrypt.encrypt(userxplus));
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
            HttpPost request = new HttpPost("http://" + serverxxx[0] + "/androidfanti/uloz_ufir.php?sid=" + String.valueOf(Math.random()));
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

            postParameters.add(new BasicNameValuePair(TAG_DPRIE, dpriex));
            postParameters.add(new BasicNameValuePair(TAG_DMENO, dmenox));
            postParameters.add(new BasicNameValuePair(TAG_DTITL, dtitlx));

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

                if (resultxxx[0].equals("1")) {


                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about product update
                    setResult(100, i);
                    finish();
                } else {

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

                              }//end run
            });//end runonthread

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
        }
    }
    //koniec save

}