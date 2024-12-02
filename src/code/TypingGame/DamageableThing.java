package TypingGame;

/**
 * Represents an abstract damageable entity with health and related properties.
 * This class provides a foundation for any object that can take damage, have health,
 * and check for defeat.
 *
 * @author Ben Henry
 * @version 1.0
 */
public abstract class DamageableThing
{
    public static final int MINIMUM_HEALTH = 0;

    private int maxHealth;
    private int health;

    /**
     * Constructs a DamageableThing with specified maximum health.
     *
     * @param maxHealth The maximum health of the entity.
     */
    public DamageableThing(int maxHealth)
    {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    /**
     * Reduces the health of the entity by a specified amount of damage.
     * Ensures health does not drop below the minimum threshold.
     *
     * @param damage The amount of damage to apply.
     */
    public void takeDamage(int damage)
    {
        this.health -= damage;
        if(this.health < MINIMUM_HEALTH)
        {
            this.health = MINIMUM_HEALTH;
        }
    }

    /**
     * Checks if the entity is defeated (health is at or below the minimum threshold).
     *
     * @return True if the entity is defeated, false otherwise.
     */
    public boolean isDefeated()
    {
        return this.health <= MINIMUM_HEALTH;
    }

    /**
     * Gets the current health as a percentage of the maximum health.
     *
     * @return The health percentage as a double.
     */
    public double getHealthPercentage()
    {
        return (double) this.health / this.maxHealth;
    }

    /**
     * Gets the current health of the entity.
     *
     * @return The current health value.
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * Gets the maximum health of the entity.
     *
     * @return The maximum health value.
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }

    /**
     * Sets a new maximum health and optionally restores health to the new max.
     *
     * @param maxHealth   The new maximum health.
     * @param resetHealth Whether to reset current health to the new max.
     */
    public void setMaxHealth(int maxHealth, boolean resetHealth)
    {
        this.maxHealth = maxHealth;
        if(resetHealth)
        {
            this.health = maxHealth;
        }
    }
}
