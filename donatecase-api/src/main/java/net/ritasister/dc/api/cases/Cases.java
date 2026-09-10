package net.ritasister.dc.api.cases;

import java.util.List;

/**
 * Represents a case that can contain items, manage keys for players, and store locations.
 *
 * @param <I> The type of items contained in the case.
 * @param <L> The type of locations associated with the case.
 */
public interface Cases<I, L> {

    /**
     * Adds an item to the case.
     *
     * @param item The item to be added.
     */
    void addItem(I item);

    /**
     * Retrieves the list of items contained in the case.
     *
     * @return A list of case items.
     */
    List<CaseItem<?>> getItems();

    /**
     * Adds keys to a player's account for this case.
     *
     * @param player The name of the player.
     * @param keys   The number of keys to add.
     */
    void addKeys(String player, int keys);

    /**
     * Adds a location associated with this case.
     *
     * @param loc The location to be added.
     */
    void addLocation(L loc);

    /**
     * Retrieves the name of the case.
     *
     * @return The case name.
     */
    String getName();

    /**
     * Retrieves the title of the case.
     *
     * @return The case title.
     */
    String getTitle();

    /**
     * Sets the commands associated with this case.
     *
     * @param g A list of commands to set.
     */
    void setCommands(List<String> g);

    /**
     * Retrieves the commands associated with this case.
     *
     * @return A list of commands.
     */
    List<String> getCommands();
}
