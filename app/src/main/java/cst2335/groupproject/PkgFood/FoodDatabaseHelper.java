package cst2335.groupproject.PkgFood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodInfo.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private String foodDatabase_className =FoodDatabaseHelper.class.getSimpleName();

    public static final String TABLE_NAME = "foodInfo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOOD_NAME = "foodName";
    public static final String COLUMN_SERVINGS = "foodServings";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_FAT = "fat";
    public static final String COLUMN_CARBOHYDRATE = "carbohydrate";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";

    /**
     * Creates database table
     */
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FOOD_NAME + " TEXT," +
            COLUMN_SERVINGS + " INTEGER," +
            COLUMN_CALORIES + " INTEGER," +
            COLUMN_FAT + " INTEGER," +
            COLUMN_CARBOHYDRATE + " INTEGER," +
            COLUMN_DATE + " TEXT," +
            COLUMN_TIME + " TEXT" +
            ")";

    /**
     * Constructor
     *
     * @param ctx database context
     */
    public FoodDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time
     *
     * @param sqLiteDatabase the database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        Log.i(foodDatabase_className, "Calling onCreate");
    }

    /**
     * Called when the database needs to be upgraded
     *
     * @param sqLiteDatabase the database
     * @param oldVersion old version number of database
     * @param newVersion new version number of database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
        Log.i(foodDatabase_className, "Calling onUpgrade, " +
                "oldVersion =" + oldVersion + "newVersion =" + newVersion);
    }

    /**
     * Called when the database needs to be downgraded
     *
     * @param sqLiteDatabase the database
     * @param oldVersion old version number of database
     * @param newVersion new version number of database
     */
    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        Log.i(foodDatabase_className, "Calling onDowngrade, " +
                "oldVersion =" + oldVersion + "newVersion =" + newVersion);
    }

    /**
     * Opens the database
     */
    public void openDatabase() {
        database = this.getWritableDatabase();
    }

    /**
     * Closes the database
     */
    public void closeDatabase() {
        if(database != null && database.isOpen()){
            database.close();
        }
    }

    /**
     * Inserts food information into the database
     *
     * @param foodName food name
     * @param foodServings servings
     * @param calories food calories
     * @param fat food fat
     * @param carbohydrate food carbohydrate
     * @param date the date food eaten
     * @param time the time food eaten
     * @return insert result
     */
    public boolean insertData(String foodName, String foodServings, String calories, String fat, String carbohydrate, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, foodName);
        values.put(COLUMN_SERVINGS, Integer.parseInt(foodServings));
        values.put(COLUMN_CALORIES, Integer.parseInt(calories));
        values.put(COLUMN_FAT, Integer.parseInt(fat));
        values.put(COLUMN_CARBOHYDRATE, Integer.parseInt(carbohydrate));
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        long result = database.insert(TABLE_NAME, null, values);
        if(result == -1)
            return false;
        else
            return true;
    }

    /**
     * Updates a record of database
     *
     * @param id column id
     * @param foodName food name column
     * @param foodServings food servings column
     * @param calories calories column
     * @param fat fat column
     * @param carbohydrate carbohydrate column
     * @param date date column
     * @param time time column
     */
    public void updateData(String id, String foodName, String foodServings, String calories, String fat, String carbohydrate, String date, String time) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, foodName);
        values.put(COLUMN_SERVINGS, Integer.parseInt(foodServings));
        values.put(COLUMN_CALORIES, Integer.parseInt(calories));
        values.put(COLUMN_FAT, Integer.parseInt(fat));
        values.put(COLUMN_CARBOHYDRATE, Integer.parseInt(carbohydrate));
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        database.update(TABLE_NAME, values, COLUMN_ID + " = " + id, null);
    }

    /**
     * Deletes a record from the database
     *
     * @param id column id
     */
    public void deleteData(String id) {
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id);
/*        database.delete(TABLE_NAME, "id = ?", new String[] {id});*/
    }

    /**
     * Gets database record
     *
     * @return database record
     */
    public Cursor getRecords() {
        return database.query(TABLE_NAME, null, null, null, null, null, null);
    }

    /**
     * Counts total days have been entered into database
     *
     * @return total days
     */
    public int countDays() {
        int totalDays = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(DISTINCT " + COLUMN_DATE + ") FROM " + TABLE_NAME, null);
        if(cursor.moveToFirst()) {
            totalDays = cursor.getInt(0);
        }
        return totalDays;

    }

    /**
     * Counts total food calories have been entered into database
     *
     * @return total calories
     */
    public int countCalories() {
        int totalCalories = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(" + COLUMN_CALORIES + ") FROM " + TABLE_NAME, null);
        if(cursor.moveToFirst()) {
            totalCalories = cursor.getInt(0);
        }
        return totalCalories;

    }

    /**
     * Counts total rows
     *
     * @return total row number
     */
    public int getRowsCount() {
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME,null);
        if(cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        return count;
    }

    /**
     * Gets last day calories
     *
     * @return last day calories
     */
    public int getYesterdayCalories() {
        int yesterdayCalories = 0;
        Cursor cursor = database.rawQuery("SELECT SUM(" + COLUMN_CALORIES + ") FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = DATE('now', '-1 day')",null);
        if(cursor.moveToFirst()){
            yesterdayCalories = cursor.getInt(0);
        }
        return yesterdayCalories;
    }
}
