package kay.kookmin.ac.kr.term_project;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMyLocationChangeListener {

    // google map
    static final LatLng SEOUL = new LatLng(37.56,126.97);
    private GoogleMap map;

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "eventData.db";
    String tableName = "eventDataTable";
    int dbMode = Context.MODE_PRIVATE;

    // layout object
    ArrayAdapter<CharSequence> adapter = null;
    Spinner spinner = null;

    EditText et_event;
    Button bt_insert;
    Button bt_resultNum;
    Button bt_resultMap;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // spinner
        adapter = ArrayAdapter.createFromResource(this,R.array.type_data,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("사건선택");

        spinner.setAdapter(adapter);

        // google map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(this);

        // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);

        // 테이블 생성
        createTable();

        context = this.getApplication().getApplicationContext();

        et_event = (EditText) findViewById(R.id.event);
        bt_insert = (Button) findViewById(R.id.insert);
        bt_resultNum = (Button) findViewById(R.id.result_num);
        bt_resultMap = (Button) findViewById(R.id.result_map);

        bt_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinner.getSelectedItem().toString();
                GregorianCalendar today = new GregorianCalendar();

                String time = today.get(today.YEAR) + "/"
                        + Integer.toString(today.get(today.MONTH)+1) + "/"
                        + today.get(today.DAY_OF_MONTH) + "/"
                        + today.get(today.HOUR_OF_DAY)+ ":"
                        + today.get(today.MINUTE) + ":"
                        + today.get(today.SECOND);


                String location = map.getMyLocation().getLatitude() + "," +
                        map.getMyLocation().getLongitude();

                String event = et_event.getText().toString();
                insertData(type,time,location,event);
            }
        });

        bt_resultNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultNumActivity.class);
                startActivity(intent);
            }
        });

        bt_resultMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultMapActivity.class);
                String location = map.getMyLocation().getLatitude() + ","
                        + map.getMyLocation().getLongitude();
                intent.putExtra("Location",location);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMyLocationChange(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.e("onMyLocationChange", lat + "," + lng);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),15));
    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " +
                    "type text, time text, location text, etc text)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("sqlite", "error: " + e);
        }
    }

    // Data 추가
    public void insertData(String type, String time, String location, String event) {
        String sql = "insert into " + tableName + " values(NULL, '"
                + type + "','" + time + "','" + location + "','" + event + "');";
        Toast.makeText(context, type + "/" + time + "/" + location + "/" + event,
                Toast.LENGTH_LONG).show();
        db.execSQL(sql);
    }
}
