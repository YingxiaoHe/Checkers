package com.example.hyx.billiardball.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.dialog.CustomDialog;

import static com.example.hyx.billiardball.R.id.btn_endGame;
import static com.example.hyx.billiardball.R.id.btn_option;
import static com.example.hyx.billiardball.R.id.btn_startGame;

/**
 * Created by HYX on 2017/3/3.
 */

public class HelloActivity extends Activity implements View.OnClickListener {

    private TextView tvTitle;
    private Button btnStartGame, btnOption, btnEndGame;
    private MediaPlayer mHelloSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        ActivityList.getInstance().addActivity(this);

        mHelloSound = MediaPlayer.create(this, R.raw.hellosound);
        mHelloSound.setLooping(true);
        mHelloSound.start();

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        bindView();

        tvTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/XingShu.ttf"));
    }

    private void bindView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnStartGame = (Button) findViewById(R.id.btn_startGame);
        btnOption = (Button) findViewById(R.id.btn_option);
        btnEndGame = (Button) findViewById(R.id.btn_endGame);

        btnStartGame.setOnClickListener(this);
        btnOption.setOnClickListener(this);
        btnEndGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case btn_startGame:
                if (mHelloSound != null) mHelloSound.stop();
                /*CustomDialog choiceDialog = new CustomDialog(this, new CustomDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(int id) {
                        switch (id) {
                            case R.id.btn_1p_game:
                                Intent intentStart1PGame = new Intent(HelloActivity.this, SelectActivity.class);
                                startActivity(intentStart1PGame);
                                break;
                            case R.id.btn_2p_game:
                                CustomDialog roomDialog = new CustomDialog(HelloActivity.this, new CustomDialog.ICustomDialogEventListener() {
                                    @Override
                                    public void customDialogEvent(int id) {
                                        nextDialog(id);
                                    }
                                }, R.style.dialog, R.layout.is_room_menu);
                                roomDialog.show();
                                break;
                        }
                    }
                }, R.style.dialog, R.layout.choice_game_menu);
                choiceDialog.show();*/
                Intent intent = new Intent(HelloActivity.this, SelectActivity.class);
                startActivity(intent);
                break;
            case btn_option:
                CustomDialog optionDialog = new CustomDialog(this, new CustomDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(int id) {
                        if (mHelloSound.isPlaying()) {
                            mHelloSound.pause();
                        }
                        else {
                            mHelloSound.start();
                        }
                    }
                }, R.style.dialog, R.layout.option_menu);
                optionDialog.show();
                break;
            case btn_endGame:
                if (mHelloSound != null) mHelloSound.stop();
                finish();
                break;
        }
    }

    private void nextDialog(int i) {
        switch (i) {
            case R.id.btn_server:
                Intent server = new Intent(HelloActivity.this, RoomActivity.class);
                startActivity(server);
                break;
            case R.id.btn_find_server:
                Intent findServer = new Intent(HelloActivity.this,findRoomActivity.class);
                startActivity(findServer);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityList.getInstance().removeActivity(this);
    }
}
