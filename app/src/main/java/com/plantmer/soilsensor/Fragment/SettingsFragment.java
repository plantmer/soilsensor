package com.plantmer.soilsensor.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;
import com.plantmer.soilsensor.util.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private MainActivity main;

    public void setMain(MainActivity main) {
        this.main = main;
    }

    public void append(String[] split){
        if(!init){
            return;
        }

        if(split.length==7){
            lwIntervalEt.setText(split[0]);
            lwDevEuiEt.setText(split[1]);
            lwAppEuiEt.setText(split[2]);
            lwAppKeyEt.setText(split[3]);
            lwNWKSKeyEt.setText(split[4]);
            lwAPPSKeyEt.setText(split[5]);
            lwDevAddrEt.setText(split[6]);
        }
    }
    public SettingsFragment() {
        // Required empty public constructor
    }
    private Button rawButton;
    private Button airButton;
    private Button waterButton;
    private Button usbIntervalButton;
//    private Button usbTimeButton;
    private Button lwIntervalButton;
    private Button lwInfoButton;
    private Button lwGenButton;
    private Button lwDevEuiButton;
    private Button lwAppEuiButton;
    private Button lwAppKeyButton;
    private Button lwDevAddrButton;
    private Button lwNWKSKeyButton;
    private Button lwAPPSKeyButton;


    private EditText rawEt;
