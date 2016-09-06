package com.eusecom.samfantozzi;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.DatePicker;
import android.support.v4.app.FragmentActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
 
import java.util.Calendar;


@SuppressLint("ValidFragment")
public class NastavDatum extends FragmentActivity {

	private static final String TAG_DATX = "datx";
	private static final String TAG_ODKADE = "odkade";
	EditText mEdit;
	String datumx;
	String odkade;
	
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.nastavdatum);

Intent i = getIntent();

Bundle extras = i.getExtras();
datumx = extras.getString(TAG_DATX);
//odkade 1=uprava poklzah, 2=newpoklzah, 11-faknewdat
odkade = extras.getString(TAG_ODKADE);

mEdit = (EditText)findViewById(R.id.editText1);
mEdit.setText(datumx);
mEdit.setVisibility(View.GONE);

if( odkade.equals("11")) { this.setTitle(getResources().getString(R.string.popisDat));}
if( odkade.equals("12")) { this.setTitle(getResources().getString(R.string.popisDaz));}
if( odkade.equals("13")) { this.setTitle(getResources().getString(R.string.popisDas));}
if( odkade.equals("14")) { this.setTitle(getResources().getString(R.string.popisDat));}
if( odkade.equals("15")) { this.setTitle(getResources().getString(R.string.popisDaz));}
if( odkade.equals("16")) { this.setTitle(getResources().getString(R.string.popisDas));}

DialogFragment newFragment = new SelectDateFragment();
newFragment.show(getSupportFragmentManager(), "DatePicker");

}
//koniec oncreate 
 
public void selectDate(View view) {
DialogFragment newFragment = new SelectDateFragment();
newFragment.show(getSupportFragmentManager(), "DatePicker");
}

public void populateSetDate(int year, int month, int day) {
mEdit = (EditText)findViewById(R.id.editText1);
mEdit.setText(day+"."+month+"."+year);

String datumsetx = day+"."+month+"."+year;
Intent i = getIntent();
i.putExtra("datumset", datumsetx);

if( odkade.equals("1")) { setResult(101, i); }
if( odkade.equals("2")) { setResult(102, i); }
if( odkade.equals("11")) { setResult(111, i); }
if( odkade.equals("12")) { setResult(112, i); }
if( odkade.equals("13")) { setResult(113, i); }
if( odkade.equals("14")) { setResult(114, i); }
if( odkade.equals("15")) { setResult(115, i); }
if( odkade.equals("16")) { setResult(116, i); }

finish();
}

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	final Calendar calendar = Calendar.getInstance();
	int yy = calendar.get(Calendar.YEAR);
	int mm = calendar.get(Calendar.MONTH);
	int dd = calendar.get(Calendar.DAY_OF_MONTH);
	//dd = 38;
	
	String delims = "[.]+";
	String[] datumxxx = datumx.split(delims);
	
	String ddx = datumxxx[0];
	String mmx = datumxxx[1];
	String yyx = datumxxx[2];
	
	int ddi = Integer.parseInt(ddx);
	int mmi = Integer.parseInt(mmx);
	int yyi = Integer.parseInt(yyx);
	dd=ddi; mm=mmi-1; yy=yyi;
	
	DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, yy, mm, dd);
	//takto zmenim text buttonu
	//dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Button Plus Text", dpd);
	dpd.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.kontnic), new DialogInterface.OnClickListener() 
	{
	    public void onClick(DialogInterface dialog, int which) {
	        if (which == DialogInterface.BUTTON_NEGATIVE) {
	         finish();   
	        }
	    }
	    
	});
	//koniec setbutton
	
	//zrusim tlacitko BACK aby mi to nezostalo v okne bez dialogu po stlaceni BACK
	dpd.setOnKeyListener(new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && 
                event.getAction() == KeyEvent.ACTION_UP && 
                !event.isCanceled()) {

                return true;
            }
            return false;
        }
    });
	//koniec zrusim tlacitko BACK
	
	return dpd;
	}
 
	public void onDateSet(DatePicker view, int yy, int mm, int dd) {
		populateSetDate(yy, mm+1, dd);
	}
	

	
}
//koniec selectdatefragment

}
//koniec activity