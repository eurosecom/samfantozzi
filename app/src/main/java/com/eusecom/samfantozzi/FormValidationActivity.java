package com.eusecom.samfantozzi;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class FormValidationActivity extends FragmentActivity {


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(android.R.id.content, new FormValidationMainFragment(), this.toString())
                                       .commit();
        }
    }


}