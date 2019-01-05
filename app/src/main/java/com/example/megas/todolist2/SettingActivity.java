package com.example.megas.todolist2;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    CheckBox ckbShowNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Setting");

        sharedPreferences = getSharedPreferences(Ulti.SHARED_PREFERENCES_NAME, this.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setVar();

        setEvent();
    }

    private void setVar() {
        ckbShowNotification = findViewById(R.id.ckbShowNotificationSetting);
        ckbShowNotification.setChecked(sharedPreferences.getBoolean(Ulti.IS_SHOW_NOTIFICATION, false));
    }

    private void setEvent() {
        ckbShowNotification.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ckbShowNotificationSetting:
                editor.putBoolean(Ulti.IS_SHOW_NOTIFICATION, isChecked);
                editor.commit();

                if (isChecked) {
                    Ulti.pushNotification(this);
                } else {
                    Ulti.cancelNotification(this);
                }
                break;
        }
    }
}
