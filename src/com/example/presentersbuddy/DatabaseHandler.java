package com.example.presentersbuddy;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "sectionManager";

	// Section table name
	private static final String TABLE_SECTION = "section";

	// Section Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_TITLE = "title";
	private static final String KEY_TIME = "time";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SECTION_TABLE = "CREATE TABLE " + TABLE_SECTION + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TITLE + " TEXT,"
				+ KEY_TIME + " TEXT" + ")";
		db.execSQL(CREATE_SECTION_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECTION);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new section
	void addSection(Section section) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, section.getTitle()); // Section Title
		values.put(KEY_TIME, section.getTime()); // Section Time

		// Inserting Row
		db.insert(TABLE_SECTION, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Section getSection(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SECTION, new String[] { KEY_ID,
				KEY_TITLE, KEY_TIME }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Section section = new Section(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return section
		return section;
	}
	
	// Getting All Section
	public List<Section> getAllSections() {
		List<Section> sectionList = new ArrayList<Section>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SECTION;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Section section = new Section();
				section.setID(Integer.parseInt(cursor.getString(0)));
				section.setTitle(cursor.getString(1));
				section.setTime(cursor.getString(2));
				// Adding section to list
				sectionList.add(section);
			} while (cursor.moveToNext());
		}

		// return section list
		return sectionList;
	}

	// Updating single contact
	public int updateSection(Section section) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE,section.getTitle());
		values.put(KEY_TIME, section.getTime());

		// updating row
		return db.update(TABLE_SECTION, values, KEY_ID + " = ?",
				new String[] { String.valueOf(section.getID()) });
	}

	// Deleting single section
	public void deleteSection(Section section) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SECTION, KEY_ID + " = ?",
				new String[] { String.valueOf(section.getID()) });
		db.close();
	}


	// Getting section Count
	public int getSectionCount() {
		int count=0;
		String countQuery = "SELECT  * FROM " + TABLE_SECTION;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		count=cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

}
