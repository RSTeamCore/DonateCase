package net.ritasister.dc.api.config.provider;

/**
 * Generic interface for resource providers.
 *
 * @param <P> plugin type
 * @param <T> provided object type
 * @since 1.0.21
 */
public interface ResourceProvider<P, T> {

    /**
     * Initializes the provider with the given plugin instance.
     *
     * @param plugin plugin instance
     */
    void init(P plugin);

    /**
     * Retrieves the provided object.
     *
     * @return provided object
     */
    T get();
}
