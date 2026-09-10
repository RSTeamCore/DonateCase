package net.ritasister.dc.api.cases.service;

/**
 * Service interface for handling firework effects.
 */
public interface FireworkService<L> {

    /**
     * Launches a firework effect at the specified location.
     *
     * @param loc The location where the firework should be launched.
     */
    void launchFirework(L location);
}
