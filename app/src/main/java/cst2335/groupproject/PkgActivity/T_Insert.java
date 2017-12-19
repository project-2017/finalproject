package cst2335.groupproject.PkgActivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import cst2335.groupproject.R;

/**
 * This class is used for creating the insert GUI of activity tracker
 *
 * @author Geyan Huang
 */
public class T_Insert extends Activity {

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
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tracker_insert_container, fragment);
        fragmentTransaction.commit();
    }
}
