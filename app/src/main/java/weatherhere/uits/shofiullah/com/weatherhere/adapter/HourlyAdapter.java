package weatherhere.uits.shofiullah.com.weatherhere.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import weatherhere.uits.shofiullah.com.weatherhere.R;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Hour;

/**
 * Created by shofi on 04-Jan-18.
 */

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourViewHolder> {
    private Hour[] mHours;
    private Context mContext;

    public HourlyAdapter(Context context, Hour[] hours) {
        mContext = context;
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_list_item, parent, false);
        HourViewHolder viewHolder = new HourViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }


    public class HourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTimeLabel;
        public TextView mSummaryLabel;
        public TextView mTemperatureLabel;
        public ImageView mIconImageView;


        public HourViewHolder(View itemView) {
            super(itemView);

            mTimeLabel = (TextView) itemView.findViewById(R.id.timeLabelH);
            mSummaryLabel = (TextView) itemView.findViewById(R.id.summaryTextviewH);
            mTemperatureLabel = (TextView) itemView.findViewById(R.id.tempTextViewH);
            mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageViewH);

            itemView.setOnClickListener(this);
        }

        public void bindHour(Hour hour) {
            mTimeLabel.setText(hour.getHour());
            mSummaryLabel.setText((hour.getSummary()));
            mTemperatureLabel.setText(hour.getTemperature() + "");
            mIconImageView.setImageResource(hour.getIconId());
        }

        @Override
        public void onClick(View view) {
            String time = mTimeLabel.getText().toString();
            String temperature = mTemperatureLabel.getText().toString();
            String summary = mSummaryLabel.getText().toString();
            String message = String.format("At %s ts will be %s and %s", time, temperature, summary);
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

}
