package com.example.appweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.czp.library.ArcProgress;
//import com.czp.library.OnTextCenter;
//import com.czp.library.onImageCenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.marcinmoskala.arcseekbar.ArcSeekBar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import time.Time;
import time.TimeAdapter;

public class MainActivity extends AppCompatActivity {
    final String APP_ID = "7b16a3bb0d4c6253ab56ca6a2a14f500";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    //    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall?";
    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    EditText edtSearch;
    Button btnChangeActivity, btnAddTimeWeather, btnAddCurrentWeather, btnHistoryWeather;
    TextView txtViewName, txtViewCountry, txtViewTemp, txtViewStatus, txtViewHumidity, txtViewCloud, txtViewMill, txtViewDay
            , txtUpdateTime, txtUpdateCurrent
            , txtBinhMinh, txtHoangHon
            , txtTocDoGio, txtLuongMua, txtDoAmTuongDoi, txtCamGiacNhu, txtUV, txtTamNhin
            , txtAqiAir, txtNO2, txtPM10, txtO3, txtPM2_5;
    ImageView iconLocation, iconSearch, imgIcon;

    String City = "", lat, lon;
    Context context;

    int pStatus = 0;
    private Handler handler = new Handler();
    TextView tv;
    ProgressBar mProgressAir;

    List<Time> list;
    private RecyclerView rcvTime;
    private TimeAdapter mTimeAdapter;

    ArcSeekBar gradientSeekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.custom_progressbar2);
        mProgressAir = (ProgressBar) findViewById(R.id.circularProgressbar);
        // Main Progress
        mProgressAir.setProgress(0);
        // Secondary Progress
        mProgressAir.setSecondaryProgress(100);
        // Maximum Progress
        mProgressAir.setMax(100);
        mProgressAir.setProgressDrawable(drawable);
        tv = (TextView) findViewById(R.id.tv);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus < 1) {
                    pStatus += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressAir.setProgress(pStatus);
                            tv.setText(pStatus + "%");
                        }
                    });
                    try {
                        // Thời gian xoay trên 1%
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                callAll(lat, lon);
            }
        }).start();

        // Set Gradient
//        int[] colorArrays = getResources().getIntArray(R.array.gradient);
//        gradientSeekbar.setProgressGradient(colorArrays);


        context = this;

        Anhxa();
        // gán 1 thành phố mặc định khi mở app
        getCurrentWeatherData("Vĩnh Long");
        callAll(lat, lon);
        getWeatherForCurrentLocation();


//        iconLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getData();
//            }
//        });

        // Tìm kiếm thành phố
        iconSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Info").child("city name");
                String city = edtSearch.getText().toString();
                myRef.setValue(city);

                // Cách 1
                if (city.equals("")) {
                    city = "Vĩnh Long";
                    getCurrentWeatherData(City);
                }
                else {
                    City = city;
                    getCurrentWeatherData(City);
                }
                // Cách 2
