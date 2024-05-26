package ru.mephi.polundriks.lab5.view;

import ru.mephi.polundriks.lab5.controller.GameController;
import ru.mephi.polundriks.lab5.model.Enemy;
import ru.mephi.polundriks.lab5.model.GameState;
import ru.mephi.polundriks.lab5.model.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameController gameController;
    private JLabel locationLabel;
    private JLabel playerStats;
    private JLabel enemyStats;
    private JButton attackButton;
    private JButton defendButton;
    private JButton skipButton;

    public GamePanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new BorderLayout());
        locationLabel = new JLabel();
        playerStats = new JLabel();
        enemyStats = new JLabel();

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.add(playerStats);
        statsPanel.add(enemyStats);

        JPanel actionsPanel = new JPanel();
        attackButton = new JButton("Атака");
        defendButton = new JButton("Защита");
        skipButton = new JButton("Пропуск хода");

        actionsPanel.add(attackButton);
        actionsPanel.add(defendButton);
        actionsPanel.add(skipButton);

        attackButton.addActionListener(e -> {
            gameController.playerAttack();
            update(gameController.getGameState());
        });

        defendButton.addActionListener(e -> {
            gameController.playerDefend();
            update(gameController.getGameState());
        });

        skipButton.addActionListener(e -> {
            gameController.playerSkip();
            update(gameController.getGameState());
        });

        add(statsPanel, BorderLayout.NORTH);
        add(actionsPanel, BorderLayout.SOUTH);

        update(gameController.getGameState());
    }

    public void update(GameState gameState) {
        Player player = gameState.getPlayer();
        Enemy enemy = gameState.getCurrentEnemy();
        locationLabel.setText(String.format("Локация %d из %d",
            gameState.getCurrentLocation(), gameState.getTotalLocations()));
        playerStats.setText(String.format(
            """
                <html>Игрок:<br/>\
                Уровень: %d<br/>\
                Здоровье: %d<br/>\
                Урон: %d<br/>\
                Опыт: %d<br/>\
                Очки: %d<br/>\
                Следующий уровень через: %d опыта<br/>\
                </html>""",
            player.getLevel(),
            player.getHealth(),
            player.getAttackDamage(),
            player.getExperience(),
            gameState.getScore(),
            (player.getLevel() * 100) - player.getExperience() // требуется 100 опыта для каждого уровня
        ));

        enemyStats.setText(String.format(
            """
                <html>Противник:<br/>\
                Тип: %s (%s)<br/>\
                Здоровье: %d<br/>\
                Урон: %d<br/>\
                </html>""",
            enemy.getType().name(),
            enemy.getType().getType().getStr(),
            enemy.getHealth(),
            enemy.getAttackDamage()
        ));

        if (gameState.isGameOver()) {
            String playerName = JOptionPane.showInputDialog(this, "Введите ваше имя:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                gameController.addRecord(playerName, gameState.getScore());
            }
            JOptionPane.showMessageDialog(this, "Игра окончена! Вы набрали " + gameState.getScore() + " очков.");
            attackButton.setEnabled(false);
            defendButton.setEnabled(false);
            skipButton.setEnabled(false);
        }
    }

    public void enableActionButtons(boolean enabled) {
        attackButton.setEnabled(enabled);
        defendButton.setEnabled(enabled);
        skipButton.setEnabled(enabled);
    }
}
