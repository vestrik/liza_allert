package vestrik.liza_allert;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.splunk.mint.Mint;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener  {


    Button btnStartService, btnStopService, btnSave;
    SeekBar seekBar;
    TextView seekBarText;
    SharedPreferences sPref;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBarText = findViewById(R.id.seekBarText);
        seekBar = findViewById(R.id.seekBar);
        Mint.initAndStartSession(this.getApplication(), "0ff11f04");
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        btnSave = findViewById(R.id.btnSave);

        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION}, 10);

            }

        }
        sPref = getPreferences(MODE_PRIVATE);
        int savedTime = sPref.getInt("time", 0);
        if(savedTime != 0) {
            Log.d("Saved time", Integer.toString(savedTime));
            seekBar.setProgress(savedTime);
            seekBarText.setText("Отправлять каждые " + savedTime + " минут");
        }
        else {
            seekBar.setProgress(1);
            seekBarText.setText("Отправлять каждые 1 минут");
        }
        seekBar.setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener) this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickinfo(MenuItem item)
    {
        startActivity(new Intent(MainActivity.this, infoActivity.class));
    }

    public void onClickLocationSettings(MenuItem item)
    {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void onClickMaps(View view)
    {
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    public void onClickStartSendService(View view)
    {
        Intent startIntent = new Intent(MainActivity.this, SendSmsAndGpsService.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("time", seekBar.getProgress());
        ed.apply();
        // startIntent.putExtra("number", numberText.getText().toString());
        // startIntent.putExtra("name", nameText.getText().toString());
        startIntent.putExtra("time", seekBar.getProgress());
        startService(startIntent);
    }

    public void onClickStopSendService(View view)
    {
        Intent stopIntent = new Intent(MainActivity.this, SendSmsAndGpsService.class);
        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        stopService(stopIntent);
        stopService(new Intent(MainActivity.this, SendSmsAndGpsService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarText.setText("Отправлять каждые " + String.valueOf(seekBar.getProgress())+" минут");
        if(seekBar.getProgress() == 0)
            seekBar.setProgress(1);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onBackPressed() {
        if(!getSupportFragmentManager().popBackStackImmediate()) {
            moveTaskToBack(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


