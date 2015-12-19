package kay.kookmin.ac.kr.term_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultNumActivity extends AppCompatActivity {
    TextView resultView;
    TextView countResultView;
    Button bt_job;
    Button bt_hobby;
    Button bt_etc;
    Button bt_all;
    DecimalFormat df = new DecimalFormat("0.##");

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "eventData.db";
    String tableName = "eventDataTable";
    int dbMode = Context.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_num);

        countResultView = (TextView)findViewById(R.id.countResult);
        resultView = (TextView)findViewById(R.id.resultView);

        // Database 생성 및 열기
        db = openOrCreateDatabase(dbName,dbMode,null);

        countResultView.setText(resultCount());
        resultView.setText(selectAll());

        Button bt_back = (Button)findViewById(R.id.back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_job = (Button)findViewById(R.id.job);
        bt_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultView.setText(selectJob());
            }
        });

        bt_hobby = (Button)findViewById(R.id.hobby);
        bt_hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultView.setText(selectHobby());
            }
        });

        bt_etc = (Button)findViewById(R.id.etc);
        bt_etc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultView.setText(selectEtc());
            }
        });

        bt_all = (Button) findViewById(R.id.all);
        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultView.setText(selectAll());
            }
        });
    }
    // Data Count 읽기
    public String resultCount() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();
        int[] typeCount = new int[3];
        String view = "총 횟수\n";

        while(!results.isAfterLast()) {
            String type = results.getString(1);
            if(type.equals("일"))
                typeCount[0]++;
            else if(type.equals("취미"))
                typeCount[1]++;
            else
                typeCount[2]++;

            results.moveToNext();
        }

        int typeSum = typeCount[0] + typeCount[1] + typeCount[2];

        view += "일 : " + typeCount[0] + "\n";
        view += "취미 : " + typeCount[1] + "\n";
        view += "기타 : " + typeCount[2] + "\n";
        view += "전체 : " + typeSum + "\n";

        results.close();

        return view;
    }

    // 모든 Data 읽기
    public String selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        String view = "사건\t\t시간\t\t위치\t\t내용\n";

        String[] location = results.getString(3).split(",");

        while (!results.isAfterLast()) {
            for(int i=1; i<=4; i++) {
                if(i == 3)
                    view += df.format(Double.parseDouble(location[0])) + " " +
                            df.format(Double.parseDouble(location[1])) + "\t\t";
                else
                    view += results.getString(i) + "\t\t";
            }
            view += "\n";
            results.moveToNext();
        }
        results.close();

        return view;
    }

    public String selectJob() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        String view = "사건\t\t시간\t\t위치\t\t내용\n";

        String[] location = results.getString(3).split(",");
        DecimalFormat df = new DecimalFormat("0.##");

        while (!results.isAfterLast()) {
            for(int i=1; i<=4; i++) {
                if(results.getString(1).equals("일")) {
                    if (i == 3)
                        view += df.format(Double.parseDouble(location[0])) + " " +
                                df.format(Double.parseDouble(location[1])) + "\t\t";
                    else
                        view += results.getString(i) + "\t\t";
                }
            }
            if(results.getString(1).equals("일"))
                view += "\n";
            results.moveToNext();
        }
        results.close();

        return view;
    }

    public String selectHobby() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        String view = "사건\t\t시간\t\t위치\t\t내용\n";

        String[] location = results.getString(3).split(",");

        while (!results.isAfterLast()) {
            for(int i=1; i<=4; i++) {
                if(results.getString(1).equals("취미")) {
                    if (i == 3)
                        view += df.format(Double.parseDouble(location[0])) + " " +
                                df.format(Double.parseDouble(location[1])) + "\t\t";
                    else
                        view += results.getString(i) + "\t\t";
                }
            }
            if(results.getString(1).equals("취미"))
                view += "\n";
            results.moveToNext();
        }
        results.close();

        return view;
    }

    public String selectEtc() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        String view = "사건\t\t시간\t\t위치\t\t내용\n";

        String[] location = results.getString(3).split(",");

        while (!results.isAfterLast()) {
            for(int i=1; i<=4; i++) {
                if(results.getString(1).equals("기타")){
                    if (i == 3)
                        view += df.format(Double.parseDouble(location[0])) + " " +
                                df.format(Double.parseDouble(location[1])) + "\t\t";
                    else
                        view += results.getString(i) + "\t\t";
                }
            }
            if(results.getString(1).equals("기타"))
                view += "\n";
            results.moveToNext();
        }
        results.close();

        return view;
    }
}