//    private EditText airEt;
//    private EditText waterEt;
    private EditText usbIntervalEt;
    private EditText lwIntervalEt;
    private EditText lwDevEuiEt;
    private EditText lwAppEuiEt;
    private EditText lwAppKeyEt;
    private EditText lwDevAddrEt;
    private EditText lwNWKSKeyEt;
    private EditText lwAPPSKeyEt;

    boolean init = false;

    @Override
    public void onClick(View view)
    {
        Log.d("settings", "onClick: "+view.getId());
        if(!main.isConnected()){
            main.alertNotConn();
            return;
        }
        switch (view.getId()) {
            case R.id.rawButton:
                main.getSerial().writeCmd("raw "+rawEt.getText());
                break;
            case R.id.airButton:
                main.getSerial().writeCmd("air");//+airEt.getText());
                break;
            case R.id.waterButton:
                main.getSerial().writeCmd("water");//+waterEt.getText());
                break;
            case R.id.usbIntervalButton:
                main.getSerial().writeCmd("millis "+usbIntervalEt.getText());
                break;
//            case R.id.usbTimeButton:
//                main.getSerial().writeCmd("time "+System.currentTimeMillis());
//                break;
            case R.id.lwIntervalButton:
                main.getSerial().writeCmd("int "+lwIntervalEt.getText());
                break;
            case R.id.lwInfoButton:
                main.getSerial().writeCmd("csv");
                break;
            case R.id.lwGenButton:
                lwDevEuiEt.setText(Utils.randomHex(8));
                lwAppKeyEt.setText(Utils.randomHex(16));
                break;
            case R.id.lwDevEuiButton:
                main.getSerial().writeCmd("deveui "+lwDevEuiEt.getText());
                break;
            case R.id.lwAppEuiButton:
                main.getSerial().writeCmd("appeui "+lwAppEuiEt.getText());
                break;
            case R.id.lwAppKeyButton:
                main.getSerial().writeCmd("key "+lwAppKeyEt.getText());
                break;
            case R.id.lwDevAddrButton:
                main.getSerial().writeCmd("addr "+lwDevAddrEt.getText());
                break;
            case R.id.lwNWKSKeyButton:
                main.getSerial().writeCmd("nwks "+lwNWKSKeyEt.getText());
                break;
            case R.id.lwAPPSKeyButton:
                main.getSerial().writeCmd("apps "+lwAPPSKeyEt.getText());
                break;
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rawButton =  getActivity().findViewById(R.id.rawButton);
        rawButton.setOnClickListener(this);
        airButton =  getActivity().findViewById(R.id.airButton);
        airButton.setOnClickListener(this);
        waterButton =  getActivity().findViewById(R.id.waterButton);
        waterButton.setOnClickListener(this);
        usbIntervalButton =  getActivity().findViewById(R.id.usbIntervalButton);
        usbIntervalButton.setOnClickListener(this);
//        usbTimeButton =  getActivity().findViewById(R.id.usbTimeButton);
//        usbTimeButton.setOnClickListener(this);
        lwIntervalButton =  getActivity().findViewById(R.id.lwIntervalButton);
        lwIntervalButton.setOnClickListener(this);
        lwInfoButton =  getActivity().findViewById(R.id.lwInfoButton);
        lwInfoButton.setOnClickListener(this);
        lwGenButton =  getActivity().findViewById(R.id.lwGenButton);
        lwGenButton.setOnClickListener(this);
        lwDevEuiButton =  getActivity().findViewById(R.id.lwDevEuiButton);
        lwDevEuiButton.setOnClickListener(this);
        lwAppEuiButton =  getActivity().findViewById(R.id.lwAppEuiButton);
        lwAppEuiButton.setOnClickListener(this);
        lwAppKeyButton =  getActivity().findViewById(R.id.lwAppKeyButton);
        lwAppKeyButton.setOnClickListener(this);
        lwDevAddrButton =  getActivity().findViewById(R.id.lwDevAddrButton);
        lwDevAddrButton.setOnClickListener(this);
        lwNWKSKeyButton =  getActivity().findViewById(R.id.lwNWKSKeyButton);
        lwNWKSKeyButton.setOnClickListener(this);
        lwAPPSKeyButton =  getActivity().findViewById(R.id.lwAPPSKeyButton);
        lwAPPSKeyButton.setOnClickListener(this);

        rawEt =  getActivity().findViewById(R.id.rawEt);
//        airEt =  getActivity().findViewById(R.id.airEt);
//        waterEt =  getActivity().findViewById(R.id.waterEt);
        usbIntervalEt =  getActivity().findViewById(R.id.usbIntervalEt);
        lwIntervalEt =  getActivity().findViewById(R.id.lwIntervalEt);
        lwDevEuiEt =  getActivity().findViewById(R.id.lwDevEuiEt);
        lwAppEuiEt =  getActivity().findViewById(R.id.lwAppEuiEt);
        lwAppKeyEt =  getActivity().findViewById(R.id.lwAppKeyEt);
        lwDevAddrEt =  getActivity().findViewById(R.id.lwDevAddrEt);
        lwDevAddrEt =  getActivity().findViewById(R.id.lwDevAddrEt);
        lwNWKSKeyEt =  getActivity().findViewById(R.id.lwNWKSKeyEt);
        lwAPPSKeyEt =  getActivity().findViewById(R.id.lwAPPSKeyEt);
        usb = getActivity().findViewById(R.id.llUSB);
        lw = getActivity().findViewById(R.id.llLW);
        llRaw = getActivity().findViewById(R.id.llRaw);
        init = true;
        setRawEnabled(false);
        setUsbEnabled(false);
        setLwEnabled(false);
        if(main.getType()!=null) {
            if(main.getType().equals(main.TYPE_USB)){
                setUsbEnabled(true);
            }else if(main.getType().equals(main.TYPE_LWA)){
                setLwEnabled(true);
            }
        }
    }

    LinearLayout usb;
    LinearLayout lw;
    LinearLayout llRaw;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

    public void setUsbEnabled(boolean enable){
        if(!init){
            return;
        }

        if(enable){
            usb.setVisibility(View.VISIBLE);
        }else{
            usb.setVisibility(View.GONE);
        }
    }
    public void setLwEnabled(boolean enable){
        if(!init){
            return;
        }

        if(enable){
            lw.setVisibility(View.VISIBLE);
        }else{
            lw.setVisibility(View.GONE);
        }
    }
    public void setRawEnabled(boolean enable){
        if(!init){
            return;
        }

        if(enable){
            llRaw.setVisibility(View.VISIBLE);
        }else{
            llRaw.setVisibility(View.GONE);
        }
    }

}
