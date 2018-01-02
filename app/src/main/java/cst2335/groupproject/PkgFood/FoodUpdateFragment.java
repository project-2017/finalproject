package cst2335.groupproject.PkgFood;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import cst2335.groupproject.R;


public class FoodUpdateFragment extends Fragment {

    private View view;
    private EditText updateFoodName, updateServings, updateCalories, updateFat, updateCarbohydrate, updateDate, updateTime;
    private Button buttonUpdate, buttonDelete;
    private FoodDatabaseHelper foodDatabaseHelper;
    private String columnID;
    private ImageView crossButton;

    /**
     * Default constructor
     */
    public FoodUpdateFragment() {

    }

    /**
     * Creates and returns the view hierarchy associated with the fragment
     *
     * @param inflater the inflater
     * @param container the container
     * @param savedInstanceState contains the activity's previously frozen state
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.food_fragment_food_update, container, false);
        return view;
    }

    /**
     * Called when the fragment's activity has been created and this fragment's view hierarchy instantiated
     *
     * @param savedInstanceState contains the activity's previously frozen state
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateFoodName = (EditText) view.findViewById(R.id.addFoodName);
        updateServings = (EditText) view.findViewById(R.id.addServings);
        updateCalories = (EditText) view.findViewById(R.id.addCalories);
        updateFat = (EditText) view.findViewById(R.id.addFat);
        updateCarbohydrate = (EditText) view.findViewById(R.id.addCarbo);
        updateDate = (EditText) view.findViewById(R.id.addDate);
        updateTime = (EditText) view.findViewById(R.id.addTime);
        buttonUpdate = (Button) view.findViewById(R.id.updateButton);
        buttonDelete = (Button) view.findViewById(R.id.deleteButton);
        crossButton = view.findViewById(R.id.cross);
        foodDatabaseHelper = new FoodDatabaseHelper(view.getContext());

        columnID = getArguments().getString("food_ID"); //pass food_ID from FoodList Intent
        updateFoodName.setText(getArguments().getString("food_Name"));
        updateServings.setText(getArguments().getString("food_Serving"));
        updateCalories.setText(getArguments().getString("food_Calories"));
        updateFat.setText(getArguments().getString("food_Fat"));
        updateCarbohydrate.setText(getArguments().getString("food_Carbohydrate"));
        updateDate.setText(getArguments().getString("food_Date"));
        updateTime.setText(getArguments().getString("food_Time"));

        foodDatabaseHelper.openDatabase();

        //Get a calendar using the default locale and time-zone
        final Calendar myCalendar = Calendar.getInstance();
        final Calendar myCurrentTime = Calendar.getInstance();

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

        /**
         * Pops up date picker dialog when user clicks Date edit text
         */
        updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), date, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        /**
         * Pops up time picker dialog when user clicks Date edit text
         */
        updateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                int hour = myCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = myCurrentTime.get(Calendar.MINUTE);

                TimePickerDialog myTimePicker;
                myTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        myCurrentTime.set(Calendar.MINUTE, selectedMinute);
                        updateTime.setText(timeFormat.format(myCurrentTime.getTime()));
                    }
/*                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        addTime.setText(selectedHour + ":" + selectedMinute);
                    }*/
                }, hour, minute, false);
                myTimePicker.show();
            }
        });

        /**
         * Updates information when user changes any fields
         */
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                foodDatabaseHelper.updateData(getArguments().getString("food_ID"),
                        updateFoodName.getText().toString(),
                        updateServings.getText().toString(),
                        updateCalories.getText().toString(),
                        updateFat.getText().toString(),
                        updateCarbohydrate.getText().toString(),
                        updateDate.getText().toString(),
                        updateTime.getText().toString());

                getActivity().finish();
            }
        });

        /**
         * Deletes item form ListView and remove from database
         */
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                foodDatabaseHelper.deleteData(columnID);
                getActivity().finish();
            }

        });
    }

    /**
     * Closes the database
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        foodDatabaseHelper.closeDatabase();
    }

}

