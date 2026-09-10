package net.ritasister.dc.plugin;

import net.ritasister.dc.DonateCaseApiProvider;
import net.ritasister.dc.api.logging.PluginLogger;
import net.ritasister.dc.api.metadata.DonateCaseMetadata;
import net.ritasister.dc.api.platform.Platform;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Represent class for any platform.
 */
public interface DonateCasePlugin {

    /**
     * Gets the bootstrap plugin instance
     *
     * @return the bootstrap plugin
     */
    DonateCaseBootstrap getBootstrap();

    /**
     * Gets the platform type this instance of WorldGuardRegionProtect is running on.
     *
     * @return the platform type
     */
    Platform.Type getType();

    /**
     * Gets the {@link PluginLogger} own of plugin loggers.
     *
     * @return the logger server where plugin in running.
     */
    @NonNull
    PluginLogger getLogger();

    /**
     * Returns the class implementing the WorldGuardRegionProtect on this platform.
     *
     * @return the api
     */
    DonateCaseApiProvider getApiProvider();

    /**
     * Gets a wrapped metadata instance for the platform.
     *
     * @return the plugin's logger
     */
    DonateCaseMetadata getMetaData();

    /**
     * Attempts to identify the plugin behind the given classloader.
     *
     * <p>Used for giving more helpful log messages when things break.</p>
     *
     * @param classLoader the classloader to identify
     * @return the name of the classloader source
     * @throws Exception anything
     */
    default @Nullable String identifyClassLoader(ClassLoader classLoader) throws Exception {
        return null;
    }

}
