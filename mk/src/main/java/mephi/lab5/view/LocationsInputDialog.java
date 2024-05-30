package mephi.lab5.view;

import mephi.lab5.controller.GameController;

import javax.swing.*;
import java.awt.*;

public class LocationsInputDialog extends JDialog {
    private JTextField locationsField;
    private JButton okButton;

    public LocationsInputDialog(GameController gameController) {
        setModal(true);
        setTitle("Новая игра");
        setSize(250, 150);
        setLayout(new FlowLayout());

        locationsField = new JTextField(10);
        okButton = new JButton("OK");

        okButton.addActionListener(e -> {
            int totalLocations = Integer.parseInt(locationsField.getText());
            gameController.startNewGame(totalLocations);
            setVisible(false);
        });

        add(new JLabel("Введите количество локаций:"));
        add(locationsField);
        add(okButton);
    }
}