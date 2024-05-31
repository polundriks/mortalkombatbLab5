package view;

import controller.GameController;
import model.Record;
import model.RecordTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RecordsFrame extends JFrame {
    private GameController gameController;
    private JTable recordsTable;
    private String[] columnNames = {"Игрок", "Счет"};
    private Object[][] data;

    public RecordsFrame(GameController gameController) {
        this.gameController = gameController;
        setTitle("Таблица рекордов");
        setSize(400, 300);
        setLayout(new BorderLayout());
        RecordTable recordTable = gameController.getIoController().loadRecordTable();
        gameController.setRecordTable(recordTable);

        updateRecordsTable();
        recordsTable = new JTable(data, columnNames);
        add(new JScrollPane(recordsTable), BorderLayout.CENTER);
    }

    public void updateRecordsTable() {
        RecordTable recordTable = gameController.getRecordTable();
        data = new Object[recordTable.getRecords().size()][2];
        int row = 0;
        for (Record record : recordTable.getRecords()) {
            data[row][0] = record.getPlayerName();
            data[row][1] = record.getScore();
            row++;
        }
        if (recordsTable != null) {
            recordsTable.setModel(new DefaultTableModel(data, columnNames));
        }
    }
}
