package ru.mephi.polundriks.lab5.view;

import ru.mephi.polundriks.lab5.controller.GameController;
import ru.mephi.polundriks.lab5.model.GameState;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private GameController gameController;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JLabel healthLabel;
    private JLabel attackLabel;

    public ScorePanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new GridLayout(4, 1));

        scoreLabel = new JLabel("Счет: 0");
        levelLabel = new JLabel("Уровень: 1");
        healthLabel = new JLabel("Здоровье: 100");
        attackLabel = new JLabel("Атака: 10");

        add(scoreLabel);
        add(levelLabel);
        add(healthLabel);
        add(attackLabel);
    }

    // Метод для обновления состояния панели
    public void update(GameState gameState) {
        scoreLabel.setText("Счет: " + gameState.getScore());
        levelLabel.setText("Уровень: " + gameState.getLevel());
        healthLabel.setText("Здоровье: " + gameState.getPlayer().getHealth());
        attackLabel.setText("Атака: " + gameState.getPlayer().getAttackDamage());
    }
}
