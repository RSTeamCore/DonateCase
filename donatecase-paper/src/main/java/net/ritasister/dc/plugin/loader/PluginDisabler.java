package net.ritasister.dc.plugin.loader;

import net.ritasister.dc.DonateCasePaperPlugin;
import org.bukkit.Bukkit;

public class PluginDisabler {

    private final DonateCasePaperPlugin plugin;

    public PluginDisabler(DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
    }

    public void disableWithMessage(String reason) {
        if (plugin.getBootstrap().getLoader().isEnabled()) {
            plugin.getLogger().severe(String.format("Disabling plugin '%s' due to: %s", plugin.getBootstrap().getLoader().getName(), reason));
            Bukkit.getServer().getPluginManager().disablePlugin(plugin.getBootstrap().getLoader());
        } else {
            plugin.getLogger().warn(String.format("Attempted to disable plugin '%s', but it was already disabled.", plugin.getBootstrap().getLoader().getName()));
        }
    }

    public DonateCasePaperPlugin getPlugin() {
        return plugin;
    }
}
