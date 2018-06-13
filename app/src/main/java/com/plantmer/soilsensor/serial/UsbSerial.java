package com.plantmer.soilsensor.serial;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;


import com.plantmer.soilsensor.MainActivity;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class UsbSerial implements Runnable {
    public static final String TAG = "Usb";

    public UsbSerial(MainActivity handler) {
        this.handler = handler;
    }
    private UsbSerialPort mDriver;
    private static PendingIntent mPermissionIntent = null;
    protected UsbManager manager;
    protected UsbDevice device;
    protected UsbDeviceConnection connection;
    protected MainActivity handler;

    public static void getPermission(final UsbManager manager, Context context, UsbDevice device) {
        if (mPermissionIntent == null) {
            mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("USB_PERMISSION"), 0);
        }
        if (manager == null)
            return;
        if (device != null && mPermissionIntent != null) {
            if (!manager.hasPermission(device)) {
                Log.d(TAG, "Request permission : " + device.toString());
                manager.requestPermission(device, mPermissionIntent);
            }
        }
    }

    public boolean findFirstDevice() {
        if (manager == null) {
            manager = (UsbManager) handler.getSystemService(Context.USB_SERVICE);
        }
        for (final UsbDevice usbDevice : manager.getDeviceList().values()) {
            getPermission(manager, handler, usbDevice);
            if (!manager.hasPermission(usbDevice)) {
                Log.d(TAG, "Doesn't have permission device: " + device.toString());
                continue;
            } else {
                device = usbDevice;
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();

                if (deviceVID != 0x1d6b && (devicePID != 0x0001 || devicePID != 0x0002 || devicePID != 0x0003)) {
                    // There is a device connected to our Android device. Try to
                    // open it as a Serial Port.
                    android.util.Log.i(TAG, "DEVICE OK" + device);
                    connection = manager.openDevice(device);
                    return true;
                } else {
                    connection = null;
                    device = null;
                }
            }

        }
        return false;
    }
