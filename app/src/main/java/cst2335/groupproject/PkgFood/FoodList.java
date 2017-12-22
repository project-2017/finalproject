package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Xml;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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


    class FoodListAdapter extends ArrayAdapter<FoodTransferObject> {

        public FoodListAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return foodArrayList.size();
        }

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


        //User clicks on the circle Add button to go to FoodAdd activity
        buttonCircleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, FoodAdd.class);
                startActivityForResult(intent, 1);
            }
        });

        buttonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodList.this, FoodDashboard.class);
                startActivityForResult(intent, 3);
            }
        });

        /**
         * Click on Facebook icon to show a custom dialog.
         */
        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateFacebookDialog();
            }
        });

        /**
         * Click on Google+ icon to show a custom dialog.
         */
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateGoogleDialog();
            }
        });

        /**
         * Click on Twitter icon to show a custom dialog.
         */
        buttonTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateTwitterDialog();
            }
        });

        /**
         * Click on Help icon to show a custom dialog.
         */
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateHelpDialog();
            }
        });



        /**
         * User clicks on each item of ListView to go to FoodUpdate fragment.
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FoodTransferObject fto = (FoodTransferObject) (adapterView.getItemAtPosition(position));

/*                if(findViewById(R.id.frameLayout) != null) {*/

                    String food_ID = fto.getFoodID()+"";
                    String food_Name = fto.getFoodName();
                    String food_Serving = fto.getFoodServings();
                    String food_Calories = fto.getFoodCalories();
                    String food_Fat = fto.getFoodFat();
                    String food_Carbohydrate = fto.getFoodCarbohydrate();
                    String food_Date = fto.getFoodDate();
                    String food_Time = fto.getFoodTime();


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

                    Log.i(foodList_className, "Run on a phone");
/*                }*/
            }
        });
    }

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
     * Refresh the ArrayList every time when it's called (after add, update or delete items from )
     */
    private void displaySQL() {
        FoodListAsync fla = new FoodListAsync();
        fla.execute();
/*        foodArrayList.clear();
        cursor = foodDatabaseHelper.getRecords();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(foodList_className, "SQL MESSAGE: "
                        + cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_FOOD_NAME)));
                foodArrayList.add(new FoodTransferObject(cursor.getInt(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_FOOD_NAME)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_SERVINGS)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_CALORIES)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_FAT)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_CARBOHYDRATE)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(foodDatabaseHelper.COLUMN_TIME))));
                cursor.moveToNext();
            }
        }

        Collections.sort(foodArrayList);
        foodListAdapter.notifyDataSetChanged();*/

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        foodDatabaseHelper.closeDatabase();
    }

    //Create Facebook custom dialog
    public void onCreateFacebookDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.food_custom_dialog_facebook, null);
        dialogBuilder.setView(dialogView);

        final EditText comment = (EditText) dialogView.findViewById(R.id.dialog_comment);
        // Add the buttons
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

    //Create Google+ custom dialog
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

    //Create Twitter custom dialog
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

    //Create Help custom dialog
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

        @Override
        protected void onPreExecute() {
            foodArrayList.clear();
        }

        @Override
        protected void onProgressUpdate(Integer ... value){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(value[0]);

        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.INVISIBLE);
            foodArrayList.addAll(tempArrayList);
            Collections.sort(foodArrayList);
            foodListAdapter.notifyDataSetChanged();
        }


    }






}