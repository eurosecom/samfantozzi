package com.eusecom.samfantozzi;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
 
public class OverIcdphActivity extends Activity {

	Button send;
    EditText name;
 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overicdph);
        
        name = (EditText) findViewById(R.id.etFName);
        send = (Button) findViewById(R.id.bSend);
        
        
        
        
        send.setOnClickListener(new OnClickListener() {

        	public void onClick(View v) {
        	    
        		new SubmitForm().execute(name.getText().toString());
 
        	}
        	
        });

     
    }
 //koniec oncreate
    
    private class SubmitForm extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
                    //String sName = params[0];
                    
        			String url = "http://ec.europa.eu/taxation_customs/vies/";
        			WebView webview=(WebView)findViewById(R.id.webview); 
        			webview.loadUrl(url);
                    
                    //String url = "http://www.ala.sk/androidfanti/overicd.php";
                    //Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  Uri.parse(url));
                    //startActivity(intent);
					
                    
                    return null;
                }
        
    } 
    //koniec submitform

}
//koniec activity