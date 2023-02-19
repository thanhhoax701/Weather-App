package time;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appweather.MainActivity;
import com.example.appweather.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeViewHolder> {
    private Context mContext;
    private List<Time> mListTime;

    public TimeAdapter(Context mContext, List<Time> mListTime) {
        this.mContext = mContext;
        this.mListTime = mListTime;
    }

    public void setData (List<Time> list) {
        this.mListTime = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_time, parent, false);
        return new TimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {
        Time time = mListTime.get(position);
        if (time == null) {
            return;
        }

        holder.tvTime.setText(time.getTime());
        holder.tvTemp.setText(time.getTemp()+ "°C");
        Picasso.with(mContext).load("https://openweathermap.org/img/wn/"+time.getImgIcon()+".png").into(holder.imgIcon);
        holder.tvSpeed.setText(time.getSpeed() + "m/s");
    }

    @Override
    public int getItemCount() {
        if (mListTime!= null) {
            return mListTime.size();
        }
        return 0;
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        private TextView tvTime, tvTemp, tvSpeed;

        public TimeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgIcon = (ImageView) itemView.findViewById(R.id.img_icon);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvTemp = (TextView) itemView.findViewById(R.id.tv_temp);
            tvSpeed = (TextView) itemView.findViewById(R.id.tv_speed);
        }
    }
    public void giaiPhong(){
        mContext = null;
    }

}
