package cst2335.groupproject.PkgActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import cst2335.groupproject.R;

/**
 * This class is used for creating the insert GUI of activity tracker
 *
 * @author Geyan Huang
 */
public class T_Insert extends AppCompatActivity {

    /**
     * On create
     *
     * @param savedInstanceState The savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_insert);

        Fragment fragment = new T_Fragment_Insert();
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tracker_insert_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * On configuration changed
     * @param newConfig The newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        finish();
    }
}
