package cst2335.groupproject.PkgHouse.homeThermo_mainPkg.fragmentTempPkg;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import cst2335.groupproject.R;


//public class FragmentMainActivity extends FragmentActivity {
public class FragmentMainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_fragment_main);

        Fragment editFragment = new Fragment_editTemp();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.tempContainer_h, editFragment)
//                .add(R.id.fragment_container_2_h, editFragment)
                .commit();

/*        android.support.v4.app.FragmentTransaction fragmentTransaction_1 = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction_1.add(tempContainer_h, editFragment)
                .commit()

        this.getSupportFragmentManager()
                .beginTransaction()
                .add(tempContainer_h,editFragment)
                .commit();
//        .add(R.id.tempContainer_h, editFragment)*/
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    //    interface SendMessage{
//        void getData_fromFragment();
//
//    }
//
//    @Override
//    public void onAttachFragment(Fragment fragment) {
//        super.onAttachFragment(fragment);
//    }

}