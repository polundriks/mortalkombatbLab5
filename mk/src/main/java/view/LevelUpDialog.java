package view;

import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class LevelUpDialog extends JDialog {
    private JButton increaseDamageButton;
    private JButton increaseHealthButton;

    public LevelUpDialog(GameController gameController) {
        setModal(true);
        setTitle("Повышение уровня");
        setSize(300, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("Выберите характеристику для улучшения:");
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        int levelUpAttackDamageIncrement = gameController.getGameState().getPlayer().getLevelUpAttackDamageIncrement();
        increaseDamageButton = new JButton("Увеличить урон на " + levelUpAttackDamageIncrement);
        increaseDamageButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        int levelUpHealthIncrement = gameController.getGameState().getPlayer().getLevelUpHealthIncrement();
        increaseHealthButton = new JButton("Увеличить здоровье на " + levelUpHealthIncrement);
        increaseHealthButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        increaseDamageButton.addActionListener(e -> {
            gameController.increasePlayerDamage();
            setVisible(false);
        });

        increaseHealthButton.addActionListener(e -> {
            gameController.increasePlayerHealth();
            setVisible(false);
        });

        add(Box.createRigidArea(new Dimension(0, 10))); // Отступ сверху
        add(messageLabel);
        add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между элементами
        add(increaseDamageButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // Отступ между элементами
        add(increaseHealthButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // Отступ снизу
    }
}