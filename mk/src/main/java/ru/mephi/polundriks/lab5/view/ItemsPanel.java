package ru.mephi.polundriks.lab5.view;

import ru.mephi.polundriks.lab5.controller.GameController;
import ru.mephi.polundriks.lab5.model.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemsPanel extends JFrame {
    private GameController gameController;
    private JList<Item> itemsList;
    private DefaultListModel<Item> itemsListModel;
    private JButton useButton;
    private JButton closeButton;

    public ItemsPanel(GameController gameController, Runnable updateGuiCallback) {
        this.gameController = gameController;
        setTitle("Мешок предметов");
        setSize(300, 400);
        setLayout(new BorderLayout());

        itemsListModel = new DefaultListModel<>();
        itemsList = new JList<>(itemsListModel);
        itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(itemsList), BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        useButton = new JButton("Использовать");
        useButton.addActionListener(e -> {
            useSelectedItem(updateGuiCallback);
        });
        buttonsPanel.add(useButton);

        itemsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    useSelectedItem(updateGuiCallback);
                }
            }
        });

        closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> setVisible(false));
        buttonsPanel.add(closeButton);

        add(buttonsPanel, BorderLayout.SOUTH);
        updateItemsList();
    }

    public void updateItemsList() {
        itemsListModel.clear();
        for (Item item : gameController.getGameState().getPlayer().getItems()) {
            itemsListModel.addElement(item);
        }
    }

    private void useSelectedItem(Runnable updateGameInterface) {
        Item selectedItem = itemsList.getSelectedValue();
        if (selectedItem != null) {
            gameController.useItem(selectedItem);
            updateItemsList();
            updateGameInterface.run();
        }
    }
}
