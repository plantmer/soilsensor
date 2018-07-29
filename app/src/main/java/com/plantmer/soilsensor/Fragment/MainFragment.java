package com.plantmer.soilsensor.Fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.SignInButton;
import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;
import com.plantmer.soilsensor.dao.DataObj;
import com.plantmer.soilsensor.dao.DataSourceDTO;
import com.plantmer.soilsensor.dao.DeviceObj;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements View.OnClickListener{

    private MainActivity main;

    public void setMain(MainActivity main) {
        this.main = main;

    }
    public MainFragment() {
        // Required empty public constructor
    }
    public void setConnected(boolean connected){
        if(connText==null){
            return;
        }
       if(connected){
            readButton.setVisibility(View.VISIBLE);
            connText.setText("USB Connected "+main.getType());
        }else{
            connText.setText("USB Disconnected ");
            readButton.setVisibility(View.GONE);
        }

    }
    public void append(String[] split){
        if(!init){
            return;
        }
        if(split.length==4){ // readings
            mDielectricPermittivityView.setText(getString(R.string.dp_label, split[0]));
            mElectricalConductivityView.setText(getString(R.string.ec_label, split[1]));
            mTemperatureView.setText(getString(R.string.temp_label, split[2]));
            mVWCView.setText(getString(R.string.vwc_label, getWString(split[3]), mPercentSign));
        }else if(split.length==5){ //readings with date
            mDielectricPermittivityView.setText(getString(R.string.dp_label, split[1]));
            mElectricalConductivityView.setText(getString(R.string.ec_label, split[2]));
            mTemperatureView.setText(getString(R.string.temp_label, split[3]));
            mVWCView.setText(getString(R.string.vwc_label, getWString(split[4]), mPercentSign));
        }
    }
    DecimalFormat df = new DecimalFormat("#.00");
    public void append(DataObj dob){
        if(!init){
            return;
        }//String.format ("%.2f",
        mDielectricPermittivityView.setText(getString(R.string.dp_label, String.format ("%.2f",dob.getDp())));
        mElectricalConductivityView.setText(getString(R.string.ec_label, String.format ("%.2f",dob.getEc())));
        mTemperatureView.setText(getString(R.string.temp_label, String.format ("%.2f",dob.getTemp())));
        mVWCView.setText(getString(R.string.vwc_label, getWString(String.format ("%.2f",dob.getVwc())), mPercentSign));
        mBatView.setText(getString(R.string.bat_label, getWString(String.format ("%.2f",dob.getBatVolt())), mVoltSign));
        mRssiView.setText(getString(R.string.rssi_label, String.valueOf(dob.getRssi())));
    }

    private String getWString(String s) {
        return String.valueOf(BigDecimal.valueOf(Float.valueOf(s)*100.0).round(new MathContext(2, RoundingMode.HALF_UP)));
    }

    private TextView mDielectricPermittivityView;
    private TextView mElectricalConductivityView;
    private TextView mTemperatureView;
    private TextView mVWCView;
    private TextView mBatView;
    private TextView mRssiView;

    private TextView mIconDPView;
    private TextView mIconECView;
    private TextView mIconTempView;
    private TextView mIconVWCView;
    private TextView mIconBatView;
    private TextView mIconRssiView;

    private TextView connText;
//    private Button connButton;
    private Button readButton;
    boolean init = false;
    private String mPercentSign;
    private String mVoltSign;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_0, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        weatherConditionsIcons();
        initializeTextView();
        //updateCurrentWeather();
        setConnected(main.isConnected());
    }

    private void updateCurrentWeather() {
        mDielectricPermittivityView.setText(getString(R.string.dp_label, "0.00"));
        mElectricalConductivityView.setText(getString(R.string.ec_label, "0.00"));
        mTemperatureView.setText(getString(R.string.temp_label, "0.00"));
        mVWCView.setText(getString(R.string.vwc_label, "0.00", mPercentSign));
        mBatView.setText(getString(R.string.bat_label, "0.00", mVoltSign));
        mRssiView.setText(getString(R.string.rssi_label, "0"));

     }


    @Override
    public void onClick(View view)
    {
        //Log.d("page", "onClick: "+view.getId());
        switch (view.getId()) {
//            case R.id.connButton:
//                try {
//                    main.run();
//                } catch (Exception e) {
//                    Log.e("main","connButton",e);
//                }
//                break;
            case R.id.readButton:
                if(!main.isConnected()){
                    main.alertNotConn();
                    return;
                }

                main.getSerial().writeCmd("read");
                break;
            case R.id.sign_in_button:
                main.signIn();
                break;
        }
    }
    private void initializeTextView() {
        mPercentSign = getString(R.string.percent_sign);
        mVoltSign = getString(R.string.volt_sign);
        Typeface weatherFontIcon = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/Roboto-Light.ttf");

//        connButton =  getActivity().findViewById(R.id.connButton);
//        connButton.setOnClickListener(this);
        readButton =  getActivity().findViewById(R.id.readButton);
        readButton.setOnClickListener(this);
        connText =  getActivity().findViewById(R.id.connText);

        mDielectricPermittivityView =  getActivity().findViewById(R.id.main_dp);
        mElectricalConductivityView =  getActivity().findViewById(R.id.main_ec);
        mTemperatureView =  getActivity().findViewById(R.id.main_temp);
        mVWCView =  getActivity().findViewById(R.id.main_vwc);
        mBatView =  getActivity().findViewById(R.id.main_bat);
        mRssiView =  getActivity().findViewById(R.id.main_rssi);

        mIconDPView =  getActivity().findViewById(R.id.icon_dp);
        mIconECView=  getActivity().findViewById(R.id.icon_ec);
        mIconTempView=  getActivity().findViewById(R.id.icon_temp);
        mIconVWCView=  getActivity().findViewById(R.id.icon_vwc);
        mIconBatView=  getActivity().findViewById(R.id.icon_bat);
        mIconRssiView=  getActivity().findViewById(R.id.icon_rssi);

        connText.setTypeface(robotoLight);
        mDielectricPermittivityView.setTypeface(robotoLight);
        mElectricalConductivityView.setTypeface(robotoLight);
        mTemperatureView.setTypeface(robotoLight);
        mVWCView.setTypeface(robotoLight);
        mBatView.setTypeface(robotoLight);
        mRssiView.setTypeface(robotoLight);

        mIconDPView.setTypeface(weatherFontIcon);
        mIconDPView.setText(mIconDp);
        mIconECView.setTypeface(weatherFontIcon);
        mIconECView.setText(mIconEc);
        mIconTempView.setTypeface(weatherFontIcon);
        mIconTempView.setText(mIconTemp);
        mIconVWCView.setTypeface(weatherFontIcon);
        mIconVWCView.setText(mIconVWC);
        mIconBatView.setTypeface(weatherFontIcon);
        mIconBatView.setText(mIconBat);
        mIconRssiView.setTypeface(weatherFontIcon);
        mIconRssiView.setText(mIconRssi);
        signInButton = getActivity().findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(this);
        updateCurrentWeather();
        radiogroup = (RadioGroup) getActivity().findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.i("MF","Checked change:"+checkedId);
                // This will get the radiobutton that has changed in its check state
                DeviceObj dev = devices.get(checkedId);
                // This puts the value (true/false) into the variable
                Log.i("MF","Checked change1:"+dev);
                if(dev!=null) {
                    main.setCurrentDevice(dev.getDeviceId());
                }
            }
        });
        initDeviceList(new DeviceObj(main.USB_DEV,"USB Device"));
        init=true;
    }
    RadioGroup radiogroup;
    private List<DeviceObj> devices = new ArrayList<>();

    public void setDss(ArrayList<DataSourceDTO> dss) {
        Log.i("MF","setDss:"+dss.size());
        if(dss.size()==0){
            return;
        }
        devices.clear();
        devices.add(new DeviceObj(main.USB_DEV,"USB Device"));
        for(DataSourceDTO ds:dss){
            if(ds.getId()!=null && ds.getType().equals(main.DEV_TYPE)) {
                devices.add(new DeviceObj(ds.getId(), ds.getName()));
            }
        }
        populateDevList();
    }

    private void initDeviceList(DeviceObj dev){
        devices.clear();
        devices.add(dev);
        populateDevList();
    }

    private void populateDevList() {
        radiogroup.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<devices.size();i++){
            DeviceObj dev = devices.get(i);
            RadioButton newRadioButton = new RadioButton(this.getContext());
            newRadioButton.setText(dev.getName());
            newRadioButton.setTag(dev.getDeviceId());
            newRadioButton.setId(i);
            if(dev.getDeviceId().equals(main.getCurrentDevice())){
                newRadioButton.setChecked(true);
            }
            radiogroup.addView(newRadioButton, layoutParams);
        }

    }

    SignInButton signInButton;
    public void setSignInEnabled(boolean enable){
        if(!init){
            return;
        }

        if(enable){
            signInButton.setVisibility(View.VISIBLE);
        }else{
            signInButton.setVisibility(View.GONE);
        }
    }

    private String mIconDp;
    private String mIconTemp;
    private String mIconEc;
    private String mIconVWC;
    private String mIconBat;
    private String mIconRssi;

    private void weatherConditionsIcons() {
        mPercentSign = getString(R.string.percent_sign);
        mVoltSign = getString(R.string.volt_sign);
        mIconDp = getString(R.string.icon_dp);
        mIconTemp = getString(R.string.icon_temp);
        mIconEc = getString(R.string.icon_ec);
        mIconVWC = getString(R.string.icon_vwc);
        mIconBat = getString(R.string.icon_bat);
        mIconRssi = getString(R.string.icon_rssi);
    }
}
