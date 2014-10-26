package com.eusecom.samfantozzi;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ImageAdapterFromRes extends PagerAdapter {
	
 
        Context context;
        ArrayList<String> textArray;
        ArrayList<String> urlArray;
        ArrayList<Bitmap> bitmapArray;
        ImageView imageView;
        TextView textreklama1;
        Button btnMoreinfo1;
        public Activity activity;
 
        private int[] GalImages = new int[] { 
                //Images from resource folder.
            R.drawable.one,  
            R.drawable.two,
            R.drawable.three
        };
 
        ImageAdapterFromRes(Context context, ArrayList<String> textArray, ArrayList<String> urlArray, ArrayList<Bitmap> bitmapArray){
            this.context=context;
            this.textArray=textArray;
            this.urlArray=urlArray;
            this.bitmapArray=bitmapArray;
        }
 
        @Override
        public int getCount() {
            return GalImages.length;
        }
 
        public Object instantiateItem(ViewGroup collection, int position) {
        	 LayoutInflater inflater = (LayoutInflater) collection.getContext()
        	 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	 
        	 final String urlx = urlArray.get(position).toString();
 
        	 View view = inflater.inflate(R.layout.reklamator_new, null);
        	 
        	 imageView = (ImageView) view.findViewById(R.id.imgreklama1);
        	 imageView.setImageBitmap(bitmapArray.get(position));
        	 
        	 textreklama1 = (TextView) view.findViewById(R.id.textreklama1);
        	 textreklama1.setText(textArray.get(position).toString());
        	 
        	 btnMoreinfo1 = (Button) view.findViewById(R.id.btnMoreinfo1);
        	 btnMoreinfo1.setOnClickListener(new View.OnClickListener() {
  
                 @Override
                 public void onClick(View v) {
                 	

                	 //String uris = urlArray.get(position).toString();
                	 Uri uri = Uri.parse(urlx);
                	 //Uri uri = Uri.parse("http://www.edcom.sk");
                     Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                     context.startActivity(intent);

                     
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

//koniec adapter
}