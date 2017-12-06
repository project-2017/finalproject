package cst2335.groupproject.PkgAutomobile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cst2335.groupproject.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AutomobileFragment extends Fragment {

    private View view;
    private EditText liters,price,kilometers;
    private String literValue,priceValue,kmValue;

    public AutomobileFragment() {
        super();
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.auto_fragment_automobile, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Intent intent = new Intent(view.getContext(), AutomobileEntry.class);
//        startActivity(intent);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.menu_automobile);
//    }
}
