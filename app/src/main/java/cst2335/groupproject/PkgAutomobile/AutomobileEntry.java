package cst2335.groupproject.PkgAutomobile;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cst2335.groupproject.R;

import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_DATE;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_KM;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_LITERS;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_MONTH;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_PRICE;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.table_name;


public class AutomobileEntry extends Activity {

    private static final String ACTIVITY_NAME = "AutomobileEntry";
    private Button saveButton;
    private AutomobileDatabaseHelper dHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private EditText liters, price, kilometers;
    protected String formattedDate;
    protected int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_activity_automobil_entry);

//        load the fragment into the layout
        AutomobileFragment myFragment = new AutomobileFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.entry, myFragment);
        fragmentTransaction.commit();

        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dHelper = new AutomobileDatabaseHelper(AutomobileEntry.this);
                db = dHelper.getWritableDatabase();
//                Get the current time and format the style
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                formattedDate = df.format(c.getTime());
                month = c.get(Calendar.MONTH);
                liters = (EditText)findViewById(R.id.LitersInput);
                price = (EditText)findViewById(R.id.priceInput);
                kilometers = (EditText)findViewById(R.id.KMInput);
                String valueL = liters.getText().toString();
                String valueP = price.getText().toString();
                String valueKM = kilometers.getText().toString();
                db.execSQL("INSERT INTO "+table_name+"(" +PURCHASE_DATE+","+PURCHASE_LITERS+","+PURCHASE_PRICE+","+PURCHASE_KM+","+PURCHASE_MONTH+") VALUES (" +formattedDate+", " +valueL+", " +valueP+ ", " +valueKM+", "+month+ ")");
//                Create a toast to show the time when data saved
                Toast.makeText(AutomobileEntry.this,"Your purchase had been saved at "+c.getTime(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }
}
