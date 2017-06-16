package com.example.hyx.billiardball.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.hyx.billiardball.R;
import com.example.hyx.billiardball.activity.WindowActivity;
import com.example.hyx.billiardball.thread.TutorialThread;
import com.example.hyx.billiardball.unit.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HYX on 2017/3/2.
 */

public class BoardView extends SurfaceView implements SurfaceHolder.Callback {


    WindowActivity activity;
    private List<Player> players = new ArrayList<>();
    private TutorialThread thread;
    private Bitmap board, select, over, menu, background1, gameOver;
    private Paint paint;
    private int mFromX, mFromY, mToX, mToY;
    private boolean canMove = false;
    private int index = 0;
    private int mMoveSum = 0;
    private boolean win = false;

    /*public int[][] boardPoint = new int[][]{
                                                                                   {511, 384},
                                                                            {474, 448}, {543, 448},
                                                                       {437, 512}, {511, 512}, {585, 512},
                                                                {400, 576}, {474, 576}, {548, 576}, {622, 576},
            {62, 650}, {136, 650}, {210, 650}, {284, 650}, {363, 650}, {437, 650}, {511, 650}, {585, 650}, {659, 650}, {743, 650}, {817, 650}, {821, 650}, {965, 650},
                { 99, 714}, {173, 714}, {247, 714}, {326, 714}, {400, 714}, {474, 714}, {548, 714}, {622, 714}, {696, 714}, {770, 714}, {854, 714}, {928, 714},
                       {136, 778}, {210, 778}, {284, 778}, {363, 778}, {437, 778}, {511, 778}, {585, 778}, {659, 778}, {743, 778}, {817, 778}, {821, 778},
                            {173, 842}, {247, 842}, {326, 842}, {400, 842}, {474, 842}, {548, 842}, {622, 842}, {696, 842}, {770, 842}, {854, 842},
                                   {210, 906}, {284, 906}, {363, 906}, {437, 906}, {511, 906}, {585, 906}, {659, 906}, {743, 906}, {817, 906},
                            {173, 980}, {247, 980}, {326, 980}, {400, 980}, {474, 980}, {548, 980}, {622, 980}, {696, 980}, {770, 980}, {854, 980},
                       {136,1044}, {210,1044}, {284,1044}, {363,1044}, {437,1044}, {511,1044}, {585,1044}, {659,1044}, {743,1044}, {817,1044}, {821,1044},
                { 99,1108}, {173,1108}, {247,1108}, {326,1108}, {400,1108}, {474,1108}, {548,1108}, {622,1108}, {696,1108}, {770,1108}, {854,1108}, {928,1108},
            {62,1172}, {136,1172}, {210,1172}, {284,1172}, {363,1172}, {437,1172}, {511,1172}, {585,1172}, {659,1172}, {743,1172}, {817,1172}, {821,1172}, {965,1172},
                                                                {400, 1246}, {474,1246}, {548,1246}, {622,1246},
                                                                       {437,1314}, {511,1314}, {585,1314},
                                                                             {474,1374}, {548,1374},
                                                                                   {511,1438}
    };*/

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.activity = (WindowActivity) context;
        getHolder().addCallback(this);
        initResources();
        thread = new TutorialThread(getHolder(), this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int getX = (int) event.getX();
        int getY = (int) event.getY();

        move(getX, getY);
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
        if (win) {
            canvas.drawBitmap(gameOver,90,480,paint);
        }
    }

    private void move(int getX, int getY) {
        Player player;
        player = players.get((mMoveSum % players.size()));

        int[] arrayPoint = reconvertPoint(getX, getY);
        if (isBoard(arrayPoint[0], arrayPoint[1]) && player.ismHaveSelected() && canMove) {         //移动棋子
            if (rule(player.getmPieceArray()[index].getmPiecePoint()[0], player.getmPieceArray()[index].getmPiecePoint()[1],
                    arrayPoint[0], arrayPoint[1])) {
                if (!isPiece(arrayPoint[0], arrayPoint[1])) {
                    player.getmPieceArray()[index].setmPiecePoint(arrayPoint);
                    WindowActivity.mMoveSound.start();
                }
            }
        } else if (getX > 780 && getY > 1700) {                                                     //回合结束
            mMoveSum++;
            canMove = false;
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
        if (isWin(player)) {
            win = true;
        }

    }

    public boolean rule(int oldX, int oldY, int newX, int newY) {                                   //规则
        if (Math.abs(oldX - newX) == 1 && Math.abs(oldY - newY) == 1 ||                             //移动一格
                Math.abs(oldX - newX) == 2 && Math.abs(oldY - newY) == 0 ) {
            return true;
        } else if ((Math.abs(oldX - newX) == 2 && Math.abs(oldY - newY) == 2) ||                    //跳
                (Math.abs(oldX - newX) == 4 && Math.abs(oldY - newY) == 0)) {
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

    private boolean isWin(Player player) {
        Player player1 = new Player((player.getmPlayerId() + 3) % 6, (player.getmPlayerId() + 3) % 6);
        int n = 0;
        for (int i = 0; i < player.getmPieceArray().length; i++) {
            for (int j = 0; j < player1.getmPieceArray().length; j++) {
                if (player.getmPieceArray()[i].getmPiecePoint()[0] == player1.getmPieceArray()[j].getmPiecePoint()[0]
                        && player.getmPieceArray()[i].getmPiecePoint()[1] == player1.getmPieceArray()[j].getmPiecePoint()[1]) {
                    n++;
                }
            }
        }
        if (n == 10) {
            return true;
        } else {
            return false;
        }
    }

    private void initResources() {
        paint = new Paint();
        board = BitmapFactory.decodeResource(getResources(), R.drawable.board);
        select = BitmapFactory.decodeResource(getResources(), R.drawable.select);
        over = BitmapFactory.decodeResource(getResources(), R.drawable.over);
        menu = BitmapFactory.decodeResource(getResources(), R.drawable.menu);
        background1 = BitmapFactory.decodeResource(getResources(), R.drawable.background1);
        gameOver = BitmapFactory.decodeResource(getResources(), R.drawable.game_over);
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
