package net.ritasister.dc.api;

import net.ritasister.dc.api.metadata.DonateCaseMetadata;
import net.ritasister.dc.api.platform.Platform;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * <p>The main interface for interacting with the DonateCase API.
 * Provides access to various managers and utilities for region interaction,
 * metadata retrieval, platform information, and more.</p>
 *
 * <p>For platforms without a Service Manager, this interface can be accessed through
 * the static singleton accessor in {@link DonateCaseProvider}.</p>
 */
public interface DonateCase {

    @NonNull
    DonateCaseMetadata getMetaData();

    /**
     * Provides the {@link Platform} the plugin is running on.
     * This includes details about the server platform type and its version.
     *
     * @return the platform information.
     */
    @NotNull
    Platform getPlatform();

}
