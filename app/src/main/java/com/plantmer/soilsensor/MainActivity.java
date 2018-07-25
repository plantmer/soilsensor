package com.plantmer.soilsensor;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
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
    public static final String USB_DEV = "USB";
    private String currentDevice = USB_DEV;

    public String getCurrentDevice() {
        return currentDevice;
    }

    BottomNavigationView bottomNavigationView;

    //This is our viewPager
    private ViewPager viewPager;

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    public void initLog(){
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
                        //Log.i("main", "in:::" + log );
                        addLog(log);
                        String line;
                        if (log.startsWith(">")) {
                            line = log.substring(1,log.length());
                        }else{
                            line = log;
                        }
                        if (!connected.get() && line.length()>3) {
                            //Log.i("main", "line:" + line );
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
                                //android.util.Log.e("main", "DETECTED : " + type);
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
        settingsFragment.addLog(log);
    }
    //Fragments

    GraphFragment graphFragment;
    MainFragment mainFragment;
    SettingsFragment settingsFragment;
    MenuItem prevMenuItem;

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
        FirebaseApp.initializeApp(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    public void setCurrentDevice(String currentDevice) {
        if(!this.currentDevice.equals(currentDevice)){
            graphFragment.updateRange();
        }
        this.currentDevice = currentDevice;

    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            mainFragment.setSignInEnabled(false);
            Log.i(TAG,"updateUI"+currentUser.getDisplayName());
            currentUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                // Send token to your backend via HTTPS
                                // ...
                            } else {
                                // Handle error -> task.getException();
                            }
                        }
                    });
        }else{
            Log.i(TAG,"updateUI"+currentUser);
            mainFragment.setSignInEnabled(true);
        }
    }

    private GoogleSignInClient signInClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth auth;

    public FirebaseAuth getAuth() {
        return auth;
    }

    public GoogleSignInOptions getGso() {
        return gso;
    }

    public GoogleSignInClient getSignInClient() {
        return signInClient;
    }
    int RC_SIGN_IN = 12345;
    public void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    String TAG = "main";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.activity_main), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
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
