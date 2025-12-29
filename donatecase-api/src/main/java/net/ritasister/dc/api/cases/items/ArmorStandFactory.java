package net.ritasister.dc.api.cases.items;

/**
 * Factory interface for creating ArmorStand entities in a given world and location.
 */
public interface ArmorStandFactory<A, W, L> {

    /**
     * Spawns an ArmorStand in the specified world at the given location.
     *
     * @param world    the world where the ArmorStand will be spawned
     * @param location the location where the ArmorStand will be spawned
     * @return the spawned ArmorStand instance
     */
    A spawn(W world, L location);
}
