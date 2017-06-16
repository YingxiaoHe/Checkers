package com.example.hyx.billiardball.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.hyx.billiardball.R;

/**
 * Created by HYX on 2017/2/21.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private int mLayoutId;
    private Button btnStartGame, btnEndGame, btnOption, btn1PGame, btn2PGame,btnRoom,btnFindRoom;

    private ICustomDialogEventListener mCustomDialogEventListener;

    public interface ICustomDialogEventListener {
        void customDialogEvent(int id);
    }

    public CustomDialog(Context context, ICustomDialogEventListener listener, int theme, int layoutId) {
        super(context, theme);
        mContext = context;
        mCustomDialogEventListener = listener;
        mLayoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View layoutView = showLayout(mLayoutId);
        bindChickEvent(layoutView);
        this.setContentView(layoutView);
    }

    private View showLayout(int layoutId) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutView = null;
        switch (layoutId) {
            case R.layout.main_menu:
                layoutView = inflater.inflate(R.layout.main_menu, null);
                break;
            case R.layout.option_menu:
                layoutView = inflater.inflate(R.layout.option_menu, null);
                break;
            case R.layout.choice_game_menu:
                layoutView = inflater.inflate(R.layout.choice_game_menu, null);
                break;
            case R.layout.is_room_menu:
                layoutView = inflater.inflate(R.layout.is_room_menu,null);
                break;
        }
        return layoutView;
    }

    private void bindChickEvent(View layout) {
        switch (mLayoutId) {
            case R.layout.is_room_menu:
                btnRoom = (Button) layout.findViewById(R.id.btn_server);
                btnFindRoom = (Button) layout.findViewById(R.id.btn_find_server);

                btnRoom.setOnClickListener(this);
                btnFindRoom.setOnClickListener(this);
                break;
            case R.layout.choice_game_menu:
                btn1PGame = (Button) layout.findViewById(R.id.btn_1p_game);
                btn2PGame = (Button) layout.findViewById(R.id.btn_2p_game);

                btn1PGame.setOnClickListener(this);
                btn2PGame.setOnClickListener(this);
                break;
            case R.layout.main_menu:
                btnStartGame = (Button) layout.findViewById(R.id.btn_menu_restartGame);
                btnEndGame = (Button) layout.findViewById(R.id.btn_menu_endGame);
                btnOption = (Button) layout.findViewById(R.id.btn_menu_option);
                //btnBack = (Button) layout.findViewById(R.id.btn_menu_back) ;

                btnStartGame.setOnClickListener(this);
                btnEndGame.setOnClickListener(this);
                btnOption.setOnClickListener(this);
                //btnBack.setOnClickListener(this);
                break;
            case R.layout.option_menu:
                //Button btnRePiece = (Button) layout.findViewById(R.id.re_piece);
                Button btnMusic = (Button) layout.findViewById(R.id.btn_music);

                btnMusic.setOnClickListener(this);
                //btnRePiece.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = -1;
        switch (v.getId()) {
            case R.id.btn_menu_restartGame:
                viewId = R.id.btn_menu_restartGame;
                break;
            case R.id.btn_menu_option:
                viewId = R.id.btn_option;
                break;
            case R.id.btn_menu_endGame:
                viewId = R.id.btn_menu_endGame;
                break;
            case R.id.btn_music:
                viewId = R.id.btn_music;
                break;
            case R.id.btn_1p_game:
                viewId = R.id.btn_1p_game;
                break;
            case R.id.btn_2p_game:
                viewId = R.id.btn_2p_game;
                break;
            case R.id.btn_find_server:
                viewId = R.id.btn_find_server;
                break;
            case R.id.btn_server:
                viewId = R.id.btn_server;
                break;
        }
        if (viewId != -1) {
            mCustomDialogEventListener.customDialogEvent(viewId);
        }
        dismiss();
    }
}
