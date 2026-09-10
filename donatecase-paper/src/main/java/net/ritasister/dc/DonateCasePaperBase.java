package net.ritasister.dc;

import org.bukkit.plugin.java.JavaPlugin;

public final class DonateCasePaperBase extends JavaPlugin {

    private final DonateCasePaperPlugin plugin;

    public DonateCasePaperBase() {
        this.plugin = new DonateCasePaperPlugin(this);
    }

    @Override
    public void onEnable() {
        this.plugin.onEnable();
    }

    @Override
    public void onDisable() {
        this.plugin.onDisable();
    }

}
