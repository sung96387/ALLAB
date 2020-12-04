package com.example.allab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class drug_alarm extends AppCompatActivity {

    public static final int REQUEST_CODE1 = 1000;
    public static final int REQUEST_CODE2 = 1001;
    private Adapter arrayAdapter;
    private Button tpBtn, removeBtn;
    private ListView listView;
    private TextView textView;
    private int hour, minute;
    private String drug_name, am_pm;
    private Handler handler;
    private SimpleDateFormat mFormat;
    private int adapterPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_alarm);

        arrayAdapter = new Adapter();


        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(arrayAdapter);

        //List에 있는 항목들 눌렀을 때 시간변경 가능
        LoadArrayList(drug_alarm.this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterPosition = position;
                arrayAdapter.removeItem(position);
                Intent intent = new Intent(drug_alarm.this, Time_Picker_Activity.class);
                startActivityForResult(intent,REQUEST_CODE2);
            }
        });

        //TimePicker의 시간 셋팅값을 받기 위한 startActivityForResult()
        tpBtn = (Button) findViewById(R.id.alarm_add);
        tpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tpIntent = new Intent(drug_alarm.this, Time_Picker_Activity.class);
                startActivityForResult(tpIntent, REQUEST_CODE1);
                finish();
            }
        });

        removeBtn = (Button) findViewById(R.id.alarm_delete);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteArrayList(drug_alarm.this);
                arrayAdapter.removeItem();
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
    protected void DeleteArrayList(Context context)
    {
        String temp = drug_data.getArray(drug_alarm.this);
        Log.d("test",temp);
        if(temp != "fail")
        {
            try{
                JSONArray response = new JSONArray(temp);
                JSONObject jsonObjectTemp = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for(int i = 0;i<response.length() -1 ;i++)
                {
                    JSONObject jsonObject = response.getJSONObject(i);
                    Log.d("test",jsonObject.getString("drug_name"));
                    jsonObjectTemp.put("drug_name", jsonObject.getString("drug_name"));
                    jsonObjectTemp.put("hour", jsonObject.getInt("hour"));
                    jsonObjectTemp.put("minute", jsonObject.getInt("minute"));
                    jsonObjectTemp.put("am_pm", jsonObject.getString("am_pm"));
                    jsonArray.put(jsonObjectTemp);
                }
                drug_data.setArray(drug_alarm.this,jsonArray.toString());
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        temp = drug_data.getArray(drug_alarm.this);
        Log.d("test",temp);
    }
    protected void LoadArrayList(Context context)
    {

        String temp = drug_data.getArray(drug_alarm.this);
        Log.d("test",temp);
        if(temp != "fail")
        {
            try{
                JSONArray response = new JSONArray(temp);
                for(int i = 0;i<response.length();i++)
                {
                    JSONObject jsonObject = response.getJSONObject(i);
                    Log.d("test",jsonObject.getString("drug_name"));
                    drug_name = jsonObject.getString("drug_name");
                    hour = jsonObject.getInt("hour");
                    minute = jsonObject.getInt("minute");
                    am_pm = jsonObject.getString("am_pm");
                    arrayAdapter.addItem(hour, minute, am_pm,drug_name);
                    arrayAdapter.notifyDataSetChanged();
                }


            }catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
    //TimePicker 셋팅값 받아온 결과를 arrayAdapter에 추가
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //시간 리스트 추가
        if(requestCode == REQUEST_CODE1 && resultCode == RESULT_OK && data != null) {
            hour = data.getIntExtra("hour", 1);
            minute = data.getIntExtra("minute",2);
            am_pm = data.getStringExtra("am_pm");
            drug_name = data.getStringExtra("name");

            arrayAdapter.addItem(hour, minute, am_pm, drug_name);
            arrayAdapter.notifyDataSetChanged();
        }
        //시간 리스트 터치 시 변경된 시간값 저장
        if(requestCode == REQUEST_CODE2 && resultCode == RESULT_OK && data != null) {
            hour = data.getIntExtra("hour", 1);
            minute = data.getIntExtra("minute", 2);
            am_pm = data.getStringExtra("am_pm");
            drug_name = data.getStringExtra("name");

            arrayAdapter.addItem(hour, minute, am_pm, drug_name);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}