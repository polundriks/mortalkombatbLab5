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
        setSize(200, 100);
        setLayout(new FlowLayout());

        increaseDamageButton = new JButton("Увеличить урон");
        increaseHealthButton = new JButton("Увеличить здоровье");

        increaseDamageButton.addActionListener(e -> {
            gameController.increasePlayerDamage();
            setVisible(false);
        });

        increaseHealthButton.addActionListener(e -> {
            gameController.increasePlayerHealth();
            setVisible(false);
        });

        add(new JLabel("Выберите характеристику для улучшения:"));
        add(increaseDamageButton);
        add(increaseHealthButton);
    }
}