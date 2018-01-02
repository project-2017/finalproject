package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

public class FoodAdd extends Activity {

    private EditText addFoodName, addServings, addCalories, addFat, addCarbohydrate, addDate, addTime;
    private Button buttonAdd;
    private FoodDatabaseHelper foodDatabaseHelper;

    /**
     * Initializes this activity
     *
     * @param savedInstanceState contains the activity's previously frozen state
     */
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

        /**
         * Gets a calendar using the default locale and time-zone
         */
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCurrentTime = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String dateFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.CANADA);
                addDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        /**
         * Pops up date picker dialog when user clicks Date edit text
         */
        addDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FoodAdd.this, date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         * Pops up time picker dialog when user clicks Time edit text
         */
        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                int hour = myCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = myCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog myTimePicker;
                myTimePicker = new TimePickerDialog(FoodAdd.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCurrentTime.set(Calendar.MINUTE, selectedMinute);
                        addTime.setText(timeFormat.format(myCurrentTime.getTime()));
                    }
                }, hour, minute, false);
                myTimePicker.show();
            }
        });

        /**
         * Clicks on Add button to insert food information into database
         * User must be fill out all required fields
         * If data inserted successfully, displays a toast to show confirmation message. Otherwise, displays an failure message
         */
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!addFoodName.getText().toString().equals("") && !addServings.getText().toString().equals("")
                        && !addCalories.getText().toString().equals("") && !addFat.getText().toString().equals("")
                        && !addCarbohydrate.getText().toString().equals("") && !addDate.getText().toString().equals("")
                        && !addTime.getText().toString().equals("")) {

                    if(Integer.parseInt(addServings.getText().toString()) <= 0) {
                        Toast.makeText(FoodAdd.this, R.string.food_Add_serving_warning, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Insert records into database
                        boolean isInserted = foodDatabaseHelper.insertData(addFoodName.getText().toString(),
                                addServings.getText().toString(),
                                addCalories.getText().toString(),
                                addFat.getText().toString(),
                                addCarbohydrate.getText().toString(),
                                addDate.getText().toString(),
                                addTime.getText().toString());
                        if (isInserted == true) {
                            Toast.makeText(FoodAdd.this, R.string.food_Add_infoAddSuccessful, Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(FoodAdd.this, R.string.food_Add_infoAddUnsuccessful, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.food_Add_empty_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Clicks on the cross image to close current activity
     *
     * @param view the Food Add view
     */
    //Click on cross image to close current activity
    public void close_return(View view) {
        finish();
    }

    /**
     * Closes the database
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        foodDatabaseHelper.closeDatabase();
    }
}
