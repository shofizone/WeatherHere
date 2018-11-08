package weatherhere.uits.shofiullah.com.weatherhere;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Current;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Day;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Forcast;
import weatherhere.uits.shofiullah.com.weatherhere.weather.Hour;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORCAST = "DAILY_FORCAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // private TextView  mTempLabel;
    @BindView(R.id.timeLabel)
    TextView mTimeLabel;
    @BindView(R.id.tempLabel)
    TextView mTempLabel;
    @BindView(R.id.humidityValue)
    TextView mHumedityValue;
    @BindView(R.id.pricpValue)
    TextView mPricipValue;
    @BindView(R.id.summaryLebel)
    TextView mSummary;
    @BindView(R.id.iconImageView)
    ImageView mIconImageView;
    @BindView(R.id.refreshImageButton)
    ImageView mrefreshButton;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.textView2)
    TextView mCityName;
    private Forcast mForcast;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private double mLat, mLang;
    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  final double latitude =23.810332;
        //  final double longtude = 90.41251809999994;

        getCurrentLocation();
        final double latitude = mLat;
        final double longtude = mLang;


        ButterKnife.bind(this);
        // mTempLabel = (TextView) findViewById(R.id.tempLabel);
        mProgressBar.setVisibility(View.INVISIBLE);
        mrefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getForcast(latitude, longtude);
            }
        });

        getForcast(latitude, longtude);


    }

    private void getCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService((MainActivity.this).LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                mLat = location.getLatitude();
                mLang = location.getLongitude();
                Log.d(TAG, "getCurrentLocation: Location is not null Lat: " + location.getLatitude() + "Lng: " + location.getLatitude());

            } else {
                Log.d(TAG, "getCurrentLocation: Unable to find location");
                mLat = 23.810332;
                mLang = 90.41251809999994;
            }
        }

    }


    private void getForcast(double latitude, double longtude) {

        String apikey = "93a291a8c8962b2b7d4b3211a5614fbf";
        String forCastUrl = "https://api.darksky.net/forecast/" + apikey + "/" + latitude + "," + longtude;
        if (isNetworkAvailabel()) {
            toggleVisibility();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forCastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUseraboutError();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleVisibility();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleVisibility();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForcast = parsForcastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });


                        } else {
                            alertUseraboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception Caught:", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception Caught:", e);
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.network_unavalible_message, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleVisibility() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mrefreshButton.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mrefreshButton.setVisibility(View.VISIBLE);
        }

    }

    private Forcast parsForcastDetails(String jsonData) throws JSONException {
        Forcast forcast = new Forcast();
        forcast.setCurrent(getCurrentDetails(jsonData));
        forcast.setHourlyForcast(getHourlyforcast(jsonData));
        forcast.setDailyforcast(getDailyForcast(jsonData));

        return forcast;
    }

    private Day[] getDailyForcast(String jsonData) throws JSONException {
        JSONObject forcast = new JSONObject(jsonData);
        String timezone = forcast.getString("timezone");

        JSONObject daily = forcast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);
            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTime(jsonDay.getLong("time"));
            day.setTimezone(timezone);
            day.setCity(timezone.substring(timezone.lastIndexOf("/") + 1));

            days[i] = day;
            Log.i(TAG, "From Day JESON: " + days[i].getSummary());

        }
        return days;
    }

    private Hour[] getHourlyforcast(String jsonData) throws JSONException {
        JSONObject forcast = new JSONObject(jsonData);
        String timezone = forcast.getString("timezone");
        JSONObject hourly = forcast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonhour = data.getJSONObject(i);
            Hour hour = new Hour();

            hour.setSummary(jsonhour.getString("summary"));
            hour.setTemperature(jsonhour.getDouble("temperature"));
            hour.setIcon(jsonhour.getString("icon"));
            hour.setTime(jsonhour.getLong("time"));
            hour.setTimezone(timezone);
            hours[i] = hour;
            hour.setCity(timezone.substring(timezone.lastIndexOf("/") + 1));
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {

        JSONObject forcast = new JSONObject(jsonData);
        String timezone = forcast.getString("timezone");

        JSONObject currently = forcast.getJSONObject("currently");

        Current currentWeather = new Current();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setPrecipChance(currently.getInt("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTimeZone(timezone);
        currentWeather.setCity(timezone.substring(timezone.lastIndexOf("/") + 1));
        Log.d(TAG, currentWeather.getFormatedTime());


        return currentWeather;
    }

    private void updateDisplay() {

        Current current = mForcast.getCurrent();
        mTempLabel.setText(current.getTemperature() + "");
        Log.i(TAG, "Temp: " + current.getTemperature());
        mTimeLabel.setText("At " + current.getFormatedTime() + " it will be");
        mHumedityValue.setText(current.getHumidity() + "");
        mPricipValue.setText(current.getPrecipChance());
        mSummary.setText("Summary: " + current.getSummary() + "");
        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
        mCityName.setText(current.getCity());

    }


    private boolean isNetworkAvailabel() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    private void alertUseraboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivery(View view) {
        Intent intent = new Intent(MainActivity.this, DayListActivity.class);
        intent.putExtra(DAILY_FORCAST, mForcast.getDailyforcast());
        Log.i(TAG, "From Day JESON: " + mForcast.getDailyforcast());
        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(MainActivity.this, HourlyListActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForcast.getHourlyForcast());
        startActivity(intent);
    }

}
