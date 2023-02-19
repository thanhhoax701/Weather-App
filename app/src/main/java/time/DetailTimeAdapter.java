package time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweather.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailTimeAdapter extends RecyclerView.Adapter<DetailTimeAdapter.DetailTimeViewHolder> {

    private Context mContextDetailTime;
    private List<DetailTime> mListDetailTime;

    public DetailTimeAdapter(Context mContextDetailTime, List<DetailTime> listDetail) {
        this.mContextDetailTime = mContextDetailTime;
    }

    public void setData (List<DetailTime> listDetail) {
        this.mListDetailTime = listDetail;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DetailTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_weather_time, parent, false);
        return new DetailTimeAdapter.DetailTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailTimeViewHolder holder, int position) {
        DetailTime detailTime = mListDetailTime.get(position);
        if (detailTime == null) {
            return;
        }

        Picasso.with(mContextDetailTime).load("https://openweathermap.org/img/wn/"+detailTime.getImgTrangThaiTime()+".png").into(holder.imgIconTrangThaiTime);
        holder.tvDay.setText(detailTime.getDetailDay());
        holder.tvTime.setText(detailTime.getDetailTime());
        holder.tvNhietDoTime.setText(detailTime.getNhietDoTime() + "°C");
        holder.tvTrangThai.setText(detailTime.getTrangThaiTime());
        holder.tvTocDoGioTime.setText(detailTime.getGioTime() + "m/s");
        holder.tvApSuatTime.setText(detailTime.getApSuatTime() + "hPa");
        holder.tvDoAmTime.setText(detailTime.getDoAmTime() + "%");
        holder.tvChiSoUVTime.setText(detailTime.getChiSoUVTime());
    }

    @Override
    public int getItemCount() {
        if (mListDetailTime!= null) {
            return mListDetailTime.size();
        }
        return 0;
    }

    public class DetailTimeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIconTrangThaiTime;
        private TextView tvDay, tvTime, tvNhietDoTime, tvTrangThai, tvTocDoGioTime, tvApSuatTime, tvDoAmTime, tvChiSoUVTime;

        public DetailTimeViewHolder(@NonNull View itemView) {
            super(itemView);

            // detailDay, detailTime, nhietDoTime, trangThaiTime, apSuatTime, doAmTime, chiSoUVTime, gioTime
            imgIconTrangThaiTime = (ImageView) itemView.findViewById(R.id.imgTrangThaiTime);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvNhietDoTime = (TextView) itemView.findViewById(R.id.tvNhietDoTime);
            tvTrangThai = (TextView) itemView.findViewById(R.id.tvTrangThaiTime);
            tvTocDoGioTime = (TextView) itemView.findViewById(R.id.tvTocDoGioTime);
            tvApSuatTime = (TextView) itemView.findViewById(R.id.tvApSuatTime);
            tvDoAmTime = (TextView) itemView.findViewById(R.id.tvDoAmTime);
            tvChiSoUVTime = (TextView) itemView.findViewById(R.id.tvChiSoUVTime);
        }
    }

    public void giaiPhong(){
        mContextDetailTime= null;
    }
}