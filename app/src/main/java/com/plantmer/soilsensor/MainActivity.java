package com.plantmer.soilsensor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.plantmer.soilsensor.Fragment.MainFragment;
import com.plantmer.soilsensor.Fragment.GraphFragment;
import com.plantmer.soilsensor.Fragment.SettingsFragment;
import com.plantmer.soilsensor.serial.UsbSerial;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;

    private List<String> logs = new ArrayList<>(10);

    public void initLog(){

        for(int i=0;i<5;i++){
            logs.add("");
        }
        cmdLog.setText("\n\n\n\n\n\n\n\n\n\n");
    }
    private String type=null;

    public void addLine(final String log){
        if(log!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("main","in:"+log);
                    if(!isConnected()){
                        type= log.substring(0,3);
                        android.util.Log.i("main", "DETECTED : " + type);
                        connected = true;
                        mainFragment.setConnected(connected);
                    }else {
                        String[] split = log.split(",");
                        if (split.length > 0) {
                            mainFragment.append(split);
                            graphFragment.append(split);
                            settingsFragment.append(split);
                        }
                    }
                    addLog(log);
                }
            });
        }
    }
    public void addLog(String log){
        if(logs.size()>5) {
            logs.remove(0);
        }
        logs.add(log);
        final StringBuilder sb = new StringBuilder("");
        for(int i=0;i<logs.size();i++){
            sb.append(logs.get(i)).append("\n");
        }
        cmdLog.setText(sb.toString());
    }
    //Fragments

    GraphFragment graphFragment;
    MainFragment mainFragment;
    SettingsFragment settingsFragment;
    MenuItem prevMenuItem;
    private TextView cmdLog;

    private UsbSerial serial = new UsbSerial(this);

    public UsbSerial getSerial() {
        return serial;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        cmdLog =  findViewById(R.id.cmdLog);
        initLog();
        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_call:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_chat:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_contact:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /*  //Disable ViewPager Swipe

       viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        */

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mainFragment =new MainFragment();
        mainFragment.setMain(this);
        graphFragment =new GraphFragment();
        graphFragment.setMain(this);
        settingsFragment =new SettingsFragment();
        settingsFragment.setMain(this);
        adapter.addFragment(mainFragment);
        adapter.addFragment(graphFragment);
        adapter.addFragment(settingsFragment);
        viewPager.setAdapter(adapter);
    }
    boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    public void alertNotConn(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Device not connected");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
    public void setLog(boolean enable) {
        if(enable){
            cmdLog.setVisibility(View.VISIBLE);
        }else{
            cmdLog.setVisibility(View.GONE);
        }
    }

    public String getType() {
        return type;
    }


    public void setConnected(final boolean conn) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connected = conn;
                mainFragment.setConnected(connected);
                if(!conn){
                    type = null;
                }

            }
        });
    }
}
