package net.ritasister.dc.handler;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.handler.Handler;
import net.ritasister.dc.listener.EventsListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class ListenerHandler implements Handler<PluginManager> {

    private final DonateCasePaperPlugin donateCasePaperPlugin;

    public ListenerHandler(final DonateCasePaperPlugin donateCasePaperPlugin) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
    }

    @Override
    public void handle(final @NotNull PluginManager pluginManager) {
        final List<Listener> allListeners = getListeners(pluginManager);

        this.donateCasePaperPlugin.getLogger().info("Finished registering listeners.");
        this.donateCasePaperPlugin.getLogger().info(String.format("All listeners registered successfully! List of Listeners: %s",
                allListeners.stream()
                        .map(listener -> listener.getClass().getSimpleName())
                        .collect(Collectors.joining(", "))));
    }

    private @NonNull List<Listener> getListeners(@NonNull PluginManager pluginManager) {
        final List<Listener> allListeners = List.of(
                new EventsListener(this.donateCasePaperPlugin)
        );

        allListeners.forEach(listener -> {
            this.donateCasePaperPlugin.getLogger().info("Registered listener: " + listener.getClass().getSimpleName());
            this.donateCasePaperPlugin.getListenerHandlerMap().put(listener.getClass(), listener);
            pluginManager.registerEvents(listener, this.donateCasePaperPlugin.getBootstrap().getLoader());
        });
        return allListeners;
    }

}
