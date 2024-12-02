package TypingGame;

public class Enemy extends LevelObserver {
    private static final int BASE_DAMAGE = 10;
    private String name;
    private int maxHealth;
    private int health;
    private String imageLocation;
    private int damage;

    public Enemy(String name, int maxHealth, String imageLocation) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.imageLocation = imageLocation;
        this.damage = BASE_DAMAGE;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isDefeated() {
        return this.health <= 0;
    }

    public double getHealthPercentage() {
        return (double) this.health / this.maxHealth;
    }

    @Override
    public void updateLevel(int level) {
        maxHealth *= (1 + level * 0.1);
        health = maxHealth;  // Reset health to max whenever level is updated
    }

    // Reset health after each encounter
    public void resetHealth() {
        this.health = maxHealth;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDamage() {
        return damage;
    }
}
