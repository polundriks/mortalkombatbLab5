package view;

import lombok.extern.slf4j.Slf4j;
import controller.GameController;
import model.Enemy;
import model.GameState;
import model.Player;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class GamePanel extends JPanel {
    private GameController gameController;
    private Runnable updateScoreCallback;
    private JLabel locationLabel;
    private JLabel playerStats;
    private JLabel playerIconHolder;
    private JLabel enemyStats;
    private JLabel enemyIconHolder;
    private JButton attackButton;
    private JButton defendButton;
    private JButton weakenButton;
    private JButton skipButton;

    public GamePanel(GameController gameController, Runnable updateScoreCallback) {
        this.gameController = gameController;
        this.updateScoreCallback = updateScoreCallback;
        setLayout(new BorderLayout());
        locationLabel = new JLabel();
        playerStats = new JLabel();
        playerIconHolder = new JLabel();
        enemyStats = new JLabel();
        enemyIconHolder = new JLabel();

        JPanel statsPanel = new JPanel(new GridLayout(1, 2));
        this.add(locationLabel, BorderLayout.NORTH);
        statsPanel.add(playerStats);
        statsPanel.add(playerIconHolder);
        statsPanel.add(enemyIconHolder);
        statsPanel.add(enemyStats);

        JPanel actionsPanel = new JPanel();
        attackButton = new JButton("Атака");
        defendButton = new JButton("Защита");
        weakenButton = new JButton("Ослабить");
        skipButton = new JButton("Пропуск хода");

        actionsPanel.add(attackButton);
        actionsPanel.add(defendButton);
        actionsPanel.add(weakenButton);
        actionsPanel.add(skipButton);

        attackButton.addActionListener(e -> {
            gameController.playerAttack();
            update(gameController.getGameState());
        });

        defendButton.addActionListener(e -> {
            gameController.playerDefend();
            update(gameController.getGameState());
        });

        weakenButton.addActionListener(e -> {
            gameController.playerWeaken();
            update(gameController.getGameState());
        });

        skipButton.addActionListener(e -> {
            gameController.playerSkip();
            update(gameController.getGameState());
        });

        //add(fightPanel, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.CENTER);
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
                Очки: %d<br/>\
                </html>""",
            player.getLevel(),
            player.getHealth(),
            player.getAttackDamage(),
            gameState.getScore()
        ));

        playerIconHolder.setIcon(player.getImageBuffer() == null ? null : new ImageIcon(player.getImageBuffer()));

        if (enemy == null) {
            enemyStats.setText("Противник: нет");
            enemyIconHolder.setIcon(null);
            //enableActionButtons(false);
        } else {
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
            enemyIconHolder.setIcon(enemy.getImageBuffer() == null ? null : new ImageIcon(enemy.getImageBuffer()));
        }

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
        updateScoreCallback.run();
    }

    public void enableActionButtons(boolean enabled) {
        log.info("enableActionButtons {}", enabled);
        attackButton.setEnabled(enabled);
        defendButton.setEnabled(enabled);
        skipButton.setEnabled(enabled);
        weakenButton.setEnabled(enabled);
    }
}
