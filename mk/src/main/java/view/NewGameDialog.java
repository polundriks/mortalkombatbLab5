package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class NewGameDialog extends JDialog {
    private JTextField locationsField;

    public NewGameDialog(JFrame parent, GameController gameController) {
        super(parent, "Новая игра", true);
        setSize(250, 150);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Введите количество локаций:"));
        locationsField = new JTextField(10);
        inputPanel.add(locationsField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            int totalLocations = Integer.parseInt(locationsField.getText());
            gameController.startNewGame(totalLocations);
            dispose();
            showGameFrame(gameController);
        });

        add(inputPanel, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private void showGameFrame(GameController gameController) {
        GameFrame gameFrame = new GameFrame(gameController);
        gameFrame.updateView();
        gameFrame.setVisible(true);
    }
}