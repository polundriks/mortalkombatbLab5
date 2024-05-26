package ru.mephi.polundriks.lab5.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Player {
    private int health;
    private int maxHealth;
    private int attackDamage;
    private int experience;
    private int level;
    private List<Item> items;
    private int weakenedTurns;

    public Player() {
        this.maxHealth = ConfigManager.getIntProperty("human", "initialHealth");
        this.health = maxHealth;
        this.attackDamage = ConfigManager.getIntProperty("human", "initialAttackDamage");
        this.experience = 0;
        this.level = 1;
        this.items = new ArrayList<>();
    }

    public void increasePlayerDamage() {
        level++;
        attackDamage += ConfigManager.getIntProperty("human", "levelUpAttackDamageIncrement");
    }

    public void increasePlayerMaxHealth() {
        level++;
        maxHealth += ConfigManager.getIntProperty("human", "levelUpHealthIncrement");
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item selectedItem) {
        items.remove(selectedItem);
    }
}
