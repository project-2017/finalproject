package cst2335.groupproject.PkgHouse.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * this class will handle the data access logic to the database;
 *  and everything about the database will call functions in this class
 *
 *  the database store information about the time of the week, and temperature setting;
 *
 */
public class H_DatabaseHelper extends SQLiteOpenHelper {

    /**
     * the name of the field can indicate the useage of the instance
     */
    private static final String DATABASE_NAME = "House.db";

    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "House";

    public static final String COLUMN_ID = "HouseID";

    public static final String COLUMN_ITEM = "Temp";

    public static final String COLUMN_TIME = "Time";

    public SQLiteDatabase database;

    /**
     * string to create table
     */
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TIME + " INTEGER, " +
            COLUMN_ITEM + " TEXT" +
            ")";

    /**
     * constructor for database helper
     * @param context
     */
    public H_DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * create table when database if not exists
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    /**
     * update version
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * downgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * onOpen() gets called the last. and gets called regardless
     */
    public void openDatabase() {
        database = getWritableDatabase();
    }

    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    /**
     * insert new row/item
     *
     * @param time
     * @param item
     */
    public void insert(int time, String item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_ITEM, item);

        database.insert(TABLE_NAME, null, values);

    }

    /**
     * delete row
     * @param time
     */
    public void delete(int time) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TIME + " = " + time);
    }

    /**
     * update row
     * @param time
     * @param item
     */
    public void update(int time, String item) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_ITEM, item);

        database.update(TABLE_NAME, values, COLUMN_TIME + " = " + time, null);
    }

    /**
     * read all
     * @return
     */
    public Cursor read() {
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * drop the table and create new
     */
    public void clear(){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    /**
     * check if the time is exist in the database
     *
     * @param time
     * @return
     */
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

