package com.example.hyx.billiardball.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.thread.ClientThread;

import java.util.ArrayList;


/**
 * Created by HYX on 2017/5/7.
 */

public class findRoomActivity extends Activity implements View.OnClickListener {

    private static final int CLIENT = 0;
    private static final int SERVER = 1;

    private EditText input, etIP;
    private TextView show;
    private Button send, btnSendIP, btnStart;
    private Handler handler;
    public static ClientThread clientThread;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_room);
        ActivityList.getInstance().addActivity(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        input = (EditText) findViewById(R.id.input);
        etIP = (EditText) findViewById(R.id.et_ip);
        btnSendIP = (Button) findViewById(R.id.btn_send_ip);
        send = (Button) findViewById(R.id.send);
        show = (TextView) findViewById(R.id.show);
        btnStart = (Button) findViewById(R.id.btn_start);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SERVER:
                        show.append("\n" + msg.obj.toString());
                        break;
                }
            }
        };
        btnSendIP.setOnClickListener(this);
        send.setOnClickListener(this);
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_ip:
                clientThread = new ClientThread(handler, etIP.getText().toString().trim());
                new Thread(clientThread).start();
                break;
            case R.id.send:
                try {
                    Message msg = new Message();
                    msg.what = CLIENT;
                    msg.obj = input.getText().toString();
                    System.out.println(msg.obj.toString());
                    clientThread.setHandler.sendMessage(msg);
                    input.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_start:
                Intent intent = new Intent(findRoomActivity.this, ClientWindowActivity.class);
                startActivity(intent);
        }
    }
}
