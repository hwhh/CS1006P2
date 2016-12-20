package com.company.backgammon.ui.menus;

import com.company.backgammon.Logger;
import com.company.backgammon.ai.ExpectiMiniMaxAI;
import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.GameWithSpectator;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.network.NetworkClient;
import com.company.backgammon.ui.BackGammonGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

public class JoinGameMenu implements ActionListener {
    private JButton joinButton;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel mainPanel;
    private JRadioButton humanRadioButton;
    private JRadioButton AIRadioButton;
    private String selection;

    public JoinGameMenu() {
        joinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String hostname = textField1.getText();
                int port = Integer.parseInt(textField2.getText());
                createClient(hostname,port);
                ((JFrame)mainPanel.getTopLevelAncestor()).dispose();
            }
        });

        selection = "Human";
        humanRadioButton.setSelected(true);

        humanRadioButton.addActionListener(this);
        AIRadioButton.addActionListener(this);
    }

    private void createClient(String hostname, int port) {
        if(selection.equals("Human")) {
            SwingUtilities.invokeLater(() -> {
                try {
                    IPlayer player1 = new BackGammonGUI();
                    IPlayer player2 = new NetworkClient(new Socket(hostname, port));
                    Game game = new Game(player2, player1);
                    game.start();
                } catch (Exception e) {
                    Logger.log(this, "Something happened while creating the client. " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            });
        }
        else if(selection.equals("AI")) {
            SwingUtilities.invokeLater(() -> {
                try {
                    IPlayer player1 = new ExpectiMiniMaxAI(Evaluator.getBestEvaluator(),3);
                    IPlayer player2 = new NetworkClient(new Socket(hostname, port));
                    BackGammonGUI spectator = new BackGammonGUI();
                    Game game = new GameWithSpectator(player2, player1,spectator);
                    game.start();
                } catch (Exception e) {
                    Logger.log(this, "Something happened while creating the client. " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            });
        }
    }

    public static void initialize() {
        JFrame frame = new JFrame("JoinGameMenu");
        frame.setContentPane(new JoinGameMenu().mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setMinimumSize(new Dimension(200,200));
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if(actionEvent.getSource() == humanRadioButton) {
            selection = "Human";
        }
        else if(actionEvent.getSource() == AIRadioButton) {
            selection = "AI";
        }
    }
}
