package vestrik.liza_allert;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.*;
import com.splunk.mint.Mint;
import java.util.Objects;

public class infoActivity extends AppCompatActivity {

    EditText phone, name,surname,region,addData;
    Button btnSave;
    Spinner spinner;
    private static String phonestr;
    private static String namestr;
    private static String surnamestr;
    private static String regionstr;
    private static String datastr;
   // private static String spin;
    private static final String DB_NAME = "database.db";
    public static final String TABLE_INFO = "info";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_REGION = "region";
   // public static final String COLUMN_SPIN = "spin";
    public static final String COLUMN_DATA = "data";
    private static final String TABLE_CREATE = "create table IF NOT EXISTS " + TABLE_INFO + "(" + COLUMN_NUMBER + " text, "
            + COLUMN_NAME + " text, " + COLUMN_SURNAME + " text," + COLUMN_REGION + " text,"  + COLUMN_DATA + " text"
            + ");";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this.getApplication(), "0ff11f04");
        setContentView(R.layout.activity_info);
        btnSave = findViewById(R.id.btnSave);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        region = findViewById(R.id.region );
        addData = findViewById(R.id.multiline);
        //spinner =  findViewById(R.id.spinner);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.execSQL(TABLE_CREATE);
        try {
            Cursor query = db.rawQuery("SELECT * FROM info;", null);
            if (query != null) {
                if(query.moveToFirst())
                {do
                    {
                        phone.setText(query.getString(0));
                        name.setText(query.getString(1));
                        surname.setText(query.getString(2));
                        region.setText(query.getString(3));
                       // spinner.setSelection(Integer.parseInt(query.getString(4)));
                        addData.setText(query.getString(4));
                    }
                while(query.moveToNext());
                }
            }

            query.close();
        }
        catch (Exception e)
        {

        }
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void onClickbtnSave(View  view)
    {
       database();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void database()
    {
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase(DB_NAME, MODE_PRIVATE, null);
        db.delete(TABLE_INFO,null, null);


        //spin = spinner.getSelectedItem().toString();
        phonestr=phone.getText().toString();
        namestr=name.getText().toString();
        surnamestr=surname.getText().toString();
        regionstr=region.getText().toString();
        datastr=addData.getText().toString();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER,phonestr);
        values.put(COLUMN_NAME, namestr);
        values.put(COLUMN_SURNAME,surnamestr);
        values.put(COLUMN_REGION,regionstr);
       // values.put(COLUMN_SPIN,spin);
        values.put(COLUMN_DATA,datastr);
        db.insert(TABLE_INFO, null,values);
        db.close();
    }



}
