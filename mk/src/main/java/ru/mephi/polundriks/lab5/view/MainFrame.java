package ru.mephi.polundriks.lab5.view;

import ru.mephi.polundriks.lab5.controller.GameController;

import javax.swing.*;

public class MainFrame extends JFrame {
    private GameController gameController;
    private ItemsPanel itemsPanel;
    private RecordsFrame recordsFrame;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;

    public MainFrame() {
        gameController = new GameController();
        itemsPanel = new ItemsPanel(gameController);
        recordsFrame = new RecordsFrame(gameController);
        gamePanel = new GamePanel(gameController);
        scorePanel = new ScorePanel(gameController);

        setTitle("Игра");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton startGameButton = new JButton("Начать игру");
        startGameButton.setBounds(50, 50, 200, 30);
        startGameButton.addActionListener(e -> {
            gameController.startNewGame();
            gamePanel.update(gameController.getGameState());
            scorePanel.update(gameController.getGameState());
            resetGameInterface();
        });
        add(startGameButton);

        JButton showRecordsButton = new JButton("Посмотреть результаты");
        showRecordsButton.setBounds(50, 100, 200, 30);
        showRecordsButton.addActionListener(e -> {
            recordsFrame.updateRecordsTable();
            recordsFrame.setVisible(true);
        });
        add(showRecordsButton);

        JButton itemsButton = new JButton("Предметы");
        itemsButton.setBounds(50, 150, 200, 30);
        itemsButton.addActionListener(e -> {
            itemsPanel.updateItemsList();
            itemsPanel.setVisible(true);
        });
        add(itemsButton);

        // Добавить игровые панели
        gamePanel.setBounds(300, 50, 450, 300);
        add(gamePanel);

        scorePanel.setBounds(300, 370, 450, 150);
        add(scorePanel);
    }

    private void resetGameInterface() {
        // Очистить журнал игры
        gamePanel.clearGameLog();

        // Включить кнопки действий
        gamePanel.enableActionButtons(true);

        // Обновить отображение
        gamePanel.update(gameController.getGameState());
        scorePanel.update(gameController.getGameState());
    }
}
