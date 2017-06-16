package com.example.hyx.billiardball.unit;

import android.util.Log;

import com.example.hyx.billiardball.R;

import static android.content.ContentValues.TAG;

/**
 * Created by HYX on 2017/3/2.
 */

public class Player {

    private final static int BROWN = 1;
    private final static int GREEN = 2;
    private final static int PURPLE = 3;
    private final static int BLUE = 4;
    private final static int RED = 5;
    private final static int YELLOW = 6;

    private int mPlayerId;
    private int mPlayerColor;
    private Piece[] mPieceArray = new Piece[10];
    private boolean mHaveSelected = true;


    public Player(int playerId, int pieceColor) {
        this.mPlayerId = playerId;
        convertColorId(pieceColor);
        for (int i = 0; i < 10; i++) {
            mPieceArray[i] = new Piece(mPlayerId, mPlayerColor);
        }
        initPieceArray();
    }

    public void convertColorId(int PieceColor) {
        switch (PieceColor) {
            case BROWN:
                mPlayerColor = R.drawable.brown;
                break;
            case GREEN:
                mPlayerColor = R.drawable.green;
                break;
            case PURPLE:
                mPlayerColor = R.drawable.purple;
                break;
            case BLUE:
                mPlayerColor = R.drawable.blue;
                break;
            case RED:
                mPlayerColor = R.drawable.red;
                break;
            case YELLOW:
                mPlayerColor = R.drawable.yellow;
                break;
        }
    }

    private void initPieceArray() {
        switch (mPlayerColor) {
            case R.drawable.brown:
                mPieceArray[0].setmPiecePoint(new int[]{13, 17});
                mPieceArray[1].setmPiecePoint(new int[]{12, 16});
                mPieceArray[2].setmPiecePoint(new int[]{14, 16});
                mPieceArray[3].setmPiecePoint(new int[]{11, 15});
                mPieceArray[4].setmPiecePoint(new int[]{13, 15});
                mPieceArray[5].setmPiecePoint(new int[]{15, 15});
                mPieceArray[6].setmPiecePoint(new int[]{10, 14});
                mPieceArray[7].setmPiecePoint(new int[]{12, 14});
                mPieceArray[8].setmPiecePoint(new int[]{14, 14});
                mPieceArray[9].setmPiecePoint(new int[]{16, 14});
                break;
            case R.drawable.green:
                mPieceArray[0].setmPiecePoint(new int[]{25, 13});
                mPieceArray[1].setmPiecePoint(new int[]{23, 13});
                mPieceArray[2].setmPiecePoint(new int[]{24, 12});
                mPieceArray[3].setmPiecePoint(new int[]{21, 13});
                mPieceArray[4].setmPiecePoint(new int[]{22, 12});
                mPieceArray[5].setmPiecePoint(new int[]{23, 11});
                mPieceArray[6].setmPiecePoint(new int[]{19, 13});
                mPieceArray[7].setmPiecePoint(new int[]{20, 12});
                mPieceArray[8].setmPiecePoint(new int[]{21, 11});
                mPieceArray[9].setmPiecePoint(new int[]{22, 10});
                break;
            case R.drawable.purple:
                mPieceArray[0].setmPiecePoint(new int[]{25, 5});
                mPieceArray[1].setmPiecePoint(new int[]{24, 6});
                mPieceArray[2].setmPiecePoint(new int[]{23, 5});
                mPieceArray[3].setmPiecePoint(new int[]{23, 7});
                mPieceArray[4].setmPiecePoint(new int[]{22, 6});
                mPieceArray[5].setmPiecePoint(new int[]{21, 5});
                mPieceArray[6].setmPiecePoint(new int[]{22, 8});
                mPieceArray[7].setmPiecePoint(new int[]{21, 7});
                mPieceArray[8].setmPiecePoint(new int[]{20, 6});
                mPieceArray[9].setmPiecePoint(new int[]{19, 5});
                break;
            case R.drawable.blue:
                mPieceArray[0].setmPiecePoint(new int[]{13, 1});
                mPieceArray[1].setmPiecePoint(new int[]{12, 2});
                mPieceArray[2].setmPiecePoint(new int[]{14, 2});
                mPieceArray[3].setmPiecePoint(new int[]{11, 3});
                mPieceArray[4].setmPiecePoint(new int[]{13, 3});
                mPieceArray[5].setmPiecePoint(new int[]{15, 3});
                mPieceArray[6].setmPiecePoint(new int[]{10, 4});
                mPieceArray[7].setmPiecePoint(new int[]{12, 4});
                mPieceArray[8].setmPiecePoint(new int[]{14, 4});
                mPieceArray[9].setmPiecePoint(new int[]{16, 4});
                break;
            case R.drawable.red:
                mPieceArray[0].setmPiecePoint(new int[]{1, 5});
                mPieceArray[1].setmPiecePoint(new int[]{2, 6});
                mPieceArray[2].setmPiecePoint(new int[]{3, 5});
                mPieceArray[3].setmPiecePoint(new int[]{3, 7});
                mPieceArray[4].setmPiecePoint(new int[]{4, 6});
                mPieceArray[5].setmPiecePoint(new int[]{5, 5});
                mPieceArray[6].setmPiecePoint(new int[]{4, 8});
                mPieceArray[7].setmPiecePoint(new int[]{5, 7});
                mPieceArray[8].setmPiecePoint(new int[]{6, 6});
                mPieceArray[9].setmPiecePoint(new int[]{7, 5});
                break;
            case R.drawable.yellow:
                mPieceArray[0].setmPiecePoint(new int[]{1, 13});
                mPieceArray[1].setmPiecePoint(new int[]{3, 13});
                mPieceArray[2].setmPiecePoint(new int[]{2, 12});
                mPieceArray[3].setmPiecePoint(new int[]{5, 13});
                mPieceArray[4].setmPiecePoint(new int[]{4, 12});
                mPieceArray[5].setmPiecePoint(new int[]{3, 11});
                mPieceArray[6].setmPiecePoint(new int[]{7, 13});
                mPieceArray[7].setmPiecePoint(new int[]{6, 12});
                mPieceArray[8].setmPiecePoint(new int[]{5, 11});
                mPieceArray[9].setmPiecePoint(new int[]{4, 10});
                break;
        }
    }

    public int getmPlayerId() {
        return mPlayerId;
    }

    public int getmPlayerColor() {
        return mPlayerColor;
    }

    public void setmPlayerColor(int mPlayerColor) {
        this.mPlayerColor = mPlayerColor;
        for (int i = 0; i < 10; i++) {
            mPieceArray[i].setmPieceColor(mPlayerColor);
        }
    }

    public Piece[] getmPieceArray() {
        return mPieceArray;
    }

    public boolean ismHaveSelected() {
        return mHaveSelected;
    }

    public void setmHaveSelected(boolean mHaveSelected) {
        this.mHaveSelected = mHaveSelected;
    }
}