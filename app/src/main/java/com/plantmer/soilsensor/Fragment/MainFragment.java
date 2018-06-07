package com.plantmer.soilsensor.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plantmer.soilsensor.R;
import com.plantmer.soilsensor.util.Utils;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }

    private String mIconWind;
    private String mIconHumidity;
    private String mIconPressure;
    private String mIconCloudiness;
    private String mIconSunrise;
    private String mIconSunset;
    private String mPercentSign;
    private String mPressureMeasurement;

    private TextView mHumidityView;
    private TextView mWindSpeedView;
    private TextView mPressureView;
    private TextView mCloudinessView;
    private TextView mSunriseView;
    private TextView mSunsetView;
    private TextView mIconWindView;
    private TextView mIconHumidityView;
    private TextView mIconPressureView;
    private TextView mIconCloudinessView;
    private TextView mIconSunriseView;
    private TextView mIconSunsetView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv =inflater.inflate(R.layout.fragment_1, container, false);
        weatherConditionsIcons();
        initializeTextView(vv);

        return vv;
    }
    private void updateCurrentWeather() {
        String pressure = String.format(Locale.getDefault(), "%.1f",10.01f);
        String wind = String.format(Locale.getDefault(), "%.1f", 10.01f);


        mHumidityView.setText(getString(R.string.humidity_label,
                String.valueOf(50),
                mPercentSign));
        mPressureView.setText(getString(R.string.pressure_label, pressure,
                mPressureMeasurement));
        mWindSpeedView.setText(getString(R.string.wind_label, wind, "20"));
        mSunriseView.setText(getString(R.string.sunrise_label, "19:00"));
        mSunsetView.setText(getString(R.string.sunset_label, "19:00"));

     }

    private void initializeTextView(View vv) {
        Typeface weatherFontIcon = Typeface.createFromAsset(vv.getContext().getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoLight = Typeface.createFromAsset(vv.getContext().getAssets(),
                "fonts/Roboto-Light.ttf");

        mPressureView = (TextView) vv.findViewById(R.id.main_pressure);
        mHumidityView = (TextView) vv.findViewById(R.id.main_humidity);
        mWindSpeedView = (TextView) vv.findViewById(R.id.main_wind_speed);
        mCloudinessView = (TextView) vv.findViewById(R.id.main_cloudiness);
        mSunriseView = (TextView) vv.findViewById(R.id.main_sunrise);
        mSunsetView = (TextView) vv.findViewById(R.id.main_sunset);

        mWindSpeedView.setTypeface(robotoLight);
        mHumidityView.setTypeface(robotoLight);
        mPressureView.setTypeface(robotoLight);
        mCloudinessView.setTypeface(robotoLight);
        mSunriseView.setTypeface(robotoLight);
        mSunsetView.setTypeface(robotoLight);

        /**
         * Initialize and configure weather icons
         */
        mIconWindView = (TextView) vv.findViewById(R.id.main_wind_icon);
        mIconWindView.setTypeface(weatherFontIcon);
        mIconWindView.setText(mIconWind);
        mIconHumidityView = (TextView) vv.findViewById(R.id.main_humidity_icon);
        mIconHumidityView.setTypeface(weatherFontIcon);
        mIconHumidityView.setText(mIconHumidity);
        mIconPressureView = (TextView) vv.findViewById(R.id.main_pressure_icon);
        mIconPressureView.setTypeface(weatherFontIcon);
        mIconPressureView.setText(mIconPressure);
        mIconCloudinessView = (TextView) vv.findViewById(R.id.main_cloudiness_icon);
        mIconCloudinessView.setTypeface(weatherFontIcon);
        mIconCloudinessView.setText(mIconCloudiness);
        mIconSunriseView = (TextView) vv.findViewById(R.id.main_sunrise_icon);
        mIconSunriseView.setTypeface(weatherFontIcon);
        mIconSunriseView.setText(mIconSunrise);
        mIconSunsetView = (TextView) vv.findViewById(R.id.main_sunset_icon);
        mIconSunsetView.setTypeface(weatherFontIcon);
        mIconSunsetView.setText(mIconSunset);
    }

    private void weatherConditionsIcons() {
        mIconWind = getString(R.string.icon_wind);
        mIconHumidity = getString(R.string.icon_humidity);
        mIconPressure = getString(R.string.icon_barometer);
        mIconCloudiness = getString(R.string.icon_cloudiness);
        mPercentSign = getString(R.string.percent_sign);
        mPressureMeasurement = getString(R.string.pressure_measurement);
        mIconSunrise = getString(R.string.icon_sunrise);
        mIconSunset = getString(R.string.icon_sunset);
    }
}
