package com.eusecom.samfantozzi;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
 

		@SuppressWarnings("unused")
		private int[] GalImages = new int[] { 
                //Images from resource folder.
            R.drawable.one,  
            R.drawable.two,
            R.drawable.three
        };
        
        //int icount =textArray.size();
 
        ImageAdapterFromRes(Context context, ArrayList<String> textArray, ArrayList<String> urlArray, ArrayList<Bitmap> bitmapArray, OnPagerItemSelected listener){
            this.context=context;
            this.textArray=textArray;
            this.urlArray=urlArray;
            this.bitmapArray=bitmapArray;
            
            this.mListener = listener;
        }
 
        @Override
        public int getCount() {
            //return GalImages.length;
            return textArray.size();
        }
 
        public Object instantiateItem(ViewGroup collection, int position) {
        	 LayoutInflater inflater = (LayoutInflater) collection.getContext()
        	 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
        	 //final String urlx = urlArray.get(position).toString();
 
        	 View view = inflater.inflate(R.layout.reklamator_new, null);
        	 
        	 imageView = (ImageView) view.findViewById(R.id.imgreklama1);
        	 imageView.setImageBitmap(bitmapArray.get(1));
        	 
        	 //String datatxt = i2 + " ;" + uce + " ;" + pozx + " ;" + dokladx + " ;" + dat + " ;" + ico + " ;" + fak + " ;" + kto
             //+ " ;" + txp + " ;" + zk0 + " ;" + zk1 + " ;" + zk2 + " ;" + dn1 + " ;" + dn2 + " ;" + poh + " ;" + clk + " \n";

        	 	String indexx = textArray.get(position).toString();
        	 	String delims2 = "[;]+";
         		String[] riadokxxx = indexx.split(delims2);
         		String ucex =  riadokxxx[1].trim();
         		String pohx =  riadokxxx[2].trim();
         		String dokx =  riadokxxx[3].trim();
         		String datx =  riadokxxx[4].trim();
         		
         		textreklama1 = (TextView) view.findViewById(R.id.textreklama1);         		
         		if( pohx.equals("1")) {
         		textreklama1.setText(((Activity) context).getResources().getString(R.string.popisprijmovy));
         		}else{
         			textreklama1.setText(((Activity) context).getResources().getString(R.string.popisvydavkovy));	
         		}
         		
         		EditText inputUce = (EditText) view.findViewById(R.id.inputUce);
         		inputUce.setText(ucex);
         		
         		EditText inputDat = (EditText) view.findViewById(R.id.inputDat);
         		inputDat.setText(datx);
         		
         		EditText inputDok = (EditText) view.findViewById(R.id.inputDok);
         		inputDok.setText(dokx);
         		


        	 
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
        	 btnUprav.setOnClickListener(new View.OnClickListener() {
  
                 @Override
                 public void onClick(View v) {

                	 Intent i = new Intent(v.getContext(), NewPoklZahActivitySD.class);
                     Bundle extras = new Bundle();
                     extras.putString("pozx", "1");
                     extras.putString("fakx", "10003");
                     extras.putString("newx", "0");
                     extras.putString("cat", "1");
                     i.putExtras(extras);
                     context.startActivity(i);
                     //it is running ok but better is implement interface
                     //((Activity) context).finish();
                     mListener.pagerItemSelected();
   
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