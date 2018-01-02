package cst2335.groupproject.PkgHouse.homeThermo_mainPkg.fragmentTempPkg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Map;
import java.util.TreeMap;

import cst2335.groupproject.PkgHouse.DTO.DTO_TemperatureSetting;
import cst2335.groupproject.PkgHouse.homeThermo_mainPkg.HouseThermostatActivity;
import cst2335.groupproject.R;

/**
 * Created by H.LIU on 2017-11-12.
 *
 *  the fragment to handle editing the temperature settings
 */

public class Fragment_editTemp extends Fragment {
    private Button button_save; // not used hiden from the user; future improvement
    private Button button_cancel; // go back to main page
    private Button button_newRule; // save as the new rule
    private Button button_delete; // delete


    //    private NumberPicker numberPicker_hour;
    private TimePicker timePicker; // pick time
    private Spinner spinner_day; // day of week
    private double temperature; // hole the user input of the temperature

    private final double default_temp = 20.0;
    private DTO_TemperatureSetting newTemp; // DTO


    private TreeMap<Integer, Double> listTemp; // tree map to hold the temperature settings

    private int time_msg; // valuse passed from/to main activity
    private double temperature_msg; // valuse passed from/to main activity

    private View myView; // view from the main activity

    private static final int DELETE_ITEM = 20; // valuse passed from/to main activity code

    //--------------------------------------------
    public Fragment_editTemp() {
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.temp_fragment_temp_setting, container, false);
        myView = view;
        return view;
    }

//    fragmentLayout_tempEdit_h

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("Fragment temp", "Fragment onResume");
//    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timePicker = myView.findViewById(R.id.timePicker_fg_h);
        timePicker.setIs24HourView(true);



//        numberPicker_hour = myView.findViewById(R.id.numberPicker_hours_fg_h);
//        numberPicker_min = myView.findViewById(R.id.numberPicker_min_fg_h);
//
//        numberPicker_hour.setMinValue(0);
//        numberPicker_hour.setMaxValue(23);
//        numberPicker_min.setMinValue(0);
//        numberPicker_min.setMaxValue(59);


        button_save = myView.findViewById(R.id.button_save_fg_h);
        button_cancel = myView.findViewById(R.id.button_cancel_fg_h);

        button_newRule = myView.findViewById(R.id.button_saveNewRule_fg_h);
        button_delete = myView.findViewById(R.id.button_delete_fg_h);


        spinner_day = myView.findViewById(R.id.spinner_fg_h);
        final EditText temp_editText = myView.findViewById(R.id.temperature_fg_h);

        //-------------get the data from main fragment - phone mode with vertical

        Log.i("ActivityName",getActivity().getLocalClassName());
        //horizential mode; the main activity is the main activity;
        if(getActivity().getLocalClassName().equals("PkgHouse.homeThermo_mainPkg.HouseThermostatActivity")){
            listTemp = new TreeMap<>((Map<Integer, Double>) getArguments().getSerializable("treeMap"));
            time_msg = getArguments().getInt("newItem_time");
            temperature_msg = getArguments().getDouble("newItem_temp");
        }else {
            // the vertical mode; the main activity is the FragmentMainActivity;
            listTemp = new TreeMap<>((Map<Integer, Double>) getActivity().getIntent().getExtras().get("treeMap"));
            time_msg = getActivity().getIntent().getIntExtra("newItem_time", 0);
            temperature_msg = getActivity().getIntent().getDoubleExtra("newItem_temp", -900);
        }


        // display to the head which setting is picked by user;
        // str is the tostring from DTO
        String str = (new DTO_TemperatureSetting(time_msg, temperature_msg)).toString();
        TextView title = myView.findViewById(R.id.stringText_mainfragement_title);
