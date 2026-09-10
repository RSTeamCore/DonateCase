package net.ritasister.dc.api.cases.service;

import net.ritasister.dc.api.cases.CaseItem;
import net.ritasister.dc.api.cases.Cases;

/**
 * Service for applying rewards to players from cases.
 *
 * @param <P> Type representing a player
 * @param <C> Type representing a case
 */
public interface CaseRewardService<P, C extends Cases<?, ?>> {

    /**
     * Applies the reward to the player based on the case and item.
     *
     * @param player the player receiving the reward
     * @param cases  the case from which the reward is given
     * @param item   the item representing the reward
     */
    void applyReward(P player, C cases, CaseItem<?> item);
}
