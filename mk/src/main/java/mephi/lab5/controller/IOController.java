package mephi.lab5.controller;

import mephi.lab5.model.RecordTable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import mephi.lab5.model.Record;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class IOController {
    private static final String FILE_NAME = "results.xlsx";

    public void saveRecordTable(RecordTable recordTable) {
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(FILE_NAME)) {

            Sheet sheet = workbook.createSheet("Results");

            int rowCount = 0;
            for (Record record : recordTable.getRecords()) {
                Row row = sheet.createRow(rowCount++);
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(record.getPlayerName());
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(record.getScore());
            }

            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RecordTable loadRecordTable() {
        RecordTable recordTable = new RecordTable();

        try (Workbook workbook = new XSSFWorkbook(new FileInputStream(FILE_NAME))) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();
                Cell nameCell = row.getCell(0);
                Cell scoreCell = row.getCell(1);

                if (nameCell != null && scoreCell != null) {
                    String playerName = nameCell.getStringCellValue();
                    int score = (int) scoreCell.getNumericCellValue();
                    recordTable.addRecord(new Record(playerName, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recordTable;
    }

    // test
    public static void main(String[] args) {
        // test
        IOController ioController = new IOController();
        RecordTable recordTable = new RecordTable();
        recordTable.addRecord(new Record("Player1", 100));
        recordTable.addRecord(new Record("Player2", 200));
        recordTable.addRecord(new Record("Player3", 300));
        ioController.saveRecordTable(recordTable);
        RecordTable loadedRecordTable = ioController.loadRecordTable();
        System.out.println(loadedRecordTable.getRecords());
    }
}
