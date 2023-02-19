package com.example.appweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Thoitiet> arrayList;

    // Tạo constructor cho 2 thằng trên
    public CustomAdapter(Context context, ArrayList<Thoitiet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dong_listview,null);

        Thoitiet thoitiet = arrayList.get(i);

        TextView txtDay = (TextView) view.findViewById(R.id.textViewNgay);
        TextView txtTrangthai = (TextView) view.findViewById(R.id.textViewTrangthai);
        TextView txtMaxTemp = (TextView) view.findViewById(R.id.textViewMaxTemp);
        TextView txtMinTemp = (TextView) view.findViewById(R.id.textViewMinTemp);
        ImageView imgStatus = (ImageView) view.findViewById(R.id.imageViewTrangthai);

        txtDay.setText(thoitiet.Day);
        txtTrangthai.setText(thoitiet.Status);
        txtMaxTemp.setText(thoitiet.MaxTemp + "°C");
        txtMinTemp.setText(thoitiet.MinTemp + "°C");

        Picasso.with(context).load("https://openweathermap.org/img/wn/"+thoitiet.Image+".png").into(imgStatus);

        return view;
    }
}
