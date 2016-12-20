package com.company.backgammon.ui.menus;

import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.GameAITest;
import com.company.backgammon.ui.BackGammonGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu {
    private JButton localStartButton;
    private JButton hostAGameButton;
    private JButton joinAGameButton;
    private JPanel mainPanel;

    public MainMenu() {
        localStartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LocalGameMenu.initialize();
                ((JFrame)mainPanel.getTopLevelAncestor()).dispose();
            }
        });
        hostAGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                HostGameMenu.initialize();
                ((JFrame)mainPanel.getTopLevelAncestor()).dispose();
            }
        });
        joinAGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JoinGameMenu.initialize();
                ((JFrame)mainPanel.getTopLevelAncestor()).dispose();
            }
        });
    }

    public static void initialize() {
        JFrame frame = new JFrame("MainMenu");
        frame.setContentPane(new MainMenu().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(500,450));
        frame.pack();
        frame.setVisible(true);
    }
}
