package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SevenDayWeather extends AppCompatActivity {

    String cityName = "", lat, lon;

    ImageView imgBack;
    TextView txtCityName;
    ListView lstView;

    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seven_day_weather);

        Anhxa();

        // Nhận dữ liệu về
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "Du lieu truyen qua: " + city);
        if (city.equals("")) {
            cityName = "Vĩnh Long";
            currentWeather(cityName);
        } else {
            cityName = city;
            currentWeather(cityName);
        }
        // Trở về màn hình trước bằng action onBackPressed();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void currentWeather(String data) {
        // Thực thi những request mà mình gửi đi
        // Cú pháp Request của thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(SevenDayWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
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

                            oneCallCurrentWeather(lat, lon);

                            // Lấy ra tên thành phố
                            String name = jsonObject.getString("name");
                            // Xét text cho tên thành phố
                            txtCityName.setText(name);

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

    public void oneCallCurrentWeather(String lat1, String lon1) {
        RequestQueue requestQueue = Volley.newRequestQueue(SevenDayWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat1 + "&lon=" + lon1 + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        Log.d("url7ngay", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HI", response);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Response").child("7 day forecast");
                        myRef.setValue(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("daily");

                            // Do các thẻ có thuộc tính giống nhau
                            // => dùng for để đọc giá trị của từng thẻ trong tag "daily"
                            for (int i = 0; i < jsonArrayList.length(); i++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String day = jsonObjectList.getString("dt");

                                // Chuyển biến day về dạng long
                                long l = Long.valueOf(day);
                                // Chuyển thành mili giây
                                Date date = new Date(l * 1000L);
                                // Định dạng thứ ngày tháng năm
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String minTemp = jsonObjectTemp.getString("min");
                                String maxTemp = jsonObjectTemp.getString("max");
                                // -> cần chuyển đổi nhiệt độ sang kiểu int
                                Double y = Double.valueOf(minTemp);
                                Double x = Double.valueOf(maxTemp);
                                // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                                String NhietdoMax = String.valueOf(x.intValue());
                                String NhietdoMin = String.valueOf(y.intValue());
//
                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                // Do nó có 1 thẻ object thôi
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

//                                String Day = "Thứ 6 16/10/2001";
//                                String status = "Mưa";
//                                String icon = "04n";
//                                String NhietdoMax = "30°C";
//                                String NhietdoMin = "10°C";
                                mangthoitiet.add(new Thoitiet(Day, status, icon, NhietdoMax, NhietdoMin));

                                DatabaseReference myRefMang7Ngay = database.getReference("Weather").child("7 Day Forecast");
                                myRefMang7Ngay.setValue(mangthoitiet);
                            }

                            // Cập nhật khi có dữ liệu mới
                            customAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "hoa");
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void Anhxa() {
        imgBack = findViewById(R.id.imageViewBack);
        txtCityName = findViewById(R.id.textViewCityName);
        lstView = findViewById(R.id.listView);

        mangthoitiet = new ArrayList<Thoitiet>();
        customAdapter = new CustomAdapter(SevenDayWeather.this, mangthoitiet);
        lstView.setAdapter(customAdapter);
    }

}