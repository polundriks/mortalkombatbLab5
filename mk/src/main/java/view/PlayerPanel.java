package view;

import controller.GameController;
import model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PlayerPanel extends JPanel {
    private GameController gameController;
    private JLabel playerImageLabel;
    private JPanel infoPanel;
    private JLabel playerLevelLabel;
    private JLabel playerHealthLabel;
    private JLabel playerDamageLabel;
    private JLabel playerScoreLabel;

    public PlayerPanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        setBorder(border);

        playerImageLabel = new JLabel();
        playerImageLabel.setPreferredSize(new Dimension(400, 300));
        playerImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(playerImageLabel, BorderLayout.CENTER);

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        playerLevelLabel = new JLabel();
        playerLevelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(playerLevelLabel);

        playerHealthLabel = new JLabel();
        playerHealthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(playerHealthLabel);

        playerDamageLabel = new JLabel();
        playerDamageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(playerDamageLabel);

        playerScoreLabel = new JLabel();
        playerScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(playerScoreLabel);

        add(infoPanel, BorderLayout.SOUTH);
        updateView();
    }

    public void updateView() {
        Player player = gameController.getGameState().getPlayer();
        ImageIcon imageIcon = new ImageIcon(player.getImageBuffer());
        playerImageLabel.setIcon(imageIcon);
        playerLevelLabel.setText("Уровень: " + player.getLevel());
        playerHealthLabel.setText("Здоровье: " + player.getHealth());
        playerDamageLabel.setText("Урон: " + player.getAttackDamage());
        playerScoreLabel.setText("Очки: " + gameController.getGameState().getScore());
    }
}