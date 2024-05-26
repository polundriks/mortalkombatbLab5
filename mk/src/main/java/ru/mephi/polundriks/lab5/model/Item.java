package ru.mephi.polundriks.lab5.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
    private ItemType type;
    private int effect;

    public Item(ItemType type) {
        this.type = type;
        switch (type) {
            case SMALL_HEALTH_POTION -> this.effect = ConfigManager.getIntProperty("item.smallHealthPotionEffect");
            case LARGE_HEALTH_POTION -> this.effect = ConfigManager.getIntProperty("item.largeHealthPotionEffect");
            case RESURRECTION_CROSS -> this.effect = ConfigManager.getIntProperty("item.resurrectionCrossEffect");
        }
    }
}
