package cst2335.groupproject.PkgFood;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import cst2335.groupproject.R;

public class FoodUpdateDetails extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_update_details);

        Bundle bundle = new Bundle();
        bundle.putString("food_ID", getIntent().getStringExtra("food_ID"));
        bundle.putString("food_Name", getIntent().getStringExtra("food_Name"));
        bundle.putString("food_Serving", getIntent().getStringExtra("food_Serving"));
        bundle.putString("food_Calories", getIntent().getStringExtra("food_Calories"));
        bundle.putString("food_Fat", getIntent().getStringExtra("food_Fat"));
        bundle.putString("food_Carbohydrate", getIntent().getStringExtra("food_Carbohydrate"));
        bundle.putString("food_Date", getIntent().getStringExtra("food_Date"));
        bundle.putString("food_Time", getIntent().getStringExtra("food_Time"));

        FoodUpdateFragment fragment = new FoodUpdateFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.phone_frameLayout, fragment);
/*        //Call transaction.addToBackStack(String name) if you want to undo this transaction with the back button.
        ft.addToBackStack("A string");*/
        ft.commit();
    }
}
