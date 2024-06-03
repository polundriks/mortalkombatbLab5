package view;

import controller.GameController;
import lombok.extern.slf4j.Slf4j;
import model.GameState;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class GameFrame extends JFrame {
    private GameController gameController;
    private JLabel locationLabel;
    private PlayerPanel playerPanel;
    private EnemyPanel enemyPanel;
    private ActionsPanel actionsPanel;
    private ItemsPanel itemsPanel;
    private ScorePanel scorePanel;

    public GameFrame(GameController gameController) {
        this.gameController = gameController;
        setTitle("Игра");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        locationLabel = new JLabel();
        add(locationLabel, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(1, 2));

        playerPanel = new PlayerPanel(gameController);
        gamePanel.add(playerPanel);

        enemyPanel = new EnemyPanel(gameController);
        gamePanel.add(enemyPanel);

        add(gamePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        actionsPanel = new ActionsPanel(gameController, this);
        bottomPanel.add(actionsPanel, BorderLayout.NORTH);

        scorePanel = new ScorePanel(gameController);
        bottomPanel.add(scorePanel, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        itemsPanel = new ItemsPanel(gameController, this::updateView);
    }

    public void updateView() {
        locationLabel.setText(String.format("Локация %d из %d",
            gameController.getGameState().getCurrentLocation(),
            gameController.getGameState().getTotalLocations()));
        playerPanel.updateView();
        enemyPanel.updateView();
        actionsPanel.updateView();
        scorePanel.updateView(gameController.getGameState());
        checkIsGameOver();
    }

    public void showItemsPanel() {
        itemsPanel.updateItemsList();
        itemsPanel.setVisible(true);
    }

    private void checkIsGameOver() {
        GameState gameState = gameController.getGameState();
        if (gameState.isGameOver()) {
            log.info("Game over");
            String playerName = JOptionPane.showInputDialog(this, "Введите ваше имя:");
            if (playerName != null && !playerName.trim().isEmpty()) {
                gameController.addRecord(playerName, gameState.getScore());
            }
            JOptionPane.showMessageDialog(this,
                "Игра окончена! Вы набрали %d очков.".formatted(gameState.getScore()));
            this.setVisible(false);
        }
    }
}