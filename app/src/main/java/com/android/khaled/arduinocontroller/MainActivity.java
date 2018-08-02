package com.android.khaled.arduinocontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

public class MainActivity extends AppCompatActivity {
    private Button searchButton;
    private RecyclerView recyclerView;
    private DevicesAdapter mAdapter;
    private SmoothBluetooth mSmoothBluetooth;
    public static final int ENABLE_BT__REQUEST = 1;
    private RecyclerView.LayoutManager mLayoutManager;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton=findViewById(R.id.search);
        mLayoutManager = new LinearLayoutManager(this);
        relativeLayout=findViewById(R.id.controllers);

        mSmoothBluetooth = new SmoothBluetooth(this, SmoothBluetooth.ConnectionTo.ANDROID_DEVICE,
                SmoothBluetooth.Connection.SECURE,
                mListener);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmoothBluetooth.tryConnection();
            }
        });
    }

    public void moveForward(View view){
        mSmoothBluetooth.send("f");
    }
    public void moveRight(View view){
        mSmoothBluetooth.send("r");
    }
    public void moveLeft(View view){
        mSmoothBluetooth.send("l");
    }
    public void moveBackward(View view){
        mSmoothBluetooth.send("b");
    }
    public void stop(View view){
        mSmoothBluetooth.send("s");
    }
    public void dc(View view){
        mSmoothBluetooth.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSmoothBluetooth.stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ENABLE_BT__REQUEST) {
            if(resultCode == RESULT_OK) {
                mSmoothBluetooth.tryConnection();
            }
        }
    }

    private SmoothBluetooth.Listener mListener = new SmoothBluetooth.Listener(){

        @Override
        public void onBluetoothNotSupported() {

        }

        @Override
        public void onBluetoothNotEnabled() {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, ENABLE_BT__REQUEST);

        }

        @Override
        public void onConnecting(Device device) {
            Toast.makeText(MainActivity.this,"Connecting",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnected(Device device) {
            Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();
            searchButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onDisconnected() {
            recyclerView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            searchButton.setVisibility(View.VISIBLE);

        }

        @Override
        public void onConnectionFailed(Device device) {
            Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDiscoveryStarted() {
        }

        @Override
        public void onDiscoveryFinished() {
        }

        @Override
        public void onNoDevicesFound() {
        }

        @Override
        public void onDevicesFound(final List<Device> deviceList,final SmoothBluetooth.ConnectionCallback connectionCallback) {
            mAdapter=new DevicesAdapter(deviceList);
            recyclerView=findViewById(R.id.devices_list);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    connectionCallback.connectTo(deviceList.get(position));
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));
        }

        @Override
        public void onDataReceived(int data) {

        }
    };
}
