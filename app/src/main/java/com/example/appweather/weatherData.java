package com.example.appweather;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {
    private String mTemperature,micon,mcity,mWeatherType;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject) {
        try {
            weatherData weatherD = new weatherData();
            weatherD.mcity = jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            return weatherD;

        }

        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
