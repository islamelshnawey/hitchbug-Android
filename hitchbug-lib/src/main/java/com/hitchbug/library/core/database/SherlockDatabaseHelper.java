package com.hitchbug.library.core.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hitchbug.library.core.investigation.Crash;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SherlockDatabaseHelper extends SQLiteOpenHelper {
  private static final int VERSION = 2;
  private static final String DB_NAME = "Sherlock";

  public SherlockDatabaseHelper(Context context) {
    super(context, DB_NAME, null, VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(CrashTable.CREATE_QUERY);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL(CrashTable.DROP_QUERY);
    sqLiteDatabase.execSQL(CrashTable.CREATE_QUERY);
  }

  public int insertCrash(CrashRecord crashRecord) {
    ContentValues values = new ContentValues();
    values.put(CrashTable.PLACE, crashRecord.getPlace());
    values.put(CrashTable.REASON, crashRecord.getReason());
    values.put(CrashTable.STACKTRACE, crashRecord.getStackTrace());
    values.put(CrashTable.DATE, crashRecord.getDate());

    SQLiteDatabase database = getWritableDatabase();
    long id = database.insert(CrashTable.TABLE_NAME, null, values);
    database.close();

    return Long.valueOf(id).intValue();
  }

  public List<Crash> getCrashes() {
    SQLiteDatabase readableDatabase = getReadableDatabase();
    ArrayList<Crash> crashes = new ArrayList<>();
    Cursor cursor = readableDatabase.rawQuery(CrashTable.SELECT_ALL, null);
    if (isCursorPopulated(cursor)) {
      do {
        crashes.add(toCrash(cursor));
      } while (cursor.moveToNext());
    }

    cursor.close();
    readableDatabase.close();

    return crashes;
  }

  public JSONArray getCrashesAsJson() {

    SQLiteDatabase readableDatabase = getReadableDatabase();
    Cursor cursor = readableDatabase.rawQuery(CrashTable.SELECT_ALL, null);

    JSONArray resultSet  = new JSONArray();

    cursor.moveToFirst();
    while (cursor.isAfterLast() == false) {

      int totalColumn = cursor.getColumnCount();
      JSONObject rowObject = new JSONObject();

      for( int i=0 ;  i< totalColumn ; i++ )
      {
        if( cursor.getColumnName(i) != null )
        {
          try
          {
            if( cursor.getString(i) != null )
            {
              //Log.d("TAG_NAME", cursor.getString(i) );
              rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
            }
            else
            {
              rowObject.put( cursor.getColumnName(i) ,  "" );
            }
          }
          catch( Exception e )
          {
            Log.d("TAG_NAME", e.getMessage()  );
          }
        }
      }
      resultSet.put(rowObject);
      cursor.moveToNext();
    }

    cursor.close();
    //Log.d("TAG_NAME", resultSet.toString() );
    return resultSet;
  }

  public Crash getCrashById(int id) {
    SQLiteDatabase readableDatabase = getReadableDatabase();
    Cursor cursor = readableDatabase.rawQuery(CrashTable.selectById(id), null);
    Crash crash = null;

    if (isCursorPopulated(cursor)) {
      crash = toCrash(cursor);
      cursor.close();
      readableDatabase.close();
    }

    return crash;
  }

  /**
   * Clear one table data
   *
   */
  public void deleteFromTable() {
    SQLiteDatabase db = getWritableDatabase();
    db.execSQL(CrashTable.TRUNCATE_QUERY);
    db.close();

  }

  @NonNull
  private Crash toCrash(Cursor cursor) {
    int id = cursor.getInt(cursor.getColumnIndex(CrashTable._ID));
    String placeOfCrash = cursor.getString(cursor.getColumnIndex(CrashTable.PLACE));
    String reasonOfCrash = cursor.getString(cursor.getColumnIndex(CrashTable.REASON));
    String stacktrace = cursor.getString(cursor.getColumnIndex(CrashTable.STACKTRACE));
    String date = cursor.getString(cursor.getColumnIndex(CrashTable.DATE));
    return new Crash(id, placeOfCrash, reasonOfCrash, stacktrace, date);
  }

  private boolean isCursorPopulated(Cursor cursor) {
    return cursor != null && cursor.moveToFirst();
  }
}
