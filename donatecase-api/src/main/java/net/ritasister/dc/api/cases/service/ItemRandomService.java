package net.ritasister.dc.api.cases.service;

import java.util.List;

/**
 * Service for randomly picking an item from a list.
 *
 * @param <T> the type of items to pick from
 */
public interface ItemRandomService<T> {

    /**
     * Picks a random item from the provided list.
     *
     * @param items the list of items to pick from
     * @return a randomly selected item
     */
    T pick(List<T> items);
}
