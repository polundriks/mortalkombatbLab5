package ru.mephi.polundriks.lab5.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player {
    private int health;
    private int attackDamage;
    private int experience;
    private int level;
    private List<Item> items;

    public Player() {
        this.health = ConfigManager.getIntProperty("human", "initialHealth");
        this.attackDamage = ConfigManager.getIntProperty("human", "initialAttackDamage");
        this.experience = 0;
        this.level = 1;
        this.items = new ArrayList<>();
    }

    public void levelUp() {
        level++;
        health += ConfigManager.getIntProperty("human", "levelUpHealthIncrement");
        attackDamage += ConfigManager.getIntProperty("human", "levelUpAttackDamageIncrement");
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void useItem(Item item) {
        switch (item.getType()) {
            case SMALL_HEALTH_POTION:
                health += (health * item.getEffect()) / 100;
                break;
            case LARGE_HEALTH_POTION:
                health += (health * item.getEffect()) / 100;
                break;
            case RESURRECTION_CROSS:
                if (health <= 0) {
                    health = (health * item.getEffect()) / 100;
                }
                break;
        }
        items.remove(item);
    }
}
