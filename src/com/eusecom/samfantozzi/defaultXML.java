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
            	
            	//uctosnova 0;uce;nuc;crv;crs;pmd;pda;prm1;prm2;prm3;prm4;ucc
            	//1;2;Nákup materiálu;5;0;0.00;0.00;2;1;0;0;2
            	String fileName2 = "/eusecom/" + adresar + "/uctosnova" + firma + ".xml";
            	File myFile2 = new File(baseDir + File.separator + fileName2);
            	
            	if(!myFile2.exists()){
            		
        			try {
						myFile2.createNewFile();

						FileOutputStream fOut2 = null;
						fOut2 = new FileOutputStream(myFile2, true);
						OutputStreamWriter myOutWriter2 = new OutputStreamWriter(fOut2);
						
							String datatxt1 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
							datatxt1 += "<ucty>";
                    
                    		datatxt1 += "<ucet>";
                    		datatxt1 += "<prz>1</prz>";
                    		datatxt1 += "<uce>21101</uce>";
                    		datatxt1 += "<nuc>Pokladnica</nuc>";
                    		datatxt1 += "<crv>0</crv>";
                    		datatxt1 += "<crs>10</crs>";
                    		datatxt1 += "<pmd>0</pmd>";
                    		datatxt1 += "<pda>0</pda>";
                    		datatxt1 += "<prm1>0</prm1>";
                    		datatxt1 += "<prm2>0</prm2>";
                    		datatxt1 += "<prm3>0</prm3>";
                    		datatxt1 += "<prm4>0</prm4>";
                    		datatxt1 += "<ucc>21100</ucc>";
                    		datatxt1 += "</ucet>";
                    		
                    		//1;2;Nákup materiálu;5;0;0.00;0.00;2;1;0;0;2
                    		datatxt1 += "<ucet>";
                    		datatxt1 += "<prz>1</prz>";
                    		datatxt1 += "<uce>2</uce>";
                    		datatxt1 += "<nuc>Nakup materialu</nuc>";
                    		datatxt1 += "<crv>5</crv>";
                    		datatxt1 += "<crs>0</crs>";
                    		datatxt1 += "<pmd>0</pmd>";
                    		datatxt1 += "<pda>0</pda>";
                    		datatxt1 += "<prm1>2</prm1>";
                    		datatxt1 += "<prm2>1</prm2>";
                    		datatxt1 += "<prm3>0</prm3>";
                    		datatxt1 += "<prm4>0</prm4>";
                    		datatxt1 += "<ucc>2</ucc>";
                    		datatxt1 += "</ucet>";
                    		
                    		//1;30;Tržba tovar;1;0;0.00;0.00;1;1;0;0;30
                    		datatxt1 += "<ucet>";
                    		datatxt1 += "<prz>1</prz>";
                    		datatxt1 += "<uce>30</uce>";
                    		datatxt1 += "<nuc>Trzba tovar</nuc>";
                    		datatxt1 += "<crv>1</crv>";
                    		datatxt1 += "<crs>0</crs>";
                    		datatxt1 += "<pmd>0</pmd>";
                    		datatxt1 += "<pda>0</pda>";
                    		datatxt1 += "<prm1>1</prm1>";
                    		datatxt1 += "<prm2>1</prm2>";
                    		datatxt1 += "<prm3>0</prm3>";
                    		datatxt1 += "<prm4>0</prm4>";
                    		datatxt1 += "<ucc>30</ucc>";
                    		datatxt1 += "</ucet>";
                    		
                    		datatxt1 += "</ucty>";

						myOutWriter2.append(datatxt1);
						myOutWriter2.close();
						fOut2.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}

            	}
            	//koniec myfile2 neexistuje
            	
            	//uctpohyby 0;cpoh;pohp;ucto;druh;uzk0;dzk0;uzk1;dzk1;uzk2;dzk2;udn1;ddn1;udn2;ddn2;hfak;hico;hstr;hzak;id;datm
            	//1;1;tržbu v hotovosti za tovar;9;1;30;51;30;60;30;55;34300;0;34300;0;0;0;0;0;;2011-02-01 13:19:18
            	String fileName3 = "/eusecom/" + adresar + "/autopohyby" + firma + ".xml";
            	File myFile3 = new File(baseDir + File.separator + fileName3);
            	
            	if(!myFile3.exists()){
            		
        			try {
						myFile3.createNewFile();

						FileOutputStream fOut3 = null;
						fOut3 = new FileOutputStream(myFile3, true);
						OutputStreamWriter myOutWriter3 = new OutputStreamWriter(fOut3);
						
							String datatxt1 = "<?xml version = \"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>";
							datatxt1 += "<uctpohyby>";

							//uctpohyby 0;cpoh;pohp;ucto;druh;uzk0;dzk0;uzk1;dzk1;uzk2;dzk2;udn1;ddn1;udn2;ddn2;hfak;hico;hstr;hzak;id;datm
			            	//1;1;tržbu v hotovosti za tovar;9;1;30;51;30;60;30;55;34300;0;34300;0;0;0;0;0;;2011-02-01 13:19:18
							
                    		datatxt1 += "<uctpohyb>";
                    		datatxt1 += "<prz>1</prz>";
                    		datatxt1 += "<cpoh>1</uce>";
                    		datatxt1 += "<pohp>trzbu v hotovosti za tovar</nuc>";
                    		datatxt1 += "<ucto>9</crv>";
                    		datatxt1 += "<druh>1</crs>";
                    		datatxt1 += "<uzk0>30</uzk0>";
                    		datatxt1 += "<dzk0>51</dzk0>";
                    		datatxt1 += "<uzk1>30</uzk1>";
                    		datatxt1 += "<dzk1>60</dzk1>";
                    		datatxt1 += "<uzk2>30</uzk2>";
                    		datatxt1 += "<dzk2>55</dzk2>";
                    		datatxt1 += "<udn1>34300</udn1>";
                    		datatxt1 += "<ddn1>0</ddn1>";
                    		datatxt1 += "<udn2>34300</udn2>";
                    		datatxt1 += "<ddn2>0</ddn2>";
                    		datatxt1 += "<hfak>0</hfak>";
                    		datatxt1 += "<hico>0</hico>";
                    		datatxt1 += "<hstr>21100</hstr>";
                    		datatxt1 += "<hzak>21100</hzak>";
                    		datatxt1 += "</uctpohyb>";
                    		
                    		//1;51;nákup materiálu v hotovosti;9;2;2;1;2;30;2;25;34300;0;34300;0;0;0;0;0;;2011-02-01 13:19:18
                    		datatxt1 += "<uctpohyb>";
                    		datatxt1 += "<prz>1</prz>";
                    		datatxt1 += "<cpoh>51</uce>";
                    		datatxt1 += "<pohp>nakup materialu v hotovosti</nuc>";
                    		datatxt1 += "<ucto>9</crv>";
                    		datatxt1 += "<druh>2</crs>";
                    		datatxt1 += "<uzk0>2</uzk0>";
                    		datatxt1 += "<dzk0>1</dzk0>";
                    		datatxt1 += "<uzk1>2</uzk1>";
                    		datatxt1 += "<dzk1>30</dzk1>";
                    		datatxt1 += "<uzk2>2</uzk2>";
                    		datatxt1 += "<dzk2>25</dzk2>";
                    		datatxt1 += "<udn1>34300</udn1>";
                    		datatxt1 += "<ddn1>0</ddn1>";
                    		datatxt1 += "<udn2>34300</udn2>";
                    		datatxt1 += "<ddn2>0</ddn2>";
                    		datatxt1 += "<hfak>0</hfak>";
                    		datatxt1 += "<hico>0</hico>";
                    		datatxt1 += "<hstr>21100</hstr>";
                    		datatxt1 += "<hzak>21100</hzak>";
                    		datatxt1 += "</uctpohyb>";

                    		datatxt1 += "</uctpohyby>";

						myOutWriter3.append(datatxt1);
						myOutWriter3.close();
						fOut3.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
        			
            		}
            		//koniec myfile3 neexistuje
        			
        			String fileName4 = "/eusecom/" + adresar + "/poklzah" + firma + ".csv";
                	File myFile4 = new File(baseDir + File.separator + fileName4);
                	
                	if(!myFile4.exists()){
                		
            			try {
    						myFile4.createNewFile();

    						FileOutputStream fOut4 = null;
    						fOut4 = new FileOutputStream(myFile4, true);
    						OutputStreamWriter myOutWriter4 = new OutputStreamWriter(fOut4);
    						
    							String datatxt1 = "";
    							

    						myOutWriter4.append(datatxt1);
    						myOutWriter4.close();
    						fOut4.close();
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();

    					}
                	}
            		//koniec myfile4 neexistuje
                	
                	String fileName5 = "/eusecom/" + adresar + "/poklpol" + firma + ".csv";
                	File myFile5 = new File(baseDir + File.separator + fileName5);
                	
                	if(!myFile5.exists()){
                		
            			try {
    						myFile5.createNewFile();

    						FileOutputStream fOut5 = null;
    						fOut5 = new FileOutputStream(myFile5, true);
    						OutputStreamWriter myOutWriter5 = new OutputStreamWriter(fOut5);
    						
    							String datatxt1 = "";
    							

    						myOutWriter5.append(datatxt1);
    						myOutWriter5.close();
    						fOut5.close();
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();

    					}
                	}
            		//koniec myfile5 neexistuje
            	
            	
            	int okset=1;
            	return okset;
            }
                
        }