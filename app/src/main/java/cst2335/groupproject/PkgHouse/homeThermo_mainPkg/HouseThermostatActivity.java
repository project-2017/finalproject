package cst2335.groupproject.PkgHouse.homeThermo_mainPkg;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import cst2335.groupproject.PkgHouse.DAO.H_DatabaseHelper;
import cst2335.groupproject.PkgHouse.DTO.DTO_TemperatureSetting;
import cst2335.groupproject.PkgHouse.adapterPkg.HelpActivity;
import cst2335.groupproject.PkgHouse.adapterPkg.TempSetting_Adapter;
import cst2335.groupproject.PkgHouse.homeThermo_mainPkg.fragmentTempPkg.FragmentMainActivity;
import cst2335.groupproject.PkgHouse.homeThermo_mainPkg.fragmentTempPkg.Fragment_editTemp;
import cst2335.groupproject.R;

/**
 * This is the starting  activity of the project;
 * <p>
 * the orientation changes is handled properly by savedInstance;
 */
//public class HouseThermostatActivity extends Activity {
public class HouseThermostatActivity extends Activity {
    private static final String FIRST_USER_SETTING = "isFirstUser_CST2335_h";
    private static final String STORED_TREE_MAP = "stored treeMap";
    private static final String KEY_SetString = "KEY_SetString_h";


    private final static int ADD_TEMP_REQUEST_CODE = 10;
    private final static int EDIT_TEMP_REQUEST_CODE = 11;
    private final static int DELETE_ITEM = 20;

    // for display in the view properly
    private ArrayList<String> arrayListString_listView;
    // treemap to handle add and delete
    private TreeMap<Integer, Double> listTemperature;

    // for display the progress bar property
    private TextView countProgress;
    private ProgressBar progressBar;


    private FloatingActionButton floatingActionButton;
    private BottomNavigationView bottomNavigationView;
    private H_DatabaseHelper databaseHelper;

    //list view display fields
    private ListView listView;
    private TempSetting_Adapter tempSetting_adapter;

    /**
     * to handle the orientation change
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("treeMap", listTemperature);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_thermostat);

//---------------------------------
        countProgress = findViewById(R.id.progressBar_stringCount_h);
        progressBar = findViewById(R.id.progressBar_h);

        listView = findViewById(R.id.listView_tempList_h);

        listTemperature = new TreeMap<>();

        floatingActionButton = findViewById(R.id.button_addNewTemp_h);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationBar_help_h);

        arrayListString_listView = new ArrayList<>();

        databaseHelper = new H_DatabaseHelper(this);
        databaseHelper.openDatabase();

        //---------------listview setup
        tempSetting_adapter = new TempSetting_Adapter(this, arrayListString_listView);
        listView.setAdapter(tempSetting_adapter);
//---------------------------------------------------------
        /*
         load the information from the database;
         use another thread to load the information at the background
         display the database information
          */

//        listTemperature = read_sharedPre();
//        listTemperature = read_database();
        ReadDatabase readDatabase = new ReadDatabase();
        readDatabase.execute(listTemperature);
//        if(listTemperature != null){
//
//        }
        updateProgressBar(listTemperature);
//        if(listTemperature == null){
//            // Probably initialize members with default values for a new instance
//            listTemperature = new TreeMap<>();
//            updateProgressBar(listTemperature);
//        }
        //-------------------------------------
        updateListView_toolbar();


        //-----------------
        // Restore value of members from saved state
        if (savedInstanceState != null) {
            Serializable serializableObj = savedInstanceState.getSerializable("treeMap");
            if (serializableObj instanceof TreeMap) {
                listTemperature = (TreeMap<Integer, Double>) serializableObj;
                // update list view
                updateListView_toolbar();
            }
        } else {

        }


//        //---------------listview setup
//        tempSetting_adapter = new TempSetting_Adapter(this, arrayListString_listView);
//        listView.setAdapter(tempSetting_adapter);

