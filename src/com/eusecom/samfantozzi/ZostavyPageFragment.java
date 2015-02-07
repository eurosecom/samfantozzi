/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eusecom.samfantozzi;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.eusecom.samfantozzi.MCrypt;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy title indicating
 * the page number, along with some dummy text.
 *
 * <p>This class is used by the {@link CardFlipActivity} and {@link
 * ScreenSlideActivity} samples.</p>
 */

public class ZostavyPageFragment extends Fragment {
	
	String encrypted;
	
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ZostavyPageFragment create(int pageNumber) {
        ZostavyPageFragment fragment = new ZostavyPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ZostavyPageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }
    

    @SuppressLint("CutPasteId")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        
    	ViewGroup rootView = null;
    	
    	switch (mPageNumber) {

        case 0:
        	rootView = (ViewGroup) inflater.inflate(R.layout.uctovnezos, container, false);
        	//TextView obsahKosika = (TextView)rootView.findViewById(R.id.obsahKosika);
        	//obsahKosika.setText("xxxxxxxxxxx1page");
        	Button btnUme0 = (Button)rootView.findViewById(R.id.btnUme);
        	btnUme0.setText(SettingsActivity.getUme(getActivity()));
        	
        	// ume button click event
            btnUme0.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), VyberUmeActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("odkial", "0");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    getActivity().finish();
             	   
                }
            });
            
            // penden button click event
            Button btnPenden1 = (Button)rootView.findViewById(R.id.btnPenden1);
            btnPenden1.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "4");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // penden button click event
            Button btnPenden = (Button)rootView.findViewById(R.id.btnPenden);
            btnPenden.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "1");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // privyd button click event
            Button btnPrivyd = (Button)rootView.findViewById(R.id.btnPrivyd);
            btnPrivyd.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "2");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // privyd button click event
            Button btnMajzav = (Button)rootView.findViewById(R.id.btnMajzav);
            btnMajzav.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "3");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // kniodb button click event
            Button btnKniOdb = (Button)rootView.findViewById(R.id.btnKniOdb);
            btnKniOdb.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "5");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // knidod button click event
            Button btnKniDod = (Button)rootView.findViewById(R.id.btnKniDod);
            btnKniDod.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "6");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
         

        	break;

        case 1:
        	rootView = (ViewGroup) inflater.inflate(R.layout.danovezos, container, false);
        	Button btnUme1 = (Button)rootView.findViewById(R.id.btnUme);
        	btnUme1.setText(SettingsActivity.getUme(getActivity()));
        	
        	// ume button click event
            btnUme1.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), VyberUmeActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("odkial", "1");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    getActivity().finish();
             	   
                }
            });
            
            // dph button click event
            Button btnDph = (Button)rootView.findViewById(R.id.btnDph);
            btnDph.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), DphActivity.class);
                    Bundle extramd = new Bundle();
                    extramd.putString("cat", "8");
                    extramd.putString("dcex", "0");
                    extramd.putString("page", "1");
                    extramd.putString("icox", "0");
                    iz.putExtras(extramd);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // privyd button click event
            Button btnPrivyd2 = (Button)rootView.findViewById(R.id.btnPrivyd2);
            btnPrivyd2.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "2");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // privyd button click event
            Button btnMajzav2 = (Button)rootView.findViewById(R.id.btnMajzav2);
            btnMajzav2.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), UctoZosActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("copern", "3");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // dph button click event
            Button btnFob = (Button)rootView.findViewById(R.id.btnFob);
            btnFob.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {
                	
                	String copern="2";
                	String userx = "Nick/" + SettingsActivity.getNickName(getActivity()) + "/ID/" + SettingsActivity.getUserId(getActivity()) + "/PSW/" 
                            + SettingsActivity.getUserPsw(getActivity()) + "/druhID/" + SettingsActivity.getDruhId(getActivity()) 
                            + "/Doklad/" + SettingsActivity.getDoklad(getActivity());

                	String serverx = SettingsActivity.getServerName(getActivity());
                	String delims = "[/]+";
                	String[] serverxxx = serverx.split(delims);
                	
                	String allx = "Fir/" + SettingsActivity.getFir(getActivity()) + "/Firrok/" + SettingsActivity.getFirrok(getActivity());
                	String[] allxxx = allx.split(delims);
                	
                	String kli_vume = SettingsActivity.getUme(getActivity());
                	
                	String userxplus = userx + "/" + copern;
                	
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

                	uri = Uri.parse("http://" + serverxxx[0] + 
                    		"/ucto/priznanie_fob2014.php?copern=10&drupoh=1&page=1&zandroidu=1&anduct=1&kli_vume=" + kli_vume + "&serverx=" 
                    		+ serverx + "&userhash=" + encrypted + "&rokx=" + allxxx[3] + "&firx=" + allxxx[1] );
                	
                	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //finish();
             	   
                }
            });
            
            //platby button click event
            Button btnPlatbystatu = (Button)rootView.findViewById(R.id.btnPlatbystatu);
            btnPlatbystatu.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), OdvodyActivity.class);
                    Bundle extramd = new Bundle();
                    extramd.putString("cat", "8");
                    extramd.putString("dcex", "0");
                    extramd.putString("page", "1");
                    extramd.putString("icox", "0");
                    iz.putExtras(extramd);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
        	
        	
        	break;
        	
        case 2:
        	
        	rootView = (ViewGroup) inflater.inflate(R.layout.rozne, container, false);
        	Button btnUme2 = (Button)rootView.findViewById(R.id.btnUme);
        	btnUme2.setText(SettingsActivity.getUme(getActivity()));
        	// ume button click event
            btnUme2.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), VyberUmeActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("odkial", "2");
 
                    iz.putExtras(extrasz);
                    startActivity(iz);
                    getActivity().finish();
             	   
                }
            });
            
            // mnedlzia button click event
            Button btnMnedlzia = (Button)rootView.findViewById(R.id.btnMnedlzia);
            btnMnedlzia.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), DlhyActivity.class);
                    Bundle extramd = new Bundle();
                    extramd.putString("cat", "8");
                    extramd.putString("dcex", "0");
                    extramd.putString("page", "1");
                    extramd.putString("icox", "0");
                    iz.putExtras(extramd);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            // jadlzim button click event
            Button btnJadlzim = (Button)rootView.findViewById(R.id.btnJadlzim);
            btnJadlzim.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), DlhyActivity.class);
                    Bundle extramd = new Bundle();
                    extramd.putString("cat", "9");
                    extramd.putString("dcex", "0");
                    extramd.putString("page", "1");
                    extramd.putString("icox", "0");
                    iz.putExtras(extramd);
                    startActivity(iz);
                    //getActivity().finish();
             	   
                }
            });
            
            // btnVypispohybov click event
            Button btnVypispohybov = (Button)rootView.findViewById(R.id.btnVypispohybov);
            btnVypispohybov.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), VypisPohybActivity.class);
                    Bundle extrasz = new Bundle();
                    extrasz.putString("odkade", "99");
                    extrasz.putString("pohx", "0");
                    extrasz.putString("pagex", "1");
                    iz.putExtras(extrasz);
                    startActivity(iz);
             	   
                }
            });
            
            // btnZamkni click event
            Button btnZamkni = (Button)rootView.findViewById(R.id.btnZamkni);
            btnZamkni.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), ZamkniActivity.class);
                    Bundle extramd = new Bundle();
                    extramd.putString("cat", "8");
                    extramd.putString("dcex", "1");
                    extramd.putString("page", "1");
                    extramd.putString("icox", "0");
                    iz.putExtras(extramd);
                    startActivity(iz);
             	   
                }
            });
            
            // btnOvericdph click event
            Button btnOvericdph = (Button)rootView.findViewById(R.id.btnOvericdph);
            btnOvericdph.setOnClickListener(new View.OnClickListener() {
      
                @Override
                public void onClick(View arg0) {

                	Intent iz = new Intent(getActivity(), OverIcdphActivity.class);
                    Bundle extramd = new Bundle();
                    extramd.putString("cat", "8");
                    iz.putExtras(extramd);
                    startActivity(iz);
             	   
                }
            });
            

            
        	break;
        	
        case 3:
        	rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        	// Set the title view to show the page number.
        	//((TextView) rootView.findViewById(android.R.id.text1)).setText(
            //getString(R.string.title_template_step, mPageNumber + 1));
        	break;
        	
        case 4:
        	rootView = (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        	// Set the title view to show the page number.
        	//((TextView) rootView.findViewById(android.R.id.text1)).setText(
            //getString(R.string.title_template_step, mPageNumber + 1));
        	break;

        	
    		
    		}
    	
        
        return rootView;
           
    }
    //koniec oncreateview
    
   

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }



}
