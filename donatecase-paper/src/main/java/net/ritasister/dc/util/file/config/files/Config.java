package net.ritasister.dc.util.file.config.files;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.plugin.DonateCaseBootstrap;
import net.ritasister.dc.util.file.config.field.ConfigFields;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Config implements net.ritasister.dc.api.config.Config {

    private final DonateCasePaperPlugin donateCasePaperPlugin;

    private Map<String, List<String>> regionProtect;
    private Map<String, List<String>> playerRegionProtect;
    private Map<String, List<String>> regionProtectAllow;
    private Map<String, List<String>> regionProtectOnlyBreakAllow;

    public Config(DonateCasePaperPlugin donateCasePaperPlugin) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
        this.reloadConfig();
    }

    public void reloadConfig() {
        regionProtect = new HashMap<>();
        playerRegionProtect = new HashMap<>();
        regionProtectAllow = new HashMap<>();
        regionProtectOnlyBreakAllow = new HashMap<>();

        donateCasePaperPlugin.getBootstrap().getLoader().saveDefaultConfig();
        donateCasePaperPlugin.getBootstrap().getLoader().reloadConfig();

        for (ConfigFields field : ConfigFields.values()) {
            try {
                final Object value = switch (field.getFieldType()) {
                    case STRING -> field.asString(donateCasePaperPlugin);
                    case BOOLEAN -> field.asBoolean(donateCasePaperPlugin);
                    case INTEGER -> field.asInt(donateCasePaperPlugin);
                    case DOUBLE -> field.asDouble(donateCasePaperPlugin);
                    case LONG -> field.asLong(donateCasePaperPlugin);
                    case FLOAT -> field.asFloat(donateCasePaperPlugin);
                    case STRING_LIST -> field.asStringList(donateCasePaperPlugin);
                    case INTEGER_LIST -> field.asIntegerList(donateCasePaperPlugin);
                    case DOUBLE_LIST -> field.asDoubleList(donateCasePaperPlugin);
                    case LONG_LIST -> field.asLongList(donateCasePaperPlugin);
                    case FLOAT_LIST -> field.asFloatList(donateCasePaperPlugin);
                    case BOOLEAN_LIST -> field.asBooleanList(donateCasePaperPlugin);
                };
                saveConfig(field.getPath(), value);
            } catch (Exception e) {
                donateCasePaperPlugin.getLogger().severe("Could not load config.yml for " + field.name() + "! Error: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
        this.loadRegionProtectConfig();
    }

    public void loadRegionProtectConfig() {
        final Configuration configFile = wgrpPaperBase.getBootstrap().getLoader().getConfig();

        for (World w : Bukkit.getWorlds()) {
            regionProtect.putIfAbsent(w.getName(), new ArrayList<>());
            playerRegionProtect.putIfAbsent(w.getName(), new ArrayList<>());
            regionProtectAllow.putIfAbsent(w.getName(), new ArrayList<>());
            regionProtectOnlyBreakAllow.putIfAbsent(w.getName(), new ArrayList<>());
        }

        this.logRegionProtectConfig();
    }

    public void saveRegionProtectConfig() {
        donateCasePaperPlugin.getBootstrap().getLoader().saveConfig();
        this.logRegionProtectConfig();
    }

    public void saveConfig(final String path, final Object field) {
        try {
            wgrpPaperBase.getBootstrap().getLoader().getConfig().set(path, field);
            wgrpPaperBase.getBootstrap().getLoader().saveConfig();
        } catch (Exception e) {
            wgrpPaperBase.getLogger().severe("Could not save config.yml! Error: " + e.getMessage());
            e.fillInStackTrace();
        }
    }

    public void saveConfigFiles() {
        wgrpPaperBase.getLogger().info("Saving all configuration files before the plugin shuts down...");
        for (ConfigFields field : ConfigFields.values()) {
            try {
                final Object value = switch (field.getFieldType()) {
                    case STRING -> field.asString(wgrpPaperBase);
                    case BOOLEAN -> field.asBoolean(wgrpPaperBase);
                    case INTEGER -> field.asInt(wgrpPaperBase);
                    case DOUBLE -> field.asDouble(wgrpPaperBase);
                    case LONG -> field.asLong(wgrpPaperBase);
                    case FLOAT -> field.asFloat(wgrpPaperBase);
                    case STRING_LIST -> field.asStringList(wgrpPaperBase);
                    case INTEGER_LIST -> field.asIntegerList(wgrpPaperBase);
                    case DOUBLE_LIST -> field.asDoubleList(wgrpPaperBase);
                    case LONG_LIST -> field.asLongList(wgrpPaperBase);
                    case FLOAT_LIST -> field.asFloatList(wgrpPaperBase);
                    case BOOLEAN_LIST -> field.asBooleanList(wgrpPaperBase);
                };

                this.saveConfig(field.getPath(), value);
                wgrpPaperBase.getLogger().info(String.format("Checking and saving field: %s = %s", field.name(), value));

            } catch (Exception e) {
                wgrpPaperBase.getLogger().severe("Could not save config.yml for field " + field.name() + "! Error: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

}
