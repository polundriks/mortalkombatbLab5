package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private GameController gameController;
    private JPanel mainPanel;
    private JButton startGameButton;
    private JButton showRecordsButton;
    private LevelUpDialog levelUpDialog;

    public MainFrame() {
        gameController = new GameController(() -> levelUpDialog.setVisible(true));
        levelUpDialog = new LevelUpDialog(gameController);

        setTitle("Игра");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        startGameButton = new JButton("Начать игру");
        startGameButton.addActionListener(e -> showNewGameDialog());
        mainPanel.add(startGameButton, gbc);

        showRecordsButton = new JButton("Посмотреть результаты");
        showRecordsButton.addActionListener(e -> showRecordsFrame());
        mainPanel.add(showRecordsButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void showNewGameDialog() {
        NewGameDialog newGameDialog = new NewGameDialog(this, gameController);
        newGameDialog.setVisible(true);
    }

    private void showRecordsFrame() {
        RecordsFrame recordsFrame = new RecordsFrame(gameController);
        recordsFrame.setVisible(true);
    }
}