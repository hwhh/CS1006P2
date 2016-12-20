package com.company.backgammon.ui;

import com.company.backgammon.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PieceMovingHandler implements MouseListener {



    private BackGammonGUI backGammonGUI;
    private com.company.backgammon.ui.GUIPiece GUIPiece;

    private Image originalImage;
    private int startPos;

    private final Image greenCounterImage = new ImageIcon("GreenCounter.png").getImage();


    public PieceMovingHandler(BackGammonGUI backGammonGUI) {
        this.backGammonGUI = backGammonGUI;
        this.GUIPiece = null;
    }


    @Override
    public void mouseClicked(MouseEvent evt) {
        int x = evt.getPoint().x;
        int y = evt.getPoint().y;
        Logger.log(evt,"Click at " + x + " " + y + " which translates to " + CoordsUtil.boardIdxFromPixelPos(x, y));
        if(backGammonGUI.isMyTurn()) {
            //if a counter is slected
            if (this.GUIPiece != null) {
                backGammonGUI.clearHighlighting();
                int endPos = CoordsUtil.boardIdxFromPixelPos(x, y);
                this.GUIPiece.setImg(originalImage);
                //check where the have clicked next and if valid
                if (backGammonGUI.isClickOnDraw(x,y) ){
                    backGammonGUI.bearOffIfValid(startPos);
                }
                else if(startPos == -1) {
                    backGammonGUI.reenterIfValid(endPos);
                }
                else {
                    backGammonGUI.moveIfValid(startPos, endPos);
                }

                this.backGammonGUI.repaint();
                this.GUIPiece = null;
            } else {
                for (GUIPiece piece:backGammonGUI.GUIPieces) {
                    //Find the counter which has been clicked on
                    if (mouseOverPiece(piece, x, y) && piece.getColour() == backGammonGUI.getMyCounterType()) {
                        this.GUIPiece = piece;
                        originalImage = this.GUIPiece.getImage();
                        this.GUIPiece.setImg(greenCounterImage);
                        this.backGammonGUI.repaint();
                        this.startPos = CoordsUtil.boardIdxFromPixelPos(x, y);
                        backGammonGUI.showAvailableMoves(CoordsUtil.boardIdxFromPixelPos(x, y), this.GUIPiece.getColour());

                        break;
                    }
                }
                //checks of the user has clicked on a capture piece
                for(GUIPiece piece: backGammonGUI.getMyCaptured()) {
                    if (mouseOverPiece(piece, x, y) && piece.getColour() == backGammonGUI.getMyCounterType()) {
                        this.GUIPiece = piece;
                        originalImage = this.GUIPiece.getImage();
                        this.GUIPiece.setImg(greenCounterImage);
                        this.backGammonGUI.repaint();
                        this.startPos = -1;
                        backGammonGUI.showAvailableMoves(CoordsUtil.boardIdxFromPixelPos(x, y), this.GUIPiece.getColour());
                        break;
                    }
                }

            }
        }

    }

    @Override
    public void mousePressed(MouseEvent evt) {
    }


    private boolean mouseOverPiece(GUIPiece piece, int x, int y) {
        return (piece.getX() <= x && piece.getX() + piece.getWidth() >= x && piece.getY() <= y && piece.getY()+ piece.getHeight() >= y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }



}