//I/Usb: DEVICE OKUsbDevice[mName=/dev/bus/usb/001/002,mVendorId=9025,mProductId=32845,mClass=239,mSubclass=2,mProtocol=1,mManufacturerName=Arduino LLC,mProductName=SoilSensor3RF,mVersion=2.0,mSerialNumber=579BBD9F514D4E544B202020FF051B0E,mConfigurations=[

    public boolean open() throws IOException {
        Log.i(TAG,"open");
// Find all available drivers from attached devices.
        if (manager == null) {
            manager = (UsbManager) handler.getSystemService(Context.USB_SERVICE);
        }
        List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        Log.i(TAG,"drivers:"+drivers.size());
        if(drivers.size()==0){
            return false;
        }
        List<UsbSerialPort> ports = drivers.get(0).getPorts();
        Log.i(TAG,"ports:"+ports.size());
        if(ports.size()==0){
            return false;
        }
// Open a connection to the first available driver.

        mDriver = ports.get(0);
        int deviceVID = mDriver.getDriver().getDevice().getVendorId();
        int devicePID = mDriver.getDriver().getDevice().getProductId();
        String deviceName = mDriver.getDriver().getDevice().getDeviceName();
        Log.e(TAG,"deviceVID:"+deviceVID+" devicePID:"+devicePID+" deviceName:"+deviceName);
        try {
            if (mDriver == null) {
                return false;
            }
            mDriver.open(connection);
            mDriver.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            mDriver.setDTR(true);
            android.util.Log.i(TAG, "conn on : " + 115200);
        } catch (IOException e) {
            android.util.Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
            try {
                mDriver.close();
            } catch (IOException e2) {
                // Ignore.
            }
            mDriver = null;
            return false;
        }
        run = true;
        mExecutor.submit(this);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        write("ver\r\n".getBytes());
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
    public void close() {
        run = false;
        if (mDriver != null) {
            try {
                mDriver.close();
                mDriver = null;
            } catch (IOException e2) {
                // Ignore.
            }
        }
        stop();
        handler.setConnected(false);
    }

    public InputStream getIN() {
        return inputBuf.getInputStream();
    }

    public OutputStream getOUT() {
        return out;
    }

    private static final boolean DEBUG = true;

    private static final int READ_WAIT_MILLIS = 20;
    private static final int BUFSIZ = 1024;

    CircularByteBuffer inputBuf = new CircularByteBuffer();

    // Synchronized by 'mWriteBuffer'
    private final ByteBuffer mWriteBuffer = ByteBuffer.allocate(BUFSIZ);

    OutputStream out = new OutputStream() {

        public void write(int b) throws IOException {
            synchronized (mWriteBuffer) {
                mWriteBuffer.put((byte) b);
            }
        }

        public void write(byte[] bytes, int off, int len)
                throws IOException {
            synchronized (mWriteBuffer) {
                mWriteBuffer.put(bytes, off, len);
            }
        }

    };

    public void writeCmd(final String cmd) {
        String wr = cmd+"\r\n";
        write(wr.getBytes());
//        handler.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                handler.addLog(">"+cmd);
//            }
//        });

    }

    private enum State {
        STOPPED,
        RUNNING,
        STOPPING
    }

     private State mState = State.STOPPED;

    public void write(byte[] data) {
        synchronized (mWriteBuffer) {
            mWriteBuffer.put(data);
        }
    }

    public synchronized void stop() {
        if (getState() == State.RUNNING) {
            android.util.Log.i(TAG, "Stop requested");
            mState = State.STOPPING;
        }
    }

    private synchronized State getState() {
        return mState;
    }

    private boolean run = true;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    /**
     * Continuously services the read and write buffers until {@link #stop()} is
     * called, or until a driver exception is raised.
     */
    @Override
    public void run() {
        synchronized (this) {
            if (getState() != State.STOPPED) {
                throw new IllegalStateException("Already running.");
            }
            mState = State.RUNNING;
        }

        android.util.Log.i(TAG, "Running ..");
        try {
            while (run) {
                if (getState() != State.RUNNING) {
                    android.util.Log.i(TAG, "Stopping mState=" + getState());
                    break;
                }
                step();
            }
        } catch (Exception e) {
            android.util.Log.w(TAG, "Run ending due to exception: " + e.getMessage(), e);
        } finally {
            synchronized (this) {
                mState = State.STOPPED;
                android.util.Log.i(TAG, "USB Stopped.");
                close();
                handler.setConnected(false);
            }
        }
    }

    // volatile boolean reading = true;
    private byte[] rcv_bytes = new byte[BUFSIZ];
    //private AtomicBoolean block = new AtomicBoolean(false);

//    public AtomicBoolean getBlock() {
//        return block;
//    }

    private void step() throws IOException {
        // Handle incoming data.
        int len = mDriver.read(rcv_bytes, READ_WAIT_MILLIS);
        if (len > 0) {
            // if (DEBUG) android.util.Log.i(TAG, "Read data len=" + len);
            String rec="";
            for (int i = 0; i < len; i++) {
                char c = (char)rcv_bytes[i];
                if (DEBUG) rec += c;

                inputBuf.getOutputStream().write(rcv_bytes[i]);
                if(c=='\n' ){//&& !block.get()
                    byte[] arr = new byte[getIN().available()];
                    getIN().read(arr);
                    String ret = new String(arr);
                    handler.addLine(ret);
                }

            }
            //if (DEBUG) android.util.Log.i(TAG, "rec=" + rec);

        }

        // Handle outgoing data.
        byte[] outBuff = null;
        synchronized (mWriteBuffer) {
            if (mWriteBuffer.position() > 0) {
                len = mWriteBuffer.position();
                outBuff = new byte[len];
                mWriteBuffer.rewind();
                mWriteBuffer.get(outBuff, 0, len);
                mWriteBuffer.clear();
            }
        }
        if (outBuff != null) {
//            if (DEBUG) {
//                android.util.Log.d(TAG, "Writing data len=" + len);
//            }
            mDriver.write(outBuff, READ_WAIT_MILLIS);
        }
    }


}
