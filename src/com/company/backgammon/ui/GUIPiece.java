package com.company.backgammon.ui;

import com.company.backgammon.logic.Board;

import java.awt.*;

public class GUIPiece {

    private Image img;
    private int x, y;
    private Board.CounterType colour;

    public GUIPiece(Image img, int x, int y, Board.CounterType colour) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.colour = colour;
    }

    public void drawOn(Graphics g) {
        g.drawImage(img, x, y, null);
    }

    public Image getImage() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return 40;
    }

    public int getHeight() {
        return img.getHeight(null);
    }

    public Board.CounterType getColour() {
        return colour;
    }
}
