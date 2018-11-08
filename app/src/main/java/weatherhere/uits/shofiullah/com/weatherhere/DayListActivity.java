package weatherhere.uits.shofiullah.com.weatherhere;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import weatherhere.uits.shofiullah.com.weatherhere.adapter.DayAdapter;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Day;

public class DayListActivity extends AppCompatActivity {
    public TextView mEmptyTextView;
    ListView mListView;
    private Day[] mDays;
    private TextView mCityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_list);
        mListView = (ListView) findViewById(R.id.dailyList);
        mCityTV = (TextView) findViewById(R.id.locationTextView);
        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORCAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter adapter = new DayAdapter(this, mDays);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String daysOfWeek = mDays[i].getDayOfTheWeek();
                String condition = mDays[i].getSummary();
                String highTemp = mDays[i].getTemperatureMax() + "";
                String message = String.format("On %s the high will be %s and it will be %s", daysOfWeek, highTemp, condition);
                Toast.makeText(DayListActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        mEmptyTextView = (TextView) findViewById(R.id.emptyDayListTextView);
        if (mDays != null) {
            mEmptyTextView.setVisibility(View.INVISIBLE);
        }


    }


}
