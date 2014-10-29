package com.eusecom.samfantozzi;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.eusecom.samfantozzi.MCrypt;

public class ReklamaPagerActivity extends Activity {
	
    private ProgressDialog pDialog;
    String encrypted;
    BufferedReader in;
    String itext1, itext2, itext3, iurl1, iurl2, iurl3;
    Bitmap bitmap1, bitmap2, bitmap3;
    int uznatiahol = 0;
    List<NameValuePair> postParameters;
    ViewPager viewPager;
    ArrayList<Bitmap> bitmapArray=null;
    ArrayList<String> textArray=null;
    ArrayList<String> urlArray=null;
    String strana="1";
    private MenuItem menuitem0;
    private MenuItem menuitem1;
    
    String firmax;
    String adresarx;


	
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_reklamapager);
	    
	    firmax=SettingsActivity.getFir(this);
        adresarx=SettingsActivity.getServerName(this);
        String delims = "[/]+";
    	String[] serverxxx = adresarx.split(delims);
    	adresarx=serverxxx[1];
	    

	     	//ak je pripojenie natiahni text a obr z webu TUTO NEBUDEM NATAHOVAT z WEBU LEN local
	     	// new nacitajTextyAobr().execute();
	    	 
	    	 
	    	 bitmapArray = new ArrayList<Bitmap>();
	    	 new nacitajTextyAobrOffline().execute();

	 
	    viewPager = (ViewPager) findViewById(R.id.view_pager);
	    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	int stranai = position + 1;
            	strana=stranai + "";
            	
            	menuitem0.setEnabled(true);
            	menuitem1.setEnabled(true);
    	        

    	        if( strana.equals("1")) { menuitem0.setEnabled(false); }
    	        if( strana.equals("3")) { menuitem1.setEnabled(false); }
            	
            	
                Toast.makeText(ReklamaPagerActivity.this, getString(R.string.changeinfopage) + " " + strana, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

	  }
	  //koniec oncreate
	  
	  /**
	     * Background Async Task to  nacitaj textyoffline
	     * */
	    class nacitajTextyAobrOffline extends AsyncTask<String, String, String> {
	 
	        /**
	         * Before starting background thread Show Progress Dialog
	         * */
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(ReklamaPagerActivity.this);
	            pDialog.setMessage(getString(R.string.progdata));
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	 
	        /**
	         * Nacitaj texty
	         * */
	        protected String doInBackground(String... args) {
	        	
	        	 textArray = new ArrayList<String>();
	  			 //textArray.add(getString(R.string.infotext1));
	  			 //textArray.add(getString(R.string.infotext2));
	  			 //textArray.add(getString(R.string.infotext3));
	  			 
	  			 //read poklzah144.csv and full textArray
	  			try {
	            	
	            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
	            	String fileName = "/eusecom/" + adresarx + "/poklzah"+ firmax + ".csv";
	            	File myFile = new File(baseDir + File.separator + fileName);

	                FileInputStream fIn = new FileInputStream(myFile);
	                BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
	                String aDataRow = "";
	                //do ip napocitam kolko riadkov mam
	                int ip=0;
	            	
	                while ((aDataRow = myReader.readLine()) != null) {
	                    String aBuffer = aDataRow;
	                    ip = ip+1;
	                    textArray.add(aBuffer);
	                }

	                myReader.close();
	  			 

	  			 urlArray = new ArrayList<String>();
	  			 urlArray.add(getString(R.string.infourl1));
	  			 urlArray.add(getString(R.string.infourl2));
	  			 urlArray.add(getString(R.string.infourl3));
	  			 
	  			
	  			 bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.add1new);
	   	   	  	 bitmapArray.add(bitmap1);
	   	   	  	 bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.add2new);
		   	  	 bitmapArray.add(bitmap2);
		   	  	 bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.add3new);
		   	  	 bitmapArray.add(bitmap3);
		   	  	 
	  			} catch (Exception e) {
	                //Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
	            }

	 
	            return null;
	        }
	 
	        /**
	         * After completing background task Dismiss the progress dialog
	         * **/
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog 
	            pDialog.dismiss();
	            
	         // updating UI from Background Thread
	            	runOnUiThread(new Runnable() {
	                public void run() {
	                	
	                	//toto vyrabam adapter dynamicky
	                	//ImageAdapter adapter = new ImageAdapter(MainActivity.this, bitmap1, bitmapArray );
	                	//toto skusim vyrobit adapter nafuknuty z rozlozenia
	                	ImageAdapterFromRes adapter = new ImageAdapterFromRes(ReklamaPagerActivity.this, textArray, urlArray, bitmapArray );
	            	    viewPager.setAdapter(adapter);
	            	    

		
	                }
	            });
	        }
	        //koniec onpostexec
	    }
	    //koniec nacitajTextyoffline
	  

	  
	    /**
	     * Background Async Task to  nacitaj texty a obr z WEBU
	     * */
	    class nacitajTextyAobr extends AsyncTask<String, String, String> {
	 
	        /**
	         * Before starting background thread Show Progress Dialog
	         * */
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(ReklamaPagerActivity.this);
	            pDialog.setMessage(getString(R.string.progdata));
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }
	 
	        /**
	         * Nacitaj texty
	         * */
	        protected String doInBackground(String... args) {
	 
	        	String prmall="kambala1206";
	        	String userx="xxx";
	        	String randomnum = String.valueOf(Math.random());
	        	
	        	String userxplus = prmall + "/" + userx + "/" + randomnum;
	        	
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
	            HttpPost request = new HttpPost("http://www.eshoptest.sk/androidrsp/rsp_infotxt.php?sid=" + randomnum);
	            request.getParams().setParameter("http.socket.timeout", 5000);

	        	List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	        	postParameters.add(new BasicNameValuePair("prmall", prmall));
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
	  
	            String delimso = "[;]+";
	         	String[] resultxxx = result.split(delimso);

	             		if( resultxxx[0].equals("1")) {

	             			itext1=resultxxx[1];
	             			itext2=resultxxx[2];
	             			itext3=resultxxx[3];
	             			
	             			textArray = new ArrayList<String>();
	             			textArray.add(itext1);
	             			textArray.add(itext2);
	             			textArray.add(itext3);
	             			
	             			iurl1=resultxxx[4];
	             			iurl2=resultxxx[5];
	             			iurl3=resultxxx[6];
	             			
	             			urlArray = new ArrayList<String>();
	             			urlArray.add(iurl1);
	             			urlArray.add(iurl2);
	             			urlArray.add(iurl3);
	 
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

	            
	            	bitmapArray = new ArrayList<Bitmap>();
	            	String imageUrl = "";
	    	   	 	LoadImageFromWebOperations(imageUrl);
	 
	            return null;
	        }
	 
	        /**
	         * After completing background task Dismiss the progress dialog
	         * **/
	        protected void onPostExecute(String file_url) {
	            // dismiss the dialog 
	            pDialog.dismiss();
	            
	         // updating UI from Background Thread
	            	runOnUiThread(new Runnable() {
	                public void run() {
	                	
	                	//toto vyrabam adapter dynamicky
	                	//ImageAdapter adapter = new ImageAdapter(MainActivity.this, bitmap1, bitmapArray );
	                	//toto skusim vyrobit adapter nafuknuty z rozlozenia
	                	ImageAdapterFromRes adapter = new ImageAdapterFromRes(ReklamaPagerActivity.this, textArray, urlArray, bitmapArray );
	            	    viewPager.setAdapter(adapter);
	            	    

		
	                }
	            });
	        }
	        //koniec onpostexec
	    }
	    //koniec nacitajTexty
	    
	    public void LoadImageFromWebOperations(String url) {
	    	
	    	try {

	    		  //ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

	    		  String imageUrl1 = "http://www.eshoptest.sk/dokumenty/FIR144/amaterial/d1001.jpg";
	     	   	  bitmap1 = BitmapFactory.decodeStream((InputStream)new URL(imageUrl1).getContent());
	     	   	  bitmapArray.add(bitmap1);
	     	   	  String imageUrl2 = "http://www.eshoptest.sk/dokumenty/FIR144/amaterial/d1002.jpg";
	     	   	  bitmap2 = BitmapFactory.decodeStream((InputStream)new URL(imageUrl2).getContent());
	     	   	  bitmapArray.add(bitmap2);
	     	   	  String imageUrl3 = "http://www.eshoptest.sk/dokumenty/FIR144/amaterial/d1003.jpg";
	     	   	  bitmap3 = BitmapFactory.decodeStream((InputStream)new URL(imageUrl3).getContent());
	     	   	  bitmapArray.add(bitmap3);
	   	   	  

	   	   	  
	   	   	} catch (MalformedURLException e) {
	   	   	  e.printStackTrace();
	   	   	} catch (IOException e) {
	   	   	  e.printStackTrace();
	   	   	}
	    	
	    }
	    //koniecloadimage
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        super.onCreateOptionsMenu(menu);
	        getMenuInflater().inflate(R.menu.menuactivity_reklama_slide, menu);
	        
	        menuitem0 = menu.getItem(0);
	        menuitem1 = menu.getItem(1);
	        menuitem0.setEnabled(false);
	        //tuto nereagoval na premennu strana a nezhasinal menu item a
	    	//menu.findItem(R.id.action_previous).setEnabled(true);
	        //menu.findItem(R.id.action_next).setEnabled(true);
	    	//menu.getItem(0).setEnabled(true);
	        //menu.getItem(1).setEnabled(true);
	        
	        //if( strana.equals("1")) { menu.findItem(R.id.action_previous).setEnabled(false); }
	        //if( strana.equals("3")) { menu.findItem(R.id.action_next).setEnabled(false); }

	        //if( strana.equals("3")) { menu.getItem(1).setEnabled(false); }


	        return true;
	    }
	    
	    @Override
	    public boolean onPrepareOptionsMenu (Menu menu) {
	    	
	    	//tuto nereagoval na premennu strana a nezhasinal menu item a
	    	//menu.findItem(R.id.action_previous).setEnabled(true);
	        //menu.findItem(R.id.action_next).setEnabled(true);
	    	//menu.getItem(0).setEnabled(true);
	        //menu.getItem(1).setEnabled(true);
	        
	        //if( strana.equals("1")) { menu.findItem(R.id.action_previous).setEnabled(false); }
	        //if( strana.equals("3")) { menu.findItem(R.id.action_next).setEnabled(false); }

	        //if( strana.equals("3")) { menu.getItem(1).setEnabled(false); }
	
	        return true;
	    }
	    

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	

	    	switch (item.getItemId()) {
	        
	            case R.id.action_previous:
	                // Go to the previous step in the wizard. If there is no previous step,
	                // setCurrentItem will do nothing.
	            	viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
	                return true;

	            case R.id.action_next:
	                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
	                // will do nothing.
	            	viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
	                return true;
	        }
	    
	        return super.onOptionsItemSelected(item);
	    }
	    //koniec onoptionselected
	    
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
	    
	  
	  
	//koniec activity 
	}
