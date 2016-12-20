package com.company.backgammon.ui;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.*;
import com.company.backgammon.logic.moves.*;
import com.company.backgammon.logic.events.player.PlayerEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import static com.company.backgammon.ui.CoordsUtil.*;

public class BackGammonGUI extends JPanel implements IPlayer {

    public static final int PIECE_SIZE = 42;
    public static final int REROLL_FRAMES = 10;

    public Board.CounterType selectedCounter;

    private static final int[][] BOARD_LABEL_LOCATIONS ={{655,365},{610,365},{560,365},{510,365},{460,365},{410,365},{304,365},{254,365},{204,365},{154,365},{104,365},{54,365},
                                                         {54,255},{104,255},{154,255},{204,255},{254,255},{304,255},{410,255},{460,255},{510,255},{560,255},{610,255}, {655,255}, {355,580},{355,50}};
    private List<Integer> highlighthedCounterPos = new ArrayList<>();

    private JLabel[] overflowLabels = new JLabel[26];

    private boolean rolledAlready = false;

    public final List<Integer> diceValues = new ArrayList<>();

    private static int instancesRunning = 0;
    private final JFrame parentFrame;

    private Board.CounterType myCounterType;

    private Timer timer;

    private boolean isMyTurn;

    public JButton btnRollDice;

    public List<GUIPiece> myCaptured;
    public final List<GUIPiece> GUIPieces = new ArrayList<>();
    public final List<JLabel> Labels = new ArrayList<>();


    public List<GUIPiece> whiteCaptured = new ArrayList<>();
    public List<GUIPiece> redCaptured = new ArrayList<>();
    public List<GUIPiece> redBornOff = new ArrayList<>();
    public List<GUIPiece> whiteBornOff = new ArrayList<>();

    private Game game;

    private final Image imgBackground = new ImageIcon("Background.png").getImage();
    private final Image redCounterImg = new ImageIcon("RedCounter.png").getImage();
    private final Image whiteCounterImg = new ImageIcon("WhiteCounter.png").getImage();
    private final Image whiteCounterSideImg = new ImageIcon("WhiteSideCounter.png").getImage();
    private final Image redCounterSideImg = new ImageIcon("RedSideCounter.png").getImage();
    private final Image AvailablePositionUp= new ImageIcon("AvailablePositionUp.png").getImage();
    private final Image AvailablePositionDown= new ImageIcon("AvailablePositionDown.png").getImage();
    private final Image BearOffImage= new ImageIcon("BearOff.png").getImage();

    private JLabel lblGameState;
    private JLabel lblRedScore;
    private JLabel lblWhiteScore;
    private JLabel lblMyPieceName;

    private int playerID;


    public BackGammonGUI() {
        /*Creates the frame buttons and action listeners
          Sets up the frame
        */
        this.setLayout(null);
        PieceMovingHandler listener = new PieceMovingHandler(this);
        this.addMouseListener(listener);
        createRollButton();
        createPieceNameLabel();
        createGameStateLabel();
        createScoreLabels();

        this.repaint();

        this.parentFrame = new JFrame();
        bindToParentJFrame();

        parentFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.disconnect(playerID);
            }

            @Override
            public void windowOpened(WindowEvent windowEvent) {}

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        initializeOverflowLabels();

