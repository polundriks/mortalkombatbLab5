package mephi.lab5.model;

import lombok.Getter;

@Getter
public enum ItemType {
    SMALL_HEALTH_POTION("Малое зелье лечение"),
    LARGE_HEALTH_POTION("Большое зелье лечение"),
    RESURRECTION_CROSS("Крест воскрешения");

    private final String name;

    ItemType(String s) {
        name = s;
    }
}
