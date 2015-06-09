package com.supinfo.supsms.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.supinfo.supsms.R;
import com.supinfo.supsms.data.LoginData;

public class MainActivity extends ActionBarActivity {

	private Button btBackupSMS;
	private Button btBackupContacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setExitTransition(new Explode());
        }

		setContentView(R.layout.activity_main);
		
		btBackupContacts = (Button) findViewById(R.id.btBackupContacts);
		btBackupSMS = (Button) findViewById(R.id.btBackupSMS);

        btBackupContacts.setOnClickListener(startOperationClickListener);
        btBackupSMS.setOnClickListener(startOperationClickListener);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

    private View.OnClickListener startOperationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String operation = null;
            if(v == btBackupContacts) operation = OperationActivity.OPERATION_BACKUP_CONTACTS;
            else if (v == btBackupSMS) operation = OperationActivity.OPERATION_BACKUP_SMS;

            Intent intent = new Intent(MainActivity.this, OperationActivity.class);
            intent.putExtra(OperationActivity.KEY_OPERATION, operation);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.menu_logout:
                LoginData.getInstance().logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.menu_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
