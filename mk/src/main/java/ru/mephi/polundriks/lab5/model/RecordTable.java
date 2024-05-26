package ru.mephi.polundriks.lab5.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecordTable {
    private List<Record> records;

    public RecordTable() {
        records = new ArrayList<>();
    }

    public void addRecord(Record record) {
        records.add(record);
        records.sort((r1, r2) -> Integer.compare(r2.getScore(), r1.getScore())); // Сортировка по убыванию очков
        if (records.size() > 10) {
            records = records.subList(0, 10); // Оставляем только топ 10
        }
    }
}
