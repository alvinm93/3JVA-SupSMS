package com.supinfo.supsms.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.supinfo.supsms.R;
import com.supinfo.supsms.util.Util;

public class AboutActivity extends ActionBarActivity {

    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvVersion = (TextView) findViewById(R.id.tvVersion);


        //binding
        tvVersion.setText(String.format(getResources().getString(R.string.version_info), Util.getAppVersionName()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
