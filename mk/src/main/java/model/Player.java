package model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@Setter
public class Player {
    private int health;
    private int maxHealth;
    private int attackDamage;
    private int level;
    private List<Item> items;
    private int weakenedTurns;
    private BufferedImage imageBuffer;

    public Player() {
        this.maxHealth = ConfigManager.getIntProperty("human", "initialHealth");
        this.health = maxHealth;
        this.attackDamage = ConfigManager.getIntProperty("human", "initialAttackDamage");
        String imagePath = ConfigManager.getStringProperty("human", "imagePath");
        try {
            imageBuffer = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void setHealth(int health) {
        if (health < 0) {
            log.info("Победа противника");
            this.health = 0;
        } else if (health > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health = health;
        }
    }

    public void spawnInNewLocation() {
        health = maxHealth;
        weakenedTurns = 0;
        items.clear();
    }
}
