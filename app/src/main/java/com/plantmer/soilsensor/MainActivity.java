package com.plantmer.soilsensor;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
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
import com.plantmer.soilsensor.util.AppDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity implements Runnable {

    public static final String TYPE_USB = "USB";
    public static final String TYPE_LWA = "LWA";
    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;

    private List<String> logs = new ArrayList<>(10);
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void initLog(){

        for(int i=0;i<5;i++){
            logs.add("");
        }
        cmdLog.setText("\n\n\n\n\n");
        scheduler.scheduleAtFixedRate(this, 3, 3, SECONDS);
    }

    @Override
    public void run() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!connected.get()){
                        try {
                            getSerial().connect();
                        } catch (IOException e) {
                            Log.e("main","connButton",e);
                        }

                    }
                }catch (Exception ex){
                    Log.e("main","addLine",ex);
                }
            }
        });
    }

    private String type=null;

    public void addLine(final String log){
        if(log!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("main", "in:::" + log );
                        addLog(log);
                        String line;
                        if (log.startsWith(">")) {
                            line = log.substring(1,log.length());
                        }else{
                            line = log;
                        }
                        if (!connected.get() && line.length()>3) {
                            Log.i("main", "line:" + line );
                            type = line.substring(0, 3);;
                            if(type.equalsIgnoreCase(TYPE_USB)){
                                settingsFragment.setUsbEnabled(true);
                                settingsFragment.setLwEnabled(false);
                            }else if(type.equalsIgnoreCase(TYPE_LWA)){
                                settingsFragment.setUsbEnabled(false);
                                settingsFragment.setLwEnabled(true);
                            }else{
                                type = null;
                            }
                            if(type!=null) {
                                android.util.Log.e("main", "DETECTED : " + type);
                                connected.compareAndSet(false,true);
                                mainFragment.setConnected(connected.get());
                            }
                        } else {
                            String[] split = line.split(",");
                            if (split.length > 2) {
                                mainFragment.append(split);
                                graphFragment.append(split);
                                settingsFragment.append(split);
                            }
                        }
                    }catch (Exception ex){
                        Log.e("main","addLine",ex);
                    }
                }
            });
        }
    }
    boolean init = false;
    public void addLog(String log){
        if(!init){
            return;
        }

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

    public AppDatabase getDb() {
        return db;
    }

    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "soilSensDb").build();
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        cmdLog =  findViewById(R.id.cmdLog);
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
               // Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);
        init = true;
        initLog();
        setLog(false);

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
    AtomicBoolean connected = new AtomicBoolean(false);

    public boolean isConnected() {
        return connected.get();
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


    public void disconnected() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("main","!!!!!!!DISCONNECTED!!!!!!!!!!!!");
                connected.compareAndSet(true,false);
                mainFragment.setConnected(connected.get());
                type = null;

            }
        });
    }
}