//                if (TextUtils.isEmpty(city)) {
//                    Toast.makeText(context, "Bạn chưa nhập dữ liệu", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                getCurrentWeatherData(city);
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String city = edtSearch.getText().toString();
                // Cách 1
                if (city.equals("")) {
                    city = "Vĩnh Long";
                    getCurrentWeatherData(City);
                }
                else {
                    City = city;
                    getCurrentWeatherData(City);
                }
                getCurrentWeatherData(city);
                return false;
            }
        });

        // Dự báo 7 ngày
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, SevenDayWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });

        // Dự báo theo giờ
        btnAddTimeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, TimeWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });

        // Xem thời tiết hiện tại chi tiết hơn
        btnAddCurrentWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, CurrentWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });


        list = new ArrayList<>();

        rcvTime = findViewById(R.id.rcv_time);
        mTimeAdapter = new TimeAdapter(getApplicationContext(),list);
        // Category hướng horizontal
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rcvTime.setLayoutManager(linearLayoutManager);

        // Xét dữ liệu cho adapter
        mTimeAdapter.setData(list);
        rcvTime.setAdapter(mTimeAdapter);


        // Xem lịch sử thời tiết trong vòng 30 ngày
        btnHistoryWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = edtSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, HistoryWeather.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat" ,Latitude);
                params.put("lon",Longitude);
                params.put("appid",APP_ID);
                letsDoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE) {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,"Location Get Successfully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else {
                //user denied the permission
            }
        }
    }

    private  void letsDoSomeNetworking (RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL,params,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                Toast.makeText(MainActivity.this,"Data Get Success",Toast.LENGTH_SHORT).show();
                weatherData weatherD = weatherData.fromJson(response);
                updateUI(weatherD);
//                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private  void updateUI(weatherData weather){
        txtViewTemp.setText(weather.getmTemperature());
        txtViewName.setText(weather.getMcity());
        txtViewStatus.setText(weather.getmWeatherType());
//        int resourceID=getResources().getIdentifier(weather.getMicon(),"drawable",getPackageName());
//        imgIcon.setImageResource(resourceID);
        Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/01d.png").into(imgIcon);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationManager!=null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    public void getCurrentWeatherData(String data) {
        // Thực thi những request mà mình gửi đi
        // Cú pháp Request của thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Dòng dưới là hiển thị ra Logcat để coi thử thôi
                        // Log.d("ketqua", response);
                        FirebaseDatabase databaseMain = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = databaseMain.getReference("Response").child("Main Weather");
                        myRef.setValue(response);
                        try {
                            // Gán biến response vào để đọc dữ liệu từ Object
                            JSONObject jsonObject = new JSONObject(response);

                            // Lấy lon và lat
                            JSONObject jsonObjectLonLat = jsonObject.getJSONObject("coord");
                            lat = jsonObjectLonLat.getString("lat");
                            lon = jsonObjectLonLat.getString("lon");
//                          push lon va lot cho details history:
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference lonFirebase = database.getReference("Info").child("loncity");
                            DatabaseReference latFirebase = database.getReference("Info").child("latcity");
                            lonFirebase.setValue(lon);
                            latFirebase.setValue(lat);

                            // Log.d("lat", lat);
                            // Log.d("lon", lon);

                            callAll(lat, lon);
                            getAirPollution(lat, lon);

                            // Lấy ra tên thành phố
                            String name = jsonObject.getString("name");
                            // Xét text cho tên thành phố
                            txtViewName.setText(name);

                            // Lấy dữ liệu từ tag "sys" ---------
                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            // Xét text cho country
                            txtViewCountry.setText(", " + country);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat="+lat1+"&lon="+lon1+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
//        Date date = new Date();
        Log.d("urlOneCall", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Main", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            // Dự báo theo giờ
                            JSONArray jsonArrayList = jsonObject.getJSONArray("hourly");
                            // Do các thẻ có thuộc tính giống nhau
                            // => dùng for để đọc giá trị của từng thẻ trong tag "hourly"
                            list.clear();
                            for (int j = 0; j < jsonArrayList.length(); j++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(j);

                                String dayHourly = jsonObjectList.getString("dt");
                                // Chuyển biến day về dạng long
                                long l = Long.valueOf(dayHourly);
                                // Chuyển thành mili giây
                                Date dateHourly = new Date(l * 1000L);
                                // Định dạng thứ ngày tháng năm
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                                String time = simpleDateFormat.format(dateHourly);


                                String tempHourly = jsonObjectList.getString("temp");
                                // -> cần chuyển đổi nhiệt độ sang kiểu int
                                Double x = Double.valueOf(tempHourly);
                                // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                                String temp = String.valueOf(x.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                // Do nó có 1 thẻ object thôi
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String imgIcon = jsonObjectWeather.getString("icon");

                                String speed = jsonObjectList.getString("wind_speed");

                                Log.d("TAG", "onResponse: "+imgIcon +","+time +","+temp +","+speed);
                                if (j == 0) {
                                    time = "Bây giờ";
                                }
                                list.add(new Time(imgIcon, time, temp, speed));
                            }
//                            // Cập nhật khi có dữ liệu mới
                            mTimeAdapter.notifyDataSetChanged();


                            JSONObject jsonObjectCurrent = jsonObject.getJSONObject("current");

                            // Lấy ra thằng weather
                            JSONArray jsonArrayWeather = jsonObjectCurrent.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String icon = jsonObjectWeather.getString("icon");

                            // Vào đường dẫn https://openweathermap.org/weather-conditions -> vào mục How to get icon URL để lấy link icon
                            // Gọi lại thư viện PICASSO
                            // Sử dụng with để cho biết đang ở trang hiện tại
                            // sử dụng load để lấy đường dẫn của icon về
                            // sử dụng into để hiển thị ra màn hình, bên trong into là tên biến của cái icon đã được đặt để hiển thị
                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);

                            // Lấy nhiệt độ và convert sang chuỗi
                            String temp = jsonObjectCurrent.getString("temp");
                            // Nhiệt độ có kiểu dữ liệu là double
                            // -> cần chuyển đổi nhiệt độ sang kiểu int
                            Double a = Double.valueOf(temp);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String nhietDo = String.valueOf(a.intValue());
                            // Xét text cho nhiệt độ
                            txtViewTemp.setText(nhietDo + "°C");

                            String Status = jsonObjectWeather.getString("description");
                            // Xét text cho status: kiểu như in ra màn hình vậy
                            txtViewStatus.setText(Status);

                            // Lấy phần trăm độ ẩm
                            String doAm = jsonObjectCurrent.getString("humidity");
                            txtViewHumidity.setText(doAm + "%");
                            tv.setText(doAm + "%");
                            int x = Integer.parseInt(doAm);
                            mProgressAir.setProgress(x);

                            // Lấy mây
                            String cloud = jsonObjectCurrent.getString("clouds");
                            txtViewCloud.setText(cloud + "%");

                            // Lấy tốc độ gió
                            String windSpeed = jsonObjectCurrent.getString("wind_speed");
                            txtViewMill.setText(windSpeed + "m/s");


                            // Lấy ra thời gian hiện tại
                            String day = jsonObjectCurrent.getString("dt");
                            // Chuyển biến day về dạng long
                            long l = Long.valueOf(day);
                            // Giá trị mili giây
                            Date date = new Date(l*1000L);
                            // Định dạng thứ ngày tháng năm giờ phút giây
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, d MMM yyyy HH:mm:ss");
                            String Day = simpleDateFormat.format(date);
                            // Xét text cho ngày
                            txtViewDay.setText(Day);

                            // Này là cập nhật thời gian hiện tại của Dự báo theo giờ
                            SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("HH:mm:ss");
                            String DayTime = simpleDateFormatDay.format(date);
                            txtUpdateTime.setText("Cập nhật: " + DayTime);

                            // Này là cập nhật thời gian hiện tại của Thời tiết hiện tại
                            txtUpdateCurrent.setText("Cập nhật: " + DayTime);


                            // Bình minh và hoàng hôn
                            String binhMinh = jsonObjectCurrent.getString("sunrise");
                            // Chuyển biến day về dạng long
                            long bm = Long.valueOf(binhMinh);
                            // Giá trị mili giây
                            Date BM = new Date(bm*1000L);
                            // Định dạng giờ phút giây
                            SimpleDateFormat simpleDateFormatBM = new SimpleDateFormat("h:mm a");
                            String BinhMinh = simpleDateFormatBM.format(BM);
                            txtBinhMinh.setText(BinhMinh);

                            String hoangHon = jsonObjectCurrent.getString("sunset");
                            // Chuyển biến day về dạng long
                            long hh = Long.valueOf(hoangHon);
                            // Giá trị mili giây
                            Date HH = new Date(hh*1000L);
                            // Định dạng giờ phút giây
                            SimpleDateFormat simpleDateFormatHH = new SimpleDateFormat("h:mm a");
                            String HoangHon = simpleDateFormatHH.format(HH);
                            txtHoangHon.setText(HoangHon);


                            // Thời tiết hiện tại
                            // Tốc độ gió
                            txtTocDoGio.setText(windSpeed + "m/s");

//                            // Lượng mưa
                            JSONObject jsonObjectRain = jsonObjectCurrent.getJSONObject("rain");
                            String luongMua = jsonObjectRain.getString("1h");
                            txtLuongMua.setText(luongMua + "mm");

                            // Độ ẩm tương đối
                            String doAmTuongDoi = jsonObjectCurrent.getString("humidity");
                            txtDoAmTuongDoi.setText(doAmTuongDoi + "%");

                            // Cảm giác như
                            String camGiacNhu = jsonObjectCurrent.getString("feels_like");
                            Double b = Double.valueOf(camGiacNhu);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String CamGiacNhu = String.valueOf(b.intValue());
                            txtCamGiacNhu.setText(CamGiacNhu + "°C");
//
                            // Chỉ số UV
                            String UV = jsonObjectCurrent.getString("uvi");
                            txtUV.setText(UV);

//                            // Tầm nhìn
                            String tamNhin = jsonObjectCurrent.getString("visibility");
                            txtTamNhin.setText(tamNhin + "m");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", "Có Lỗi");
                    }
                });
        requestQueue.add(stringRequest);
    }


    public void getAirPollution(String lat, String lon) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/air_pollution?lat="+lat+"&lon="+lon+"&units=metric&lang=vi&appid=7b16a3bb0d4c6253ab56ca6a2a14f500";
        Log.d("urlAir", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Response").child("Air Pollution");
                        myRef.setValue(response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("air", "onResponse: " + jsonObject);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            JSONObject jsonObjectAir = jsonArrayList.getJSONObject(0);

                            JSONObject jsonObjectMain = jsonObjectAir.getJSONObject("main");
                            String aqi = jsonObjectMain.getString("aqi");
                            aqi = chatLuong(aqi);
                            txtAqiAir.setText(aqi);
                            DatabaseReference myRefAqi = database.getReference("Weather").child("Air Pollution").child("aqi");
                            myRefAqi.setValue(aqi);

                            JSONObject jsonObjectComponents = jsonObjectAir.getJSONObject("components");
                            String no2 = jsonObjectComponents.getString("no2");
                            Double a = Double.valueOf(no2);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String NO2 = String.valueOf(a.intValue());
                            Log.d("1111", "" + no2 + ", " + NO2);
                            txtNO2.setText(NO2);
                            DatabaseReference myRefno2 = database.getReference("Weather").child("Air Pollution").child("no2");
                            myRefno2.setValue(NO2);

                            String pm10 = jsonObjectComponents.getString("pm10");
                            Double b = Double.valueOf(pm10);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String PM10 = String.valueOf(b.intValue());
                            Log.d("1111", "" + pm10 + ", " + PM10);
                            txtPM10.setText(PM10);
                            DatabaseReference myRefpm10 = database.getReference("Weather").child("Air Pollution").child("pm10");
                            myRefpm10.setValue(PM10);

                            String o3 = jsonObjectComponents.getString("o3");
                            Double c = Double.valueOf(o3);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String O3 = String.valueOf(c.intValue());
                            Log.d("1111", "" + o3 + ", " + O3);
                            txtO3.setText(O3);
                            DatabaseReference myRefo3 = database.getReference("Weather").child("Air Pollution").child("o3");
                            myRefo3.setValue(O3);

                            String pm2_5 = jsonObjectComponents.getString("pm2_5");
                            Double d = Double.valueOf(pm2_5);
                            // Đổi về kiểu int bằng intValue rồi đổi sang chuỗi
                            String PM2_5 = String.valueOf(d.intValue());
                            Log.d("1111", "" + pm2_5 + ", " + PM2_5);
                            txtPM2_5.setText(PM2_5);
                            DatabaseReference myRefpm2_5 = database.getReference("Weather").child("Air Pollution").child("pm2_5");
                            myRefpm2_5.setValue(PM2_5);

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
        requestQueue.add(stringRequest);
    }

    // Ánh xạ để lấy từng thành phần đã được đặt id
    private void Anhxa() {
        iconLocation = findViewById(R.id.viTriHienTai);

        edtSearch = findViewById(R.id.editTextSearch);
        iconSearch = findViewById(R.id.iconSearch);
        btnChangeActivity = findViewById(R.id.buttonChangeActivity);
        txtViewName = findViewById(R.id.textViewName);
        txtViewCountry = findViewById(R.id.textViewCountry);
        txtViewTemp = findViewById(R.id.textViewTemp);
        txtViewStatus = findViewById(R.id.textViewStatus);
        txtViewHumidity = findViewById(R.id.textViewHumidity);
        txtViewCloud = findViewById(R.id.textViewCloud);
        txtViewMill = findViewById(R.id.textViewMill);
        txtViewDay = findViewById(R.id.textViewDay);
        imgIcon = findViewById(R.id.imageIcon);


        // Dự báo theo giờ
        txtUpdateTime = findViewById(R.id.textViewUpdateTime);
        rcvTime = findViewById(R.id.rcv_time);
        list = new ArrayList<Time>();
        mTimeAdapter = new TimeAdapter(MainActivity.this, list);
        rcvTime.setAdapter(mTimeAdapter);
        btnAddTimeWeather = findViewById(R.id.btnAddTimeWeather);


        // Bình minh và hoàng hôn
        txtBinhMinh = findViewById(R.id.textViewBinhMinh);
        txtHoangHon = findViewById(R.id.textViewHoangHon);


        // Thời tiết hiện tại tại màn hình chính
        txtUpdateCurrent = findViewById(R.id.textViewUpdateCurrent);
        txtTocDoGio = findViewById(R.id.textViewTocDoGio);
        txtLuongMua = findViewById(R.id.textViewLuongMua);
        txtDoAmTuongDoi = findViewById(R.id.textViewDoAmTuongDoi);
        txtCamGiacNhu = findViewById(R.id.textViewCamGiacNhu);
        txtUV = findViewById(R.id.textViewUV);
        txtTamNhin = findViewById(R.id.textViewTamNhin);
        btnAddCurrentWeather = findViewById(R.id.btnAddCurrentWeather);

        // Chất lượng không khí
        txtAqiAir = findViewById(R.id.tvAqiAir);
        txtNO2 = findViewById(R.id.tvNO2);
        txtPM10 = findViewById(R.id.tvPM10);
        txtO3 = findViewById(R.id.tvO3);
        txtPM2_5 = findViewById(R.id.tvPM2_5);

        // Xem lịch sử thời tiết trong vòng 30 ngày, dữ liệu được tính trên mỗi giờ
        btnHistoryWeather =  findViewById(R.id.btnHistoryWeather);
    }

    public  String chatLuong(String abc){
        int a = Integer.parseInt(abc);
        switch (a){
            case 1:
                abc= "Tốt";
                break;
            case 2:
                abc = "Dịu nhẹ";
                break;
            case 3:
                abc = "Trung bình";
                break;
            case 4:
                abc = "Xấu";
                break;
            case 5:
                abc = "Kém";
                break;
        }
        return abc;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimeAdapter.giaiPhong();
    }

}