package by.md.gornak.homework.db;


import android.provider.BaseColumns;

public class Tables {

    public Tables() {
        //
    }

    static final String TABLE_NAME = "Application";

    interface Columns extends BaseColumns {
        String PACKAGE = "package";
        String FAVOURITE = "favourite";
        String FREQUENCY = "frequency";
        String DESKTOP = "desktop";
        String POSITION = "position";
    }

    static final String CREATE_TABLE_SCRIPT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(" +
                    Columns.PACKAGE + " TEXT, " +
                    Columns.FAVOURITE + " BLOB, " +
                    Columns.FREQUENCY + " REAL, " +
                    Columns.DESKTOP + " BLOB, " +
                    Columns.POSITION + " REAL" +
                    ")";

    static final String DROP_TABLE_SCRIPT =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
