package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import cst2335.groupproject.R;
import static java.lang.Thread.sleep;

public class FoodList extends Activity {

    private ListView listView;
    private ImageView buttonCircleAdd, buttonDashboard, buttonFacebook, buttonGoogle, buttonTwitter, buttonHelp;
    private ArrayList<FoodTransferObject> foodArrayList;
    private FoodListAdapter foodListAdapter;
    private Cursor cursor;
    private FoodDatabaseHelper foodDatabaseHelper;
    private TextView foodName, foodServing, foodDateTime;
    private String foodList_className = FoodList.class.getSimpleName();
    private ProgressBar progressBar;

    /**
     * An inner class of FoodList, displays food item details which get from FoodTransferObject class in an array
     */
    class FoodListAdapter extends ArrayAdapter<FoodTransferObject> {

        /**
         * Constructor: Pass 0 as the int resource parameter because it will not be using the default layout
         *
         * @param ctx the Adapter context
         */
        public FoodListAdapter(Context ctx) {
            super(ctx, 0);
        }

        /**
         * Counts how many items are in the data set represented by this Adapter
         *
         * @return the size of the ArrayList
         */
        public int getCount() {
            return foodArrayList.size();
        }

        /**
         * Gets food item
         *
         * @param position cursor position
         * @return
         */
        public FoodTransferObject getItem(int position) {
            return foodArrayList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = FoodList.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.food_item_list, parent, false);
            foodServing = result.findViewById(R.id.food_serving);
            foodName = result.findViewById(R.id.food_name);
            foodDateTime = result.findViewById(R.id.food_date_time);

