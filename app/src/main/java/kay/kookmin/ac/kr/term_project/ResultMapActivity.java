package kay.kookmin.ac.kr.term_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ResultMapActivity extends AppCompatActivity {
    // google map
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_map);

        // Database 관련 객체들
        SQLiteDatabase db;
        String dbName = "eventData.db";
        String tableName = "eventDataTable";
        int dbMode = Context.MODE_PRIVATE;

        Intent intent = getIntent();
        String currentLocation = intent.getStringExtra("Location");
        String[] l = currentLocation.split(",");

        // google map
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(l[0]),Double.parseDouble(l[1])), 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);

        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            String[] location = results.getString(3).split(",");
            String[] date = results.getString(2).split("/");

            if(results.getString(1).equals("일")) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])))
                        .snippet(results.getString(1) + " " + results.getString(4))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .title(date[0].substring(2) + "년" + date[1] + "월" + date[2] + "일"));

                results.moveToNext();
            }
            else if(results.getString(1).equals("취미")) {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])))
                        .snippet(results.getString(1) + " " + results.getString(4))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(date[0].substring(2) + "년" + date[1] + "월" + date[2] + "일"));

                results.moveToNext();
            }
            else {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])))
                        .snippet(results.getString(1) + " " + results.getString(4))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(date[0].substring(2) + "년" + date[1] + "월" + date[2] + "일"));

                results.moveToNext();
            }

        }
        results.close();

    }
}
