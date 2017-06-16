package com.example.hyx.billiardball.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by HYX on 2017/5/17.
 */

public class ServerThread extends Thread {

    private static final int PORT = 9999;
    private static final int CLIENT = 0;
    private static final int SERVER = 1;
    private static final int CLIENT_DATA = 2;
    private static final int SERVER_DATA = 3;

    private volatile boolean flag = true;
    private Socket socket;
    BufferedReader in = null;
    private String msg = "null";
    private volatile ServerSocket server = null;
    OutputStream os = null;

    public static Handler setHandler, getHandler;

    public ServerThread(ServerSocket server, Handler handler) {
        this.server = server;
        this.getHandler = handler;
    }

    public void sendmsg(String msg) {
        System.out.println(msg);
        PrintWriter pout = null;
        try {
            pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            pout.println(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {
            server = new ServerSocket(PORT);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            socket = server.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();
            msg = "成功连接服务器！";
            this.sendmsg(msg);

            new Thread() {

                @Override
                public void run() {
                    while (flag) {
                        String strMsg = null;
                        try {
                            while ((strMsg = in.readLine()) != null) {
                                Message msg = new Message();
                                if (strMsg.split("|")[0].equals("run") || strMsg.split("|")[0].equals("over")) {
                                    msg.what = CLIENT_DATA;
                                } else {
                                    msg.what = CLIENT;
                                }
                                msg.obj = strMsg;
                                getHandler.sendMessage(msg);
                            }
                        } catch (IOException e) {
                            flag = false;
                            e.printStackTrace();
                        }
                    }
                }

            }.start();

            Looper.prepare();

            setHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case SERVER:
                            try {
                                os.write((msg.obj.toString() + "（服务器发送）\n").getBytes("utf-8"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
