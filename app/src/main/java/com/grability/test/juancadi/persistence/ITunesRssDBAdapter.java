
package com.grability.test.juancadi.persistence;

//class-name:     	ITunesRssDBAdapter
//class-overview: 	Implementa la logica necesaria para almacenar la informacion obtenida desde
//                  el RSS en una Base de datos SQLite.
//class-autor:    	Juancadi
//class-date:     	2015-11-13

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.grability.test.juancadi.model.ITunesEntry;

import java.util.ArrayList;


public class ITunesRssDBAdapter {

	private static final String DATABASE_NAME = "itunes_rss.db";
	private static final String DATABASE_TABLE = "itunes_entries";
	private static final int DATABASE_VERSION = 1;

	public static final String KEY_ENTRY_ID = "entry_id";
	public static final String KEY_APP_ID = "app_id";
	public static final String KEY_APP_NAME = "app_name";
	public static final String KEY_APP_IMG_LINK = "app_img_link";
    public static final String KEY_APP_IMG_B64 = "app_img_b64";
	public static final String KEY_APP_SUMMARY = "app_summary";
	public static final String KEY_APP_PRICE = "app_price";
	public static final String KEY_APP_RIGHTS = "app_rights";
    public static final String KEY_APP_ARTIST = "app_artist";
    public static final String KEY_APP_DOWNLOAD_LINK = "app_download_ink";
    public static final String KEY_APP_CATEGORY = "app_category";
    public static final String KEY_APP_RELEASE_DATE = "app_release_date";

	

	private SQLiteDatabase db;
	private final Context context;
	private ContactsDBOpenHelper dbHelper;

