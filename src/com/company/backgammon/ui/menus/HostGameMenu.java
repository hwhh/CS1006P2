package com.company.backgammon.ui.menus;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.IPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostGameMenu implements ActionListener {
    private JTextField portEntry;
    private JButton hostGameButton;
    private JTextField hostName;
    private JPanel mainPanel;
    private JRadioButton humanRadioButton;
    private JRadioButton AIRadioButton;

    private String selection = "Human";

    public HostGameMenu() {
        hostGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                createServer();
            }
        });
        hostGameButton.setMnemonic(KeyEvent.VK_ENTER);
        try {
            hostName.setText(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            Logger.log(this,"Unable to determine host name");
        }

        humanRadioButton.setSelected(true);

        humanRadioButton.addActionListener(this);
        AIRadioButton.addActionListener(this);

    }

    public static void initialize() {
        JFrame frame = new JFrame("HostGameMenu");
        frame.setContentPane(new HostGameMenu().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(300,200));
        frame.pack();
        frame.setVisible(true);
    }

    public void createServer() {
        WaitingOnConnect.initialize(Integer.parseInt(portEntry.getText()),selection);
        ((JFrame)mainPanel.getTopLevelAncestor()).dispose();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == humanRadioButton) {
            selection = "Human";
        }
        else if (actionEvent.getSource() == AIRadioButton) {
            selection = "AI";
        }
    }
}
