package controller;

import model.RecordTable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import model.Record;

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
}
