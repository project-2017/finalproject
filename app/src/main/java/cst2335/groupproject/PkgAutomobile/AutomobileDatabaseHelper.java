package cst2335.groupproject.PkgAutomobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by bruce on 2017-11-26.
 */

public class AutomobileDatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "GasPurchased.db";
    protected final static String table_name = "GasPurchase_Table";
    protected final static String KEY_ID = "_id";
    protected final static String PURCHASE_DATE = "DATE";
    protected final static String PURCHASE_LITERS = "LITERS";
    protected final static String PURCHASE_PRICE = "PRICE";
    protected final static String PURCHASE_KM = "KILOMETERS";
    protected final static String PURCHASE_MONTH = "MONTH";
    protected final static String PURCHASE_YEAR = "YEAR";
    private static int VERSION_NUM = 10;
    protected final static String [] DATA_FIELD = {KEY_ID,PURCHASE_DATE,PURCHASE_LITERS,PURCHASE_PRICE,PURCHASE_KM,PURCHASE_MONTH,PURCHASE_YEAR};
    private String CREATE_TABLE_MESSAGE = "CREATE TABLE " + table_name + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PURCHASE_DATE + " text," + PURCHASE_LITERS + " numeric(1000,2)," +PURCHASE_PRICE + " numeric(100,2)," + PURCHASE_KM +
            " integer,"+ PURCHASE_MONTH + " integer," + PURCHASE_YEAR + " varChar)";

    protected AutomobileDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("DatabaseHelper", "Calling onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        Log.w(TAG, "Upgrading database from version " + oldVer + " to " + newVer + ", which will destroy all old data,");
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("DatabaseHelper","Calling onUpgrade(), oldVersion=" + oldVer + "newVersion=" +newVer);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        db.execSQL(CREATE_TABLE_MESSAGE);
        Log.i("DatabaseHelper","Calling onDowngrade(), oldVersion=" + oldVer + "newVersion=" +newVer);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("DatabaseHelper", "Calling onOpen()");
    }
}

