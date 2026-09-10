package net.ritasister.dc.api.cases;

/**
 * Interface for managing key storage for players.
 */
public interface KeyStorage {

    /**
     * Sets the number of keys for a specific player.
     *
     * @param player The player's identifier.
     * @param keys   The number of keys to set.
     */
    void setKeys(String player, int keys);

    /**
     * Retrieves the number of keys a specific player has.
     *
     * @param player The player's identifier.
     * @return The number of keys the player has.
     */
    int getKeys(String player);

    /**
     * Adds a specified number of keys to a player's total.
     *
     * @param player The player's identifier.
     * @param keys   The number of keys to add.
     */
    void addKeys(String player, int keys);

    /**
     * Removes a specified number of keys from a player's total.
     *
     * @param player The player's identifier.
     * @param keys   The number of keys to remove.
     */
    void removeKeys(String player, int keys);
}
