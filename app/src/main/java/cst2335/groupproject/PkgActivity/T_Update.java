package cst2335.groupproject.PkgActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import cst2335.groupproject.R;

/**
 * This class is used for creating the update GUI of activity tracker
 *
 * @author Geyan Huang
 */
public class T_Update extends Activity {


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
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tracker_insert_container, fragment);
        fragmentTransaction.commit();
    }

}
