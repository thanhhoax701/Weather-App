package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import time.DetailTime;
import time.DetailTimeAdapter;
import time.Time;
import time.TimeAdapter;

public class TimeWeather extends AppCompatActivity {
    TextView txtCityNameTimeWeather;
    ImageView imgCloseTimeWeather;

    String cityName = "", lat, lon;

    Context context;

    List<DetailTime> listDetail;
    private RecyclerView rcvDetailTime;
    private DetailTimeAdapter mDetailTimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_weather);

        context = this;

        Anhxa();
        callAll(lat, lon);

        // Nhận dữ liệu về
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "Du lieu truyen qua: " + city);
        if (city.equals("")) {
            cityName = "Vĩnh Long";
            currentWeather(cityName);
        }
        else {
            cityName = city;
            currentWeather(cityName);
        }

        // Trở về màn hình trước bằng action onBackPressed();
        imgCloseTimeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listDetail = new ArrayList<>();

        rcvDetailTime = findViewById(R.id.rcvDetailTime);
        mDetailTimeAdapter= new DetailTimeAdapter(getApplicationContext(), listDetail);
        // Category hướng vertical
        LinearLayoutManager linearLayoutManagerA = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvDetailTime.setLayoutManager(linearLayoutManagerA);

        // Xét dữ liệu cho adapter
        mDetailTimeAdapter.setData(listDetail);
        rcvDetailTime.setAdapter(mDetailTimeAdapter);

    }

    public void currentWeather (String data) {
        // Thực thi những request mà mình gửi đi
        // Cú pháp Request của thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(TimeWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Dòng dưới là hiển thị ra Logcat để coi thử thôi
                        // Log.d("ketqua", response);
                        try {
                            // Gán biến response vào để đọc dữ liệu từ Object
                            JSONObject jsonObject = new JSONObject(response);

                            // Lấy lon và lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
                            // Log.d("lat", lat);
                            // Log.d("lon", lon);

                            callAll(lat, lon);

                            // Lấy ra tên thành phố
                            String name = jsonObject.getString("name");
                            // Xét text cho tên thành phố
                            txtCityNameTimeWeather.setText(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        // thực thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void callAll (String lat1, String lon1) {
        RequestQueue requestQueue = Volley.newRequestQueue(TimeWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat1+"&lon="+lon1+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        Log.d("url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HI", response);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Response").child("Time Weather");
                        myRef.setValue(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
//
                            JSONArray jsonArrayList = jsonObject.getJSONArray("hourly");
                            // Do các thẻ có thuộc tính giống nhau
                            // => dùng for để đọc giá trị của từng thẻ trong tag "hourly"
                            listDetail.clear();
                            for (int k = 0; k < jsonArrayList.length(); k++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(k);

                                String dayTime = jsonObjectList.getString("dt");
                                // Chuyển biến day về dạng long
                                long l = Long.valueOf(dayTime);
                                // Chuyển thành mili giây
                                Date dateTime = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("EEEE, d MMMM yyyy");
                                String detailDay = simpleDateFormatDay.format(dateTime);

                                SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("H:mm");
                                String detailTime = simpleDateFormatTime.format(dateTime);

                                String tempTime = jsonObjectList.getString("temp");
                                // -> cần chuyển đổi nhiệt độ sang kiểu int
                                Double x = Double.valueOf(tempTime);
                                // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                                String nhietDoTime = String.valueOf(x.intValue());

                                String apSuatTime = jsonObjectList.getString("pressure");

                                String doAmTime = jsonObjectList.getString("humidity");

                                String chiSoUVTime = jsonObjectList.getString("uvi");

                                String gioTime = jsonObjectList.getString("wind_speed");
//
                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                // Do nó có 1 thẻ object thôi
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String imgTrangThaiTime = jsonObjectWeather.getString("icon");
                                String trangThaiTime = jsonObjectWeather.getString("description");

                                Log.d("detail", "onResponse: "+imgTrangThaiTime +"," +detailDay +","+detailTime +","+nhietDoTime+","+trangThaiTime+","+gioTime+","+apSuatTime+","+doAmTime+","+chiSoUVTime);
                                if (k == 0) {
                                    detailTime = "Bây giờ";
                                }
                                listDetail.add(new DetailTime(imgTrangThaiTime, detailDay, detailTime, nhietDoTime, trangThaiTime, gioTime, apSuatTime, doAmTime, chiSoUVTime));
                                DatabaseReference myRefListDetailTime = database.getReference("Weather").child("Time Weather");
                                myRefListDetailTime.setValue(listDetail);
                            }
//
//                            // Cập nhật khi có dữ liệu mới
                            mDetailTimeAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("errordetail", "Có Lỗi");
                    }
                });
        requestQueue.add(stringRequest);
    }

    public void Anhxa () {
        txtCityNameTimeWeather = findViewById(R.id.textViewCityNameTimeWeather);
        imgCloseTimeWeather = findViewById(R.id.closeTimeWeather);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailTimeAdapter.giaiPhong();
    }
}