package net.ritasister.dc.plugin.loader;

import net.luckperms.api.LuckPerms;
import net.ritasister.dc.DonateCasePaperPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class LoadLuckPerms {

    private final DonateCasePaperPlugin donateCasePaperPlugin;

    private LuckPerms luckPermsAPI;

    public LoadLuckPerms(DonateCasePaperPlugin donateCasePaperPlugin) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
    }

    public void hookLuckPerms() {
        final Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("LuckPerms");
        RegisteredServiceProvider<LuckPerms> rsp = null;
        if (plugin != null && plugin.isEnabled()) {
            try {
                rsp = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                donateCasePaperPlugin.getLogger().info(String.format("Plugin: %s loaded successful!.", plugin.getName()));
            } catch (NullPointerException | ClassCastException | NoClassDefFoundError exception) {
                donateCasePaperPlugin.getLogger().severe(exception.getMessage());
            }
            if (rsp != null) {
                luckPermsAPI = rsp.getProvider();
            }
        }

    }

    public LuckPerms hookAPILuckPerms() {
        return luckPermsAPI;
    }
}
