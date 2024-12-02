package TypingGame;

/**
 * Represents the player in the Typing Game.
 * The player has health, takes damage, and can be defeated.
 *
 * @author Ben Henry
 * @version 1.0
 */
class Player extends DamageableThing
{
    private final int damageAmount;

    /**
     * Constructs a Player with specified initial health and damage amount.
     *
     * @param maxHealth    The maximum health of the player.
     * @param damageAmount The amount of damage the player can deal.
     */
    Player(final int maxHealth, final int damageAmount)
    {
        super(maxHealth);
        this.damageAmount = damageAmount;
    }

    /**
     * Gets the amount of damage the player deals.
     *
     * @return The player's damage amount.
     */
    int getDamageAmount()
    {
        return damageAmount;
    }

    /**
     * Resets the player's health to full.
     */
    void resetHealth()
    {
        setMaxHealth(getMaxHealth(), true);
    }
}
