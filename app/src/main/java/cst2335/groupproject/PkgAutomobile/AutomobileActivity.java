package cst2335.groupproject.PkgAutomobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import cst2335.groupproject.R;

import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.KEY_ID;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_DATE;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_LITERS;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_MONTH;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_PRICE;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.table_name;

public class AutomobileActivity extends Activity {

    private static final String ACTIVITY_NAME = "AutomobileActivity";
    private Button addButton, monthlyPurchaseButton, averPriceButton, totalPurchasedButton;
    private ListView purchaseList;
    private TextView averagePrice, lastTotal;
    private AutomobileDatabaseHelper dHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<String> timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_activity_automobile);

        addButton = (Button)findViewById(R.id.addNew);
        monthlyPurchaseButton = (Button)findViewById(R.id.everyMonthPurchased);
        averPriceButton = (Button)findViewById(R.id.lastMonthAveragePrice);
        totalPurchasedButton = (Button)findViewById(R.id.lastMonthPurchased);
        purchaseList = (ListView)findViewById(R.id.purchaseList);
        lastTotal = (TextView)findViewById(R.id.lastMonthPurchasedResult);
        averagePrice = (TextView)findViewById(R.id.lastMonthAveragePriceResult);

        createView();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(AutomobileActivity.this,AutomobileEntry.class);
                startActivity(addIntent);
            }
        });

        monthlyPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monthly = new Intent(AutomobileActivity.this,AutomobileMonthlypurchase.class);
                startActivity(monthly);
            }
        });

        purchaseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String recordTime = parent.getItemAtPosition(position).toString();
                Long Id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
                Intent intent = new Intent(AutomobileActivity.this,AutomobileDetail.class);
                intent.putExtra("time",recordTime);
                intent.putExtra("id",Id);
                startActivityForResult(intent,10);
            }
        });

        averPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dHelper = new AutomobileDatabaseHelper(AutomobileActivity.this);
                db = dHelper.getWritableDatabase();
                Calendar c = Calendar.getInstance();
                int lastMonth = c.get(Calendar.MONTH)-1;
                cursor = db.rawQuery("SELECT AVG("+PURCHASE_PRICE+") FROM "+table_name+ " WHERE "+PURCHASE_MONTH+" = '"+lastMonth+"'",null);
                cursor.moveToFirst();
                Double averageP = cursor.getDouble(0);
                averagePrice.setText(String.format("%.2f",averageP));
                cursor.close();
            }
        });

        totalPurchasedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dHelper = new AutomobileDatabaseHelper(AutomobileActivity.this);
                db = dHelper.getWritableDatabase();
                Calendar c = Calendar.getInstance();
                int lastMonth = c.get(Calendar.MONTH)-1;
                cursor = db.rawQuery("SELECT SUM("+PURCHASE_LITERS+") FROM "+table_name+ " WHERE "+PURCHASE_MONTH+" = '"+lastMonth+"'",null);
                cursor.moveToFirst();
                Long totalL = cursor.getLong(0);
                lastTotal.setText(totalL.toString());
                cursor.close();
            }
        });
    }

    public class listAdapter extends ArrayAdapter<String>{

        public listAdapter (Context ctx){ super(ctx,0); }

        public int getCount(){ return timeList.size(); }

        public String getItem(int position){
            return timeList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = AutomobileActivity.this.getLayoutInflater();
            View result=null;
            result = inflater.inflate(R.layout.auto_timelist_automobile,null);

            TextView List = (TextView)result.findViewById(R.id.timeList);
            List.setText(getItem(position));
            return result;
        }

        public long getItemId(int position){
            createView();
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(KEY_ID));
        }
    }

    public void createView(){
        timeList = new ArrayList<>();

        listAdapter listTime = new listAdapter(this);
        purchaseList.setAdapter(listTime);

        dHelper = new AutomobileDatabaseHelper(this);
        db = dHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " +table_name,null);
        int numResults = cursor.getCount();
        int numColumns = cursor.getColumnCount();

        cursor.moveToFirst();//resets the iteration of results
        while(!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL GAS PURCHASED ON : " + cursor.getString(cursor.getColumnIndex("DATE")));
            String one = cursor.getString(cursor.getColumnIndex("DATE"));
            String showList = ""+one.charAt(0)+one.charAt(1)+one.charAt(2)+one.charAt(3)+"-"+one.charAt(4)+one.charAt(5)+"-"+
                    one.charAt(6)+one.charAt(7)+" "+one.charAt(8)+one.charAt(9)+":"+one.charAt(10)+one.charAt(11)+":"+one.charAt(12)+one.charAt(13);
            timeList.add(showList);
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME,"Cursor's column count = " + numColumns);

        cursor.moveToFirst();
        for(int i=0;i<numResults;i++){
            Log.i(ACTIVITY_NAME,"The " + i + " purchase was made on " + cursor.getString(cursor.getColumnIndex(PURCHASE_DATE)));
            cursor.moveToNext();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        createView();
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
