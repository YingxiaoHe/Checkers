package com.example.hyx.billiardball.thread;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.example.hyx.billiardball.view.BoardView;
import com.example.hyx.billiardball.view.ClientView;
import com.example.hyx.billiardball.view.ServerView;

/**
 * Created by HYX on 2017/3/3.
 */

public class TutorialThread extends Thread {

    private int span = 200;
    private SurfaceHolder surfaceHolder;
    private BoardView boardView = null;
    private ServerView serverView = null;
    private ClientView clientView = null;
    private boolean flag = false;

    public TutorialThread(SurfaceHolder surfaceHolder, BoardView boardView) {
        this.surfaceHolder = surfaceHolder;
        this.boardView = boardView;
    }

    public TutorialThread(SurfaceHolder surfaceHolder, ClientView boardView) {
        this.surfaceHolder = surfaceHolder;
        this.clientView = boardView;
    }

    public TutorialThread(SurfaceHolder surfaceHolder, ServerView boardView) {
        this.surfaceHolder = surfaceHolder;
        this.serverView = boardView;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void run() {
        Canvas canvas;
        while (flag) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (boardView != null)
                        boardView.Draw(canvas);
                    else if (clientView != null)
                        clientView.Draw(canvas);
                    else if (serverView != null)
                        serverView.Draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            try {
                Thread.sleep(span);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
