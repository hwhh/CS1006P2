package com.company.backgammon.ui.menus;

import com.company.backgammon.Logger;
import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.ai.ExpectiMiniMaxAI;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.GameWithSpectator;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.network.NetworkServer;
import com.company.backgammon.ui.BackGammonGUI;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WaitingOnConnect {
    private final String selection;
    private JTextField textField1;
    private JTextField textField2;
    private JPanel mainPanel;

    WaitingOnConnect(int portNumber, String selection) {
        this.selection = selection;
        try {
            textField1.setText(InetAddress.getLocalHost().getHostName());
            textField2.setText(String.valueOf(portNumber));
            ConnectionListener listener = new ConnectionListener(portNumber);
            listener.start();
        } catch (IOException e) {
            Logger.error(e,"I/O error while setting up connection listener.");
        }
    }

    public static void initialize(int portNumber, String selection) {
        JFrame frame = new JFrame("WaitingOnConnect");
        frame.setContentPane(new WaitingOnConnect(portNumber,selection).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void startGame(Socket socket) {
        ((JFrame) mainPanel.getTopLevelAncestor()).dispose();
        if(selection.equals("Human")) {
            SwingUtilities.invokeLater(() -> {
                try {
                    IPlayer player1 = new BackGammonGUI();
                    NetworkServer player2 = new NetworkServer(socket);
                    Game game = new Game(player1, player2);
                    player2.start();
                } catch (Exception e) {
                    Logger.error(this, "Something happened while creating the server. " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
        else if(selection.equals("AI")) {
            SwingUtilities.invokeLater(() -> {
                try {
                    IPlayer player1 = new ExpectiMiniMaxAI(Evaluator.getBestEvaluator(),3);
                    NetworkServer player2 = new NetworkServer(socket);
                    BackGammonGUI spectator = new BackGammonGUI();
                    Game game = new GameWithSpectator(player1, player2,spectator);
                    player2.start();
                } catch (Exception e) {
                    Logger.error(this, "Something happened while creating the client. " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    class ConnectionListener extends Thread {
        ServerSocket server;
        ConnectionListener(int port) throws IOException {
            server = new ServerSocket(port);
        }

        @Override
        public void run() {
            try {
                Socket client = server.accept();
                startGame(client);
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
