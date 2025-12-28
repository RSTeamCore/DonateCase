package net.ritasister.dc.util.file.config.field;

import net.ritasister.dc.DonateCasePaperPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Enum representing configuration fields for the WorldGuardRegionProtect plugin.
 * Each enum constant corresponds to a specific configuration option, along with its
 * default value and path in the configuration file.
 */
public enum ConfigFields {

    CONFIG_VERSION("version", 1, "wgRegionProtect.version"),

    LANG("lang", "en_US", "wgRegionProtect.lang"),

    SEND_NO_UPDATE("sendNoUpdate", true, "wgRegionProtect.updateChecker.enable"),

    UPDATE_CHECKER("updateChecker", true, "wgRegionProtect.updateChecker.sendNoUpdate"),

    DATA_SOURCE_ENABLE("enable", false, "wgRegionProtect.dataSource.enable"),
    DATA_SOURCE_HOST("host", "localhost", "wgRegionProtect.dataSource.host"),
    DATA_SOURCE_PORT("port", 3306, "wgRegionProtect.dataSource.port"),
    DATA_SOURCE_DATABASE("database", "database", "wgRegionProtect.dataSource.database"),
    DATA_SOURCE_USER("root", "root", "wgRegionProtect.dataSource.root"),
    DATA_SOURCE_PASSWORD("password", "password", "wgRegionProtect.dataSource.password"),
    DATA_SOURCE_TABLE("wgrp_logs", "wgrp_logs", "wgRegionProtect.dataSource.wgrp_logs"),
    DATA_SOURCE_MAX_POOL_SIZE("maxPoolSize", 10, "wgRegionProtect.dataSource.maxPoolSize"),
    DATA_SOURCE_MAX_LIFE_TIME("maxLifetime", 1800, "wgRegionProtect.dataSource.maxLifetime"),
    DATA_SOURCE_CONNECTION_TIMEOUT("connectionTimeout", 5000, "wgRegionProtect.dataSource.connectionTimeout"),
    DATA_SOURCE_USE_SSL("useSsl", true, "wgRegionProtect.dataSource.useSsl"),
    DATA_SOURCE_INTERVAL_RELOAD("intervalReload", 60, "wgRegionProtect.dataSource.intervalReload");

    private final String field;
    private final Object defaultValue;
    private final String path;
    private FieldType fieldType;

    private static final Map<String, ConfigFields> CONFIG_FIELDS = new HashMap<>();
    private static final Map<Class<?>, FieldType> SIMPLE_TYPE_MAP = Map.of(
            String.class, FieldType.STRING,
            Boolean.class, FieldType.BOOLEAN,
            Integer.class, FieldType.INTEGER,
            Double.class, FieldType.DOUBLE,
            Long.class, FieldType.LONG,
            Float.class, FieldType.FLOAT
    );

    static {
        for (ConfigFields fields : values()) {
            if (fields.field != null) {
                CONFIG_FIELDS.put(fields.name().toLowerCase(Locale.ROOT), fields);
            }

            if (fields.defaultValue != null) {
                fields.fieldType = resolveFieldType(fields.defaultValue);
            } else {
                fields.fieldType = FieldType.STRING;
            }
        }
    }

    ConfigFields(String field, Object defaultValue, String path) {
        this.field = field;
        this.defaultValue = defaultValue;
        this.path = path;
    }

    /**
     * Retrieves the ConfigFields enum constant corresponding to the given field name.
     *
     * @param field The name of the configuration field.
     * @return The corresponding ConfigFields enum constant, or null if not found.
     */
    @ApiStatus.Internal
    @Contract("null -> null")
    @Nullable
    public static ConfigFields getField(String field) {
        if (field == null) {
            return null;
        }
        return CONFIG_FIELDS.get(field.toLowerCase(Locale.ROOT));
    }

    /**
     * Retrieves the default value of this configuration field.
     *
     * @return The default value of the configuration field.
     */
    public @NotNull String getPath() {
        return path;
    }

    /**
     * Retrieves the default value of this configuration field.
     *
     * @return The default value of the configuration field.
     */
    public FieldType getFieldType() {
        return fieldType;
    }

