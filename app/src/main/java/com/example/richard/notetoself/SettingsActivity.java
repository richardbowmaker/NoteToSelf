package com.example.richard.notetoself;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity
{
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    private boolean mSound;

    public static final int FAST = 0;
    public static final int SLOW = 1;
    public static final int NONE = 2;

    private int mAnimOption;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPrefs = getSharedPreferences("Note to self", MODE_PRIVATE);
        mEditor = mPrefs.edit();

        mSound = mPrefs.getBoolean("sound", true);

        CheckBox checkboxSound = (CheckBox)findViewById(R.id.checkBoxSound);

        checkboxSound.setChecked(mSound);

        checkboxSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                mSound = !mSound;
                mEditor.putBoolean("sound", mSound);
            }
        });

        // animation option
        mAnimOption = mPrefs.getInt("anim option", FAST);
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        switch (mAnimOption)
        {
            case FAST: radioGroup.check(R.id.radioFast); break;
            case SLOW: radioGroup.check(R.id.radioSlow); break;
            case NONE: radioGroup.check(R.id.radioNone); break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.radioFast: mAnimOption = FAST; break;
                    case R.id.radioSlow: mAnimOption = SLOW; break;
                    case R.id.radioNone: mAnimOption = NONE; break;
                }

                mEditor.putInt("anim option", mAnimOption);

            }
        });




//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        mEditor.commit();

    }
}
