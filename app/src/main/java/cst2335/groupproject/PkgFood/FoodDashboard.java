package cst2335.groupproject.PkgFood;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cst2335.groupproject.R;

public class FoodDashboard extends AppCompatActivity {

    private ProgressBar progressBar1, progressBar2;
    private FoodDatabaseHelper foodDatabaseHelper;
    private TextView summary1, summary2, totalCalories, totalDays;

    /**
     * Initializes this activity
     *
     * @param savedInstanceState contains the activity's previously frozen state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_dashboard);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        foodDatabaseHelper = new FoodDatabaseHelper(this);
        foodDatabaseHelper.openDatabase();

        summary1 = (TextView) findViewById(R.id.progress_circle_text1);
        summary1.setText(foodDatabaseHelper.getYesterdayCalories() + "");
        progressBar1.setProgress(foodDatabaseHelper.getYesterdayCalories());

        summary2 = (TextView) findViewById(R.id.progress_circle_text2);
        summary2.setText(foodDatabaseHelper.countCalories() / foodDatabaseHelper.countDays() + "");
        progressBar2.setProgress(foodDatabaseHelper.countCalories() / foodDatabaseHelper.countDays());

        totalCalories = (TextView) findViewById(R.id.totalCalories);
        totalCalories.setText(foodDatabaseHelper.countCalories() + "");

        totalDays = (TextView) findViewById(R.id.totalDays);
        totalDays.setText(foodDatabaseHelper.countDays() + "");
    }

    /**
     * Clicks on cross image to close current view
     *
     * @param view the dashboard view
     */
    public void close_return(View view) {
        finish();
    }

    /**
     * Inflates the menu resource into the Menu provided in the callback
     *
     * @param menu Menu type
     * @return true: adds items to the action bar if it is present
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.food_menu, menu);
        return true;
    }

    /**
     * User selects an item from the options menu to perform the appropriate action
     *
     * @param item an item from the options menu
     * @return true: returns the unique ID for the menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");
                Toast.makeText(this,R.string.food_Dashboard_toolbar_one_message, Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_two:
                Log.d("Toolbar", "Option 2 selected");
                finish();
                break;

            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.food_custom_dialog_help, null);
                dialogBuilder.setView(dialogView);

                dialogBuilder.setNegativeButton(R.string.food_Dialog_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                // Create the AlertDialog
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

        }
        return true;
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
