package ru.mephi.polundriks.lab5.view;

import ru.mephi.polundriks.lab5.controller.GameController;
import ru.mephi.polundriks.lab5.model.GameState;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private final JLabel scoreLabel;
    private final JLabel levelLabel;
    private final JLabel healthLabel;
    private final JLabel attackLabel;

    public ScorePanel(GameController gameController) {
        setLayout(new GridLayout(4, 1));

        scoreLabel = new JLabel("Счет: 0");
        levelLabel = new JLabel("Уровень: 1");
        healthLabel = new JLabel("Здоровье: " + gameController.getGameState().getPlayer().getHealth());
        attackLabel = new JLabel("Атака: " + gameController.getGameState().getPlayer().getAttackDamage());

        add(scoreLabel);
        add(levelLabel);
        add(healthLabel);
        add(attackLabel);
    }

    public void update(GameState gameState) {
        scoreLabel.setText("Счет: " + gameState.getScore());
        levelLabel.setText("Уровень: " + gameState.getLevel());
        healthLabel.setText("Здоровье: " + gameState.getPlayer().getHealth());
        attackLabel.setText("Атака: " + gameState.getPlayer().getAttackDamage());
    }
}
