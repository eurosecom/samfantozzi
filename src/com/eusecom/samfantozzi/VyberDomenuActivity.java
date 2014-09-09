/* Copyright (c) 2008-2018 -- EuroSecom

	 Licensed under the Apache License, Version 2.0 (the "License");
	 you may not use this file except in compliance with the License.
	 You may obtain a copy of the License at

		 http://www.apache.org/licenses/LICENSE-2.0

	 Unless required by applicable law or agreed to in writing, software
	 distributed under the License is distributed on an "AS IS" BASIS,
	 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 See the License for the specific language governing permissions and
	 limitations under the License.
*/
	 
package com.eusecom.samfantozzi;


import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VyberDomenuActivity extends ListActivity {

	private SQLiteDatabase db2=null;
	private Cursor constantsCursor2=null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db2=(new DatabaseDomeny(this)).getWritableDatabase();

	
		vylistuj();
		
		ListView lv = getListView();
		 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting values from selected ListItem
            	String serverxyz = ((TextView) view.findViewById(R.id.title)).getText().toString();
            	String nickxyz = ((TextView) view.findViewById(R.id.value)).getText().toString();
            	String namexyz = ((TextView) view.findViewById(R.id.namex2)).getText().toString();
            	String pswdxyz = ((TextView) view.findViewById(R.id.pswdx2)).getText().toString();
            	String mailxyz = ((TextView) view.findViewById(R.id.mailx2)).getText().toString();
            	String uzidxyz = ((TextView) view.findViewById(R.id.uzidx2)).getText().toString();
            	
            	//toto uklada preference
             	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
             	Editor editor = prefs.edit();


             	editor.putString("servername", serverxyz).apply();
             	editor.putString("nickname", nickxyz).apply();
                editor.putString("userpsw", pswdxyz).apply();
             	editor.putString("username", namexyz).apply();
             	editor.putString("userid", uzidxyz).apply();
             	editor.putString("mojmail", mailxyz).apply();

             	
             	editor.commit();

            	Intent i = new Intent(getApplicationContext(), Pripojv2Activity.class);
	            startActivity(i);
                finish();
 
            }
        });

	}
	//koniec oncreate
	
	private void vylistuj() {
		constantsCursor2=db2.rawQuery("SELECT _ID, server2, pswd2, name2, nick2, mail2, uzid2 "+
				"FROM mojedomeny WHERE _id > 0 ORDER BY server2",
				null);

		//toto bezi len v api11 ak dam prec  to "}, 0" tak je to deprecated
		ListAdapter adapter=new SimpleCursorAdapter(this,
				R.layout.rowdomeny, constantsCursor2,
				new String[] {"server2", "nick2", "name2", "pswd2", "mail2", "uzid2"},
				new int[] {R.id.title, R.id.value, R.id.namex2, R.id.pswdx2, R.id.mailx2, R.id.uzidx2}, 0);

		setListAdapter(adapter);
	}
	//koniec vylistuj
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		constantsCursor2.close();
		db2.close();
	}
	//koniec ondestroy



	







}