package com.eusecom.samfantozzi;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ImageAdapterFromRes extends PagerAdapter {
	
 
        Context context;
        OnPagerItemSelected mListener;
        ArrayList<String> textArray;
        ArrayList<String> urlArray;
        ArrayList<Bitmap> bitmapArray;
        ImageView imageView;
        TextView textreklama1;
        Button btnZoznam;
        Button btnUprav;
        public Activity activity;
        String firmax;
        String adresarx;
        String dokup;
        String pozup;
        
        private static final String TAG_NEWX = "newx";
        private static final String TAG_FAKX = "fakx";
 

 
        ImageAdapterFromRes(Context context, ArrayList<String> textArray, ArrayList<String> urlArray, 
        		ArrayList<Bitmap> bitmapArray, OnPagerItemSelected listener ){
            this.context=context;
            this.textArray=textArray;
            this.urlArray=urlArray;
            this.bitmapArray=bitmapArray;
            
            this.mListener = listener;
        }
 
        @Override
        public int getCount() {

            return textArray.size();
        }
 
        public Object instantiateItem(ViewGroup collection, int position) {
        	 LayoutInflater inflater = (LayoutInflater) collection.getContext()
        	 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
        	 //final String urlx = urlArray.get(position).toString();
 
        	 View view = inflater.inflate(R.layout.reklamator_new, null);
        	 
        	 //imageView = (ImageView) view.findViewById(R.id.imgreklama1);
        	 //imageView.setImageBitmap(bitmapArray.get(1));
        	 
        	 //String datatxt = i2 + " ;" + uce + " ;" + pozx + " ;" + dokladx + " ;" + dat + " ;" + ico + " ;" + fak + " ;" + kto
             //+ " ;" + txp + " ;" + zk0 + " ;" + zk1 + " ;" + zk2 + " ;" + dn1 + " ;" + dn2 + " ;" + poh + " ;" + clk + " \n";

        	 	String indexx = textArray.get(position).toString();
        	 	String delims2 = "[;]+";
         		String[] riadokxxx = indexx.split(delims2);
         		String ucex =  riadokxxx[1].trim();
         		String drhx =  riadokxxx[2].trim();
         		String dokx =  riadokxxx[3].trim();
         		String datx =  riadokxxx[4].trim();
         		String icox =  riadokxxx[5].trim();
         		String fakx =  riadokxxx[6].trim();
         		String ktox =  riadokxxx[7].trim();
         		String txpx =  riadokxxx[8].trim();
         		String zk0x =  riadokxxx[9].trim();
         		String zk1x =  riadokxxx[10].trim();
         		String zk2x =  riadokxxx[11].trim();
         		String dn1x =  riadokxxx[12].trim();
         		String dn2x =  riadokxxx[13].trim();
         		String pohx =  riadokxxx[14].trim();
         		String clkx =  riadokxxx[15].trim();
         		
         		textreklama1 = (TextView) view.findViewById(R.id.textreklama1);         		
         		if( drhx.equals("1")) {
         		textreklama1.setText(((Activity) context).getResources().getString(R.string.popisprijmovy));
         		}else{
         			textreklama1.setText(((Activity) context).getResources().getString(R.string.popisvydavkovy));	
         		}
         		
         		EditText inputUce = (EditText) view.findViewById(R.id.inputUce);
         		inputUce.setText(ucex);
         		final TextView inputDrh = (TextView) view.findViewById(R.id.inputDrh);
         		inputDrh.setText(drhx);
         		final EditText inputDok = (EditText) view.findViewById(R.id.inputDok);
         		inputDok.setText(dokx);         		
         		EditText inputDat = (EditText) view.findViewById(R.id.inputDat);
         		inputDat.setText(datx);
         		EditText inputIco = (EditText) view.findViewById(R.id.inputIco);
         		
         		//read autopohyby
                XMLDOMParser parser = new XMLDOMParser();
                firmax=SettingsActivity.getFir(((Activity) context));
                adresarx=SettingsActivity.getServerName(((Activity) context));
                String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String delims = "[/]+";
            	String[] serverxxx = adresarx.split(delims);
            	adresarx=serverxxx[1];
                String fileNamep = "/eusecom/" + adresarx + "/ico"+ firmax + ".xml";
            	File myFilep = new File(baseDir + File.separator + fileNamep);
            	String naix="";
                try {
            	
                Document doc = parser.getDocument(new FileInputStream(myFilep));
                
                // Get elements by name employee
                NodeList nodeList = doc.getElementsByTagName("customer");

                // Here, we have only one <employee> element
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element e = (Element) nodeList.item(i);
                    
                    String icoy = parser.getValue(e, "ico");
                    if(icoy.equals(icox)) {
                    naix = parser.getValue(e, "nai"); }

                	}//koniec for
                
                } catch (FileNotFoundException e1) {
                	// TODO Auto-generated catch block
                	e1.printStackTrace();
                }
         		
         		inputIco.setText(icox + " " + naix);
         		EditText inputFak = (EditText) view.findViewById(R.id.inputFak);
         		inputFak.setText(fakx);         		
         		EditText inputKto = (EditText) view.findViewById(R.id.inputKto);
         		inputKto.setText(ktox);
         		EditText inputTxp = (EditText) view.findViewById(R.id.inputTxp);
         		inputTxp.setText(txpx);
         		EditText inputZk0 = (EditText) view.findViewById(R.id.inputZk0);
         		inputZk0.setText(zk0x);
         		EditText inputZk1 = (EditText) view.findViewById(R.id.inputZk1);
         		inputZk1.setText(zk1x);
         		EditText inputZk2 = (EditText) view.findViewById(R.id.inputZk2);
         		inputZk2.setText(zk2x);
         		EditText inputDn1 = (EditText) view.findViewById(R.id.inputDn1);
         		inputDn1.setText(dn1x);
         		EditText inputDn2 = (EditText) view.findViewById(R.id.inputDn2);
         		inputDn2.setText(dn2x);
         		
         		//read autopohyby
                XMLDOMParser parser2 = new XMLDOMParser();

                String fileNamep2 = "/eusecom/" + adresarx + "/autopohyby"+ firmax + ".xml";
            	File myFilep2 = new File(baseDir + File.separator + fileNamep2);
            	String napx="";
                try {
            	
                Document doc = parser2.getDocument(new FileInputStream(myFilep2));
                
                // Get elements by name employee
                NodeList nodeList = doc.getElementsByTagName("uctpohyb");

                // Here, we have only one <employee> element
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element e = (Element) nodeList.item(i);
                    
                    String cpohy = parser.getValue(e, "cpoh");
                    if(cpohy.equals(pohx)) {
                    napx = parser.getValue(e, "pohp"); }

                	}//koniec for
                
                } catch (FileNotFoundException e1) {
                	// TODO Auto-generated catch block
                	e1.printStackTrace();
                }
         		
         		EditText inputPoh = (EditText) view.findViewById(R.id.inputPoh);
         		inputPoh.setText(pohx + " " + napx);
         		EditText inputClk = (EditText) view.findViewById(R.id.inputCelkom);
         		inputClk.setText(clkx);
         		


        	 
        	 btnZoznam = (Button) view.findViewById(R.id.btnZoznam);
        	 btnZoznam.setOnClickListener(new View.OnClickListener() {
  
                 @Override
                 public void onClick(View v) {

                	 Intent i = new Intent(v.getContext(), PokladnicaActivitySD.class);
                     Bundle extras = new Bundle();
                     extras.putString("cat", "1");
                     extras.putString("dcex", "0");
                     extras.putString("pagex", "1");
                     i.putExtras(extras);
                     context.startActivity(i);
                     //it is running ok but better is implement interface
                     //((Activity) context).finish();
                     mListener.pagerItemSelected();
   
                 }
                 
                 
             });
        	 
        	 btnUprav = (Button) view.findViewById(R.id.btnUprav);
        	 //btnUprav.setText(dokup);
        	 btnUprav.setOnClickListener(new View.OnClickListener() {
  
                 @Override
                 public void onClick(View v) {

                	 dokup = inputDok.getText().toString();
                	 pozup = inputDrh.getText().toString();
                	 Intent i = new Intent(v.getContext(), NewPoklZahActivitySD.class);
                     Bundle extras = new Bundle();
                     extras.putString("pozx", pozup);
                     extras.putString(TAG_FAKX, dokup);
                     extras.putString(TAG_NEWX, "0");
                     extras.putString("cat", "1");
                     i.putExtras(extras);
                     ((Activity) context).startActivity(i);
                     mListener.pagerItemSelected();
                	 
                	 //Toast.makeText(((Activity) context), dokup, Toast.LENGTH_SHORT).show();
   
                 }
                 
                 
             });
        	 
        	 
        	 ((ViewGroup) collection).addView(view, 0);
        	 return view;
        	 }
        
        	 @Override
        	 public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        	 ((ViewGroup) arg0).removeView((View) arg2);
        	 }

        	 @Override
        	 public boolean isViewFromObject(View arg0, Object arg1) {
        	 return arg0 == ((View) arg1);
        	 }
        	 
        	 public interface OnPagerItemSelected {
                 void pagerItemSelected();
             }
        	 


//end of adapter
}