package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cst2335.groupproject.R;

public class FoodList extends Activity {

    private ListView listView;
    private ImageView buttonCircleAdd;
    private ArrayList<String> foodArrayList;
    private FoodListAdapter foodListAdapter;
    private Cursor cursor;
    private FoodDatabaseHelper foodDBHelper;
    private TextView foodName, foodServing, foodDateTime;
    private String foodList_className = FoodList.class.getSimpleName();


    class FoodListAdapter extends ArrayAdapter<String> {

        public FoodListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return foodArrayList.size();
        }

        public String getItem(int position) {
            return foodArrayList.get(position);
        }

        public String getServing(int position) {
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_SERVINGS));
        }

        public String getDate(int position) {
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_DATE));
        }

        public String getTime(int position) {
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_TIME));
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = FoodList.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.food_item_list, parent, false);
            foodServing = result.findViewById(R.id.food_serving);
            foodName = result.findViewById(R.id.food_name);

            foodDateTime = result.findViewById(R.id.food_date_time);/*----------------------------------------------------------*/

            foodServing.setText(getServing(position));
            foodName.setText(getItem(position));
            foodDateTime.setText(getDate(position) + " " + getTime(position));
            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        Log.i(foodList_className, "In onCreate()");

        listView = (ListView) findViewById(R.id.foodListView);
        buttonCircleAdd = (ImageView) findViewById(R.id.buttonAdd);
        foodArrayList = new ArrayList<String>();

        //In this case, "this" is the ChatWindow, which is - A Context object
        foodListAdapter = new FoodListAdapter(this);
        listView.setAdapter(foodListAdapter);

        //Create a temporary ChatDatabaseHelper object, which then gets a writeable database
        foodDBHelper = new FoodDatabaseHelper(this);

        //User clicks on the circle Add button to go to FoodAdd activity
        buttonCircleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, FoodAdd.class);
                startActivityForResult(intent, 1);
            }
        });

        foodDBHelper.openDatabase();
        displaySQL();

        //User clicks on each item of ListView to go to item FoodUpdate activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                cursor.moveToPosition(position);

                String food_Name = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_FOOD_NAME));
                String food_Serving = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_SERVINGS));
                String food_Calories = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_CALORIES));
                String food_Fat = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_FAT));
                String food_Carbohydrate = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_CARBOHYDRATE));
                String food_Date = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_DATE));
                String food_Time = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_TIME));
                String food_ID = cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_ID));

                Intent intent = new Intent(FoodList.this, FoodUpdate.class);
                intent.putExtra("food_ID", food_ID);
                intent.putExtra("food_Name", food_Name);
                intent.putExtra("food_Serving", food_Serving);
                intent.putExtra("food_Calories", food_Calories);
                intent.putExtra("food_Fat", food_Fat);
                intent.putExtra("food_Carbohydrate", food_Carbohydrate);
                intent.putExtra("food_Date", food_Date);
                intent.putExtra("food_Time", food_Time);
                startActivityForResult(intent, 2);

                Log.i(foodList_className, "");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data){
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == 1){
            displaySQL();
        }

        if(requestCode == 2){
            displaySQL();
        }

//        if(responseCode == 1){
//            foodNameInfo = data.getStringExtra("addFoodAndServings");
//            foodArrayList.add(foodNameInfo);
//            listView.setAdapter(new ArrayAdapter<String>(this, R.layout.food_item_list, foodArrayList));
//        }
    }

    //Refresh the ArrayList every time when it's called (after add, update or delete items from )
    private void displaySQL() {
        foodArrayList.clear();
        cursor = foodDBHelper.getRecords();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(foodList_className, "SQL MESSAGE: "
                        + cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_FOOD_NAME)));
                foodArrayList.add(cursor.getString(cursor.getColumnIndex(foodDBHelper.COLUMN_FOOD_NAME)));
                cursor.moveToNext();
            }
        }
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(foodList_className, "Cursor's column name is "
                    + (i + 1) + ". " + cursor.getColumnName(i));
        }
       foodListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        foodDBHelper.closeDatabase();
    }
}

/*    private void populateListView() {
//        Cursor cursor = foodDBHelper.getRecords();
        String[] fieldName = new String[] {FoodDatabaseHelper.COLUMN_SERVINGS, FoodDatabaseHelper.COLUMN_FOOD_NAME};
        int[] toViewIDs = new int[] {R.id.food_serving, R.id.food_name};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.food_item_list,
                cursor, fieldName, toViewIDs, 0);
        ListView myList = (ListView)findViewById(R.id.foodListView);
        myList.setAdapter(myCursorAdapter);
    }
}*/