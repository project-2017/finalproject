package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cst2335.groupproject.R;

public class FoodAdd extends Activity {

    /*    protected static final String ACTIVITY_NAME = "AddItemsActivity";
        private String foodAdd = FoodAddActivity.class.getSimpleName();*/
    private EditText addFoodName, addServings, addCalories, addFat, addCarbohydrate, addDate, addTime;
    private String addFoodAndServings;
    private Button buttonAdd;
    private FoodDatabaseHelper foodDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_add);

        addFoodName = (EditText) findViewById(R.id.addFoodName);
        addServings = (EditText) findViewById(R.id.addServings);
        addCalories = (EditText) findViewById(R.id.addCalories);
        addFat = (EditText) findViewById(R.id.addFat);
        addCarbohydrate = (EditText) findViewById(R.id.addCarbo);
        addDate = (EditText) findViewById(R.id.addDate);
        addTime = (EditText) findViewById(R.id.addTime);
        buttonAdd = (Button) findViewById(R.id.addButton);
        foodDatabaseHelper = new FoodDatabaseHelper(this);

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
                addDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodAdd.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Pop up time picker dialog when user clicks Time edit text
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hour = myCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = myCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog myTimePicker;
                myTimePicker = new TimePickerDialog(FoodAdd.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        addTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);
                myTimePicker.show();
            }
        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!addFoodName.getText().toString().equals("") && !addServings.getText().toString().equals("")
                        && !addCalories.getText().toString().equals("") && !addFat.getText().toString().equals("")
                        && !addCarbohydrate.getText().toString().equals("") && !addDate.getText().toString().equals("")
                        && !addTime.getText().toString().equals("")) {

                    //Insert records into database
                    boolean isInserted = foodDatabaseHelper.insertData(addFoodName.getText().toString(),
                            addServings.getText().toString(),
                            addCalories.getText().toString(),
                            addFat.getText().toString(),
                            addCarbohydrate.getText().toString(),
                            addDate.getText().toString(),
                            addTime.getText().toString());
                    if (isInserted == true) {
                        Toast.makeText(FoodAdd.this, "Information was added successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(FoodAdd.this, "Failed to add information.", Toast.LENGTH_SHORT).show();

/*                    Intent resultIntent = new Intent();

                    if (addServings.getText().toString().equals("1")) {
                        addFoodAndServings = addServings.getText().toString() + " " + addFoodName.getText().toString();
                    } else
                        addFoodAndServings = addServings.getText().toString() + " " + addFoodName.getText().toString() + "s";

                    resultIntent.putExtra("addFoodAndServings", addFoodAndServings);
                    setResult(1, resultIntent);
                    finish();//Go back to FoodListView class after user presses Add button*/
                } else {
                    Toast.makeText(getApplicationContext(), R.string.food_empty_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //Click on cross image to close current activity
    public void close_return(View view) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodDatabaseHelper.closeDatabase();
    }
}
