package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cst2335.groupproject.R;

public class FoodUpdate extends Activity {

    private EditText updateFoodName, updateServings, updateCalories, updateFat, updateCarbohydrate, updateDate, updateTime;
    private Button buttonUpdate, buttonDelete;
    private FoodDatabaseHelper foodDatabaseHelper;
    private String columnID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_update);

        updateFoodName = (EditText) findViewById(R.id.addFoodName);
        updateServings = (EditText) findViewById(R.id.addServings);
        updateCalories = (EditText) findViewById(R.id.addCalories);
        updateFat = (EditText) findViewById(R.id.addFat);
        updateCarbohydrate = (EditText) findViewById(R.id.addCarbo);
        updateDate = (EditText) findViewById(R.id.addDate);
        updateTime = (EditText) findViewById(R.id.addTime);
        buttonUpdate = (Button) findViewById(R.id.updateButton);
        buttonDelete = (Button) findViewById(R.id.deleteButton);
        foodDatabaseHelper = new FoodDatabaseHelper(this);

        columnID = getIntent().getStringExtra("food_ID"); //pass food_ID from FoodList Intent
        updateFoodName.setText(getIntent().getStringExtra("food_Name"));
        updateServings.setText(getIntent().getStringExtra("food_Serving"));
        updateCalories.setText(getIntent().getStringExtra("food_Calories"));
        updateFat.setText(getIntent().getStringExtra("food_Fat"));
        updateCarbohydrate.setText(getIntent().getStringExtra("food_Carbohydrate"));
        updateDate.setText(getIntent().getStringExtra("food_Date"));
        updateTime.setText(getIntent().getStringExtra("food_Time"));

        foodDatabaseHelper.openDatabase();

        //Get a calendar using the default locale and time-zone
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCurrentTime = Calendar.getInstance();

        //Pop up date picker dialog when user clicks Date edit text
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
                updateDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodUpdate.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Pop up time picker dialog when user clicks Time edit text
        updateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour = myCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = myCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog myTimePicker;
                myTimePicker = new TimePickerDialog(FoodUpdate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        updateTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                myTimePicker.show();
            }
        });

        //Update information when user changes any fields
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                //Use toast to test if food_ID from FoodList Intent is passed or not. If yes, it should display passed id number.
                Toast.makeText(FoodUpdate.this, getIntent().getStringExtra("food_ID"), Toast.LENGTH_SHORT).show();*/
                foodDatabaseHelper.updateData(getIntent().getStringExtra("food_ID"),
                        updateFoodName.getText().toString(),
                        updateServings.getText().toString(),
                        updateCalories.getText().toString(),
                        updateFat.getText().toString(),
                        updateCarbohydrate.getText().toString(),
                        updateDate.getText().toString(),
                        updateTime.getText().toString());

                finish();
            }
        });

        //Delete item form ListView and remove from database
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodDatabaseHelper.deleteData(columnID);
                finish();
            }
        });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodDatabaseHelper.closeDatabase();
    }

    //Click on cross image to close current activity
    public void close_return(View view) {
        finish();
    }
}
