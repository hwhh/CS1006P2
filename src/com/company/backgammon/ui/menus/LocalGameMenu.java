package com.company.backgammon.ui.menus;

import com.company.backgammon.Logger;
import com.company.backgammon.ai.ExpectiMiniMaxAI;
import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.ai.evaluators.StaticEvaluator;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.GameWithSpectator;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.ui.BackGammonGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LocalGameMenu {

    private static final String PLAYER_VS_PLAYER = "Player vs Player";
    private static final String PLAYER_VS_AI = "Player vs AI";
    private static final String AI_VS_AI = "AI vs AI";

    private JComboBox comboBox1;
    private JButton startButton;
    private JPanel parentPanel;

    public static void startWithTwoGUI(){
        SwingUtilities.invokeLater(() -> {
            BackGammonGUI player1 = new BackGammonGUI();
            BackGammonGUI player2 = new BackGammonGUI();
            Game game = new Game(player1,player2);
            //GameAITest gameAITest=new GameAITest(player1,player2);
            game.start();
        });
    }

    public LocalGameMenu() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String selectedThing = (String) comboBox1.getModel().getSelectedItem();
                switch (selectedThing) {
                    case PLAYER_VS_PLAYER:
                        startWithTwoGUI();
                        break;
                    case PLAYER_VS_AI:
                        startPlayerVsAI();
                        break;
                    case AI_VS_AI:
                        startAIvsAI();
                        break;
                    default:
                        Logger.error(this,"Unexpected selection in combo box");
                }
            }
        });
    }

    private static void startPlayerVsAI() {
        SwingUtilities.invokeLater(() -> {
            BackGammonGUI player1 = new BackGammonGUI();
            IPlayer player2 = new ExpectiMiniMaxAI(Evaluator.getBestEvaluator(),3);
            Game game = new Game(player1,player2);
            //GameAITest gameAITest=new GameAITest(player1,player2);
            game.start();
        });
    }
    private static void startAIvsAI() {
        SwingUtilities.invokeLater(() -> {
            ExpectiMiniMaxAI ai1 = new ExpectiMiniMaxAI(Evaluator.getBestEvaluator(),3);
            ExpectiMiniMaxAI ai2 = new ExpectiMiniMaxAI(Evaluator.getBestEvaluator(),3);
            BackGammonGUI spectator = new BackGammonGUI();
            Game game = new GameWithSpectator(ai1,ai2,spectator);
            game.start();
        });
    }

    public static void initialize() {
        JFrame frame = new JFrame("LocalGameMenu");
        frame.setContentPane(new LocalGameMenu().parentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
