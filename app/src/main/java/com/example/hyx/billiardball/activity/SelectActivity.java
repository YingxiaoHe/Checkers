package com.example.hyx.billiardball.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.unit.Player;

import java.util.ArrayList;
import java.util.List;

import static com.example.hyx.billiardball.activity.RoomActivity.serverThread;

/**
 * Created by HYX on 2017/3/16.
 */

public class SelectActivity extends Activity implements View.OnClickListener{


    private RadioButton rbBrown,rbGreen,rbPurple,rbBlue,rbRed,rbYellow;
    private Button btCommit;
    private int mPieceColor;
    private List<Integer> mPlayerArray = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private MediaPlayer mSelectSound;
    private Handler handler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_start);
        ActivityList.getInstance().addActivity(this);

        mSelectSound = MediaPlayer.create(this,R.raw.selectsound);
        mSelectSound.setLooping(true);
        mSelectSound.start();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        bindView();
        /*handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch(msg.what) {
                    case SELECT:
                        initPlayer();
                        break;
                }
            }
        };*/
    }

    private void bindView() {
        rbBrown = (RadioButton) findViewById(R.id.radioButton_brown);
        rbGreen = (RadioButton) findViewById(R.id.radioButton_green);
        rbPurple = (RadioButton) findViewById(R.id.radioButton_purple);
        rbBlue = (RadioButton) findViewById(R.id.radioButton_blue);
        rbRed = (RadioButton) findViewById(R.id.radioButton_red);
        rbYellow = (RadioButton) findViewById( R.id.radioButton_yellow);
        btCommit = (Button) findViewById(R.id.bt_commit);

        rbBrown.setOnClickListener(this);
        rbGreen.setOnClickListener(this);
        rbPurple.setOnClickListener(this);
        rbBlue.setOnClickListener(this);
        rbRed.setOnClickListener(this);
        rbYellow.setOnClickListener(this);
        btCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButton_brown:
                mPieceColor = 1;
                break;
            case R.id.radioButton_green:
                mPieceColor = 2;
                break;
            case R.id.radioButton_purple:
                mPieceColor = 3;
                break;
            case R.id.radioButton_blue:
                mPieceColor = 4;
                break;
            case R.id.radioButton_red:
                mPieceColor = 5;
                break;
            case R.id.radioButton_yellow:
                mPieceColor = 6;
                break;
            case R.id.bt_commit:
                initPlayer();
                WindowActivity.setmPlayerArray(mPlayerArray);
                if (mSelectSound != null) mSelectSound.stop();
//                sendMessage();
                Intent intent = new Intent(SelectActivity.this, WindowActivity.class);
                startActivity(intent);
        }
    }

    /*private void sendMessage() {
        Message msg = new Message();
        msg.what = SELECT;
        msg.obj = mPieceColor;
        System.out.println(msg.obj.toString());
        serverThread.myHandler.sendMessage(msg);
    }*/

    private void initPlayer() {
        mPlayerArray = new ArrayList<>();
        players = new ArrayList<>();
        mPlayerArray.add(mPieceColor);
        if (mPieceColor == 3) {
            mPlayerArray.add(6);
        } else {
            mPlayerArray.add((mPieceColor + 3) % 6);
        }

        for (int i = 0; i < mPlayerArray.size(); i++) {
            Player player = new Player(i + 1, mPlayerArray.get(i));
            players.add(player);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityList.getInstance().removeActivity(this);
    }
}
