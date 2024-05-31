package model;

import lombok.Getter;

@Getter
public enum ItemType {
    SMALL_HEALTH_POTION("Small health potion"),
    LARGE_HEALTH_POTION("Large health potion"),
    RESURRECTION_CROSS("Resurrection cross");

    private final String name;

    ItemType(String s) {
        name = s;
    }
}
