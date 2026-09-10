package net.ritasister.dc.api;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serial;

import static org.jetbrains.annotations.ApiStatus.Internal;

/**
 * The DonateCase API.
 *
 * <p>Provider to get the DonateCase api instance.</p>
 */
public final class DonateCaseProvider {

    private static DonateCase instance = null;

    /**
     * Gets an instance of the {@link DonateCase} API,
     * throwing {@link IllegalStateException} if the API is not loaded yet.
     *
     * <p>This method will never return null.</p>
     *
     * @return an instance of the DonateCase API
     * @throws IllegalStateException if the API is not loaded yet
     */
    public static @NonNull DonateCase get() {
        final DonateCase instance = DonateCaseProvider.instance;
        if (instance == null) {
            throw new NotLoadedException();
        }
        return instance;
    }

    @Internal
    static void register(final DonateCase donateCase) {
        DonateCaseProvider.instance = donateCase;
    }

    @Internal
    static void unregister() {
        DonateCaseProvider.instance = null;
    }

    @Internal
    private DonateCaseProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Exception thrown when the API is requested before it has been loaded.
     */
    private static final class NotLoadedException extends IllegalStateException {

        @Serial
        private static final long serialVersionUID = 1L;

        private static final String MESSAGE = """
                The DonateCase API isn't loaded yet!
                This could be because:
                  a) the DonateCase plugin is not installed or it failed to enable
                  b) the plugin in the stacktrace does not declare a dependency on DonateCase
                  c) the plugin in the stacktrace is retrieving the API before the plugin 'enable' phase
                     (call the #get method in onEnable, not the constructor!)
                """;

        NotLoadedException() {
            super(MESSAGE);
        }

    }

}
