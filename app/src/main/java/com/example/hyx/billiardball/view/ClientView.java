package com.example.hyx.billiardball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.activity.ClientWindowActivity;
import com.example.hyx.billiardball.activity.WindowActivity;
import com.example.hyx.billiardball.activity.findRoomActivity;
import com.example.hyx.billiardball.thread.ClientThread;
import com.example.hyx.billiardball.thread.TutorialThread;
import com.example.hyx.billiardball.unit.Piece;
import com.example.hyx.billiardball.unit.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HYX on 2017/5/20.
 */

public class ClientView extends SurfaceView implements SurfaceHolder.Callback {

    //    private static final int ERROR_MOVE = 1;
    private static final int CLIENT_DATA = 2;
    private static final int SERVER_DATA = 3;

    ClientWindowActivity activity;
    private List<Player> players = new ArrayList<>();
    private TutorialThread thread;
    private Bitmap board, select, over, menu, background1;
    private Paint paint;
    private int mFromX, mFromY, mToX, mToY;
    private boolean canMove = false;
    private boolean playerMove = false;
    private int index = 0;
    private int mMoveSum = 0;
    private Handler handler = null;
    private ClientThread clientThread = findRoomActivity.clientThread;


    public ClientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (ClientWindowActivity) context;
        getHolder().addCallback(this);
        initResources();
        thread = new TutorialThread(getHolder(), this);