        // list view select
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView_selected = view.findViewById(R.id.textView_textItem_h);
                String str_pickedItem = textView_selected.getText().toString();

                /*
                 *get information from the user selected item;
                  * since there is only time availabe;
                   * we need to get the temperature from the treemap;
                 */
                DTO_TemperatureSetting picked_obj = new DTO_TemperatureSetting(str_pickedItem);
                picked_obj.setTemp(listTemperature.get(picked_obj.getTimeOfWeek()));

                //---------------------------------
                /*
                 2 cases; the vertical mode or the horzitonal mode;
                  */
                if (findViewById(R.id.container) == null) {
                    Intent intent = new Intent(HouseThermostatActivity.this, FragmentMainActivity.class);

                    // sent data to fragment
                    intent.putExtra("newItem_time", picked_obj.getTimeOfWeek());
                    intent.putExtra("newItem_temp", picked_obj.getTemp());
                    //--------pass the current treelist to add
                    intent.putExtra("treeMap", listTemperature);
//                startActivity(intent);
                    startActivityForResult(intent, EDIT_TEMP_REQUEST_CODE);
                } else {
                    // horizontal mode
                    Fragment fragment = new Fragment_editTemp();
                    Bundle bundle = new Bundle();
                    bundle.putInt("newItem_time", picked_obj.getTimeOfWeek());
                    bundle.putDouble("newItem_temp", picked_obj.getTemp());
                    bundle.putSerializable("treeMap", listTemperature);
                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        //-------------------
        /*
         2 cases; the vertical mode or the horzitonal mode;
          */
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (findViewById(R.id.container) == null) {
                    // vertical mode
                    Intent intent = new Intent(HouseThermostatActivity.this, AddNewTempActivity.class);

                    //--------pass the current treelist to add
                    intent.putExtra("treeMap", listTemperature);
                    startActivityForResult(intent, ADD_TEMP_REQUEST_CODE);
                } else {
                    // horitonal mode; load the fragment
                    Fragment fragment = new Fragment_editTemp();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("treeMap", listTemperature);
                    fragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction =
                            getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.commit();
                }
            }
        });

        //------------------------------
        /*
        this is used for help files output
         */
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Intent intent = new Intent(HouseThermostatActivity.this, HelpActivity.class);
                switch (item.getItemId()) {
                    case R.id.nav_about_h:
                        intent.putExtra("helpItem", "helpAdd");
//                        Toast.makeText(getApplicationContext(), "Navigation bar add Clicked!",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_Instruction_h:
                        intent.putExtra("helpItem", "helpDelete");
//                        Toast.makeText(getApplicationContext(), "Navigation bar delete Clicked!",
//                                Toast.LENGTH_SHORT).show();
                        break;
                }
                startActivity(intent);
            }
        });

    }// end  method

    /**
     * for the fragment layout
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (findViewById(R.id.container) != null) {
            if (getFragmentManager().findFragmentById(R.id.container) != null) {
                closeSideBar();
            }
        }
    }

    /**
     * update the database
     *
     * @param time
     * @param temp
     */
    private void changeDatabase(int time, double temp) {
        DTO_TemperatureSetting newTemp = new DTO_TemperatureSetting();
        newTemp.setTemp(temp);
        newTemp.setTimeOfWeek(time);

        if (databaseHelper.exist(time)) {
            databaseHelper.update(time, newTemp.toString());
        } else {
            databaseHelper.insert(time, newTemp.toString());
        }
        listTemperature.put(time, temp);
    }


    /**
     * close the GUI of the fragment
     */
    public void closeSideBar() {
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.container)).commit();
    }

    /**
     * update the user input from the fragments horizontal mode
     *
     * @param treeMap
     * @param time
     * @param temp
     */

    public void Change(TreeMap treeMap, int time, double temp) {
        closeSideBar();
        listTemperature = new TreeMap<>((Map<Integer, Double>) treeMap);
        int time_return = time;
        double temp_return = temp;
        changeDatabase(time_return, temp_return);
        Toast.makeText(getApplicationContext(), "New Temperature rule is added: \n" + (new DTO_TemperatureSetting(time_return, temp_return)).toString(), Toast.LENGTH_LONG)
                .show();
        updateListView_toolbar();
    }

    /**
     * delete the user input from the fragment; horizontal mode
     *
     * @param time
     * @param temp
     */
    public void delete(int time, double temp) {
        closeSideBar();
        listTemperature.remove(time);
        databaseHelper.delete(time);
        showSnackBar(time, temp);
        updateListView_toolbar();
    }

    /**
     * standard code to handle the returned results from sub activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_TEMP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                listTemperature = new TreeMap<>((Map<Integer, Double>) data.getExtras().get("treeMap"));
                int time_return = data.getIntExtra("newItem_time", 0);
                double temp_return = data.getDoubleExtra("newItem_temp", -900);

                changeDatabase(time_return, temp_return);

                Toast.makeText(getApplicationContext(), "New Temperature rule is added: \n" + (new DTO_TemperatureSetting(time_return, temp_return)).toString(), Toast.LENGTH_LONG)
                        .show();

//                updateListView_toolbar();
            }// end if
        } else if (requestCode == EDIT_TEMP_REQUEST_CODE) {

            // same code as before
            if (resultCode == Activity.RESULT_OK) {
                int time_return = data.getIntExtra("newItem_time", 0);
                double temp_return = data.getDoubleExtra("newItem_temp", -900);
                listTemperature = new TreeMap<>((Map<Integer, Double>) data.getExtras().get("treeMap"));

                changeDatabase(time_return, temp_return);
                Toast.makeText(getApplicationContext(), "New Temperature rule is edited/saved: \n" + (new DTO_TemperatureSetting(time_return, temp_return)).toString(), Toast.LENGTH_LONG)
                        .show();

//                updateListView_toolbar();
            } else if (resultCode == DELETE_ITEM) {
                int time_return = data.getIntExtra("newItem_time", 0);
                double temp_return = data.getDoubleExtra("newItem_temp", -900);
                listTemperature.remove(time_return);
                databaseHelper.delete(time_return);

//                Toast.makeText(getApplicationContext(), "Old Temperature rule is deleted: \n" + (new DTO_TemperatureSetting(time_return, temp_return)).toString(), Toast.LENGTH_LONG)
//                        .show();

//                updateListView_toolbar();
                showSnackBar(time_return, temp_return);

            } else if (resultCode == Activity.RESULT_CANCELED) { // end if

            } else {
            }
        }// end if

        updateListView_toolbar();

//            listTemperature = read_sharedPre();
    }//end of method


    /**
     * update the list view and  toolbar/progress bar views
     */
    private void updateListView_toolbar() {
//        tempSetting_adapter = new TempSetting_Adapter(this, arrayListString_listView);
        if (!arrayListString_listView.isEmpty()) {
            arrayListString_listView.clear();
        }

        // testing display to the window
        DTO_TemperatureSetting newTemp = new DTO_TemperatureSetting();
        for (Map.Entry<Integer, Double> entry : listTemperature.entrySet()) {
            Integer time_key = entry.getKey();
            Double temp = entry.getValue();
            newTemp.setTemp(temp);
            newTemp.setTimeOfWeek(time_key);
            Log.i("list temp", "returned to main " + newTemp.toString());
            /*//            testing
            DTO_TemperatureSetting testObj = new DTO_TemperatureSetting((newTemp.toString()));
            Log.i("list temp", "returned to main test1 " + testObj.toString());

            DTO_TemperatureSetting testObj2 = new DTO_TemperatureSetting((newTemp.displayTime()));
            Log.i("list temp", "returned to main test2 " + testObj2.toString());*/

            arrayListString_listView.add(newTemp.displayTime(Locale.getDefault()));
        }
        /*
        update the views
         */
        updateProgressBar(listTemperature);
        tempSetting_adapter.notifyDataSetChanged();
    }

    /**
     * update the prograss bar
     *
     * @param listTemperature
     */
    private void updateProgressBar(TreeMap<Integer, Double> listTemperature) {
        if (listTemperature == null || listTemperature.isEmpty()) {
            progressBar.setProgress(0);
            countProgress.setText("" + 0);
        } else {
            progressBar.setProgress(listTemperature.size());
            countProgress.setText("" + listTemperature.size());
        }
    }

    /**
     * snack bar code to display the results from the edit activity
     *
     * @param time
     * @param temp
     */
    private void showSnackBar(final int time, final double temp) {
        String str = "You deleted: " + (new DTO_TemperatureSetting(time, temp)).toString();

        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinatorLayout_temp_h),
                str,
                Snackbar.LENGTH_LONG);

        snackbar.setAction("undo", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Snackbar Action Click!",
                        Toast.LENGTH_SHORT).show();

                listTemperature.put(Integer.valueOf(time), Double.valueOf(temp));
                changeDatabase(time, temp);

                updateListView_toolbar();
            }
        });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    //--------------

    /**
     * close the database and write the shared preference (not used)
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        write_sharedPre(listTemperature);
//        write_database(listTemperature);
//        WriteDatabase writeDatabase = new WriteDatabase();
//        writeDatabase.execute(listTemperature);
        databaseHelper.closeDatabase();
    }



    //---------
    /**
     * shared preferance was not need; updated by the database code
     *
     * @param treeMapIn
     */
    private void write_sharedPre(TreeMap<Integer, Double> treeMapIn) {
        SharedPreferences sharedPreferences = getSharedPreferences(STORED_TREE_MAP, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> treeSet = new TreeSet<>();

//        Log.i("test write ", " test write start" );
        if (treeMapIn == null) {
            return;
        }
        DTO_TemperatureSetting newTemp = new DTO_TemperatureSetting();
        for (Map.Entry<Integer, Double> entry : treeMapIn.entrySet()) {
            Integer time_key = entry.getKey();
            Double temp = entry.getValue();
            newTemp.setTemp(temp);
            newTemp.setTimeOfWeek(time_key);

            treeSet.add(newTemp.toString());
        }
        editor.remove(KEY_SetString);
        editor.putStringSet(KEY_SetString, treeSet);
        editor.commit();


//        //test
//        for (String treeStr : treeSet) {
//            DTO_TemperatureSetting obj = new DTO_TemperatureSetting(treeStr);
//            Log.i("test write ", " test write " + obj.toString() );
//
//            listTemperature.put(obj.getTimeOfWeek(), obj.getTemp());
//
//            Log.i("test write ", " test write " + listTemperature.get(obj.getTimeOfWeek()).toString() );
//        }

//        listTemperature = read_sharedPre();
//        updateListView_toolbar();
    }

    /**
     * shared preferance was not need; updated by the database code
     *
     * @return
     */
    private TreeMap<Integer, Double> read_sharedPre() {
        TreeMap<Integer, Double> treeMap = new TreeMap<>();

        Set<String> treeSet = new TreeSet<>();
        SharedPreferences sharedPreferences = getSharedPreferences(STORED_TREE_MAP, MODE_PRIVATE);
        treeSet = sharedPreferences.getStringSet(KEY_SetString, null);

        if (treeSet != null) {
//            if (sharedPreferences != null) {
            for (String treeStr : treeSet) {
                DTO_TemperatureSetting obj = new DTO_TemperatureSetting(treeStr);
                treeMap.put(obj.getTimeOfWeek(), obj.getTemp());
            }
            return treeMap;
        } else {
            // null; empty;
//            return null;
            return treeMap;
        }

    }

//    private TreeMap<Integer, Double> read_database() {
//        TreeMap<Integer, Double> treeMap = new TreeMap<>();
//
//        Cursor cursor = databaseHelper.read();
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            String treeStr = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_ITEM));
//            DTO_TemperatureSetting obj = new DTO_TemperatureSetting(treeStr);
//            treeMap.put(obj.getTimeOfWeek(), obj.getTemp());
//        }
//
//        return treeMap;
//    }

    /**
     * asynch class to handle the read funciton at the background
     */
    public class ReadDatabase extends AsyncTask<TreeMap<Integer, Double>, String, TreeMap<Integer, Double>> {
        private DTO_TemperatureSetting obj;
        private TreeMap<Integer, Double> treeMapTemp = new TreeMap<>();

        public ReadDatabase() {
        }

        @Override
        protected TreeMap<Integer, Double> doInBackground(TreeMap<Integer, Double>[] treeMaps) {
            try {
                treeMapTemp = treeMaps[0];
                Cursor cursor = databaseHelper.read();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String string = cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_ITEM));
                    publishProgress(string);
                }
            } catch (Exception e) {

            }
            return treeMaps[0];
        }

        @Override
        protected void onProgressUpdate(String... values) {
//            Log.i("Update","OnProgressUpdate " + values[0]);
            obj = new DTO_TemperatureSetting(values[0]);
            treeMapTemp.put(obj.getTimeOfWeek(), obj.getTemp());
//            double d = listTemperature.get(obj.getTimeOfWeek());
//            Log.i("Update","OnProgressUpdate " + d);
            updateListView_toolbar();
        }

