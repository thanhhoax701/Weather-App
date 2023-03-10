package com.example.appweather;

import static android.content.ContentValues.TAG;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import time.DetailTime;
import time.DetailTimeAdapter;

public class HistoryWeather extends AppCompatActivity {
    Button btnThemNgay, btnThemNgay14, btnThemNgay21, btnThemNgay28, btnThemNgay30;
    String cityName = "", lat, lon;
    TextView txtCityNameHW;
    ImageView imgBackHW;
    Date date = new Date();
    long today = date.getTime() / 1000;
    long day7 = today - 86400 * 7;
    long day14 = day7 - 86400 * 7;
    long day21 = day14 - 86400 * 7;
    long day28 = day21 - 86400 * 7;
    long day30 = day28 - 86400 * 2;
    Context context;
    int i = 0;


    List<History> historyList;
    private RecyclerView rcvHistoryWeather;
    private HistoryAdapter mHistoryWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_weather);

        context = this;
        Anhxa();

        // Nh???n d??? li???u v???
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", "Du lieu truyen qua: " + city);
        if (city.equals("")) {
            cityName = "V??nh Long";
            currentWeather(cityName);
            Intent intent1 = new Intent(HistoryWeather.this, DetailsHistoryWeather.class);
            intent1.putExtra("cityname", cityName);
//            startActivity(intent1);
            btnThemNgay.setVisibility(View.VISIBLE);
            // Xo?? d??? li???u tr?????c ????
//            historyList.clear();
        } else {
            cityName = city;
            currentWeather(cityName);
            btnThemNgay.setVisibility(View.VISIBLE);
            // Xo?? d??? li???u tr?????c ????
//            historyList.clear();
        }
        // Tr??? v??? m??n h??nh tr?????c b???ng action onBackPressed();
        imgBackHW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        historyList = new ArrayList<>();

        rcvHistoryWeather = findViewById(R.id.rcvHistoryWeather);
        mHistoryWeatherAdapter = new HistoryAdapter(getApplicationContext(), historyList);
        // Category h?????ng vertical
        LinearLayoutManager linearLayoutManagerA = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistoryWeather.setLayoutManager(linearLayoutManagerA);

        // Click item RecycleView
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvHistoryWeather.addItemDecoration(itemDecoration);

        // X??t d??? li???u cho adapter
        mHistoryWeatherAdapter.setData(historyList);
        rcvHistoryWeather.setAdapter(mHistoryWeatherAdapter);

        btnThemNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeather2(cityName);
                btnThemNgay14.setVisibility(View.VISIBLE);
                btnThemNgay.setVisibility(View.GONE);
            }
        });

        btnThemNgay14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeather3(cityName);
                btnThemNgay14.setVisibility(View.GONE);
                btnThemNgay21.setVisibility(View.VISIBLE);
            }
        });
        btnThemNgay21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeather4(cityName);
                btnThemNgay21.setVisibility(View.GONE);
                btnThemNgay28.setVisibility(View.VISIBLE);
            }
        });
        btnThemNgay28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeather5(cityName);
                btnThemNgay28.setVisibility(View.GONE);
                btnThemNgay30.setVisibility(View.VISIBLE);
            }
        });
    }

    public void currentWeather(String data) {
        // Th???c thi nh???ng request m?? m??nh g???i ??i
        // C?? ph??p Request c???a th?? vi???n Volley
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("ketqua", response);
                        try {
                            // G??n bi???n response v??o ????? ?????c d??? li???u t??? Object
                            JSONObject jsonObject = new JSONObject(response);

                            // L???y lon v?? lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                             Log.d("lat", lat);
//                             Log.d("lon", lon);
                            historyList.clear();
                            historyWeather7(lat, lon, day7, today);

                            Log.d("TAG69", "today: " + today);
                            Log.d("TAG69", "day7: " + day7);
                            Log.d("TAG69", "day14: " + day14);
                            Log.d("TAG69", "day21: " + day21);
                            Log.d("TAG69", "day28: " + day28);
                            Log.d("TAG69", "day30: " + day30);

                            // L???y ra t??n th??nh ph???
                            String name = jsonObject.getString("name");
                            // X??t text cho t??n th??nh ph???
                            txtCityNameHW.setText(name);

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
        // th???c thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void currentWeather2(String data) {
        // Th???c thi nh???ng request m?? m??nh g???i ??i
        // C?? ph??p Request c???a th?? vi???n Volley
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("ketqua", response);
                        try {
                            // G??n bi???n response v??o ????? ?????c d??? li???u t??? Object
                            JSONObject jsonObject = new JSONObject(response);

                            // L???y lon v?? lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                             Log.d("lat", lat);
//                             Log.d("lon", lon);
                            historyWeather7(lat, lon, day14, day7);

                            // L???y ra t??n th??nh ph???
                            String name = jsonObject.getString("name");
                            // X??t text cho t??n th??nh ph???
                            txtCityNameHW.setText(name);

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
        // th???c thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void currentWeather3(String data) {
        // Th???c thi nh???ng request m?? m??nh g???i ??i
        // C?? ph??p Request c???a th?? vi???n Volley
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("ketqua", response);
                        try {
                            // G??n bi???n response v??o ????? ?????c d??? li???u t??? Object
                            JSONObject jsonObject = new JSONObject(response);

                            // L???y lon v?? lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                             Log.d("lat", lat);
//                             Log.d("lon", lon);
                            historyWeather7(lat, lon, day21, day14);

                            // L???y ra t??n th??nh ph???
                            String name = jsonObject.getString("name");
                            // X??t text cho t??n th??nh ph???
                            txtCityNameHW.setText(name);

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
        // th???c thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void currentWeather4(String data) {
        // Th???c thi nh???ng request m?? m??nh g???i ??i
        // C?? ph??p Request c???a th?? vi???n Volley
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("ketqua", response);
                        try {
                            // G??n bi???n response v??o ????? ?????c d??? li???u t??? Object
                            JSONObject jsonObject = new JSONObject(response);

                            // L???y lon v?? lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                             Log.d("lat", lat);
//                             Log.d("lon", lon);
                            historyWeather7(lat, lon, day28, day21);

                            // L???y ra t??n th??nh ph???
                            String name = jsonObject.getString("name");
                            // X??t text cho t??n th??nh ph???
                            txtCityNameHW.setText(name);

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
        // th???c thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void currentWeather5(String data) {
        // Th???c thi nh???ng request m?? m??nh g???i ??i
        // C?? ph??p Request c???a th?? vi???n Volley
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("ketqua", response);
                        try {
                            // G??n bi???n response v??o ????? ?????c d??? li???u t??? Object
                            JSONObject jsonObject = new JSONObject(response);

                            // L???y lon v?? lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                             Log.d("lat", lat);
//                             Log.d("lon", lon);
                            historyWeather7(lat, lon, day30, day28);

                            // L???y ra t??n th??nh ph???
                            String name = jsonObject.getString("name");
                            // X??t text cho t??n th??nh ph???
                            txtCityNameHW.setText(name);

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
        // th???c thi stringRequest
        requestQueue.add(stringRequest);
    }

    public void historyWeather7(String latB, String lonB, long start, long end) {
        RequestQueue requestQueue = Volley.newRequestQueue(HistoryWeather.this);
        String url7 = "http://history.openweathermap.org/data/2.5/history/city?lat=" + latB + "&lon=" + lonB + "&type=hour&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500&start=" + start + "&end=" + end;
        Log.d("urlHistory5", url7);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url7,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("30", response);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Response").child("History Weather");
                        myRef.setValue(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            Log.d("List", String.valueOf(jsonArrayList));
//                            historyList.clear();
                            // Do c??c th??? c?? thu???c t??nh gi???ng nhau
                            // => d??ng for ????? ?????c gi?? tr??? c???a t???ng th??? trong tag "list"
                            // Khi d??ng type=hour th?? k???t qu??? s??? tr??? theo gi???
                            // => Cho k+24 th?? k???t qu??? s??? hi???n th??? theo ng??y (24 ??? ????y l?? 1 ng??y c?? 24 gi???)
                            for (int k = jsonArrayList.length() - 1; k >= 0; k = k - 24) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(k);
                                String dayHW = jsonObjectList.getString("dt");

                                // Chuy???n bi???n day v??? d???ng long
                                long lHW = Long.valueOf(dayHW);
                                // Chuy???n th??nh mili gi??y
                                Date dateHW = new Date(lHW * 1000L);
                                // ?????nh d???ng th??? ng??y th??ng n??m
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
                                String DayHW = simpleDateFormat.format(dateHW);

                                JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                                // Nhi???t ?????
                                String temp = jsonObjectMain.getString("temp");
                                // -> c???n chuy???n ?????i nhi???t ????? sang ki???u int
                                Double x = Double.valueOf(temp);
                                // ?????i v??? ki???u int b???ng intValue r???i ?????i sang chu???i
                                String tempHW = String.valueOf(x.intValue());

                                // C???m gi??c nh??
                                String camGiacNhuHW = jsonObjectMain.getString("feels_like");
                                // -> c???n chuy???n ?????i nhi???t ????? sang ki???u int
                                Double y = Double.valueOf(camGiacNhuHW);
                                // ?????i v??? ki???u int b???ng intValue r???i ?????i sang chu???i
                                String feelLikeHW = String.valueOf(y.intValue());

                                // ??p su???t
                                String apSuatHW = jsonObjectMain.getString("pressure");

                                // ????? ???m
                                String doAmHW = jsonObjectMain.getString("humidity");

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                // Do n?? c?? 1 th??? object th??i
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String trangThaiHW = jsonObjectWeather.getString("description");

                                // Icon status
                                String imgTrangThaiHW = jsonObjectWeather.getString("icon");

                                // T???c ????? gi??
                                JSONObject jsonObjectWind = jsonObjectList.getJSONObject("wind");
                                String speedHW = jsonObjectWind.getString("speed");

                                // H?????ng gi??
                                String huongGioHW = jsonObjectWind.getString("deg");
                                huongGioHW = doiHuong(huongGioHW);

                                Log.d("history", "onResponse: " + imgTrangThaiHW + "," + DayHW + "," + tempHW + "," + feelLikeHW + "," + trangThaiHW + "," + speedHW + "," + huongGioHW + "," + apSuatHW + "," + doAmHW);
                                historyList.add(new History(imgTrangThaiHW, DayHW, tempHW, trangThaiHW, feelLikeHW, speedHW, huongGioHW, apSuatHW, doAmHW));
                                DatabaseReference myRefHistory = database.getReference("Weather").child("History Weather");
                                myRefHistory.setValue(historyList);
                            }

                            // C???p nh???t khi c?? d??? li???u m???i
                            mHistoryWeatherAdapter.notifyDataSetChanged();

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
            abc = "????ng B???c";
        }
        if (a > 90 && a < 180) {
            abc = "????ng Nam";
        }
        if (a > 180 && a < 270) {
            abc = "T??y Nam";
        }
        if (a > 270 && a < 360) {
            abc = "T??y B???c";
        }
        switch (a) {
            case 0:
                abc = "B???c";
                break;
            case 90:
                abc = "????ng";
                break;
            case 180:
                abc = "Nam";
                break;
            case 270:
                abc = "T??y";
                break;
        }
        return abc;
    }

    private void Anhxa() {
        imgBackHW = findViewById(R.id.imageViewBackHW);
        btnThemNgay = findViewById(R.id.btn_themNgay);
        txtCityNameHW = findViewById(R.id.textViewCityNameHW);
        btnThemNgay14 = findViewById(R.id.btn_themNgay14);
        btnThemNgay21 = findViewById(R.id.btn_themNgay21);
        btnThemNgay28 = findViewById(R.id.btn_themNgay28);
        btnThemNgay30 = findViewById(R.id.btn_themNgay30);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHistoryWeatherAdapter.giaiPhong();
    }
}