package view;

import controller.GameController;

import javax.swing.*;

public class MainFrame extends JFrame {
    private GameController gameController;
    private ItemsPanel itemsPanel;
    private RecordsFrame recordsFrame;
    private GamePanel gamePanel;
    private ScorePanel scorePanel;
    private LocationsInputDialog locationsInputDialog;
    private LevelUpDialog levelUpDialog;

    public MainFrame() {
        gameController = new GameController(
            () -> levelUpDialog.setVisible(true));
        levelUpDialog = new LevelUpDialog(gameController);

        itemsPanel = new ItemsPanel(gameController, () -> gamePanel.update(gameController.getGameState()));
        recordsFrame = new RecordsFrame(gameController);
        scorePanel = new ScorePanel(gameController);
        gamePanel = new GamePanel(gameController, () -> scorePanel.update(gameController.getGameState()));
        locationsInputDialog = new LocationsInputDialog(gameController);

        setTitle("Игра");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton startGameButton = new JButton("Начать игру");
        startGameButton.setBounds(50, 50, 200, 30);
        startGameButton.addActionListener(e -> {
            locationsInputDialog.setVisible(true);
            //gamePanel.update(gameController.getGameState());
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

       
        gamePanel.setBounds(300, 50, 450, 300);
        add(gamePanel);

        scorePanel.setBounds(300, 370, 450, 150);
        add(scorePanel);
    }

    private void resetGameInterface() {
        gamePanel.enableActionButtons(true);

        gamePanel.update(gameController.getGameState());
        scorePanel.update(gameController.getGameState());
    }
}
