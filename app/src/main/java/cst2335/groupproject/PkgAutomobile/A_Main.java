package cst2335.groupproject.PkgAutomobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cst2335.groupproject.PkgMain.M_SharedPreference;
import cst2335.groupproject.R;


/**
 * This class is used for creating the main GUI of automobile
 */
public class A_Main extends Fragment {

    private View view;
    /**
     * Using M_SharedPreference
     */
    private M_SharedPreference sharedPreference = new M_SharedPreference();

    /**
     * The construction
     */
    public A_Main() {
        // Required empty public constructor
    }

    /**
     * On create view
     *
     * @param inflater           The inflater
     * @param container          The container
     * @param savedInstanceState The savedInstanceState
     * @return The view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.automobile_main, container, false);

        // Set current layout
        sharedPreference.setLayout(view.getContext(), "A_Main");

        Intent intent = new Intent(view.getContext(), AutomobileActivity.class);
        startActivity(intent);
        return view;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());
        builder1.setTitle("Do you want to enter?");
        // Add the buttons
        builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(view.getContext(), AutomobileActivity.class);
                startActivity(intent);
            }
        });
        builder1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog
        AlertDialog dialog1 = builder1.create();
        dialog1.show();
        return true;
    }


    /**
     * On resume
     */
    @Override
    public void onResume() {
        super.onResume();

        // Set title for action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.main_menu_automobile);
    }
}
