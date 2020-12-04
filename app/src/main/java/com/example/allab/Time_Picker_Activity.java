
package com.example.allab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Time_Picker_Activity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button okBtn, cancelBtn;
    private int hour, minute;
    private String am_pm;
    private Date currentTime;
    private int requestCode;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker);

        timePicker = (TimePicker)findViewById(R.id.time_picker);

        TextView drug_name_alarm = (TextView) findViewById(R.id.drug_name_alarm);
        currentTime = Calendar.getInstance().getTime();
        final Calendar calendar = Calendar.getInstance();
        final Intent alarm_intent = new Intent(Time_Picker_Activity.this,AlarmReceiver.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        okBtn = (Button)findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            //안드로이드 버전별로 시간값 세팅을 다르게 해주어야 함. 여기선 Android API 23
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                }
                else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }

                am_pm = AM_PM(hour);
                hour = timeSet(hour);

                String temp = drug_data.getArray(Time_Picker_Activity.this);
                JSONObject jsonObject = new JSONObject();
                if(temp != "fail")
                {
                    try{JSONArray jsonTemp = new JSONArray(temp);
                        try{
                            jsonObject.put("drug_name", drug_name_alarm.getText().toString());
                            jsonObject.put("hour", hour);
                            jsonObject.put("minute", minute);
                            jsonObject.put("am_pm", am_pm);
                            jsonTemp.put(jsonObject);
                            requestCode = jsonTemp.length() + 1;
                            drug_data.setArray(Time_Picker_Activity.this,jsonTemp.toString());
                        }catch(JSONException e)
                        {
                            e.printStackTrace();
                        }}
                    catch(JSONException e)
                    {e.printStackTrace();}
                }
                else
                {
                    JSONArray jsonTemp = new JSONArray();
                    try{
                        jsonObject.put("drug_name", drug_name_alarm.getText().toString());
                        jsonObject.put("hour", hour);
                        jsonObject.put("minute", minute);
                        jsonObject.put("am_pm", am_pm);
                        jsonTemp.put(jsonObject);
                        requestCode = jsonTemp.length()+1;
                        drug_data.setArray(Time_Picker_Activity.this,jsonTemp.toString());
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
                alarm_intent.putExtra("drug_name",drug_name_alarm.getText().toString());
                pendingIntent = PendingIntent.getBroadcast(Time_Picker_Activity.this,requestCode,alarm_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                Log.d("test","alarmamamam");
                Intent intent = new Intent(getApplicationContext(), drug_alarm.class);
                startActivity(intent);
                finish();
            }
        });
        //취소버튼 누를 시 TimePickerAcitivity 종료
        cancelBtn = (Button) findViewById(R.id.cancleBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //24시 시간제 바꿔줌
    private int timeSet(int hour) {
        if(hour > 12) {
            hour-=12;
        }
        return hour;
    }
    //오전, 오후 선택
    private String AM_PM(int hour) {
        if(hour >= 12) {
            am_pm = "오후";
        }
        else {
            am_pm = "오전";
        }
        return am_pm;
    }
}