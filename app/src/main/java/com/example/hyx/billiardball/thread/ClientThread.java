package com.example.hyx.billiardball.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by HYX on 2017/5/9.
 */

public class ClientThread implements Runnable {
    private static final int CLIENT = 0;
    private static final int SERVER = 1;
    private static final int PORT = 9999;
    private static final int CLIENT_DATA = 2;
    private static final int SERVER_DATA = 3;

    private static String IP;
    private Socket socket;
    public Handler getHandler;
    public Handler setHandler;
    BufferedReader br = null;
    OutputStream os = null;

    public ClientThread(Handler handler, String IP) {
        this.getHandler = handler;
        this.IP = IP;
    }

    public void run() {
        try {
            socket = new Socket(IP, PORT);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();
            new Thread() {
                @Override
                public void run() {
                    String strMsg = null;
                    try {
                        while ((strMsg = br.readLine()) != null) {
                            Message msg = new Message();
                            if (strMsg.split("|")[0].equals("run") || strMsg.split("|")[0].equals("over")) {
                                msg.what = SERVER_DATA;
                            } else {
                                msg.what = SERVER;
                            }
                            msg.obj = strMsg;
                            getHandler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            Looper.prepare();

            setHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case CLIENT:
                            try {
                                os.write((msg.obj.toString() + "\n").getBytes("utf-8"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case CLIENT_DATA:
                            try {
                                os.write((msg.obj.toString() + "\n").getBytes("utf-8"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case SERVER_DATA:
                            try {
                                os.write((msg.obj.toString() + "\n").getBytes("utf-8"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            };
            Looper.loop();
        } catch (SocketTimeoutException e1) {
            System.out.println("网络连接超时！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}