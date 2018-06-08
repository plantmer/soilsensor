package com.plantmer.soilsensor.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    MainActivity act;
    public MainFragment() {
        // Required empty public constructor
    }


    private TextView mDielectricPermittivityView;
    private TextView mElectricalConductivityView;
    private TextView mTemperatureView;
    private TextView mVWCView;

    private TextView connText;
    private Button connButton;

    private String mPercentSign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initializeTextView();
    }

    private void updateCurrentWeather() {
        mDielectricPermittivityView.setText(getString(R.string.dp_label, "20.0"));
        mElectricalConductivityView.setText(getString(R.string.ec_label, "20.0"));
        mTemperatureView.setText(getString(R.string.temperature_with_degree, "20.0"));
        mVWCView.setText(getString(R.string.humidity_label, "10.0", mPercentSign));

     }
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.connButton:
                // Do something
        }
    }
    private void initializeTextView() {
        mPercentSign = getString(R.string.percent_sign);
        Typeface weatherFontIcon = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

        connButton =  getActivity().findViewById(R.id.connButton);
        connButton.setOnClickListener(this);
        connText =  getActivity().findViewById(R.id.connText);

        mDielectricPermittivityView =  getActivity().findViewById(R.id.main_dp);
        mElectricalConductivityView =  getActivity().findViewById(R.id.main_ec);
        mTemperatureView =  getActivity().findViewById(R.id.main_temp);
        mVWCView =  getActivity().findViewById(R.id.main_vwc);

        connButton.setTypeface(robotoLight);
        connText.setTypeface(robotoLight);
        mDielectricPermittivityView.setTypeface(robotoLight);
        mElectricalConductivityView.setTypeface(robotoLight);
        mTemperatureView.setTypeface(robotoLight);
        mVWCView.setTypeface(robotoLight);

    }
}
