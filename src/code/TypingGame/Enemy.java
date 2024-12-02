package TypingGame;

/**
 * Represents an enemy in the Typing Game.
 * Each enemy has a name, an image, and inflicts damage on the player.
 *
 * @author Ben Henry
 * @version 1.0
 */
class Enemy extends DamageableThing implements LevelObserver
{
    private static final int INITIAL_HEALTH_MODIFIER = 1;
    private static final int BASE_DAMAGE = 10;

    static final double HEALTH_LEVEL_MODIFIER = 0.1;

    private final String name;
    private final String imageLocation;
    private final int damage;

    /**
     * Constructs an enemy with a name, maximum health, and an image location.
     *
     * @param name          The name of the enemy.
     * @param maxHealth     The initial maximum health of the enemy.
     * @param imageLocation The file path to the enemy's image.
     */
    Enemy(final String name, final int maxHealth, final String imageLocation)
    {
        super(maxHealth);
        this.name = name;
        this.imageLocation = imageLocation;
        this.damage = BASE_DAMAGE;
    }

    /**
     * Updates the enemy's maximum health based on the current game level.
     * Resets the health to the new maximum value.
     *
     * @param level The current game level.
     */
    @Override
    public void updateLevel(final int level)
    {
        int newMaxHealth = (int) (getMaxHealth() * (INITIAL_HEALTH_MODIFIER + level * HEALTH_LEVEL_MODIFIER));
        setMaxHealth(newMaxHealth, true); // Reset health to the new max
    }

    String getImageLocation()
    {
        return imageLocation;
    }

    String getName()
    {
        return name;
    }

    int getDamage()
    {
        return damage;
    }
}
