package by.md.gornak.homework.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import by.md.gornak.homework.model.ApplicationDB;

public class DBService {

    private DBHelper mDbHelper;

    public DBService(Context context){
        mDbHelper = new DBHelper(context);
    }

    public void saveAll(List<ApplicationDB> list) {
        for(ApplicationDB app : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Tables.Columns.PACKAGE, app.getAppPackage());
            contentValues.put(Tables.Columns.FAVOURITE, app.isFavourite());
            contentValues.put(Tables.Columns.FREQUENCY, app.getFrequency());
            try {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.insert(Tables.TABLE_NAME, null, contentValues);
            } catch (SQLiteException e) {

            }
        }
    }

    public List<ApplicationDB> readAll() {
        List<ApplicationDB> res = new ArrayList<>();
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    Tables.TABLE_NAME,
                    null,
                    null,
                    null,
                    //GROUP BY
                    null,
                    //having
                    null,
                    //ORDER BY
                    null
            );

            while (cursor.moveToNext()) {
                res.add(new ApplicationDB(
                        cursor.getString(cursor.getColumnIndex(Tables.Columns.PACKAGE)),
                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.FAVOURITE)) > 0,
                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.FREQUENCY))));
            }
        } catch (SQLiteException e) {}
        return res;
    }
}
