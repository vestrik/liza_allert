package vestrik.liza_allert;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.splunk.mint.Mint;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String DB_NAME = "database.db";
    public static final String TABLE_PATH = "path";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_TIME = "time";
    private static final String TABLE_CREATE = "create table IF NOT EXISTS " + TABLE_PATH + "(" + COLUMN_ID + " INTEGER PRIMARY KEY , " + COLUMN_LAT + " text, "
            + COLUMN_LNG + " text, " + COLUMN_TIME + " text);";
    /* boolean bound = false;
     boolean fl=false;
     ServiceConnection sConn;
     Intent intent, smsOnMap;
     GetSmsService myServiceGet;*/
    LatLng top,bottom;
    private GoogleMap mMap;


    ArrayList<Marker> markers;
    List<LatLng> points = new ArrayList<>();
    List<String> time = new ArrayList<>();
    public int k = 0;
    double Lat;
    double Lng;
    int id = 1;
    // Random rnd = new Random();
    //int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    //интервал обновления положения всплывающего окна.
    //для плавности необходимо 60 fps, то есть 1000 ms / 60 = 16 ms между обновлениями.
    // private static final int POPUP_POSITION_REFRESH_INTERVAL = 1000;
    //Handler, запускающий обновление окна с заданным интервалом
    // private Handler handler;
    //Runnable, который обновляет положение окна
    // private Runnable updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this.getApplication(), "0ff11f04");
        // enableBroadcastReceiver();
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new ArrayList();
       /* smsOnMap = new Intent (MapsActivity.this, SMSMonitor.class);
        intent = new Intent(this, GetSmsService.class);
        sConn = new ServiceConnection() {
            public void onServiceConnected(ComponentName name, IBinder binder) {
                myServiceGet = ((GetSmsService.MyBinderGet) binder).getService();
                bound = true;
            }
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };*/
        //  handler = new Handler(Looper.getMainLooper());
        // updater = new Updater();

        //запускаем периодическое обновление
        // handler.post(updater);
    }

    public void onClickUpdateMap(View view) {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL(TABLE_CREATE);
        Cursor query = db.rawQuery("SELECT * FROM path;", null);
        if (query != null) {
            if (query.moveToFirst()) {
                points.clear();
                time.clear();
                mMap.clear();
                do {
                    id = query.getInt(0);
                    Lat = Double.parseDouble(query.getString(1).replace(",", "."));
                    Lng = Double.parseDouble(query.getString(2).replace(",", "."));
                    points.add(new LatLng(Lat, Lng));
                    time.add(query.getString(3));
                    // mMap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lng)).title(time));
                }
                while (query.moveToNext());
                for (int i = 0; i < points.size(); i = i + 12) {
                    mMap.addMarker(new MarkerOptions().position(points.get(i)).title(time.get(i)));
                }
                mMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1)).title(time.get(time.size() - 1)));
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(points);
                polylineOptions.width(5).color(Color.RED);
                mMap.addPolyline(polylineOptions);
                query.close();
            }
        }
        db.close();
        id++;
        if(points.size()>1)
        {
            bottom = SphericalUtil.computeOffset(points.get(0), 5000, 0);
            bottom = SphericalUtil.computeOffset(bottom, 5000, 90);
            top = SphericalUtil.computeOffset(points.get(0), 5000, 180);
            top = SphericalUtil.computeOffset(top, 5000, 270);
            LatLngBounds ADELAIDE = new LatLngBounds(top,bottom);
            //LatLng l=(top,bottom);

           // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0),14.0f));
           // mMap.setMaxZoomPreference(17.0f);
           // mMap.setLatLngBoundsForCameraTarget(ADELAIDE);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(ADELAIDE, 0));
            mMap.setLatLngBoundsForCameraTarget(ADELAIDE);
            mMap.setMinZoomPreference(12.0f);

            drawnet();





            /*for(int i=0;i<40;i=i+4)
            {
                net.add(SphericalUtil.computeOffset(net.get(2+i), 500, 90));
                net.add(SphericalUtil.computeOffset(net.get(3+i), 10000, 180));
                net.add(SphericalUtil.computeOffset(net.get(4+i), 500, 90));
                net.add(SphericalUtil.computeOffset(net.get(5+i), 10000, 0));
            }
            net.add(net.get(2));
            for(int i=0;i<40;i=i+4)
            {
            net.add(SphericalUtil.computeOffset(net.get(43+i), 500, 180));
            net.add(SphericalUtil.computeOffset(net.get(44+i), 500, 90));
            net.add(SphericalUtil.computeOffset(net.get(45+i), 500, 180));
            net.add(SphericalUtil.computeOffset(net.get(46+i), 500, 270));
            }*/


        }


    }




   /* private class Updater implements Runnable {

        @Override
        public void run() {
            //помещаем в очередь следующий цикл обновления
            handler.postDelayed(this, POPUP_POSITION_REFRESH_INTERVAL);
            // try {


        }

    }
                    if(fl)
                    {

                        ArrayList<LatLng> index = new ArrayList<>();
                        for (int j = 0; j < numbers.size(); j++) {
                            String n = numbers.get(j);
                            for (int t = 0; t < numbers.size(); t++) {
                                if (n.equals(numbers.get(t))) {
                                    index.add(points.get(t));
                                }
                            }
                            if (index.size() > 1) {
                                PolylineOptions polylineOptions = new PolylineOptions();
                                polylineOptions.addAll(index);
                                polylineOptions
                                        .width(5)
                                        .color(color);
                                mMap.addPolyline(polylineOptions);

                            }
                            index.clear();

                        }

                        mMap.addMarker(volunteer.markOp);


            //}
            // }
            // } catch (NumberFormatException e) {
            //e.printStackTrace();
            //}
    */

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng moscow = new LatLng(55.751244, 37.618423);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(moscow));
        //mMap.setOnInfoWindowClickListener(this);


    }



   /* @Override
    public void onBackPressed() {
        if(!getSupportFragmentManager().popBackStackImmediate()) {
            moveTaskToBack(true);
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    public  void onClickclearpath(MenuItem item)
    {
        mMap.clear();
        points.clear();
        time.clear();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.delete("path",null,null);
        db.close();
        id=1;



    }

   /* public  void onClickStartReceiveService(MenuItem item)
    {
        enableBroadcastReceiver();
    }

    public void enableBroadcastReceiver()
    {
        ComponentName receiver = new ComponentName(this, SMSMonitor.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();
    }

    public void onClickStopReceiveService(MenuItem item)
    {
        disableBroadcastReceiver();

    }

    public void disableBroadcastReceiver()
    {
        ComponentName receiver = new ComponentName(this, SMSMonitor.class);
        PackageManager pm = this.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Toast.makeText(this, "Disabled broadcast receiver", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        String mes_mas[] = null;
        String s=marker.getSnippet();
        mes_mas = s.split("\\s+");
        String numb=mes_mas[1];
        callIntent.setData(Uri.parse("tel:"+numb));
        startActivity(callIntent);
    }
*/

   void drawnet()
   {
       PolylineOptions polylineOptions = new PolylineOptions();
       polylineOptions.width(2).color(Color.BLACK);
       List<LatLng> net = new ArrayList<>();
       net.add(points.get(0));
       net.add(SphericalUtil.computeOffset(net.get(0), 500, 270));
       net.add(SphericalUtil.computeOffset(net.get(1), 500, 0));
       net.add(SphericalUtil.computeOffset(net.get(2), 500, 90));
       net.add(net.get(0));
       polylineOptions.addAll(net);
       mMap.addPolyline(polylineOptions);

       LatLng a=net.get(1),b=net.get(2),c=net.get(3);
       LatLng b1=b;
       drawLeft(a,b);
       drawUp(b,c);




   }

   void drawLeft(LatLng a, LatLng b)
   {
       PolylineOptions polylineOptions2 = new PolylineOptions();
       polylineOptions2.width(2).color(Color.BLACK);
       List<LatLng> net2 = new ArrayList<>();
       net2.add(a);
       net2.add(SphericalUtil.computeOffset(net2.get(0), 500, 270));
       net2.add(SphericalUtil.computeOffset(net2.get(1), 500, 0));
       net2.add(b);
       polylineOptions2.addAll(net2);
       mMap.addPolyline(polylineOptions2);
       net2.clear();
   }

   void drawUp(LatLng b, LatLng c)
   {
       PolylineOptions polylineOptions3 = new PolylineOptions();
       polylineOptions3.width(2).color(Color.BLACK);
       List<LatLng> net3 = new ArrayList<>();
       net3.add(b);
       net3.add(SphericalUtil.computeOffset(net3.get(0), 500, 0));
       net3.add(SphericalUtil.computeOffset(net3.get(1), 500, 90));
       net3.add(c);
       polylineOptions3.addAll(net3);
       mMap.addPolyline(polylineOptions3);
       net3.clear();
   }


}

