package cst2335.groupproject.PkgAutomobile;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cst2335.groupproject.R;

import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.KEY_ID;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_KM;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_LITERS;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_PRICE;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.table_name;


public class AutomobileDetail extends Activity {

    private static final String ACTIVITY_NAME = "AutomobileDetail";
    private TextView recordTime;
    private EditText liters, price,kilometers;
    private Button delete,update;
    private AutomobileDatabaseHelper dHelper;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_activity_automobile_detail);

        recordTime = (TextView)findViewById(R.id.recordTimeValue);
        recordTime.setText(getIntent().getStringExtra("time"));

        final Integer purchase_id = (int)getIntent().getLongExtra("id",0);
        dHelper = new AutomobileDatabaseHelper(this);
        db = dHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM "+table_name+" WHERE "+KEY_ID+" = "+purchase_id,null);
        cursor.moveToFirst();
        String l = cursor.getString(cursor.getColumnIndex(PURCHASE_LITERS));
        String p = cursor.getString(cursor.getColumnIndex(PURCHASE_PRICE));
        String k = cursor.getString(cursor.getColumnIndex(PURCHASE_KM));

        liters =(EditText) findViewById(R.id.litersV);
        liters.setText(l,TextView.BufferType.EDITABLE);
        price = (EditText) findViewById(R.id.priceV);
        price.setText(p,TextView.BufferType.EDITABLE);
        kilometers = (EditText) findViewById(R.id.kmV);
        kilometers.setText(k,TextView.BufferType.EDITABLE);

        cursor.close();

        delete = (Button)findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("DELETE FROM "+table_name+" WHERE "+KEY_ID+" = "+purchase_id);
                Intent intent = new Intent(AutomobileDetail.this,AutomobileActivity.class);
                startActivity(intent);
            }
        });

        update = (Button)findViewById(R.id.updateButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("UPDATE "+table_name+" SET "+PURCHASE_LITERS+"="+liters.getText()+", "+PURCHASE_PRICE+"="+price.getText()+", "
                        +PURCHASE_KM+"="+kilometers.getText()+" WHERE "+KEY_ID+"="+purchase_id);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.viewDetail), "Data was updated!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
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