//        str = R.string.tempRule_selected_String_h + str;
        title.setText(str);

        //if the listTemp is null; new list is created
        if (listTemp == null) {
            listTemp = new TreeMap<>();
        } else {
        }


        //-------------------------------------------

        /**
         * 2 cases; vertical mode or the horizental mode
         *
         */
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getLocalClassName().equals("PkgHouse.homeThermo_mainPkg.HouseThermostatActivity")){
                    ((HouseThermostatActivity) getActivity()).closeSideBar();
                }else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newItem_time", time_msg);
                    resultIntent.putExtra("newItem_temp", temperature_msg);


                    getActivity().setResult(Activity.RESULT_CANCELED, resultIntent);
                    getActivity().finish();
                }
            }
        });

        /**
         * 2 cases; vertical mode or the horizental mode
         *
         */
        button_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(getActivity().getLocalClassName().equals("PkgHouse.homeThermo_mainPkg.HouseThermostatActivity")){
                    // find the main activity and call the function in the main activity
                    ((HouseThermostatActivity) getActivity()).delete(time_msg,temperature_msg);
                }else {
                    //                time_msg = getActivity().getIntent().getIntExtra("newItem_time", 0);
//                temperature_msg = getActivity().getIntent().getDoubleExtra("newItem_temp", -900);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("newItem_time", time_msg);
                    resultIntent.putExtra("newItem_temp", temperature_msg);

                    getActivity().setResult(DELETE_ITEM, resultIntent);
                    getActivity().finish();
                }

            }
        });

        /**
         * 2 cases; vertical mode or the horizental mode
         *
         */
        button_newRule.setOnClickListener(new View.OnClickListener() {
//        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String day;
                int hourInput, minInput;

                /*
                 get the user input
                  */
                day = spinner_day.getSelectedItem().toString();
//                Log.i("list temp", "click the save button " + day );
//                minInput = numberPicker_min.getValue();
                hourInput = timePicker.getCurrentHour();
                minInput = timePicker.getCurrentMinute();

//                Log.i("list temp", "click the save button " + hourInput + " " + minInput);

                /*
                if no value entered, take 20 degree as the default value
                 */
                String s = temp_editText.getText().toString();
                s = s.trim();
                if ((!s.equals(""))) {
                    temperature = Double.parseDouble(s);
                } else {
                    temperature = default_temp;
                }
//                Log.i("list temp", "click the save button " + temperature);
//                DTO_TemperatureSetting newTemp = new DTO_TemperatureSetting();

                /*
                check if the time exist, if yes, ask user to confirm the changes
                 */
                newTemp = new DTO_TemperatureSetting(day, hourInput, minInput, temperature);
//                Log.i("list temp", "click the save button " + newTemp.toString());

                // time rule exists
                if (listTemp.get(newTemp.getTimeOfWeek()) != null) {
                    AlertDialog.Builder dialogListener = new AlertDialog.Builder(getActivity());
                    dialogListener.setTitle(R.string.title_addNew_h);
                    dialogListener.setMessage(R.string.timeExist_overwriteQuestion_h);
                    dialogListener.setPositiveButton(R.string.OK_h, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //------------------add to the treemap
                            addNewTempItem_exit(newTemp);
                        }
                    });

                    dialogListener.setNegativeButton(R.string.CANCEL_h, null);
                    dialogListener.create();
                    dialogListener.show();

                } else {
                    /*
                    2 cases; vertical mode or the horizental mode
                     */
                    addNewTempItem_exit(newTemp);
                }

//                //------------------add to the treemap
//                listTemp.put(newTemp.getTimeOfWeek(), temperature);
//
//                for (Map.Entry<Integer, Double> entry : listTemp.entrySet() ) {
//                    Integer time_key = entry.getKey();
//                    Double temp = entry.getValue();
//                    DTO_TemperatureSetting dto = new DTO_TemperatureSetting(time_key, temp);
////                    newTemp.setTemp(temp);
////                    newTemp.setTimeOfWeek(time_key);
//                    Log.i("list temp", "click the save button - added treeMap " + dto.toString());
//                }
            }
        });
    }

    /**
     * 2 cases; vertical mode or the horizental mode
     *
     * @param newTemp
     */
    private void addNewTempItem_exit(DTO_TemperatureSetting newTemp) {
        if(getActivity().getLocalClassName().equals("PkgHouse.homeThermo_mainPkg.HouseThermostatActivity")){
            /*
            horizontal mode
             */
            ((HouseThermostatActivity) getActivity()).Change(listTemp,newTemp.getTimeOfWeek(),newTemp.getTemp());
        }else{
            listTemp.put(newTemp.getTimeOfWeek(), newTemp.getTemp());
            //--------pass the current treelist to add
            Intent resultIntent = new Intent();
            resultIntent.putExtra("treeMap", listTemp);

            resultIntent.putExtra("newItem_time", newTemp.getTimeOfWeek());
            resultIntent.putExtra("newItem_temp", newTemp.getTemp());
            //-------------------
            getActivity().setResult(Activity.RESULT_OK, resultIntent);

            //display for testing
            for (Map.Entry<Integer, Double> entry : listTemp.entrySet()) {
                Integer time_key = entry.getKey();
                Double temp = entry.getValue();
                DTO_TemperatureSetting dto = new DTO_TemperatureSetting(time_key, temp);
                Log.i("list temp", "click the save button - added treeMap " + dto.toString());
            }
            getActivity().finish();
        }
    }

    /**
     * the vertical mode; delete the item user selected;
     * @param newTemp
     */
    private void deleteTempItem_exit(DTO_TemperatureSetting newTemp) {

//        listTemp.put(newTemp.getTimeOfWeek(), newTemp.getTemp());
        //--------pass the current treelist to add
        Intent resultIntent = new Intent();

        resultIntent.putExtra("newItem_time", newTemp.getTimeOfWeek());
        resultIntent.putExtra("newItem_temp", newTemp.getTemp());

//        listTemp.remove(newTemp.getTimeOfWeek());
//        resultIntent.putExtra("treeMap", listTemp);
        //-------------------
//        getActivity().setResult(Activity.RESULT_OK, resultIntent);

        getActivity().setResult(DELETE_ITEM, resultIntent);

        //display for testing
        for (Map.Entry<Integer, Double> entry : listTemp.entrySet()) {
            Integer time_key = entry.getKey();
            Double temp = entry.getValue();
            DTO_TemperatureSetting dto = new DTO_TemperatureSetting(time_key, temp);
            Log.i("list temp", "click the save button - added treeMap " + dto.toString());
        }
        getActivity().finish();
    }

}
