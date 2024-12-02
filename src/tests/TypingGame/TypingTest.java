package TypingGame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DamageableThingTest {

    @Test
    void playerHealthInitialization() {
        Player player = new Player(100, 10);

        assertEquals(100, player.getHealth(), "Player's initial health should be 100.");
        assertTrue(player.getHealthPercentage() == 1.0, "Player's health percentage should be 1.0 (100%).");
    }

    @Test
    void playerTakesDamage() {
        Player player = new Player(100, 10);

        player.takeDamage(30);
        assertEquals(70, player.getHealth(), "Player's health should be reduced to 70.");
        assertEquals(0.7, player.getHealthPercentage(), 0.01, "Player's health percentage should be 70%.");
    }

    @Test
    void playerHealthDoesNotGoBelowZero() {
        Player player = new Player(100, 10);

        player.takeDamage(150);
        assertEquals(0, player.getHealth(), "Player's health should not go below 0.");
        assertTrue(player.isDefeated(), "Player should be marked as defeated.");
    }

    @Test
    void playerHealthReset() {
        Player player = new Player(100, 10);

        player.takeDamage(50);
        player.resetHealth();
        assertEquals(100, player.getHealth(), "Player's health should reset to max.");
        assertTrue(player.getHealthPercentage() == 1.0, "Player's health percentage should reset to 100%.");
    }

    @Test
    void enemyHealthInitialization() {
        Enemy enemy = new Enemy("TestEnemy", 50, "path/to/image.png");

        assertEquals(50, enemy.getHealth(), "Enemy's initial health should be 50.");
        assertEquals(10, enemy.getDamage(), "Enemy's damage should be the base damage (10).");
    }

    @Test
    void enemyTakesDamage() {
        Enemy enemy = new Enemy("TestEnemy", 50, "path/to/image.png");

        enemy.takeDamage(20);
        assertEquals(30, enemy.getHealth(), "Enemy's health should be reduced to 30.");
        assertEquals(0.6, enemy.getHealthPercentage(), 0.01, "Enemy's health percentage should be 60%.");
    }

    @Test
    void enemyHealthDoesNotGoBelowZero() {
        Enemy enemy = new Enemy("TestEnemy", 50, "path/to/image.png");

        enemy.takeDamage(70);
        assertEquals(0, enemy.getHealth(), "Enemy's health should not go below 0.");
        assertTrue(enemy.isDefeated(), "Enemy should be marked as defeated.");
    }

    @Test
    void enemyUpdatesHealthOnLevelChange() {
        Enemy enemy = new Enemy("TestEnemy", 50, "path/to/image.png");

        enemy.updateLevel(2); // Assume level 2 increases health based on HEALTH_LEVEL_MODIFIER
        int expectedHealth = (int) (50 * (1 + 2 * Enemy.HEALTH_LEVEL_MODIFIER));
        assertEquals(expectedHealth, enemy.getMaxHealth(), "Enemy's max health should update with level.");
        assertEquals(expectedHealth, enemy.getHealth(), "Enemy's current health should reset to max.");
    }
}
