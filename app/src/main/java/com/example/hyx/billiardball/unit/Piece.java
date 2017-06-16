package com.example.hyx.billiardball.unit;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.hyx.billiardball.R;

import static android.content.ContentValues.TAG;

/**
 * Created by HYX on 2017/3/2.
 */

public class Piece {

    private int mPieceId;
    private int[] mPiecePoint = new int[2];
    private int mPieceColor;
    private boolean mSelected;

    public Piece(int playerId, int pieceColor) {
        this.mPieceId = playerId;
        mSelected = false;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }

    public int getmPieceId() {
        return mPieceId;
    }

    public void setmPieceId(int mPieceId) {
        this.mPieceId = mPieceId;
    }

    public int[] getmPiecePoint() {
        return mPiecePoint;
    }

    public void setmPiecePoint(int[] mPiecePoint) {
        this.mPiecePoint = mPiecePoint;
    }

    public void MovePiece(Piece piece, boolean jump, int fromX, int fromY, int toX, int toY) {
    }

    public int getmPieceColor() {
        return mPieceColor;
    }

    public void setmPieceColor(int mPieceColor) {
        this.mPieceColor = mPieceColor;
    }
}
