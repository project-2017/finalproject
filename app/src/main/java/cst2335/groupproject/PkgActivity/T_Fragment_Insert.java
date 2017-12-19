package cst2335.groupproject.PkgActivity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import cst2335.groupproject.R;

/**
 * This class is used for creating the insert GUI of activity tracker
 *
 * @author Geyan Huang
 */
public class T_Fragment_Insert extends Fragment {

    /**
     * The view of the fragment
     */
    private View view;

    /**
     * TextViews
     */
    TextView textView_date, textView_time, textView_comment;

    /**
     * Spinner
     */
    Spinner spinner_type;

    /**
     * AlertDialog for comment
     */
    AlertDialog commentDialog;

    /**
     * Year, month, day, hour, minute
     */
    int x_year, x_month, x_day, x_hour, x_minute;

    /**
     * Dialog ID for date picker
     */
    static final int DIALOG_ID_DATE = 1;

    /**
     * Dialog ID for time picker
     */
    static final int DIALOG_ID_TIME = 2;

    /**
     * EditTexts
     */
    EditText editText_minute, editText_comment;

    /**
     * LinearLayouts
     */
    LinearLayout date, time, comment, minute;

    ImageView check, close, comment_check;

    public T_Fragment_Insert() {
        // Required empty public constructor
    }


    /**
     * On create view
     *
     * @param inflater           The inflater
     * @param container          The container
     * @param savedInstanceState The savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tracker_fragment_insert, container, false);
        return view;
    }

    /**
     * On activity created
     *
     * @param savedInstanceState The savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText_minute = view.findViewById(R.id.tracker_insert_editText_minute);
        spinner_type = view.findViewById(R.id.tracker_insert_spinner_type);
        textView_date = view.findViewById(R.id.tracker_insert_textView_date);
        textView_time = view.findViewById(R.id.tracker_insert_textView_time);
        textView_comment = view.findViewById(R.id.tracker_insert_textView_comment);

        date = view.findViewById(R.id.tracker_insert_date);
        time = view.findViewById(R.id.tracker_insert_time);
        comment = view.findViewById(R.id.tracker_insert_comment_dialog);
        minute = view.findViewById(R.id.tracker_insert_minute);
        check = view.findViewById(R.id.tracker_insert_check);
        close = view.findViewById(R.id.tracker_insert_close);


        // Set current date
        final Calendar cal = Calendar.getInstance();
        x_year = cal.get(Calendar.YEAR);
        x_month = cal.get(Calendar.MONTH);
        x_day = cal.get(Calendar.DAY_OF_MONTH);
        x_hour = cal.get(Calendar.HOUR_OF_DAY);
        x_minute = cal.get(Calendar.MINUTE);

        setDate();
        setTime();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog datePicker = new DatePickerDialog(getActivity(), dpickerListner, x_year, x_month, x_day);
                datePicker.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), tpickerListner, x_hour, x_minute, true);
                timePicker.show();
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder commentBuilder = new AlertDialog.Builder(getActivity());
                View commentView = getActivity().getLayoutInflater().inflate(R.layout.tracker_insert_dialog_comment, null);
                comment_check = commentView.findViewById(R.id.tracker_insert_comment_check);
                editText_comment = commentView.findViewById(R.id.tracker_insert_dialog_comment_editText);
                editText_comment.setText(textView_comment.getText());


                commentBuilder.setView(commentView);
                commentDialog = commentBuilder.create();
                commentDialog.setCanceledOnTouchOutside(false);
                commentDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                commentDialog.show();
                editText_comment.requestFocus();

                comment_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            textView_comment.setText(editText_comment.getText());
                            commentDialog.dismiss();
                        }
                });
            }
        });

        minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(editText_minute, InputMethodManager.SHOW_IMPLICIT);
                }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_minute.getText().toString().equals("")) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Minute", editText_minute.getText().toString());
                    resultIntent.putExtra("Type", spinner_type.getSelectedItem().toString());
                    resultIntent.putExtra("Date", textView_date.getText().toString());
                    resultIntent.putExtra("Time", textView_time.getText().toString());
                    resultIntent.putExtra("Comment", textView_comment.getText().toString());
                    getActivity().setResult(1, resultIntent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), R.string.tracker_insert_empty, Toast.LENGTH_SHORT).show();

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }


    /**
     * Date Picker
     */
    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            x_year = year;
            x_month = month;
            x_day = dayOfMonth;

            setDate();
        }
    };

    /**
     * Function for setting date
     */
    private void setDate() {
        String year = Integer.toString(x_year);
        String month = Integer.toString((x_month + 1));
        String day = Integer.toString(x_day);
        if (x_month < 9) {

            month = "0" + (x_month + 1);
        }
        if (x_day < 10) {

            day = "0" + x_day;
        }
        textView_date.setText((year + "-" + month + "-" + day));
    }

    /**
     * Time picker
     */
    private TimePickerDialog.OnTimeSetListener tpickerListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            x_hour = hourOfDay;
            x_minute = minute;

            setTime();
        }
    };

    /**
     * Function for setting time
     */
    private void setTime() {
        String hour = Integer.toString(x_hour);
        String minute = Integer.toString(x_minute);
        if (x_hour < 10) {

            hour = "0" + x_hour;
        }
        if (x_minute < 10) {

            minute = "0" + x_minute;
        }
        textView_time.setText(hour + ":" + minute);
    }
}