	public ITunesRssDBAdapter(Context _context) {
		this.context = _context;
		dbHelper = new ContactsDBOpenHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	private static class ContactsDBOpenHelper extends SQLiteOpenHelper {

		public ContactsDBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {

			super(context, name, factory, version);
		}

		// SQL Statement to create a new database.
		private static final String SQL_DATABASE_CREATE = "create table "
				+ DATABASE_TABLE + " (" +
                KEY_APP_ID + " text primary key, " +
                KEY_ENTRY_ID + " integer, " +
                KEY_APP_NAME + " text, " +
                KEY_APP_IMG_LINK + " text, " +
                KEY_APP_IMG_B64 + " text, " +
                KEY_APP_SUMMARY + " text, " +
                KEY_APP_PRICE + " text, " +
                KEY_APP_RIGHTS + " text, " +
                KEY_APP_ARTIST + " text, " +
                KEY_APP_DOWNLOAD_LINK + " text, " +
                KEY_APP_CATEGORY + " text, " +
                KEY_APP_RELEASE_DATE + " text);";


		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(SQL_DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {

			// Drop the old table.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			// Create a new one.
			onCreate(_db);
		}
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	// Insert a new entry
	public long insertEntry(ITunesEntry newEntry) {

		long resultInsertEntry;

		// Create a new row of values to insert.
		ContentValues newEntryValues = new ContentValues();

		// Assign values for each row.
        newEntryValues.put(KEY_ENTRY_ID, newEntry.getIdEntry());
        newEntryValues.put(KEY_APP_ID, newEntry.getIdApp());
        newEntryValues.put(KEY_APP_NAME, newEntry.getName());
        newEntryValues.put(KEY_APP_IMG_LINK, newEntry.getImageLink());
        newEntryValues.put(KEY_APP_IMG_B64, newEntry.getImageB64());
        newEntryValues.put(KEY_APP_SUMMARY, newEntry.getSummary());
        newEntryValues.put(KEY_APP_PRICE, newEntry.getPrice());
        newEntryValues.put(KEY_APP_RIGHTS, newEntry.getRights());
        newEntryValues.put(KEY_APP_ARTIST, newEntry.getArtist());
        newEntryValues.put(KEY_APP_DOWNLOAD_LINK, newEntry.getAppDownloadLink());
        newEntryValues.put(KEY_APP_CATEGORY, newEntry.getCategory());
        newEntryValues.put(KEY_APP_RELEASE_DATE, newEntry.getReleaseDate());

		// Insert the row.
        resultInsertEntry = db.insert(DATABASE_TABLE, null, newEntryValues);

		return resultInsertEntry;
	}

	// Remove a entry based id iTunes App
	public boolean removeEntry(String iTunesIdApp) {

		return db.delete(DATABASE_TABLE, KEY_APP_ID + "= '" + iTunesIdApp + "'", null) > 0;
	}

	public void deleteAll() {

		// Delete all rows
		db.execSQL("DELETE FROM " + DATABASE_TABLE);
	}

	// Update a Entry
	public boolean updateEntry(ITunesEntry iTunesEntry) {

		ContentValues updateEntryValues = new ContentValues();

        updateEntryValues.put(KEY_ENTRY_ID, iTunesEntry.getIdEntry());
        updateEntryValues.put(KEY_APP_ID, iTunesEntry.getIdApp());
        updateEntryValues.put(KEY_APP_NAME, iTunesEntry.getName());
        updateEntryValues.put(KEY_APP_IMG_LINK, iTunesEntry.getImageLink());
        updateEntryValues.put(KEY_APP_IMG_B64, iTunesEntry.getImageB64());
        updateEntryValues.put(KEY_APP_SUMMARY, iTunesEntry.getSummary());
        updateEntryValues.put(KEY_APP_PRICE, iTunesEntry.getPrice());
        updateEntryValues.put(KEY_APP_RIGHTS, iTunesEntry.getRights());
        updateEntryValues.put(KEY_APP_ARTIST, iTunesEntry.getArtist());
        updateEntryValues.put(KEY_APP_DOWNLOAD_LINK, iTunesEntry.getAppDownloadLink());
        updateEntryValues.put(KEY_APP_CATEGORY, iTunesEntry.getCategory());
        updateEntryValues.put(KEY_APP_RELEASE_DATE, iTunesEntry.getReleaseDate());
		
		return db.update(DATABASE_TABLE, updateEntryValues, KEY_APP_ID + "='"
				+ iTunesEntry.getIdApp() + "'", null) > 0;
	}

    // Return all the iTunes entries in the DB
    public ArrayList<ITunesEntry> getAllEntries() {


        ArrayList<ITunesEntry> iTunesDbEntries = new ArrayList<ITunesEntry>();

        Cursor cursorEntries = db.query(DATABASE_TABLE,
                new String[]{
                        KEY_ENTRY_ID,
                        KEY_APP_ID,
                        KEY_APP_NAME,
                        KEY_APP_IMG_LINK,
                        KEY_APP_IMG_B64,
                        KEY_APP_SUMMARY,
                        KEY_APP_PRICE,
                        KEY_APP_RIGHTS,
                        KEY_APP_ARTIST,
                        KEY_APP_DOWNLOAD_LINK,
                        KEY_APP_CATEGORY,
                        KEY_APP_RELEASE_DATE
                },
                null, null, null, null, null);

        if (cursorEntries.moveToFirst()) {

            for (int i = 0; i < cursorEntries.getCount(); i++) {

                iTunesDbEntries.add(new ITunesEntry(
                        cursorEntries.getInt(cursorEntries
                                .getColumnIndex(KEY_ENTRY_ID)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_ID)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_NAME)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_IMG_LINK)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_IMG_B64)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_SUMMARY)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_PRICE)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_RIGHTS)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_ARTIST)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_DOWNLOAD_LINK)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_CATEGORY)),
                        cursorEntries.getString(cursorEntries
                                .getColumnIndex(KEY_APP_RELEASE_DATE))));

                cursorEntries.moveToNext();
            }


        }

        return iTunesDbEntries;

    }


    // Get a particular entry according with the idEntry
    public ITunesEntry getEntry(int idAppEntry) throws SQLException {

        ITunesEntry iTunesEntry;

        Cursor cursor = db.query(true, DATABASE_TABLE,
                new String[]{
                        KEY_ENTRY_ID,
                        KEY_APP_ID,
                        KEY_APP_NAME,
                        KEY_APP_IMG_LINK,
                        KEY_APP_IMG_B64,
                        KEY_APP_SUMMARY,
                        KEY_APP_PRICE,
                        KEY_APP_RIGHTS,
                        KEY_APP_ARTIST,
                        KEY_APP_DOWNLOAD_LINK,
                        KEY_APP_CATEGORY,
                        KEY_APP_RELEASE_DATE
                },
                KEY_ENTRY_ID + "=" + idAppEntry,
                null, null, null, null, null);

        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            iTunesEntry = null;

        } else {

            iTunesEntry = new ITunesEntry(
                    cursor.getInt(cursor
                            .getColumnIndex(KEY_ENTRY_ID)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_ID)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_NAME)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_IMG_LINK)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_IMG_B64)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_SUMMARY)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_PRICE)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_RIGHTS)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_ARTIST)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_DOWNLOAD_LINK)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_CATEGORY)),
                    cursor.getString(cursor
                            .getColumnIndex(KEY_APP_RELEASE_DATE)));
        }

        return iTunesEntry;
    }


}