package com.example.appweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailsHistoryWeather extends AppCompatActivity {
    String lat, lon;
    Context context;
    TextView tvDayDetailsHW, tvTimeDetailHW;
    ImageView imgBackHW;

    List<DetailHistory> detailHistoryList;
    private RecyclerView rcvDetailHistoryWeather;
    private DetailsHistoryAdapter mDetailHistoryWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_history_weather);

        context = this;
        Anhxa();

        // Trở về màn hình trước bằng action onBackPressed();
        imgBackHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        detailHistoryList = new ArrayList<>();

        rcvDetailHistoryWeather = findViewById(R.id.rcvDetailHistoryWeather);
        mDetailHistoryWeatherAdapter = new DetailsHistoryAdapter(getApplicationContext(), detailHistoryList);
        // Category hướng vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvDetailHistoryWeather.setLayoutManager(linearLayoutManager);

        // Xét dữ liệu cho adapter
        mDetailHistoryWeatherAdapter.setData(detailHistoryList);
        rcvDetailHistoryWeather.setAdapter(mDetailHistoryWeatherAdapter);

        callAPIFirebase();
        Log.d("lat11", "onResponse: " + lat);
        Log.d("lat11", "onResponse: " + lon);
    }


    private void CallAPIOpen(String lon, String lat, long start, long end) {
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsHistoryWeather.this);
        String url30Detail = "http://history.openweathermap.org/data/2.5/history/city?lat=" + lat + "&lon=" + lon + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500&start=" + start + "&end=" + end;
        Log.d("urlHistory2", url30Detail);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url30Detail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("1234", response);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Response").child("Detail History Weather");
                        myRef.setValue(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            // Do các thẻ có thuộc tính giống nhau
                            // => dùng for để đọc giá trị của từng thẻ trong tag "list"
                            for (int j = 0; j < jsonArrayList.length(); j++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(j);
                                String dayDetailHW = jsonObjectList.getString("dt");

                                // Chuyển biến day về dạng long
                                long lHW = Long.valueOf(dayDetailHW);
                                // Chuyển thành mili giây
                                Date dateHW = new Date(lHW * 1000L);
                                // Định dạng thứ ngày tháng năm
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H:mm a");
                                String timeDetailHW = simpleDateFormat.format(dateHW);

                                JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                                // Nhiệt độ
                                String temp = jsonObjectMain.getString("temp");
                                // -> cần chuyển đổi nhiệt độ sang kiểu int
                                Double x = Double.valueOf(temp);
                                // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                                String tempDetailHW = String.valueOf(x.intValue());

                                // Cảm giác như
                                String camGiacNhuHW = jsonObjectMain.getString("feels_like");
                                // -> cần chuyển đổi nhiệt độ sang kiểu int
                                Double y = Double.valueOf(camGiacNhuHW);
                                // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                                String feelLikeDetailHW = String.valueOf(y.intValue());

                                // Áp suất
                                String apSuatDetailHW = jsonObjectMain.getString("pressure");

                                // Độ ẩm
                                String doAmDetailHW = jsonObjectMain.getString("humidity");

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                // Do nó có 1 thẻ object thôi
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String trangThaiDetailHW = jsonObjectWeather.getString("description");

                                // Icon status
                                String imgTrangThaiDetailHW = jsonObjectWeather.getString("icon");

                                // Tốc độ gió
                                JSONObject jsonObjectWind = jsonObjectList.getJSONObject("wind");
                                String speedDetailHW = jsonObjectWind.getString("speed");

                                // Hướng gió
                                String huongGioDetailHW = jsonObjectWind.getString("deg");
                                huongGioDetailHW = doiHuong(huongGioDetailHW);

                                detailHistoryList.add(new DetailHistory(imgTrangThaiDetailHW, timeDetailHW, tempDetailHW, trangThaiDetailHW, feelLikeDetailHW, speedDetailHW, huongGioDetailHW, apSuatDetailHW, doAmDetailHW));
                                DatabaseReference myRefDetailHistory = database.getReference("Weather").child("Detail History Weather");
                                myRefDetailHistory.setValue(detailHistoryList);
                            }

                            // Cập nhật khi có dữ liệu mới
                            mDetailHistoryWeatherAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loidetailhistory", "history");
                    }
                });
        requestQueue.add(stringRequest);
    }

    // Gọi tên thành phố từ Firebase về
    // Gọi api từ link này https://testapp-dfb4c-default-rtdb.firebaseio.com/Info.json;
    // String ten = ?;//
    // String lon = ?;
    // String lat = ?;
    public void callAPIFirebase() {
        RequestQueue requestQueue = Volley.newRequestQueue(DetailsHistoryWeather.this);
        String url30 = "https://testapp-dfb4c-default-rtdb.firebaseio.com/Info.json";
        Log.d("urlHistory1", url30);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url30,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("71", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            lat = jsonObject.getString("latcity");
                            lon = jsonObject.getString("loncity");
                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String strDate = formatter.format(date);
                            try {
                                date = formatter.parse(strDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            long start = date.getTime() / 1000;
                            Log.d("TAG11", "onCreate: " + start);
                            Bundle bundle = getIntent().getExtras();
                            if (bundle == null) {
                                return;
                            }

                            // Get đúng key khi truyền
                            int history_index = (int) bundle.get("history30");
                            Date end1 = new Date();

                            Log.d("TAG11", "onCreate: " + end1.getTime());
                            long end = end1.getTime() / 1000;
                            if (history_index == 0) {
                                Log.d("TAG88", "onCreate: " + history_index);
                                CallAPIOpen(lon, lat, start, end);
                            } else if (history_index == 1) {
                                Log.d("TAG89", "onCreate: " + history_index);
                                long startMoi = start - 86400;
                                Log.d("TAG89", "start: " + startMoi + "end: " + start);
                                CallAPIOpen(lon, lat, startMoi, start);

                            } else {
                                long startMoi = start - (86400L * history_index);
                                long endMoi = startMoi - 86400;
                                Log.d("TAG90", "onCreate: " + history_index);
                                Log.d("TAG90", "start: " + startMoi + "end: " + endMoi);
                                CallAPIOpen(lon, lat, endMoi, startMoi);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loihistory", "history");
                    }
                });
        requestQueue.add(stringRequest);
    }

    public String doiHuong(String abc) {
        int a = Integer.parseInt(abc);
        if (a > 0 && a < 90) {
            abc = "Đông Bắc";
        }
        if (a > 90 && a < 180) {
            abc = "Đông Nam";
        }
        if (a > 180 && a < 270) {
            abc = "Tây Nam";
        }
        if (a > 270 && a < 360) {
            abc = "Tây Bắc";
        }
        switch (a) {
            case 0:
                abc = "Bắc";
                break;
            case 90:
                abc = "Đông";
                break;
            case 180:
                abc = "Nam";
                break;
            case 270:
                abc = "Tây";
                break;
        }
        return abc;
    }

    private void Anhxa() {
        imgBackHW = findViewById(R.id.imageViewBackHW);
        tvTimeDetailHW = findViewById(R.id.tvTimeDetailHW);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailHistoryWeatherAdapter.giaiPhong();
    }
}