package weatherhere.uits.shofiullah.com.weatherhere;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import weatherhere.uits.shofiullah.com.weatherhere.adapter.HourlyAdapter;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Hour;

public class HourlyListActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private Hour[] mHours;
    private TextView mCityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_list);
        ButterKnife.bind(this);

        mCityTV = (TextView) findViewById(R.id.locationHourly);
        Intent intent = getIntent();
        Parcelable[] parcelable = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST);
        mHours = Arrays.copyOf(parcelable, parcelable.length, Hour[].class);

        HourlyAdapter adapter = new HourlyAdapter(this, mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

    }
}






