//        @Override
//        protected void onPostExecute(TreeMap<Integer, Double> treeMap) {
//            updateListView_toolbar();
//        }
    }


}// end of class


//    private void write_database(TreeMap<Integer, Double> treeMapIn) {
//        databaseHelper.clear();
//        if (treeMapIn == null) {
//            return;
//        }
//        DTO_TemperatureSetting newTemp = new DTO_TemperatureSetting();
//        for (Map.Entry<Integer, Double> entry : treeMapIn.entrySet()) {
//            Integer time_key = entry.getKey();
//            Double temp = entry.getValue();
//            newTemp.setTemp(temp);
//            newTemp.setTimeOfWeek(time_key);
//
//            databaseHelper.insert(newTemp.toString());
//        }
//
//
//    }

//    public class WriteDatabase extends AsyncTask<TreeMap<Integer, Double>, TreeMap<Integer, Double>, TreeMap<Integer, Double>> {
//        private Integer time_key;
//        private Double temp;
//        @Override
//        protected TreeMap<Integer, Double> doInBackground(TreeMap<Integer, Double>[] treeMaps) {
//            try {
//                databaseHelper.clear();
//                if (treeMaps[0] == null) {
//                    return null;
//                }
//
//                for (Map.Entry<Integer, Double> entry : treeMaps[0].entrySet()) {
//                    time_key = entry.getKey();
//                    temp = entry.getValue();
//                    publishProgress();
//                }
//            } catch (Exception ex) {
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(TreeMap<Integer, Double> treeMap) {
//            databaseHelper.closeDatabase();
//        }
//
//        @Override
//        protected void onProgressUpdate(TreeMap<Integer, Double>[] values) {
//            DTO_TemperatureSetting newTemp = new DTO_TemperatureSetting();
//            newTemp.setTemp(temp);
//            newTemp.setTimeOfWeek(time_key);
//
//            databaseHelper.insert(newTemp.toString());
//        }
//    }

/*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar);

        findViewById(R.id.btnShowSnackbar).setOnClickListener(this);
        findViewById(R.id.btnShowActionSnackbar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowSnackbar:
                showSnackbar();
                break;
            case R.id.btnShowActionSnackbar:
                showActionSnackbar();
                break;
        }
    }*/
