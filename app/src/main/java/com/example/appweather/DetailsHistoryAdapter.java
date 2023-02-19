package com.example.appweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailsHistoryAdapter extends RecyclerView.Adapter<DetailsHistoryAdapter.DetailHistoryWeatherViewHolder> {
    private Context mContextDetailHistoryWeather;
    private List<DetailHistory> mListDetailHistoryWeather;

    public DetailsHistoryAdapter(Context mContextDetailHistoryWeather, List<DetailHistory> mListDetailHistoryWeather) {
        this.mContextDetailHistoryWeather = mContextDetailHistoryWeather;
        this.mListDetailHistoryWeather = mListDetailHistoryWeather;
    }

    public void setData(List<DetailHistory> detailHistoryList) {
        this.mListDetailHistoryWeather = detailHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailsHistoryAdapter.DetailHistoryWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_history_weather, parent, false);
        return new DetailsHistoryAdapter.DetailHistoryWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsHistoryAdapter.DetailHistoryWeatherViewHolder holder, int position) {
        int a = position;
        DetailHistory detailHistory = mListDetailHistoryWeather.get(position);
        if (detailHistory == null) {
            return;
        }
        Picasso.with(mContextDetailHistoryWeather).load("https://openweathermap.org/img/wn/" + detailHistory.getImgTrangThaiDetailHW() + ".png").into(holder.imgIconTrangThaiDetailHW);
        holder.tvTimeDetailHW.setText(detailHistory.getTimeDetailHW());
        holder.tvNhietDoDetailHW.setText(detailHistory.getNhietDoDetailHW() + "°C");
        holder.tvTrangThaiDetailHW.setText(detailHistory.getTrangThaiDetailHW());
        holder.tvCamGiacNhuDetailHW.setText(detailHistory.getCamGiacNhuDetailHW() + "°C");
        holder.tvTocDoGioDetailHW.setText(detailHistory.getTocDoGioDetailHW() + "m/s");
        holder.tvHuongGioDetailHW.setText(detailHistory.getHuongGioDetailHW());
        holder.tvApSuatDetailHW.setText(detailHistory.getApSuatDetailHW() + "hPa");
        holder.tvDoAmDetailHW.setText(detailHistory.getDoAmDetailHW() + "%");
    }

    @Override
    public int getItemCount() {
        if (mListDetailHistoryWeather != null) {
            return mListDetailHistoryWeather.size();
        }
        return 0;
    }

    public class DetailHistoryWeatherViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIconTrangThaiDetailHW;
        private TextView tvTimeDetailHW, tvNhietDoDetailHW, tvTrangThaiDetailHW, tvCamGiacNhuDetailHW, tvTocDoGioDetailHW, tvHuongGioDetailHW, tvApSuatDetailHW, tvDoAmDetailHW;

        public DetailHistoryWeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIconTrangThaiDetailHW = (ImageView) itemView.findViewById(R.id.imgTrangThaiDetailHistoryWeather);
//            tvDayHW = (TextView) itemView.findViewById(R.id.tvDayHistoryWeather);
            tvTimeDetailHW = (TextView) itemView.findViewById(R.id.tvTimeDetailHW);
            tvNhietDoDetailHW = (TextView) itemView.findViewById(R.id.tvNhietDoDetailHistoryWeather);
            tvTrangThaiDetailHW = (TextView) itemView.findViewById(R.id.tvTrangThaiDetailHistoryWeather);
            tvCamGiacNhuDetailHW = (TextView) itemView.findViewById(R.id.tvCamGiacNhuDetailHistoryWeather);
            tvTocDoGioDetailHW = (TextView) itemView.findViewById(R.id.tvTocDoGioDetailHistoryWeather);
            tvHuongGioDetailHW = (TextView) itemView.findViewById(R.id.tvHuongGioDetailHistoryWeather);
            tvApSuatDetailHW = (TextView) itemView.findViewById(R.id.tvApSuatDetailHistoryWeather);
            tvDoAmDetailHW = (TextView) itemView.findViewById(R.id.tvDoAmDetailHistoryWeather);
        }
    }

    public void giaiPhong() {
        mContextDetailHistoryWeather = null;
    }
}
