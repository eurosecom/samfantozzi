/* Copyright (c) 2008-2009 -- CommonsWare, LLC

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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.SensorManager;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME="db";
	public static final String TITLE="title";
	public static final String TITLEICO="titleico";
	public static final String TITLEODBM="titleodbm";
	public static final String VALUE="value";
	public static final String TITLEUCE="titleuce";
	public static final String VALUEUCE="valueuce";
	public static final String TITLERDP="titlerdp";
	public static final String VALUERDP="valuerdp";
	public static final String TITLEPOH="titlepoh";
	public static final String VALUEPOH="valuepoh";
	
	public DatabaseHelper(Context context) {
		//ta 3ka je verzia databaze, nesmiem dat nizsiu ak zvysim vymaze tabulku a znovu ju vytvori
		super(context, DATABASE_NAME, null, 9);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE constants (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, titleico TEXT, titleodbm TEXT," +
				" valueuce TEXT,  titleuce TEXT,  valuerdp TEXT,  titlerdp TEXT,  valuepoh TEXT,  titlepoh TEXT, value REAL);");
		
		ContentValues cv=new ContentValues();
		
		cv.put(TITLE, "Gravity, Death Star I upgrade4");
		cv.put(TITLEICO, "nazov ico");
		cv.put(TITLEODBM, "nazov odbm");
		cv.put(VALUEUCE, "hodnota uctu");
		cv.put(TITLEUCE, "nazov uctu");
		cv.put(VALUERDP, "hodnota rdp");
		cv.put(TITLERDP, "nazov rdp");
		cv.put(VALUEPOH, "hodnota poh");
		cv.put(TITLEPOH, "nazov poh");
		cv.put(VALUE, SensorManager.GRAVITY_DEATH_STAR_I);
		db.insert("constants", TITLE, cv);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.w("Constants", "Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS constants");
		onCreate(db);
	}
}