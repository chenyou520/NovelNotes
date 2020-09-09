package com.chenyou.novelnotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class UserSettingsActivity extends BaseActivity {

    private Toolbar myToolbar;
    private Switch nightMode;
    private SharedPreferences sharedPreferences;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        initView();

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (isNightMode()) {
            myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_settings_white_24dp));
        } else {
            myToolbar.setNavigationIcon(getDrawable(R.drawable.ic_settings_black_24dp));

        }
    }

    private void initView() {
        nightMode = (Switch) findViewById(R.id.nightMode);
        nightMode.setChecked(sharedPreferences.getBoolean("nightMode", false));
        nightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setNightModePref(isChecked);
                setSelfNightMode();
            }
        });
    }

    private void setNightModePref(boolean night) {
        //通过nightMode switch修改pref中的nightMode
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("nightMode", night);
        editor.commit();
    }

    private void setSelfNightMode() {
        //重新赋值并重启本activity
        super.setNightMode();
        Intent intent = new Intent(this, UserSettingsActivity.class);
        startActivity(intent);
        finish();
    }

}