        instancesRunning++;
    }


    //Functions to add the required buttons in the correct place
    private void createRollButton() {
        btnRollDice = new JButton("Roll Dice");
        btnRollDice.setBounds(320, 0, 80, 30);
        this.add(btnRollDice);
        btnRollDice.addActionListener(new RollDiceActionListener(this));
    }

    private void createPieceNameLabel() {
        this.lblMyPieceName = new JLabel("");
        lblMyPieceName.setBounds(5, 0, 100, 30);
        lblMyPieceName.setForeground(Color.WHITE);
        this.add(lblMyPieceName);
    }

    private void initializeOverflowLabels() {
        for(int i = 0; i < 26; ++i) {
            JLabel label = new JLabel();
            label.setBounds(BOARD_LABEL_LOCATIONS[i][0], BOARD_LABEL_LOCATIONS[i][1],20,20);
            label.setForeground(Color.BLACK);
            this.add(label);
            overflowLabels[i] = label;
        }
    }

    private void bindToParentJFrame() {
        parentFrame.setTitle("Backgammon");
        parentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        parentFrame.setLocation(200+75*instancesRunning,200+75*instancesRunning);
        parentFrame.setResizable(false);
        parentFrame.setMinimumSize( new Dimension(this.imgBackground.getWidth(this), this.imgBackground.getHeight(this)+20));
        parentFrame.add(this);
        parentFrame.pack();
        parentFrame.setVisible(true);
    }

    //Functions to add the required labels in the correct place
    private void createScoreLabels() {
        this.lblRedScore = new JLabel("Reds score: 0");
        lblRedScore.setBounds(645,3,200,30);
        lblRedScore.setForeground(Color.WHITE);
        this.add(lblRedScore);

        this.lblWhiteScore = new JLabel("Whites score: 0");
        lblWhiteScore.setBounds(645,610,200,30);
        lblWhiteScore.setForeground(Color.WHITE);
        this.add(lblWhiteScore);
    }

    private void createGameStateLabel() {
        this.lblGameState = new JLabel("Waiting on opponent.");
        lblGameState.setBounds(110, 0, 150, 30);
        lblGameState.setForeground(Color.WHITE);
        this.add(lblGameState);
    }

    public boolean isClickOnDraw(int x, int y) {
        //Checks whether the user has clicked on their correct bearing off area
        if(myCounterType == Board.CounterType.RED) {
            return (706 <= x && x < 774) && (37 <= y && y < 255);
        }
        else if(myCounterType == Board.CounterType.WHITE) {
            return (706 <= x && x < 774) && (388 <= y && y < 609);
        }
        return false;
    }

    public void drawDie(Graphics g, int val, int x, int y) {
        //draw the dice with black outline
        g.setColor(Color.white);
        g.fillRect(x, y, 35, 35);
        g.setColor(Color.black);
        g.drawRect(x, y, 35, 35);
        if (val > 1)  // upper left dot
            g.fillOval(x+3, y+3, 9, 9);
        if (val > 3)  // upper right dot
            g.fillOval(x+23, y+3, 9, 9);
        if (val == 6) // middle left dot
            g.fillOval(x+3, y+13, 9, 9);
        if (val % 2 == 1) // middle dot (for odd-numbered val's)
            g.fillOval(x+13, y+13, 9, 9);
        if (val == 6) // middle right dot
            g.fillOval(x+23, y+13, 9, 9);
        if (val > 3)  // bottom left dot
            g.fillOval(x+3, y+23, 9, 9);
        if (val > 1)  // bottom right dot
            g.fillOval(x+23, y+23, 9,9);
    }

    public void triggerRoll() {
        if (timer != null) {
            //Roll animation currently running...
            return;
        }
        //New timer created which lasts 100ms

        timer = new Timer(100, new ActionListener() {

            //While the timer is running the following code is executed
            int frames = 1;

            public void actionPerformed(ActionEvent evt) {
                //Set the faces of the dice to random numbers
                diceValues.clear();
                Dice dice = new Dice();
                dice.roll();
                diceValues.add(dice.getValue());
                dice.roll();
                diceValues.add(dice.getValue());
                repaint();
                //Update the faces of the dice
                frames++;
                //check of the timer has finished and get the value of displayed on the dice
                //Then calculate the available moves based on the dice roll
                if (frames == REROLL_FRAMES) {
                    timer.stop();
                    requestDiceRoll();
                    getAvailableMoves();
                    timer = null;
                }
            }
        });
        timer.start();
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    private void addPieces(int counterIdx, Board.CounterType counterType, int counters) {
        //This method adds the pieces to the board in the correct location
        int row = getRowFromBoardIdx(counterIdx);
        int col = getColFromBoardIdx(counterIdx);
        if(row == 0) {
            int pixelY = getPixelYFromRow(row);
            int pixelX = getPixelXFromCol(col);
            if(counters <= 6) {
                overflowLabels[counterIdx].setText("");
                for (int i = 0; i < counters; ++i) {
                    createAndAddPiece(pixelX, pixelY + PIECE_SIZE * i, counterType);
                }
            }
            else{
                //columns where there are more than 6 counters, additional counters are not added
                //Label signifying additional counters added
                for(int i = 0; i < 6; ++i) {
                    createAndAddPiece(pixelX,pixelY+PIECE_SIZE*i,counterType);
                }
                overflowLabels[counterIdx].setText(String.valueOf(counters-6));
            }

        }
        else {
            //get coordinates of the counter
            int pixelY = getPixelYFromRow(row);
            int pixelX = getPixelXFromCol(col);
            if(counters <= 6) {
                overflowLabels[counterIdx].setText("");
                for (int i = 0; i < counters; ++i) {
                    createAndAddPiece(pixelX, pixelY - PIECE_SIZE*i, counterType);
                }
            }
            else {
                //columns where there are more than 6 counters, additional counters are not added
                //Label signifying additional counters added
                for(int i = 0; i < 6; ++i) {
                    createAndAddPiece(pixelX,pixelY-PIECE_SIZE*i,counterType);
                }
                overflowLabels[counterIdx].setText(String.valueOf(counters-6));

            }
        }

    }

    private void createAndAddPiece(int x, int y, Board.CounterType type) {
        Image img;
        if(type == Board.CounterType.RED) {
            img = redCounterImg;
        }
        else {
            img = whiteCounterImg;
        }

        GUIPiece GUIPiece = new GUIPiece(img, x, y, type);
        this.GUIPieces.add(GUIPiece);
    }

    @Override
    public void setGame(Game game, int playerID, Board.CounterType type) {
        this.game = game;
        this.playerID = playerID;
        this.myCounterType = type;
        if(type == Board.CounterType.RED) {
            myCaptured = redCaptured;
        }
        else if (type == Board.CounterType.WHITE) {
            myCaptured = whiteCaptured;
        }
        lblMyPieceName.setText("You are " + String.valueOf(type));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(this.imgBackground, 0, 0, null);
        try {
            for (Integer highlighthedCounterPos : this.highlighthedCounterPos) {
                //adds the correct highlighting
                if(highlighthedCounterPos >23 )//if the red can bear off
                    drawHighLighted(BearOffImage,702,33, g);
                else if (highlighthedCounterPos < 0 )//if the white can bear off
                    drawHighLighted(BearOffImage,702,385, g);
                else {
                    if (highlighthedCounterPos > 17)
                        drawHighLighted(AvailablePositionDown, CoordsUtil.getPixelXFromBoardIdx(highlighthedCounterPos) - 4, CoordsUtil.getPixelYFromBoardIdx(highlighthedCounterPos) - 2, g);
                    else if (highlighthedCounterPos > 11 && highlighthedCounterPos <= 17)
                        drawHighLighted(AvailablePositionDown, CoordsUtil.getPixelXFromBoardIdx(highlighthedCounterPos) - 6, CoordsUtil.getPixelYFromBoardIdx(highlighthedCounterPos) - 2, g);
                    else if (highlighthedCounterPos <= 5)
                        drawHighLighted(AvailablePositionUp, CoordsUtil.getPixelXFromBoardIdx(highlighthedCounterPos) - 4, CoordsUtil.getPixelYFromBoardIdx(highlighthedCounterPos) - 180, g);
                    else if (highlighthedCounterPos <= 11 && highlighthedCounterPos > 5) {
                        drawHighLighted(AvailablePositionUp, CoordsUtil.getPixelXFromBoardIdx(highlighthedCounterPos) - 6, CoordsUtil.getPixelYFromBoardIdx(highlighthedCounterPos) - 180, g);

                    }
                }
            }
            //Add all the counters from the different lists to the board
            for (GUIPiece piece : this.GUIPieces) {
                piece.drawOn(g);
            }

            for (GUIPiece piece : this.whiteCaptured) {
                piece.drawOn(g);
            }
            for (GUIPiece piece : this.redCaptured) {
                piece.drawOn(g);
            }

            for (GUIPiece piece : this.whiteBornOff) {
                piece.drawOn(g);
            }
            for (GUIPiece piece : this.redBornOff) {
                piece.drawOn(g);
            }
        }
        catch (ConcurrentModificationException ex){
            Logger.log(this,"Some of the piece lists were read while being written. Ignoring...");
        }

        try {
            drawDices(g);
        }
        catch (ConcurrentModificationException | IndexOutOfBoundsException ex) {
            Logger.log(this,"Available moves list was modified while being read. Ignoring...");
        }

    }

    private void drawDices(Graphics gfx) {
        //draw the dices in the correct location
        if (diceValues.size() == 1) {
            drawDie(gfx, diceValues.get(0), 340, 300);
        } else if (diceValues.size() == 2) {
            drawDie(gfx, diceValues.get(0), 320, 300);
            drawDie(gfx, diceValues.get(1), 360, 300);
        } else if (diceValues.size() == 3) {
            drawDie(gfx, diceValues.get(0), 300, 300);
            drawDie(gfx, diceValues.get(1), 340, 300);
            drawDie(gfx, diceValues.get(2), 380, 300);
        } else if (diceValues.size() == 4) {
            drawDie(gfx, diceValues.get(0), 280, 300);
            drawDie(gfx, diceValues.get(1), 320, 300);
            drawDie(gfx, diceValues.get(2), 360, 300);
            drawDie(gfx, diceValues.get(3), 400, 300);
        }
    }

    public void updateBar(int whiteOnBar, int redOnBar) {
        //Place the captured pieces in the bar
        int x = 340;
        int redStartY = 570;
        int whiteStartY = 40;
        redCaptured.clear();
        overflowLabels[24].setText("");
        if(redOnBar > 0) {
            redCaptured.add(new GUIPiece(redCounterImg, x, redStartY, Board.CounterType.RED));
            if(redOnBar > 1) {
                overflowLabels[24].setText(String.valueOf(redOnBar));//if there are more than 1 captured piece label used to signify additional captured pieces
            }
        }

        whiteCaptured.clear();
        overflowLabels[25].setText("");
        if(whiteOnBar > 0) {
            whiteCaptured.add(new GUIPiece(whiteCounterImg,x,whiteStartY, Board.CounterType.WHITE));
            if(whiteOnBar > 1) {
                overflowLabels[25].setText(String.valueOf(whiteOnBar));//if there are more than 1 captured piece label used to signify additional captured pieces
            }
        }
    }

    public void updateBornOff(int whiteOnBar, int redOnBar) {
        //Update the bear off area and the scores for each player
        int x = 707;
        int redStartY = 235;
        int whiteStartY = 590;
        redBornOff.clear();
        for(int i = 0; i < redOnBar; ++i) {
            redBornOff.add(new GUIPiece(redCounterSideImg,x,redStartY - i*7, Board.CounterType.RED));
        }
        whiteBornOff.clear();
        for(int i = 0; i < whiteOnBar; ++i) {
            whiteBornOff.add(new GUIPiece(whiteCounterSideImg,x,whiteStartY - i*7, Board.CounterType.WHITE));
        }

        lblRedScore.setText("Reds score: " + redOnBar);
        lblWhiteScore.setText("Whites score: " + whiteOnBar);
    }

    @Override
    public void updateBoard(Board board) {
        //Removes all counters from the board and then re-add them in updated locations
        whiteCaptured.clear();
        redCaptured.clear();
        synchronized (GUIPieces) {
            GUIPieces.clear();
            for (int i = 0; i < 24; ++i) {
                addPieces(i, board.getCounterTypeAt(i), board.getCountersAt(i));
            }
        }
        updateBar(board.getWhiteCaptured(), board.getRedCaptured());
        updateBornOff(board.getWhiteBornOff(), board.getRedBornOff());
        getAvailableMoves();

        this.repaint();
    }

    //Method for switching turns

    @Override
    public void startTurn(Turn opponentTurn) {
        this.isMyTurn = true;
        this.parentFrame.toFront();
        this.parentFrame.requestFocus();
        this.lblGameState.setText("It's your turn");
        repaint();
    }

    @Override
    public void endTurn() {
        this.isMyTurn = false;
        this.lblGameState.setText("Waiting on opponent.");
        repaint();
    }

    //Methods for ending states
    private void win() {
        lblGameState.setText("You win!!");
        isMyTurn = false;
        repaint();
    }

    public void lose() {
        lblGameState.setText("You lose!! :(");
        isMyTurn = false;
        repaint();
    }

    //If a user quits during a game
    @Override
    public void opponentSurrendered() {
        Logger.log(this,"Opponent surrendered. Exiting...");
        parentFrame.dispose();
    }

    @Override
    public void gameOver(int winningPlayerID) {
        if(winningPlayerID == playerID) {
            win();
        }
        else{
            lose();
        }
    }

    public Board.CounterType getMyCounterType() {
        return myCounterType;
    }

    public void requestDiceRoll() {
        game.rollDices();
    }

    public void getAvailableMoves() {
        synchronized (diceValues) {
            diceValues.clear();
            diceValues.addAll(game.getAvailableMoves());
        }
        repaint();
    }

    public boolean canRoll() {
        return game.canRoll(playerID);
    }

    //Move validation checks
    public void moveIfValid(int startPos, int endPos) {
        try {
            game.sendMove(new BoardMove(startPos,endPos, -1, myCounterType)); //TODO compute necessary dice roll
        } catch (InvalidMoveException e) {
            JOptionPane.showMessageDialog(parentFrame,e.getMessage());
        }
    }

    public void reenterIfValid(int endPos) {
        try {
            game.sendMove(new ReenterMove(endPos,-1,myCounterType)); //TODO compute necessary dice roll
        } catch (InvalidMoveException e) {
            JOptionPane.showMessageDialog(parentFrame,e.getMessage());
        }
    }

    public void bearOffIfValid(int from) {
        try {
            game.sendMove(new BearOffMove(from,-1, myCounterType));
        } catch (InvalidMoveException e) {
            JOptionPane.showMessageDialog(parentFrame,e.getMessage());
        }
    }

    public List<GUIPiece> getMyCaptured() {
        return myCaptured;
    }

    public void sendEvent(PlayerEvent event) {
        event.applyOn(this);
    }

    public void drawHighLighted(Image img, int x, int y, Graphics g){
        g.drawImage(img, x, y, null);
    }


    public void showAvailableMoves(int counterInx, Board.CounterType type){
        highlighthedCounterPos.clear();
        Board board = game.getBoard();
        selectedCounter = type;
        List<Move> movesFromCounter = board.getPossibleMovesWithCounter(counterInx, type);
        //Gat all the amiable moves, then slit moves up into the type of move they are
        for (Move move : movesFromCounter) {
            if(move.getClass()== BoardMove.class){
                BoardMove nextMove = (BoardMove) move;
                highlighthedCounterPos.add(nextMove.getTo());
            }
            else if(move.getClass()== ReenterMove.class){
                ReenterMove nextMove = (ReenterMove) move;
                highlighthedCounterPos.add(nextMove.getTo());
            }
            else if(move.getClass()== BearOffMove.class) {
                if (type == Board.CounterType.RED)
                    //rouge values used to check for bearing off moves
                    highlighthedCounterPos.add(24);
                else
                    highlighthedCounterPos.add(-1);
            }
        }

    }

    public void clearHighlighting() {
        highlighthedCounterPos.clear();
    }

}
