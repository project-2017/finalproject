package cst2335.groupproject.PkgHouse.adapterPkg;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import cst2335.groupproject.R;

/**
 * help file for the user to understand the activity
 */
public class HelpActivity extends Activity {

    private String helpItem; // string to hold the information to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity_help);

        TextView textView_help = findViewById(R.id.helptextID_h);


        helpItem = getIntent().getStringExtra("helpItem");
        switch(helpItem){
            case "helpAdd":

                textView_help.setText(Html.fromHtml (getString(R.string.helpText_stringHTML_About_h)) );

                Toast.makeText(getApplicationContext(), "Navigation bar add Clicked!",
                        Toast.LENGTH_SHORT).show();
                break;
            case "helpDelete":
                textView_help.setText(Html.fromHtml(  getString(R.string.helpText_stringHTML_Instruction_h)));
                Toast.makeText(getApplicationContext(), "Navigation bar delete Clicked!",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}