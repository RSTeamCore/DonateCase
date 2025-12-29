package net.ritasister.dc.api.cases;

/**
 * An interface for managing player keys for cases.
 */
interface Keyable {

    /**
     * Sets the number of keys a player has.
     *
     * @param player The player's identifier.
     * @param keys   The number of keys to set.
     */
    void setKeys(String player, int keys);

    /**
     * Gets the number of keys a player has.
     *
     * @param player The player's identifier.
     * @return The number of keys the player has.
     */
    int getKeys(String player);
}
