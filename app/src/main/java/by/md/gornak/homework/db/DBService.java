package by.md.gornak.homework.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import by.md.gornak.homework.model.ApplicationDB;

public class DBService {

    private DBHelper mDbHelper;

    public DBService(Context context) {
        mDbHelper = new DBHelper(context);
    }

    public void saveAll(Collection<ApplicationDB> list) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        for (ApplicationDB app : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Tables.Columns.PACKAGE, app.getAppPackage());
            contentValues.put(Tables.Columns.FAVOURITE, app.isFavourite());
            contentValues.put(Tables.Columns.FREQUENCY, app.getFrequency());
            contentValues.put(Tables.Columns.DESKTOP, app.isDesktop());
            contentValues.put(Tables.Columns.POSITION, app.getPosition());
            try {
                int id = (int) db.insertWithOnConflict(Tables.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                if (id == -1) {
                    db.update(Tables.TABLE_NAME,
                            contentValues,
                            Tables.Columns.PACKAGE + " = ?",
                            new String[]{app.getAppPackage()});
                }
            } catch (SQLiteException e) {

            }
        }
    }

    public void remove(String packageName) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(Tables.TABLE_NAME,
                Tables.Columns.PACKAGE + " = \"" + packageName + "\"",
                null);
    }

    public Map<String, ApplicationDB> readAll() {
        Map<String, ApplicationDB> res = new HashMap<>();
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    Tables.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

//            while (cursor.moveToNext()) {
//                ApplicationDB app = new ApplicationDB(
//                        cursor.getString(cursor.getColumnIndex(Tables.Columns.PACKAGE)),
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.FAVOURITE)) > 0,
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.FREQUENCY)),
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.DESKTOP)) > 0,
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.POSITION)));
//                res.put(app.getAppPackage(), app);
//            }
            cursor.close();
        } catch (SQLiteException e) {
        }
        return res;
    }

    public Map<String, ApplicationDB> readDesktop() {
        Map<String, ApplicationDB> res = new HashMap<>();
        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String whereClause = Tables.Columns.DESKTOP + "=?";
            String[] whereArgs = new String[]{"true"};

            Cursor cursor = db.query(
                    Tables.TABLE_NAME,
                    null,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );

//            while (cursor.moveToNext()) {
//                ApplicationDB app = new ApplicationDB(
//                        cursor.getString(cursor.getColumnIndex(Tables.Columns.PACKAGE)),
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.FAVOURITE)) > 0,
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.FREQUENCY)),
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.DESKTOP)) > 0,
//                        cursor.getInt(cursor.getColumnIndex(Tables.Columns.POSITION)));
//                res.put(app.getAppPackage(), app);
//            }
            cursor.close();
        } catch (SQLiteException e) {
        }
        return res;
    }
}
