package cst2335.groupproject.PkgActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import cst2335.groupproject.R;

/**
 * This class is used for creating the update GUI of activity tracker
 *
 * @author Geyan Huang
 */
public class T_Update extends AppCompatActivity {


    /**
     * On create
     *
     * @param savedInstanceState The savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_update);

        Fragment fragment = new T_Fragment_Update();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tracker_insert_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        finish();
    }
}
