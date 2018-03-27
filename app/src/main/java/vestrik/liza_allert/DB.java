/*package vestrik.liza_allert;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    public static final String TABLE_INFO = "info";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_REGION = "region";
    public static final String COLUMN_DATA = "data";
    private static final String DB_CREATE = "create table " + TABLE_INFO + "(" + COLUMN_NUMBER + " text, "
            + COLUMN_NAME + " text, " + COLUMN_SURNAME + " text," + COLUMN_REGION + " text," + COLUMN_DATA + " text"
            + ");";

    String number;
    String name;
    String surname;
    String region;
    String data;



    private DBHelper mDBHelper;
    private SQLiteDatabase db;



    public void open() {

        db = mDBHelper.getWritableDatabase();
    }


    public void close() {
        if (mDBHelper != null) mDBHelper.close();
    }



    public void addRecINFO()
    {
        //open();
        String INFO= infoActivity.getData();
        String mes_mas[] = null;
        mes_mas = INFO.split(";");
        number=mes_mas[0];
        name=mes_mas[1];
        surname=mes_mas[2];
        region=mes_mas[3];
        data=mes_mas[4];
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER,number);
        values.put(COLUMN_NAME,name);
        values.put(COLUMN_SURNAME,surname);
        values.put(COLUMN_REGION,region);
        values.put(COLUMN_DATA,data);
        db.execSQL(DB_CREATE);
        db.insert(TABLE_INFO, null,values);
        close();

    }

    public void delINFO()
    {
        db.delete(TABLE_INFO, null, null);

    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

        }




        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO);
            onCreate(db);

        }
    }
}*/