package com.plantmer.soilsensor.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    private MainActivity main;

    public void setMain(MainActivity main) {
        this.main = main;
    }
    public MainFragment() {
        // Required empty public constructor
    }
    public void setConnected(boolean connected){
        if(connected){
            connText.setText("Connected "+main.getSerial().getType());
        }else{
            connText.setText("Disconnected ");
        }

    }
    public void append(String[] split){
        if(split.length==4){ // readings
            mDielectricPermittivityView.setText(getString(R.string.dp_label, split[0]));
            mElectricalConductivityView.setText(getString(R.string.ec_label, split[1]));
            mTemperatureView.setText(getString(R.string.temp_label, split[2]));
            mVWCView.setText(getString(R.string.vwc_label, split[3], mPercentSign));
        }else if(split.length==5){ //readings with date
            mDielectricPermittivityView.setText(getString(R.string.dp_label, split[1]));
            mElectricalConductivityView.setText(getString(R.string.ec_label, split[2]));
            mTemperatureView.setText(getString(R.string.temp_label, split[3]));
            mVWCView.setText(getString(R.string.vwc_label, split[4], mPercentSign));
        }
    }

    private TextView mDielectricPermittivityView;
    private TextView mElectricalConductivityView;
    private TextView mTemperatureView;
    private TextView mVWCView;

    private TextView mIconDPView;
    private TextView mIconECView;
    private TextView mIconTempView;
    private TextView mIconVWCView;

    private TextView connText;
    private Button connButton;
    private Button readButton;

    private String mPercentSign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        weatherConditionsIcons();
        initializeTextView();
        updateCurrentWeather();
    }

    private void updateCurrentWeather() {
        mDielectricPermittivityView.setText(getString(R.string.dp_label, "20.0"));
        mElectricalConductivityView.setText(getString(R.string.ec_label, "20.0"));
        mTemperatureView.setText(getString(R.string.temp_label, "20.0"));
        mVWCView.setText(getString(R.string.vwc_label, "10.0", mPercentSign));

     }
    @Override
    public void onClick(View view)
    {
        Log.d("page", "onClick: "+view.getId());
        switch (view.getId()) {
            case R.id.connButton:
                try {
                    main.getSerial().open();
                } catch (IOException e) {
                    Log.e("main","connButton",e);
                }
                break;
            case R.id.readButton:
                main.getSerial().writeCmd("read");
                break;
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
        readButton =  getActivity().findViewById(R.id.readButton);
        readButton.setOnClickListener(this);
        connText =  getActivity().findViewById(R.id.connText);

        mDielectricPermittivityView =  getActivity().findViewById(R.id.main_dp);
        mElectricalConductivityView =  getActivity().findViewById(R.id.main_ec);
        mTemperatureView =  getActivity().findViewById(R.id.main_temp);
        mVWCView =  getActivity().findViewById(R.id.main_vwc);

        mIconDPView =  getActivity().findViewById(R.id.icon_dp);
        mIconECView=  getActivity().findViewById(R.id.icon_ec);
        mIconTempView=  getActivity().findViewById(R.id.icon_temp);
        mIconVWCView=  getActivity().findViewById(R.id.icon_vwc);

        connButton.setTypeface(robotoLight);
        connText.setTypeface(robotoLight);
        mDielectricPermittivityView.setTypeface(robotoLight);
        mElectricalConductivityView.setTypeface(robotoLight);
        mTemperatureView.setTypeface(robotoLight);
        mVWCView.setTypeface(robotoLight);

        mIconDPView.setTypeface(weatherFontIcon);
        mIconDPView.setText(mIconDp);
        mIconECView.setTypeface(weatherFontIcon);
        mIconECView.setText(mIconEc);
        mIconTempView.setTypeface(weatherFontIcon);
        mIconTempView.setText(mIconTemp);
        mIconVWCView.setTypeface(weatherFontIcon);
        mIconVWCView.setText(mIconVWC);
        showLogCB = getActivity().findViewById( R.id.showLogCB );
        showLogCB.setChecked(true);
        showLogCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                main.setLog(isChecked);
            }
        });
    }
    CheckBox showLogCB;

    private String mIconDp;
    private String mIconTemp;
    private String mIconEc;
    private String mIconVWC;

    private void weatherConditionsIcons() {
        mPercentSign = getString(R.string.percent_sign);
        mIconDp = getString(R.string.icon_dp);
        mIconTemp = getString(R.string.icon_temp);
        mIconEc = getString(R.string.icon_ec);
        mIconVWC = getString(R.string.icon_vwc);
    }
}
