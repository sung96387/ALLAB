package com.example.allab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.content.Context;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    public ArrayList<Time> listviewitem = new ArrayList<Time>();
    private final ArrayList<Time> arrayList = listviewitem;

    @Override
    public int getCount()
    {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_theme, parent, false);

            TextView drug_name = (TextView)convertView.findViewById(R.id.drug_name_alarm);
            TextView am_pm = (TextView)convertView.findViewById(R.id.drug_ampm_alarm);
            TextView hour = (TextView)convertView.findViewById(R.id.drug_hour_alarm);
            TextView min = (TextView)convertView.findViewById(R.id.drug_min_alarm);

            holder.drug_name = drug_name;
            holder.am_pm = am_pm;
            holder.hour = hour;
            holder.min = min;

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        Time time = arrayList.get(position);
        holder.drug_name.setText(time.getName());
        holder.am_pm.setText(time.getAm_pm());
        holder.hour.setText(time.getHour()+ "시");
        holder.min.setText(time.getMinute()+ "분 ");

        return convertView;
    }

    public void addItem(int hour, int minute, String am_pm, String drug_name) {
        Time time = new Time();

        time.setName(drug_name);
        time.setHour(hour);
        time.setMinute(minute);
        time.setAm_pm(am_pm);

        listviewitem.add(time);
    }

    //List 삭제 method
    public void removeItem(int position) {
        if(listviewitem.size() < 1) {

        }
        else {
            listviewitem.remove(position);
        }
    }

    public void removeItem() {
        if(listviewitem.size() < 1) {

        }
        else {

            listviewitem.remove(listviewitem.size()-1);
        }
    }

    static class ViewHolder {
        TextView drug_name, hour, am_pm, min;
    }
}

