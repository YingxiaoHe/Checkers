package com.example.hyx.billiardball.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.dialog.CustomDialog;
import com.example.hyx.billiardball.unit.Player;
import com.example.hyx.billiardball.view.BoardView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
//import com.example.hyx.billiardball.view.MenuView;


/**
 * Created by HYX on 2017/1/19.
 */

public class WindowActivity extends Activity implements View.OnClickListener {


    private Button btnMenu;
    private BoardView boardView;
    private int mClickId = -1;
    private static List<Integer> mPlayerArray = new ArrayList<>();
    public static MediaPlayer mGameSound, mMoveSound;
    private List<Player> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ActivityList.getInstance().addActivity(this);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        bindView();
        mMoveSound = MediaPlayer.create(this, R.raw.move);
        mGameSound = MediaPlayer.create(this, R.raw.gamesound);
        mGameSound.setLooping(true);
        mGameSound.start();
        initPlayer();
    }

    private void initPlayer() {
       /* mPlayerArray = new ArrayList<>();
        players = new ArrayList<>();
        mPlayerArray.add(1);
        mPlayerArray.add(4);
*/
        for (int i = 0; i < mPlayerArray.size(); i++) {
            Player player = new Player(i + 1, mPlayerArray.get(i));
            players.add(player);
        }
        boardView.setPlayers(players);
    }

    private void bindView() {
        btnMenu = (Button) findViewById(R.id.btn_menu);
        boardView = (BoardView) findViewById(R.id.board_view);

        btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu:
                CustomDialog menuDialog = new CustomDialog(this, new CustomDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(int id) {
                        chickEvent(id);
                    }
                }, R.style.dialog, R.layout.main_menu);
                menuDialog.show();
                break;
        }
    }

    private void chickEvent(int id) {
        switch (id) {
            case R.id.btn_menu_restartGame:
                ActivityList.getInstance().removeActivity(this);
                mClickId = R.id.btn_menu_restartGame;
                break;
            case R.id.btn_menu_option:
                CustomDialog optionDialog = new CustomDialog(this, new CustomDialog.ICustomDialogEventListener() {
                    @Override
                    public void customDialogEvent(int id) {
                        if (mGameSound.isPlaying()) {
                            mGameSound.pause();
                        } else {
                            mGameSound.start();
                        }
                    }
                }, R.style.dialog, R.layout.option_menu);
                optionDialog.show();
                break;
            case R.id.btn_menu_endGame:
                ActivityList.getInstance().finishAll();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityList.getInstance().removeActivity(this);
    }

    public List<Integer> getmPlayerArray() {
        return mPlayerArray;
    }

    public static void setmPlayerArray(List<Integer> mPlayerArray) {
        WindowActivity.mPlayerArray = mPlayerArray;
    }

    public List<Player> getPlayers() {
        return players;
    }

}
