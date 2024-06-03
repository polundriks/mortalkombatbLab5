package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class ActionsPanel extends JPanel {
    private GameController gameController;
    private GameFrame gameFrame;
    private JButton attackButton;
    private JButton defendButton;
    private JButton weakenButton;
    private JButton skipButton;
    private JButton itemsButton;

    public ActionsPanel(GameController gameController, GameFrame gameFrame) {
        this.gameController = gameController;
        this.gameFrame = gameFrame;
        setLayout(new FlowLayout());

        attackButton = new JButton("Атака");
        attackButton.addActionListener(e -> {
            gameController.playerAttack();
            gameFrame.updateView();
        });
        add(attackButton);

        defendButton = new JButton("Защита");
        defendButton.addActionListener(e -> {
            gameController.playerDefend();
            gameFrame.updateView();
        });
        add(defendButton);

        weakenButton = new JButton("Ослабить");
        weakenButton.addActionListener(e -> {
            gameController.playerWeaken();
            gameFrame.updateView();
        });
        add(weakenButton);

        skipButton = new JButton("Пропуск хода");
        skipButton.addActionListener(e -> {
            gameController.playerSkip();
            gameFrame.updateView();
        });
        add(skipButton);

        itemsButton = new JButton("Предметы");
        itemsButton.addActionListener(e -> gameFrame.showItemsPanel());
        add(itemsButton);

        updateView();
    }

    public void updateView() {
        attackButton.setEnabled(gameController.isPlayerTurn());
        defendButton.setEnabled(gameController.isPlayerTurn());
        weakenButton.setEnabled(gameController.isPlayerTurn());
        skipButton.setEnabled(gameController.isPlayerTurn());
        itemsButton.setEnabled(gameController.isPlayerTurn());
    }
}