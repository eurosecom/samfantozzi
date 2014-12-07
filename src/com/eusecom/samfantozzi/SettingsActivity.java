package com.eusecom.samfantozzi;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;



@SuppressWarnings("deprecation")
public class SettingsActivity extends android.preference.PreferenceActivity {
	

	public static final String SERVER_NAME = "servername";	
	public static final String USER_ID = "userid";
	public static final String USER_PSW = "userpsw";
	public static final String USER_NAME = "username";
	public static final String NICK_NAME = "nickname";
	public static final String MOJ_MAIL = "mojmail";
	public static final String DRUH_ID = "druhid";
	public static final String DOKLAD = "doklad";
	public static final String ICO = "cisloico";
	public static final String ODBM = "cisloodbm";
	public static final String UME = "ume";
	public static final String FIR = "fir";
	public static final String FIRNAZ = "firnaz";
	public static final String FIRDUCT = "firduct";
	public static final String FIRDPH = "firdph";
	public static final String FIRDPH1 = "firdph1";
	public static final String FIRROK = "firrok";
	public static final String FIRDPH2 = "firdph2";
	public static final String SDKARTA = "sdkarta";
	public static final String POKLUCE = "pokluce";
	public static final String POKLDOK = "pokldok";
	public static final String POKLDOV = "pokldov";
	public static final String BANKUCE = "bankuce";
	public static final String BANKDOK = "bankdok";
	public static final String DODUCE = "doduce";
	public static final String DODDOK = "doddok";
	public static final String ODBUCE = "odbuce";
	public static final String ODBDOK = "odbdok";
	public static final String MYDEMO = "mydemo";
	
	protected static final String[] ORDER_BY_COLUMNS = {
		"pid",
		"name",
		"price"
	};
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		addPreferencesFromResource(R.xml.settings);
	}
	
	public static String getSDkarta(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(SDKARTA, "0");
	}
	
	public static String getServerName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(SERVER_NAME, "");
	}

	public static String getNickName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(NICK_NAME, "");
	}

	public static String getMojMail(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(MOJ_MAIL, "");
	}
	
	public static String getUserId(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_ID, "0");
	}

	public static String getDruhId(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DRUH_ID, "0");
	}
	
	public static String getUme(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(UME, "0");
	}
	
	public static String getFir(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIR, "0");
	}
	
	public static String getFirnaz(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRNAZ, "");
	}
	
	public static String getFirduct(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDUCT, "0");
	}
	
	public static String getFirdph(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDPH, "0");
	}
	
	public static String getFirdph1(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDPH1, "10");
	}
	
	public static String getFirrok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRROK, "0");
	}
	
	public static String getFirdph2(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(FIRDPH2, "20");
	}

	public static String getDoklad(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DOKLAD, "0");
	}
	
	public static String getCisloico(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ICO, "0");
	}

	public static String getCisloodbm(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ODBM, "0");
	}
	
	public static String getPokldov(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(POKLDOV, "0");
	}
	
	public static String getPokldok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(POKLDOK, "0");
	}
	
	public static String getPokluce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(POKLUCE, "0");
	}
	
	public static String getBankdok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(BANKDOK, "0");
	}
	
	public static String getBankuce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(BANKUCE, "0");
	}
	
	public static String getOdbdok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ODBDOK, "0");
	}
	
	public static String getOdbuce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ODBUCE, "0");
	}
	
	public static String getDoddok(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DODDOK, "0");
	}
	
	public static String getDoduce(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(DODUCE, "0");
	}
	
	public static String getUserName(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_NAME, "");
	}
	
	public static String getUserPsw(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(USER_PSW, "");
	}
	
	public static String getMydemo(Context ctx){
		return PreferenceManager.getDefaultSharedPreferences(ctx).getString(MYDEMO, "0");
	}
	
} 