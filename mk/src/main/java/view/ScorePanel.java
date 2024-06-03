package view;

import controller.GameController;
import model.GameState;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private final JLabel scoreLabel;
    private final JLabel levelLabel;
    private final JLabel healthLabel;
    private final JLabel attackLabel;

    public ScorePanel(GameController gameController) {
        setLayout(new GridLayout(4, 1));

        scoreLabel = new JLabel("Счет: 0", SwingConstants.CENTER);
        levelLabel = new JLabel("Уровень: 1", SwingConstants.CENTER);
        healthLabel = new JLabel("Здоровье: " + gameController.getGameState().getPlayer().getHealth(), SwingConstants.CENTER);
        attackLabel = new JLabel("Атака: " + gameController.getGameState().getPlayer().getAttackDamage(), SwingConstants.CENTER);

        add(scoreLabel);
        add(levelLabel);
        add(healthLabel);
        add(attackLabel);
    }

    public void updateView(GameState gameState) {
        scoreLabel.setText("Счет: " + gameState.getScore());
        levelLabel.setText("Уровень: " + gameState.getLevel());
        healthLabel.setText("Здоровье: " + gameState.getPlayer().getMaxHealth());
        attackLabel.setText("Атака: " + gameState.getPlayer().getAttackDamage());
    }
}
