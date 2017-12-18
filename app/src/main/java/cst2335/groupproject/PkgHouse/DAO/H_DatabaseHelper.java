package cst2335.groupproject.PkgHouse.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class H_DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "House.db";

    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "House";

    public static final String COLUMN_ID = "HouseID";

    public static final String COLUMN_ITEM = "Temp";

    public static final String COLUMN_TIME = "Time";

    public SQLiteDatabase database;

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIME + " INTEGER, " +
            COLUMN_ITEM + " TEXT" +
            ")";

    public H_DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void openDatabase() {
        database = getWritableDatabase();
    }

    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public void insert(int time, String item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_ITEM, item);

        database.insert(TABLE_NAME, null, values);

    }

    public void delete(int time) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TIME + " = " + time);
    }

    public void update(int time, String item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_ITEM, item);

        database.update(TABLE_NAME, values, COLUMN_TIME + " = " + time, null);
    }

    public Cursor read() {
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void clear(){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public boolean exist(int time){
        int count = 0;
        Cursor cur = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COLUMN_TIME + " = " + time, null);
        if (cur.moveToFirst()) {
            count = cur.getInt(0);
        }
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }
}

