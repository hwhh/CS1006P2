package com.company.backgammon.ui;

public class CoordsUtil {
    public static int boardIdxFromRowCol(int row, int col) {
        if(row == 0) {
            return 12 + col;
        }
        else {
            return 12-col -1;
        }
    }

    public static int getColFromBoardIdx(int boardIdx) {
        if(boardIdx < 12) {
            return 12 - boardIdx - 1;
        }
        else {
            return boardIdx - 12;
        }
    }

    public static int getRowFromBoardIdx(int boardIdx) {
        if(boardIdx < 12) {
            return 1;
        }
        return 0;
    }

    public static int rowFromPixelY(int pY) {
        if(pY <= 322) return 0;
        return 1;
    }

    public static int columnFromPixelX(int pX) {
        if(pX <= 330) {
            for(int i = 1; i <= 6; ++i) {
                if(pX < 35 + i*50) {
                    return i-1;
                }
            }
            return 5;
        }
        else {
            for(int i = 1; i <= 6; ++i) {
                if(pX < 390 + i*50) {
                    return i-1 + 6;
                }
            }
            return 11;
        }
    }

    public static int getPixelYFromRow(int row) {
        if(row == 0) {
            return 35;
        }
        return 566;
    }

    public static int getPixelXFromCol(int col) {
        if(col < 6) {
            return 38 + 49 * col;
        }
        else {
            return 345 + 49 * (col-5);
        }
    }

    public static int boardIdxFromPixelPos(int x, int y) {
        return boardIdxFromRowCol(rowFromPixelY(y),columnFromPixelX(x));
    }

    public static int getPixelXFromBoardIdx(int counterIdx) {
        return getPixelXFromCol(getColFromBoardIdx(counterIdx));
    }

    public static int getPixelYFromBoardIdx(int counterIdx) {
        return getPixelYFromRow(getRowFromBoardIdx(counterIdx));
    }
}
