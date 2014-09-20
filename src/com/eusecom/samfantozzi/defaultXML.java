	package com.eusecom.samfantozzi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.os.Environment;


        public class defaultXML {

            public defaultXML()
            {

            }
            
            public static int createdefaultXML(int xxx, String adresar, String firma)
            {
            	
 
            	File folder = new File(Environment.getExternalStorageDirectory().toString()+"/eusecom/" + adresar);
            	folder.mkdirs();

            	String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();  
            	String fileName1 = "/eusecom/" + adresar + "/ico" + firma + ".xml";
            	File myFile1 = new File(baseDir + File.separator + fileName1);
            	
            	if(!myFile1.exists()){
            		
        			try {
						myFile1.createNewFile();

						FileOutputStream fOut1 = null;
						fOut1 = new FileOutputStream(myFile1, true);
						OutputStreamWriter myOutWriter1 = new OutputStreamWriter(fOut1);
						
							String datatxt1 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
							datatxt1 += "<customers>";
                    
                    		datatxt1 += "<customer>";
                    		datatxt1 += "<ico>44551142</ico>";
                    		datatxt1 += "<dic>2022753887</dic>";
                    		datatxt1 += "<icd>SK2022753887</icd>";
                    		datatxt1 += "<nai>EDcom s.r.o.</nai>";
                    		datatxt1 += "<uli>Sotinska 1474/11</uli>";
                    		datatxt1 += "<psc>90501</psc>";
                    		datatxt1 += "<mes>Senica</mes>";
                    		datatxt1 += "<tel>0905/804265</tel>";
                    		datatxt1 += "<mail>edcom@edcom.sk</mail>";
                    		datatxt1 += "<www>www.edcom.sk</www>";
                    		datatxt1 += "</customer>";
                    		datatxt1 += "</customers>";
                    


						myOutWriter1.append(datatxt1);
						myOutWriter1.close();
						fOut1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

            	
            	}
            	//koniec myfile1 neexistuje
            	
            	int okset=1;
            	return okset;
            }
                
        }