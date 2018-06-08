package com.plantmer.soilsensor.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;
import com.plantmer.soilsensor.util.Utils;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    MainActivity act;
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

    private TextView mPressureView;
    private TextView mCloudinessView;
    private TextView mSunriseView;
    private TextView mSunsetView;
    private TextView mIconPressureView;
    private TextView mIconCloudinessView;
    private TextView mIconSunriseView;
    private TextView mIconSunsetView;
    private TextView connText;

    private Button connButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        weatherConditionsIcons();
        initializeTextView(view);
    }

    private void updateCurrentWeather() {
        String pressure = String.format(Locale.getDefault(), "%.1f",10.01f);
        String wind = String.format(Locale.getDefault(), "%.1f", 10.01f);


        mPressureView.setText(getString(R.string.pressure_label, pressure,
                mPressureMeasurement));
        mSunriseView.setText(getString(R.string.sunrise_label, "19:00"));
        mSunsetView.setText(getString(R.string.sunset_label, "19:00"));

     }

    private void initializeTextView(View vv) {
        Typeface weatherFontIcon = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

        connButton =  getActivity().findViewById(R.id.connButton);
        connText =  getActivity().findViewById(R.id.connText);

        mPressureView =  getActivity().findViewById(R.id.main_pressure);
        mCloudinessView =  getActivity().findViewById(R.id.main_cloudiness);
        mSunriseView =  getActivity().findViewById(R.id.main_sunrise);
        mSunsetView =  getActivity().findViewById(R.id.main_sunset);

        connButton.setTypeface(robotoLight);
        connText.setTypeface(robotoLight);
        mPressureView.setTypeface(robotoLight);
        mCloudinessView.setTypeface(robotoLight);
        mSunriseView.setTypeface(robotoLight);
        mSunsetView.setTypeface(robotoLight);

        /**
         * Initialize and configure weather icons
         */
        mIconPressureView =  getActivity().findViewById(R.id.main_pressure_icon);
        mIconPressureView.setTypeface(weatherFontIcon);
        mIconPressureView.setText(mIconPressure);
        mIconCloudinessView =  getActivity().findViewById(R.id.main_cloudiness_icon);
        mIconCloudinessView.setTypeface(weatherFontIcon);
        mIconCloudinessView.setText(mIconCloudiness);
        mIconSunriseView =  getActivity().findViewById(R.id.main_sunrise_icon);
        mIconSunriseView.setTypeface(weatherFontIcon);
        mIconSunriseView.setText(mIconSunrise);
        mIconSunsetView =  getActivity().findViewById(R.id.main_sunset_icon);
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
