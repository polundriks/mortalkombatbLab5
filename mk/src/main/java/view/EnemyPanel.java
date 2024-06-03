package view;

import controller.GameController;
import model.Enemy;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class EnemyPanel extends JPanel {
    private GameController gameController;
    private JLabel enemyImageLabel;
    private JPanel infoPanel;
    private JLabel enemyTypeLabel;
    private JLabel enemyHealthLabel;
    private JLabel enemyDamageLabel;

    public EnemyPanel(GameController gameController) {
        this.gameController = gameController;
        setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK, 2);
        setBorder(border);

        enemyImageLabel = new JLabel();
        enemyImageLabel.setPreferredSize(new Dimension(400, 300));
        enemyImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(enemyImageLabel, BorderLayout.CENTER);

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        enemyTypeLabel = new JLabel();
        enemyTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(enemyTypeLabel);

        enemyHealthLabel = new JLabel();
        enemyHealthLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(enemyHealthLabel);

        enemyDamageLabel = new JLabel();
        enemyDamageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(enemyDamageLabel);

        add(infoPanel, BorderLayout.SOUTH);
        updateView();
    }

    public void updateView() {
        Enemy enemy = gameController.getGameState().getCurrentEnemy();
        ImageIcon imageIcon = new ImageIcon(enemy.getImageBuffer());
        enemyImageLabel.setIcon(imageIcon);
        enemyTypeLabel.setText("Тип: %s (%s)".formatted(enemy.getType().name(), enemy.getType().getType().getStr()));
        enemyHealthLabel.setText("Здоровье: " + enemy.getHealth());
        enemyDamageLabel.setText("Урон: " + enemy.getAttackDamage());
    }
}