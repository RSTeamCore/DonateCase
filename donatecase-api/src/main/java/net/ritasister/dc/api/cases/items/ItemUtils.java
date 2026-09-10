package net.ritasister.dc.api.cases.items;

import net.ritasister.dc.api.cases.CaseItem;

import java.util.List;

/**
 * Utility interface for creating and managing case items.
 *
 * @param <M> The type representing the material or base item.
 * @param <C> The type of case item extending CaseItem.
 */
public interface ItemUtils<C extends CaseItem<?>, M, I> {

    I createItem(M ma, int amount, int data, String dn, final List<String> lore);

    /**
     * Selects a random item from the provided list of groups based on their chances.
     *
     * @param groups The list of item groups to select from.
     * @return A randomly selected item of type C.
     */
    C getRandomGroup(List<C> groups);
}
