package net.ritasister.dc.api.cases.service;

import net.ritasister.dc.api.cases.Cases;

/**
 * Service for managing the state of cases for players at specific locations.
 *
 * @param <P> the type representing a player
 * @param <L> the type representing a location
 * @param <C> the type representing a case
 */
public interface CaseStateService<P, L, C extends Cases<?, L>> {

    /**
     * Marks a case as opened for a player at a specific location.
     *
     * @param player   the player who opened the case
     * @param location the location of the case
     * @param cases    the case that was opened
     */
    void markOpened(P player, L location, C cases);

    /**
     * Clears the opened state of a case for a player at a specific location.
     *
     * @param player   the player whose case state is to be cleared
     * @param location the location of the case
     */
    void clear(P player, L location);
}
