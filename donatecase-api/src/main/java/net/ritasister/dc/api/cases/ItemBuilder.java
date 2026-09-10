package net.ritasister.dc.api.cases;

import java.util.List;

/**
 * Interface for building items with specified properties.
 *
 * @param <T> the type of the item to be built
 * @param <M> the type of the material used for the item
 */
public interface ItemBuilder<T, M> {

    /**
     * Builds an item with the specified properties.
     *
     * @param material    the material of the item
     * @param amount      the amount of the item
     * @param data        additional data for the item
     * @param displayName the display name of the item
     * @param lore        the lore (description) of the item
     * @return the constructed item
     */
    T build(M material, int amount, int data, String displayName, List<String> lore);
}