        getMessage();
    }

    private void getMessage() {
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                switch(msg.what) {
                    case SERVER_DATA:
                        if (msg.obj.toString().split("|")[0].equals("over")) {
                            playerMove = true;
                        } else if (msg.obj.toString().split("|")[0].equals("run")) {
                            playerMove = false;
                        }
                        messageDataSetPlayers(msg.obj.toString());
                        break;
                }
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int getX = (int) event.getX();
        int getY = (int) event.getY();

        if (playerMove) {
            move(getX, getY);
        }
        return super.onTouchEvent(event);
    }

    public void Draw(Canvas canvas) {

        canvas.drawBitmap(background1, 0, 0, paint);
        canvas.drawBitmap(board, 0, 350, paint);                                                    //画棋盘
        for (int i = 0; i < players.size(); i++) {                                                  //画棋子
            Player player = players.get(i);
            for (int j = 0; j < player.getmPieceArray().length; j++) {
                int[] point = convertPoint(player.getmPieceArray()[j].getmPiecePoint());
                int x = point[0];
                int y = point[1];
                if (player.getmPieceArray()[j].ismSelected()) {
                    canvas.drawBitmap(select, x - 5, y - 5, paint);
                }
                canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), player.getmPlayerColor()), x, y, paint);
            }
        }
        canvas.drawBitmap(over, 780, 1700, paint);                                                  //画按钮

    }

    private void move(int getX, int getY) {
        Player player;
        player = players.get((mMoveSum % players.size()));
       /* for (int k = 0; k < players.size(); k++) {
            player = players.get(k);*/

        int[] arrayPoint = reconvertPoint(getX, getY);
        if (isBoard(arrayPoint[0], arrayPoint[1]) && player.ismHaveSelected() && canMove) {         //移动棋子
            if (rule(player.getmPieceArray()[index].getmPiecePoint()[0], player.getmPieceArray()[index].getmPiecePoint()[1],
                    arrayPoint[0], arrayPoint[1])) {
                if (!isPiece(arrayPoint[0], arrayPoint[1])) {
                    player.getmPieceArray()[index].setmPiecePoint(arrayPoint);
                    ClientWindowActivity.mMoveSound.start();
                }
            }
        } else if (getX > 780 && getY > 1700) {                                                     //回合结束
            mMoveSum++;
            canMove = false;
            playerMove = false;
            player.setmHaveSelected(false);
            player.getmPieceArray()[index].setmSelected(false);
        } else {
            for (int i = 0; i < 10; i++) {
                int x = player.getmPieceArray()[i].getmPiecePoint()[0];
                int y = player.getmPieceArray()[i].getmPiecePoint()[1];
                int[] screenPoint = convertPoint(new int[]{x, y});
                x = screenPoint[0];
                y = screenPoint[1];
                if (getX > x && getX < x + 50 && getY > y && getY < y + 50) {                       //选取棋子
                    for (int j = 0; j < 10; j++) {
                        player.getmPieceArray()[j].setmSelected(false);
                    }
                    player.getmPieceArray()[i].setmSelected(true);
                    player.setmHaveSelected(true);
                    mFromX = x;
                    mFromY = y;
                    index = i;
                    canMove = true;
                    break;
                }
            }
        }

        setMessage();
    }

    public boolean rule(int oldX, int oldY, int newX, int newY) {                                   //规则
        if (Math.abs(oldX - newX) == 1 && Math.abs(oldY - newY) == 1 ||                             //移动一格
                Math.abs(oldX - newX) == 2 && Math.abs(oldY - newY) == 0 ||
                Math.abs(oldX - newX) == 0 && Math.abs(oldY - newY) == 2) {
            return true;
        } else if ((Math.abs(oldX - newX) == 2 && Math.abs(oldY - newY) == 2) ||                    //跳
                (Math.abs(oldX - newX) == 4 && Math.abs(oldY - newY) == 0) ) {//||
            //(Math.abs(oldX - newX) == 0 && Math.abs(oldY - newY) == 4)
            int middleX = (oldX + newX) / 2;
            int middleY = (oldY + newY) / 2;
            boolean flag = false;
            for (Player player : players) {
                for (int i = 0; i < player.getmPieceArray().length; i++) {
                    if (player.getmPieceArray()[i].getmPiecePoint()[0] == middleX &&
                            player.getmPieceArray()[i].getmPiecePoint()[1] == middleY) {
                        flag = true;
                    }
                }
            }
            return flag;
        }
        return false;
    }

    private boolean isPiece(int x, int y) {                                                         //判定是否为棋子
        for (Player player : players) {
            for (int i = 0; i < player.getmPieceArray().length; i++) {
                if (player.getmPieceArray()[i].getmPiecePoint()[0] == x && player.getmPieceArray()[i].getmPiecePoint()[1] == y) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoard(int x, int y) {                                                         //棋盘边界判定
        if ((x + y) % 2 == 0 && x >= 1 && x <= 25 && y >= 1 && y <= 17) {
            if ((x < 10 && y < 5) || (x < 10 && y > 13) || (x > 16 && y < 5) || (x > 16 && y > 13) ||
                    (x > 13 && x <= 16 && y >= 14 && (x + y) > 30) ||
                    (x >= 10 && x < 13 && y >= 14 && (y - x) > 4) ||
                    (x >= 10 && x < 13 && y <= 4 && (x + y) < 14) ||
                    (x > 13 && x <= 16 && y <= 4 && (x - y) > 12) ||
                    (x > 21 && x <= 25 && y > 5 && y <= 9 && (x + y) > 30) ||
                    (x > 21 && x <= 25 && y > 9 && y < 13 && (x - y) > 12) ||
                    (x >= 1 && x < 5 && y > 5 && y <= 9 && (y - x) > 4) ||
                    (x >= 1 && x < 5 && y > 9 && y < 13 && (x + y) < 14)) {
                return false;
            } else {
                return true;
            }
        } else
            return false;
    }


    private void initResources() {
        paint = new Paint();
        board = BitmapFactory.decodeResource(getResources(), R.drawable.board);
        select = BitmapFactory.decodeResource(getResources(), R.drawable.select);
        over = BitmapFactory.decodeResource(getResources(), R.drawable.over);
        menu = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
        background1 = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
    }

    private void setMessage() {
        Message msg = new Message();
        msg.what = CLIENT_DATA;
        msg.obj = messageDataToString();
        System.out.println(msg.obj.toString());
        clientThread.setHandler.sendMessage(msg);
    }


    public void messageDataSetPlayers(String data) {
        String[] strPlayers = data.split("&");
        for (int i = 0; i < strPlayers.length; i++) {
            String[] strPieces = strPlayers[i].split("\t");
            for (int j = 0; j < strPieces.length; j++) {
                if (i == 0 && j == 0) {
                    int x = Integer.parseInt(strPieces[j].split("|")[1]);
                    int y = Integer.parseInt(strPieces[j].split("|")[2]);
                    players.get(i).getmPieceArray()[j].setmPiecePoint(new int[]{x,y});
                } else {
                    int x = Integer.parseInt(strPieces[j].split("|")[0]);
                    int y = Integer.parseInt(strPieces[j].split("|")[1]);
                    players.get(i).getmPieceArray()[j].setmPiecePoint(new int[]{x,y});
                }
            }
        }
    }
    public String messageDataToString() {
        String data;
        if (playerMove) {
            data = "run|";
        } else {
            data = "over|";
        }
        for (Player player : players) {
            for (Piece piece : player.getmPieceArray()) {
                data = data + piece.getmPiecePoint()[0] + "|" + piece.getmPiecePoint()[1] + "\t";
            }
            data = data + "&";
        }
        return data;
    }

    public int[] convertPoint(int[] oldPoint) {                                                     //把屏幕坐标转化为棋子坐标
        int x = oldPoint[0];
        int y = oldPoint[1];

        if (x <= 7) x = x * 37 + 25;
        else if (x > 7 && x < 19) x = x * 37 + 30;
        else if (x >= 19) x = x * 37 + 40;

        if (y <= 4) y = y * 64 + 320;
        else if (y > 4 && y <= 9) y = y * 64 + 330;
        else if (y > 9 && y < 14) y = y * 64 + 340;
        else if (y >= 14) y = y * 64 + 350;

        return new int[]{x, y};
    }

    public int[] reconvertPoint(int x, int y) {                                                     //把棋子坐标转化为屏幕坐标

        if (x <= 7) x = (x - 20) / 37;
        else if (x < 19 && x > 7) x = (x - 30) / 37;
        else if (x >= 19) x = (x - 40) / 37;

        if (y <= 4) y = (y - 320) / 64;
        else if (y > 4 && y <= 9) y = (y - 330) / 64;
        else if (y > 9 && y < 14) y = (y - 340) / 64;
        else if (y >= 14) y = (y - 350) / 64;

        return new int[]{x, y};
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setFlag(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean flag = true;
        thread.setFlag(flag);
        while (flag) {
            try {
                thread.join();
                flag = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

}

