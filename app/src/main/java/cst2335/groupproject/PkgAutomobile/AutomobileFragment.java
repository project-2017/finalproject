package cst2335.groupproject.PkgAutomobile;


import android.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.PURCHASE_YEAR;
import static cst2335.groupproject.PkgAutomobile.AutomobileDatabaseHelper.table_name;


/**
 * A simple {@link Fragment} subclass.
 */
public class AutomobileFragment extends Fragment {

    private View view;
    private EditText liters,price,kilometers;
    private String literValue,priceValue,kmValue;
    private Button saveButton;
    private AutomobileDatabaseHelper dHelper;
    private SQLiteDatabase db;

    public AutomobileFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.auto_fragment_automobile, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        saveButton = (Button)getView().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dHelper = new AutomobileDatabaseHelper(getActivity());
                db = dHelper.getWritableDatabase();
//                Get the current time and format the style
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String formattedDate = df.format(c.getTime());
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                liters = (EditText)getView().findViewById(R.id.LitersInput);
                price = (EditText)getView().findViewById(R.id.priceInput);
                kilometers = (EditText)getView().findViewById(R.id.KMInput);
                String valueL = liters.getText().toString();
                String valueP = price.getText().toString();
                String valueKM = kilometers.getText().toString();
                db.execSQL("INSERT INTO "+table_name+"(" +PURCHASE_DATE+","+PURCHASE_LITERS+","+PURCHASE_PRICE+","+PURCHASE_KM+","+PURCHASE_MONTH+","+PURCHASE_YEAR+") VALUES (" +formattedDate+", " +valueL+", " +valueP+ ", " +valueKM+", "+month+ ", " +year+")");
//                Create a toast to show the time when data saved
                Toast.makeText(getActivity(),"Your purchase had been saved at "+c.getTime(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
