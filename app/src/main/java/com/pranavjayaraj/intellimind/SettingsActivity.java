package com.pranavjayaraj.intellimind;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Pranav on 1/8/19.
 */
public class SettingsActivity extends AppCompatActivity
{
    Switch search,stop;//Buttons to activate and deactivate the voice listener
    Spinner language;//language Dropdown
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String lang[]={};//Language for displaying in settings
    String langCode[];//Language code for the corresponding chosen language
    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Resources res = getResources();
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        search = (Switch) findViewById(R.id.search);
        search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    editor.putBoolean("search",true);
                    editor.commit();
                }
                else
                {
                    editor.putBoolean("search",false);
                    editor.commit();
                }
            }
        });
        stop = (Switch) findViewById(R.id.stop);
        stop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    editor.putBoolean("stop",true);
                    editor.commit();
                }
                else
                {
                    editor.putBoolean("stop",false);
                    editor.commit();
                }
            }
        });
        language = (Spinner) findViewById(R.id.language);
        lang= res.getStringArray( R.array.language);
        langCode = res.getStringArray( R.array.language_code);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, lang){
            public View getView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getView(position,convertView,parent);
                ((TextView) v).setGravity(Gravity.END);

                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View v = super.getDropDownView(position,convertView,parent);
                ((TextView) v).setGravity(Gravity.END);
                return v;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(arrayAdapter);
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Saving the corresponding language code for the selected language into SharedPreference
                String code = langCode[position];
                editor.putString("language-code",code);
                editor.putInt("position",position);
                editor.commit();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        getStartCommand();
        getStopCommand();
        getLanguageCode();
    }
    //Function to get whether START command is activated or deactivated
    void getStartCommand()
    {
     if(sharedPreferences.getBoolean("search",true))
         {
             search.setChecked(true);
         }
     else
         {
             search.setChecked(false);
         }
    }
    //Function to get whether STOP command is activated or deactivated
    void getStopCommand()
    {
        if(sharedPreferences.getBoolean("stop",true))
        {
            stop.setChecked(true);
        }
        else
        {
            stop.setChecked(false);
        }
    }
    //Function to get the selected Language code
    void getLanguageCode()
    {
        language.setSelection(sharedPreferences.getInt("position",0));
    }


}
