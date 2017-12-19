package cst2335.groupproject.PkgActivity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import cst2335.groupproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class T_Fragment_Update extends Fragment {

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
    LinearLayout l_date, l_time, l_comment, l_minute;

    /**
     * ImageViews
     */
    ImageView check, close, comment_check;

    /**
     * The id of row in database
     */
    String id;

    /**
     * Button delete
     */
    Button delete;

    /**
     * Strings
     */
    String type, minute, comment, date, time;

    public T_Fragment_Update() {
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
        view = inflater.inflate(R.layout.tracker_fragment_update, container, false);
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

        l_date = view.findViewById(R.id.tracker_update_date);
        l_time = view.findViewById(R.id.tracker_update_time);
        l_comment = view.findViewById(R.id.tracker_update_comment_dialog);
        l_minute = view.findViewById(R.id.tracker_update_minute);
        check = view.findViewById(R.id.tracker_insert_check);
        close = view.findViewById(R.id.tracker_insert_close);
        delete = view.findViewById(R.id.tracker_delete);

        if (getActivity().getLocalClassName().equals("PkgActivity.T_Update")) {
            // Get everything from activity list
            id = getActivity().getIntent().getStringExtra("Id");
            type = getActivity().getIntent().getStringExtra("Type");
            minute = getActivity().getIntent().getStringExtra("Minute");
            comment = getActivity().getIntent().getStringExtra("Comment");
            date = getActivity().getIntent().getStringExtra("Date");
            time = getActivity().getIntent().getStringExtra("Time");
        } else {
            id = getArguments().getString("Id");
            type = getArguments().getString("Type");
            minute = getArguments().getString("Minute");
            comment = getArguments().getString("Comment");
            date = getArguments().getString("Date");
            time = getArguments().getString("Time");
        }

        // Set spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.tracker_list_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter);
        if (!type.equals(null)) {
            int spinnerPosition = adapter.getPosition(type);
            spinner_type.setSelection(spinnerPosition);
        }

        editText_minute.setText(minute.replace(" Min", ""));
        textView_comment.setText(comment);

        if (!date.equals("")) {
            String[] dates = date.split("-");
            x_year = Integer.parseInt(dates[0]);
            x_month = Integer.parseInt(dates[1]) - 1;
            x_day = Integer.parseInt(dates[2]);
        }

        if (!time.equals("")) {
            String[] times = time.split(":");
            x_hour = Integer.parseInt(times[0]);
            x_minute = Integer.parseInt(times[1]);
        }

        setDate();
        setTime();

        // Hide soft keyboard
        editText_minute.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        l_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog datePicker = new DatePickerDialog(getActivity(), dpickerListner, x_year, x_month, x_day);
                datePicker.show();
            }
        });

        l_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), tpickerListner, x_hour, x_minute, true);
                timePicker.show();
            }
        });

        l_comment.setOnClickListener(new View.OnClickListener() {
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

        l_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText_minute, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minute = editText_minute.getText().toString();
                type = spinner_type.getSelectedItem().toString();
                date = textView_date.getText().toString();
                time = textView_time.getText().toString();
                comment = textView_comment.getText().toString();
                if (!editText_minute.getText().toString().equals("")) {
                    if (getActivity().getLocalClassName().equals("PkgActivity.T_Update")) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Id", id);
                        resultIntent.putExtra("Minute", minute);
                        resultIntent.putExtra("Type", type);
                        resultIntent.putExtra("Date", date);
                        resultIntent.putExtra("Time", time);
                        resultIntent.putExtra("Comment", comment);
                        getActivity().setResult(2, resultIntent);
                        getActivity().finish();
                    } else {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        T_Fragment_ActivityList fragment = (T_Fragment_ActivityList) fm.findFragmentById(R.id.tracker_main_container_fragment);
                        fragment.update(id, minute, type, date, time, comment);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.tracker_insert_empty, Toast.LENGTH_SHORT).show();

                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getLocalClassName().equals("PkgActivity.T_Update")) {
                    getActivity().finish();
                } else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    T_Fragment_ActivityList fragment = (T_Fragment_ActivityList) fm.findFragmentById(R.id.tracker_main_container_fragment);
                    fragment.closeSideBar();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent resultIntent = new Intent();
                resultIntent.putExtra("Id", id);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.tracker_delete_dialog_message);
                builder.setPositiveButton(R.string.tracker_delete_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int num) {
                        if (getActivity().getLocalClassName().equals("PkgActivity.T_Update")) {
                            getActivity().setResult(3, resultIntent);
                            getActivity().finish();
                        } else {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            T_Fragment_ActivityList fragment = (T_Fragment_ActivityList) fm.findFragmentById(R.id.tracker_main_container_fragment);
                            fragment.delete(id);
                        }
                    }
                })
                        .setNegativeButton(R.string.tracker_delete_dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .show();
            }
        });

    }

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
}
