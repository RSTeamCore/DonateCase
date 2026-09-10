package net.ritasister.dc.api.cases.service;

import net.ritasister.dc.api.cases.CaseItem;
import net.ritasister.dc.api.cases.Cases;

/**
 * Service interface for broadcasting case win events.
 *
 * @param <P> Type representing a player
 * @param <C> Type representing a case
 */
public interface CaseBroadcastService<P, C extends Cases<?, ?>> {

    /**
     * Broadcasts a win event for a player who has won an item from a case.
     *
     * @param player The player who won the item.
     * @param cases  The case from which the item was won.
     * @param item   The item that was won.
     */
    void broadcastWin(P player, C cases, CaseItem<?> item);
}
