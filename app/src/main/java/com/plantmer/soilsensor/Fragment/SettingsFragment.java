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

import com.plantmer.soilsensor.MainActivity;
import com.plantmer.soilsensor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    private MainActivity main;

    public void setMain(MainActivity main) {
        this.main = main;
    }

    public void append(String[] split){
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
    private Button usbTimeButton;
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
    private EditText airEt;
    private EditText waterEt;
    private EditText usbIntervalEt;
    private EditText lwIntervalEt;
    private EditText lwDevEuiEt;
    private EditText lwAppEuiEt;
    private EditText lwAppKeyEt;
    private EditText lwDevAddrEt;
    private EditText lwNWKSKeyEt;
    private EditText lwAPPSKeyEt;



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
                break;
            case R.id.airButton:
                break;
            case R.id.waterButton:
                break;
            case R.id.usbIntervalButton:
                break;
            case R.id.usbTimeButton:
                break;
            case R.id.lwIntervalButton:
                break;
            case R.id.lwInfoButton:
                break;
            case R.id.lwGenButton:
                break;
            case R.id.lwDevEuiButton:
                break;
            case R.id.lwAppEuiButton:
                break;
            case R.id.lwAppKeyButton:
                break;
            case R.id.lwDevAddrButton:
                break;
            case R.id.lwNWKSKeyButton:
                break;
            case R.id.lwAPPSKeyButton:
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
        usbTimeButton =  getActivity().findViewById(R.id.usbTimeButton);
        usbTimeButton.setOnClickListener(this);
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
        airEt =  getActivity().findViewById(R.id.airEt);
        waterEt =  getActivity().findViewById(R.id.waterEt);
        usbIntervalEt =  getActivity().findViewById(R.id.usbIntervalEt);
        lwIntervalEt =  getActivity().findViewById(R.id.lwIntervalEt);
        lwDevEuiEt =  getActivity().findViewById(R.id.lwDevEuiEt);
        lwAppEuiEt =  getActivity().findViewById(R.id.lwAppEuiEt);
        lwAppKeyEt =  getActivity().findViewById(R.id.lwAppKeyEt);
        lwDevAddrEt =  getActivity().findViewById(R.id.lwDevAddrEt);
        lwDevAddrEt =  getActivity().findViewById(R.id.lwDevAddrEt);
        lwNWKSKeyEt =  getActivity().findViewById(R.id.lwNWKSKeyEt);
        lwAPPSKeyEt =  getActivity().findViewById(R.id.lwAPPSKeyEt);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }

}
