package TypingGame;

public class Enemy implements LevelObserver
{
    private static final int BASE_DAMAGE = 10;
    private String name;
    private int maxHealth;
    private int health;
    private String imageLocation;
    private int damage; // Variable damage for the enemy

    // Constructor - no health parameter, health is set to maxHealth
    public Enemy(String name, int maxHealth, String imageLocation) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth; // Initialize health to maxHealth
        this.imageLocation = imageLocation;
        this.damage = BASE_DAMAGE; // Initialize the damage value
    }

    // Method to take damage
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0; // Ensure health doesn't go below 0
        }
    }

    // Method to check if the enemy is defeated
    public boolean isDefeated() {
        return this.health <= 0;
    }

    // Method to get health as a percentage
    public double getHealthPercentage() {
        return (double) this.health / this.maxHealth;
    }

    // Implement the updateLevel method from TypingGame.LevelObserver
    @Override
    public void updateLevel(int level) {
        // Update the damage based on the level
        maxHealth *= (1 + level*0.2);
        System.out.println(name+ " Health: " + maxHealth);
        health = maxHealth;
    }

    // Method to set the enemy's damage
    private void setDamage(int newDamage) {
        this.damage = newDamage;
    }

    // Getter for image location
    public String getImageLocation() {
        return imageLocation;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for health
    public int getHealth() {
        return health;
    }

    // Getter for max health
    public int getMaxHealth() {
        return maxHealth;
    }

    // Getter for damage
    public int getDamage() {
        return damage;
    }
}