            foodServing.setText(getItem(position).getFoodServings());
            foodName.setText(getItem(position).getFoodName());
            foodDateTime.setText(getItem(position).getFoodDate() + "   " + getItem(position).getFoodTime());
            return result;
        }
    }

    /**
     * Initializes this activity
     *
     * @param savedInstanceState contains the activity's previously frozen state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_list);
        Log.i(foodList_className, "In onCreate()");

        listView = (ListView) findViewById(R.id.foodListView);
        buttonCircleAdd = (ImageView) findViewById(R.id.buttonAdd);
        buttonDashboard = (ImageView) findViewById(R.id.buttonDashboard);
        buttonFacebook = (ImageView) findViewById(R.id.facebook);
        buttonGoogle = (ImageView) findViewById(R.id.google);
        buttonTwitter = (ImageView) findViewById(R.id.twitter);
        buttonHelp = (ImageView) findViewById(R.id.help);
        foodArrayList = new ArrayList<FoodTransferObject>();

        //In this case, "this" is the ChatWindow, which is - A Context object
        foodListAdapter = new FoodListAdapter(this);
        listView.setAdapter(foodListAdapter);

        //Create a temporary ChatDatabaseHelper object, which then gets a writeable database
        foodDatabaseHelper = new FoodDatabaseHelper(this);
        foodDatabaseHelper.openDatabase();
        displaySQL();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /**
         * User clicks on the circle Add button to go to FoodAdd activity to add a food item
         */
        buttonCircleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, FoodAdd.class);
                startActivityForResult(intent, 1);
            }
        });

        /**
         * User clicks on the circle statistic button to go to FoodDashboard activity to see calories summary
         */
        buttonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, FoodDashboard.class);
                startActivityForResult(intent, 3);
            }
        });

        /**
         * Clicks on Facebook icon to show a custom dialog
         */
        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateFacebookDialog();
            }
        });

        /**
         * Clicks on Google+ icon to show a custom dialog
         */
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateGoogleDialog();
            }
        });

        /**
         * Clicks on Twitter icon to show a custom dialog
         */
        buttonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateTwitterDialog();
            }
        });

        /**
         * Clicks on Help icon to show a custom dialog
         */
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateHelpDialog();
            }
        });

        /**
         * User clicks on each item of Food ListView to edit details
         * Goes to FoodUpdateDetails activity if the phone direction is portrait
         * Goes to FoodUpdate fragment if the phone direction is landscape
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FoodTransferObject fto = (FoodTransferObject) (adapterView.getItemAtPosition(position));

                    String food_ID = fto.getFoodID()+"";
                    String food_Name = fto.getFoodName();
                    String food_Serving = fto.getFoodServings();
                    String food_Calories = fto.getFoodCalories();
                    String food_Fat = fto.getFoodFat();
                    String food_Carbohydrate = fto.getFoodCarbohydrate();
                    String food_Date = fto.getFoodDate();
                    String food_Time = fto.getFoodTime();

                    if(findViewById(R.id.frameLayout) != null) {
                        FoodUpdateFragment foodUpdateFragment = new FoodUpdateFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("food_ID", food_ID);
                        bundle.putString("food_Name", food_Name);
                        bundle.putString("food_Serving", food_Serving);
                        bundle.putString("food_Calories", food_Calories);
                        bundle.putString("food_Fat", food_Fat);
                        bundle.putString("food_Carbohydrate", food_Carbohydrate);
                        bundle.putString("food_Date", food_Date);
                        bundle.putString("food_Time", food_Time);

                        foodUpdateFragment.setArguments(bundle);
                        FragmentTransaction ft =  getFragmentManager().beginTransaction();
                        ft.replace(R.id.frameLayout, foodUpdateFragment);
                        //Call transaction.addToBackStack(String name) if user wants to undo this transaction with the back button.
                        ft.addToBackStack("A string");
                        ft.commit();

                        Log.i(foodList_className, "Run on a phone: landscape");
                    }
                    else {
                        Intent intent = new Intent(FoodList.this, FoodUpdateDetails.class);
                        intent.putExtra("food_ID", food_ID);
                        intent.putExtra("food_Name", food_Name);
                        intent.putExtra("food_Serving", food_Serving);
                        intent.putExtra("food_Calories", food_Calories);
                        intent.putExtra("food_Fat", food_Fat);
                        intent.putExtra("food_Carbohydrate", food_Carbohydrate);
                        intent.putExtra("food_Date", food_Date);
                        intent.putExtra("food_Time", food_Time);
                        startActivityForResult(intent, 2);

                        Log.i(foodList_className, "Run on a phone: portrait");
                    }
            }
        });
    }

    /**
     * Gets data and do actions when insert and update database
     *
     * @param requestCode the request code
     * @param responseCode the response code
     * @param data the data
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        if (requestCode == 1) {
            displaySQL();
        }

        if (requestCode == 2) {
            displaySQL();
        }
    }

    /**
     * Async the database when it's called (after add, update or delete items from )
     */
    private void displaySQL() {
        FoodListAsync fla = new FoodListAsync();
        fla.execute();
    }

    /**
     * Closes the database
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        foodDatabaseHelper.closeDatabase();
    }

    /**
     * Creates Facebook custom dialog
     */
    public void onCreateFacebookDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.food_custom_dialog_facebook, null);
        dialogBuilder.setView(dialogView);

        final EditText comment = (EditText) dialogView.findViewById(R.id.dialog_comment);
        // Add positive and negative buttons
        dialogBuilder.setPositiveButton(R.string.food_Dialog_share, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Share button
                comment.getText().toString();
                Snackbar.make(findViewById(R.id.facebook), R.string.food_Snackbar_facebookMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dialogBuilder.setNegativeButton(R.string.food_Dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Creates Google+ custom dialog
     */
    public void onCreateGoogleDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.food_custom_dialog_google, null);
        dialogBuilder.setView(dialogView);

        final EditText comment = (EditText) dialogView.findViewById(R.id.dialog_comment);
        // Add the buttons
        dialogBuilder.setPositiveButton(R.string.food_Dialog_share, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Share button
                comment.getText().toString();
                Snackbar.make(findViewById(R.id.google), R.string.food_Snackbar_googleMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dialogBuilder.setNegativeButton(R.string.food_Dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Creates Twitter custom dialog
     */
    public void onCreateTwitterDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.food_custom_dialog_twitter, null);
        dialogBuilder.setView(dialogView);

        final EditText comment = (EditText) dialogView.findViewById(R.id.dialog_comment);
        // Add the buttons
        dialogBuilder.setPositiveButton(R.string.food_Dialog_share, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Share button
                comment.getText().toString();
                Snackbar.make(findViewById(R.id.twitter), R.string.food_Snackbar_twitterMessage, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dialogBuilder.setNegativeButton(R.string.food_Dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * Creates Help custom dialog
     */
    public void onCreateHelpDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.food_custom_dialog_help, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setNegativeButton(R.string.food_Dialog_exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    /**
     * AsyncTask for reading data from database
     * Performs background operations and publish results on the UI thread without having to manipulate threads and/or handlers
     */
    public class FoodListAsync extends AsyncTask<String, Integer, String> {

        private ArrayList<FoodTransferObject> tempArrayList;

        @Override
        protected String doInBackground(String ...args) {

            try {
                tempArrayList = new ArrayList<>();
                cursor = foodDatabaseHelper.getRecords();
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        Log.i(foodList_className, "SQL MESSAGE: "
                                + cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_FOOD_NAME)));
                        tempArrayList.add(new FoodTransferObject(cursor.getInt(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_FOOD_NAME)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_SERVINGS)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_CALORIES)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_FAT)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_CARBOHYDRATE)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_DATE)),
                                cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_TIME))));
                        publishProgress((int)(((double)tempArrayList.size() / (double)foodDatabaseHelper.getRowsCount()) * 100));
                        sleep(200);
                        cursor.moveToNext();
                    }
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        Log.i(foodList_className, "Cursor's column name is "
                                + (i + 1) + ". " + cursor.getColumnName(i));
                    }
                }
            }
            catch (Exception e) {
                Log.e("TEST", e.getMessage());
            }

            return null;
        }

        /**
         * Before execute
         */
        @Override
        protected void onPreExecute() {
            foodArrayList.clear();
        }

        /**
         * Displays the progress in the user interface while the background computation is still executing
         *
         * @param value the value
         */
        @Override
        protected void onProgressUpdate(Integer ... value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);
        }

        /**
         * Invoked on the UI thread after the background computation finishes
         * Sorts the food ListView by date
         *
         * @param result the result of the background computation
         */
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            foodArrayList.addAll(tempArrayList);
            Collections.sort(foodArrayList);
            foodListAdapter.notifyDataSetChanged();
        }
    }

}