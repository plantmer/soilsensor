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
import com.google.gson.reflect.TypeToken;
import com.plantmer.soilsensor.Fragment.MainFragment;
import com.plantmer.soilsensor.Fragment.GraphFragment;
import com.plantmer.soilsensor.Fragment.SettingsFragment;
import com.plantmer.soilsensor.dao.BootConfDAO;
import com.plantmer.soilsensor.dao.DataObj;
import com.plantmer.soilsensor.dao.DataSourceDTO;
import com.plantmer.soilsensor.serial.UsbSerial;
import com.plantmer.soilsensor.dao.AppDatabase;
import com.plantmer.soilsensor.util.http.Context;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainActivity extends AppCompatActivity implements Runnable {

    public static final String TYPE_USB = "USB";
    public static final String TYPE_LWA = "LWA";
    public static final String USB_DEV = "USB";
    private String currentDevice = USB_DEV;
    public static final String  DEV_TYPE = "soilwcs3";
    public static final String serverUri = "tcp://zerver.io:1883";
    MqttAndroidClient mqttAndroidClient;
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

    private GraphFragment graphFragment;
    private MainFragment mainFragment;
    private SettingsFragment settingsFragment;
    private MenuItem prevMenuItem;

    private Context httpContext=new Context("https://zerver.io/api/v1/");

    public Context getHttpContext() {
        return httpContext;
    }

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
    BootConfDAO conf = null;
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser!=null){
            Log.i(TAG,"updateUI"+currentUser.getDisplayName());
            currentUser.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                conf = httpContext.doGetRequest("auth/fire/"+idToken,BootConfDAO.class);
                                Log.i(TAG,"user:"+conf);
                                if(conf!=null){
                                    mainFragment.setSignInEnabled(false);
                                    httpContext.setToken(conf.getToken());
                                    reloadDev();
                                    connectMqtt(conf.getUser().getLogin(),conf.getUser().getPass());
                                }
                            } else {
                            }
                        }
                    });
        }else{
            Log.i(TAG,"updateUI"+currentUser);
            mainFragment.setSignInEnabled(true);
        }
    }

    public void reloadDev() {
        Type listType = new TypeToken<ArrayList<DataSourceDTO>>(){}.getType();
        ArrayList<DataSourceDTO> devs = httpContext.doGetRequest("datasources",listType);
        mainFragment.setDss(devs);
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

    public void connectMqtt(String user, String pass){
        String clientId = "SoilSensor" + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    Log.i(TAG,"Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();
                } else {
                    Log.i(TAG,"Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG,"The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG,topic + "Incoming message: " + new String(message.getPayload()));

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(user);
        mqttConnectOptions.setPassword(pass.toCharArray());




        try {
            //Log.i(TAG,"Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG,"Failed to connect to: " + serverUri);
                }
            });


        } catch (MqttException ex){
            Log.e(TAG,"subscribeToTopic",ex);
        }

    }
    static String separator = ",";
    public String  toStr(byte[] l) {
        StringBuilder sb = new StringBuilder("(");
        String sep = "";
        for (byte object : l) {
            sb.append(sep).append(object & 0xFF);
            sep = separator;
        }
        return sb.append(")").toString();
    }

    String subscriptionTopic = "ds/"+DEV_TYPE+"/+/out/#";
    public void subscribeToTopic(){
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG,"Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG,"Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    Log.i(TAG,"Message: " + topic + " : " +message.getPayload().length);
                    String[] tok = topic.split("/");
                    if(mainFragment.getDeviceIds().contains(tok[2])){
                        byte cmd = Byte.valueOf(tok[4]);
                        if(cmd==7){
                            ByteBuffer buf=ByteBuffer.wrap(message.getPayload());
                            buf.order(ByteOrder.LITTLE_ENDIAN);
//                            new DataType("e25", DataType.DT_SHORT, -2)
//                                    , new DataType("EC", DataType.DT_SHORT, -2)
//                                    , new DataType("Temp", DataType.DT_SHORT, -2)
//                                    , new DataType("VWC", DataType.DT_SHORT, 0)
//                                    , new DataType("Bat", DataType.DT_BYTE, 0)
//                                    , new DataType("RSSI", DataType.DT_SHORT, 0)
                            try{//DataObj(String devId, long dateTime, float dp, float ec, float temp, float vwc, int rssi)
                                DataObj dop = new DataObj(tok[2], System.currentTimeMillis(), buf.getShort()/100, buf.getShort()/100, buf.getShort()/100, buf.getShort(), buf.get(),buf.getShort());
                                mainFragment.append(dop);
                                graphFragment.updatez(dop);
                            } catch (Exception exe){
                                Log.e(TAG,"Message Parse failed: "  +toStr(message.getPayload()),exe);

                            }

                        }
                    }
                }
            });

        } catch (MqttException ex){
            Log.e(TAG,"subscribeToTopic",ex);
        }
    }
}