    private static FieldType resolveFieldType(@NonNull Object value) {
        final FieldType simpleType = SIMPLE_TYPE_MAP.get(value.getClass());
        if (simpleType != null) return simpleType;

        if (value instanceof List<?> list) {
            if (!list.isEmpty()) {
                final Object first = list.get(0);
                final FieldType firstType = SIMPLE_TYPE_MAP.get(first.getClass());

                return switch (firstType) {
                    case INTEGER -> FieldType.INTEGER_LIST;
                    case DOUBLE -> FieldType.DOUBLE_LIST;
                    case LONG -> FieldType.LONG_LIST;
                    case FLOAT -> FieldType.FLOAT_LIST;
                    case BOOLEAN -> FieldType.BOOLEAN_LIST;
                    default -> FieldType.STRING_LIST;
                };
            }
            return FieldType.STRING_LIST;
        }

        throw new IllegalArgumentException("Unsupported default value type: " + value.getClass());
    }

    private void ensure(FieldType expected) {
        if (this.fieldType != expected) {
            throw new IllegalStateException(name() + " is " + fieldType + ", not " + expected);
        }
    }

    /**
     * Retrieves the value of this configuration field as a String.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a String.
     * @throws IllegalStateException if the field type is not STRING.
     */
    public String asString(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.STRING);
        return plugin.getBootstrap().getLoader().getConfig().getString(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a List of Strings.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a List of Strings.
     * @throws IllegalStateException if the field type is not STRING_LIST.
     */
    public @NonNull List<String> asStringList(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.STRING_LIST);
        return plugin.getBootstrap().getLoader().getConfig().getStringList(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a List of Integers.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a List of Integers.
     * @throws IllegalStateException if the field type is not INTEGER_LIST.
     */
    public @NotNull List<Integer> asIntegerList(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.INTEGER_LIST);
        return plugin.getBootstrap().getLoader().getConfig().getIntegerList(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a List of Doubles.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a List of Doubles.
     * @throws IllegalStateException if the field type is not DOUBLE_LIST.
     */
    public @NotNull List<Double> asDoubleList(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.DOUBLE_LIST);
        return plugin.getBootstrap().getLoader().getConfig().getDoubleList(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a List of Longs.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a List of Longs.
     * @throws IllegalStateException if the field type is not LONG_LIST.
     */
    public @NonNull List<Long> asLongList(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.LONG_LIST);
        return plugin.getBootstrap().getLoader().getConfig().getLongList(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a List of Floats.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a List of Floats.
     * @throws IllegalStateException if the field type is not FLOAT_LIST.
     */
    public @NonNull List<Float> asFloatList(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.FLOAT_LIST);
        return plugin.getBootstrap().getLoader().getConfig().getFloatList(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a List of Booleans.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a List of Booleans.
     * @throws IllegalStateException if the field type is not BOOLEAN_LIST.
     */
    public @NonNull List<Boolean> asBooleanList(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.BOOLEAN_LIST);
        return plugin.getBootstrap().getLoader().getConfig().getBooleanList(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a boolean.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a boolean.
     * @throws IllegalStateException if the field type is not BOOLEAN.
     */
    public boolean asBoolean(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.BOOLEAN);
        return plugin.getBootstrap().getLoader().getConfig().getBoolean(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a double.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a double.
     * @throws IllegalStateException if the field type is not DOUBLE.
     */
    public double asDouble(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.DOUBLE);
        return plugin.getBootstrap().getLoader().getConfig().getDouble(getPath());
    }

    /**
     * Retrieves the value of this configuration field as an integer.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as an integer.
     * @throws IllegalStateException if the field type is not INTEGER.
     */
    public int asInt(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.INTEGER);
        return plugin.getBootstrap().getLoader().getConfig().getInt(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a long.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a long.
     * @throws IllegalStateException if the field type is not LONG.
     */
    public long asLong(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.LONG);
        return plugin.getBootstrap().getLoader().getConfig().getLong(getPath());
    }

    /**
     * Retrieves the value of this configuration field as a float.
     *
     * @param plugin The instance of WorldGuardRegionProtectPaperBase to access the configuration.
     * @return The value of the configuration field as a float.
     * @throws IllegalStateException if the field type is not FLOAT.
     */
    public float asFloat(@NonNull DonateCasePaperPlugin plugin) {
        ensure(FieldType.FLOAT);
        return (float) plugin.getBootstrap().getLoader().getConfig().getDouble(getPath());
    }
}
