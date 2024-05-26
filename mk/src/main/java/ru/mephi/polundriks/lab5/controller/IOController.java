package ru.mephi.polundriks.lab5.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.mephi.polundriks.lab5.model.Record;
import ru.mephi.polundriks.lab5.model.RecordTable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class IOController {
    private static final String FILE_NAME = "results.xlsx";

    public void saveRecordTable(RecordTable recordTable) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook()) {
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
        List<Record> records = new ArrayList<>();

        try (InputStream fis = getClass().getResourceAsStream(FILE_NAME);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                String name = row.getCell(0).getStringCellValue();
                int score = (int) row.getCell(1).getNumericCellValue();
                records.add(new Record(name, score));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        recordTable.setRecords(records);
        return recordTable;
    }
}
