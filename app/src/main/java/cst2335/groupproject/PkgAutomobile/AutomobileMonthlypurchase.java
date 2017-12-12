package cst2335.groupproject.PkgAutomobile;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cst2335.groupproject.R;

import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_LITERS;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_MONTH;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_YEAR;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.table_name;


public class AutomobileMonthlypurchase extends Activity {

    private static final String ACTIVITY_NAME = "AutomobileMonthly";
    private ProgressBar pBar;
    private TextView Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dec;
    private AutomobileDatabaseHelper dHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String [] monthlyPurchase = new String[12];
    AutomobileActivity aa = new AutomobileActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_activity_automobile_monthlypurchase);

        pBar = (ProgressBar)findViewById(R.id.progressBar);
        Jan = (TextView)findViewById(R.id.totalJan);
        Feb = (TextView)findViewById(R.id.totalFeb);
        Mar = (TextView)findViewById(R.id.totalMar);
        Apr = (TextView)findViewById(R.id.totalApr);
        May = (TextView)findViewById(R.id.totalMay);
        Jun = (TextView)findViewById(R.id.totalJun);
        Jul = (TextView)findViewById(R.id.totalJul);
        Aug = (TextView)findViewById(R.id.totalAug);
        Sep = (TextView)findViewById(R.id.totalSep);
        Oct = (TextView)findViewById(R.id.totalOct);
        Nov = (TextView)findViewById(R.id.totalNov);
        Dec = (TextView)findViewById(R.id.totalDec);

        pBar.setVisibility(View.VISIBLE);
        getMonthlyPurchase mp = new getMonthlyPurchase();
        mp.execute();
    }

    protected class getMonthlyPurchase extends AsyncTask<String,Integer,String>{


        @Override
        protected String doInBackground(String... params){
            dHelper = new AutomobileDatabaseHelper(AutomobileMonthlypurchase.this);
            db = dHelper.getWritableDatabase();
            for (int i =0; i<12;i++) {
                cursor = db.rawQuery("SELECT SUM(" + PURCHASE_LITERS + ") FROM " + table_name + " WHERE " + PURCHASE_MONTH + " = '" + i + "'" + " AND " + PURCHASE_YEAR + " = '" + aa.getYear() +"'", null);
                cursor.moveToFirst();
                Long monthlyP = cursor.getLong(0);
                monthlyPurchase[i] = monthlyP.toString()+ " L";
                SystemClock.sleep(200);
                publishProgress(i*8);
                cursor.close();
            }
            db.close();
            publishProgress(100);
            return null;
        }

        @Override
        protected void onProgressUpdate (Integer...value){
            pBar.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Jan.setText(monthlyPurchase[0]);
            Feb.setText(monthlyPurchase[1]);
            Mar.setText(monthlyPurchase[2]);
            Apr.setText(monthlyPurchase[3]);
            May.setText(monthlyPurchase[4]);
            Jun.setText(monthlyPurchase[5]);
            Jul.setText(monthlyPurchase[6]);
            Aug.setText(monthlyPurchase[7]);
            Sep.setText(monthlyPurchase[8]);
            Oct.setText(monthlyPurchase[9]);
            Nov.setText(monthlyPurchase[10]);
            Dec.setText(monthlyPurchase[11]);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
