package com.example.hyx.billiardball.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.thread.ClientThread;
import com.example.hyx.billiardball.thread.ServerThread;
import com.example.hyx.billiardball.unit.Player;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by HYX on 2017/4/19.
 */

public class RoomActivity extends Activity implements View.OnClickListener{

    private static final int CLIENT = 0;
    private static final int SERVER = 1;
    private static final int PORT = 9999;

    private volatile ServerSocket server = null;
    private String hostip;//本机IP
    private TextView mText1;
    private TextView mText2;
    private Button mBut1 = null;
    private Button mbtnSendData,btnStart;
    private EditText etData;
    private Handler myHandler = null;
    private Handler handler = null;
    public static ServerThread serverThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ActivityList.getInstance().addActivity(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        hostip = getLocalIpAddress();  //获取本机IP
        mText1 = (TextView) findViewById(R.id.textView1);
        mText1.setText(hostip);
        mText1.setEnabled(false);

        mText2 = (TextView) findViewById(R.id.textView2);
        mbtnSendData = (Button) findViewById(R.id.btn_send_data);
        mbtnSendData.setOnClickListener(this);
        etData = (EditText) findViewById(R.id.et_data);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch(msg.what) {
                    case CLIENT:
                        mText2.append("\n" + msg.obj.toString());
                        break;
                }
            }
        };

        serverThread = new ServerThread(server,handler);
        serverThread.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_data:
                Message msg = new Message();
                msg.what = SERVER;
                msg.obj = etData.getText().toString().trim();
                serverThread.setHandler.sendMessage(msg);
                etData.setText("");
                break;
            case R.id.btn_start:
                Intent intent = new Intent(RoomActivity.this, ServerWindowActivity.class);
                ArrayList<Integer> PlayerArray = new ArrayList<>();
                PlayerArray.add(1);
                PlayerArray.add(4);
                WindowActivity.setmPlayerArray(PlayerArray);
                startActivity(intent);
        }
    }


    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("WifiPreference", ex.toString());
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("closeeeeeeeeeeeeee","服务器已关闭");
        ActivityList.getInstance().removeActivity(this);
    }

}
