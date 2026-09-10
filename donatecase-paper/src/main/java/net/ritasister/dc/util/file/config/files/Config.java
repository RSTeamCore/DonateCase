package net.ritasister.dc.util.file.config.files;

import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.util.file.config.field.ConfigFields;
import org.bukkit.configuration.Configuration;

public final class Config implements net.ritasister.dc.api.config.Config {

    private final DonateCasePaperPlugin donateCasePaperPlugin;

    public Config(DonateCasePaperPlugin donateCasePaperPlugin) {
        this.donateCasePaperPlugin = donateCasePaperPlugin;
        this.reloadConfig();
    }

    public void reloadConfig() {
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
        final Configuration configFile = donateCasePaperPlugin.getBootstrap().getLoader().getConfig();

        // Additional loading logic if needed
    }

    public void saveRegionProtectConfig() {
        donateCasePaperPlugin.getBootstrap().getLoader().saveConfig();

        // Additional saving logic if needed
    }

    public void saveConfig(final String path, final Object field) {
        try {
            donateCasePaperPlugin.getBootstrap().getLoader().getConfig().set(path, field);
            donateCasePaperPlugin.getBootstrap().getLoader().saveConfig();
        } catch (Exception e) {
            donateCasePaperPlugin.getLogger().severe("Could not save config.yml! Error: " + e.getMessage());
            e.fillInStackTrace();
        }
    }

    public void saveConfigFiles() {
        donateCasePaperPlugin.getLogger().info("Saving all configuration files before the plugin shuts down...");
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

                this.saveConfig(field.getPath(), value);
                donateCasePaperPlugin.getLogger().info(String.format("Checking and saving field: %s = %s", field.name(), value));

            } catch (Exception e) {
                donateCasePaperPlugin.getLogger().severe("Could not save config.yml for field " + field.name() + "! Error: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

}
