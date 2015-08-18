package com.minisoftwareandgames.ryan.etherticker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.minisoftwareandgames.ryan.etherticker.objects.Widget;

/**
 * Created by ryan on 8/14/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	/* TODO: fix DB leak errors */

	private Context context;
	public String DEFAULT_EXCHANGE = "Gatecoin";
	public String DEFAULT_CURRENCY = "BTC";

	public static final String TABLE_WIDGETS           	= "widgets";
	public static final String COLUMN_ID 				= "_id";
	public static final String COLUMN_EXCHANGE			= "exchange";
	public static final String COLUMN_CURRENCY			= "currency";
	SQLiteDatabase tickerDB;

	private static final String DB_WIDGETS_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_WIDGETS + "("
			+ COLUMN_ID + " PRIMARY KEY, "
			+ COLUMN_EXCHANGE + ", "
			+ COLUMN_CURRENCY + ")";

	private static final String DATABASE_NAME = "etherticker.db";
	private static final int DATABASE_VERSION = 1;

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		tickerDB = database;
		tickerDB.execSQL(DB_WIDGETS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* TODO: add data migration */
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIDGETS);
		onCreate(db);
	}

	public boolean addWidget(Widget widget) {
		Log.d("addWidget", "start");
		tickerDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_ID, widget.id);
		cv.put(COLUMN_EXCHANGE, widget.exchange);
		cv.put(COLUMN_CURRENCY, widget.currency);
		long val = tickerDB.insert(TABLE_WIDGETS, null, cv);
		return (val > 0);	// implies success
	}

	public Widget getWidget(int wId) {
		Log.d("getWidget", "start");
		tickerDB = getReadableDatabase();
		String statement = "SELECT * FROM " + TABLE_WIDGETS + " WHERE " + COLUMN_ID + " = " + wId + "";
//		Log.d("etherticker", statement);
		Cursor cursor = tickerDB.rawQuery(statement, null);
		if (cursor.moveToFirst()) {	/* there should only be one: ids are unique */
			int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
			String exchange = cursor.getString(cursor.getColumnIndex(COLUMN_EXCHANGE));
			String currency = cursor.getString(cursor.getColumnIndex(COLUMN_CURRENCY));
//			Log.d("etherticker", id + " " + exchange + " " + currency);
			cursor.close();
			return new Widget(id, exchange, currency);
		}
		cursor.close();
		/* if there is not a widget with matching id, create one */
		Widget widget = new Widget(wId, DEFAULT_EXCHANGE, DEFAULT_CURRENCY);
		if (addWidget(widget)) {	/* and add it */
			return widget;	/* if successfully in DB, return it */
		} else return null;	/* otherwise, we failed and return null */
	}

	public boolean hasWidgets() {
		tickerDB = getReadableDatabase();
		Cursor cursor = tickerDB.rawQuery("SELECT * FROM " + TABLE_WIDGETS, null);
		boolean val = cursor.moveToFirst();	// if there is one, it will be true
		cursor.close();
		return val;
	}

	public boolean updateWidget(Widget widget) {
		tickerDB = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_EXCHANGE, widget.exchange);
		cv.put(COLUMN_CURRENCY, widget.currency);
		int val = tickerDB.update(TABLE_WIDGETS, cv, COLUMN_ID + " = " + widget.id, null);
		return (val > 0);	// implies success
	}

	private void printDB() {
		tickerDB = getReadableDatabase();
		Cursor cursor = tickerDB.rawQuery("SELECT * FROM " + TABLE_WIDGETS, null);
		Log.d("etherticker", "-id- | -exchange- | -currency-");
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
				String exchange = cursor.getString(cursor.getColumnIndex(COLUMN_EXCHANGE));
				String currency = cursor.getString(cursor.getColumnIndex(COLUMN_CURRENCY));
				Log.d("etherticker", id + " " + exchange + " " + currency);
			} while (cursor.moveToNext());
		}
		cursor.close();
	}

}
