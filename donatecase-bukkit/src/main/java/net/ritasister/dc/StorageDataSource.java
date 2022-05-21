package net.ritasister.dc;

public interface StorageDataSource {

    boolean load();
    void loadAsync();
    void setKey(final String name, String player, final int keys);
    int getKey(final String name, String player);
    boolean hasField(final String table, final String t);
    boolean hasTable(final String table);
    void close();
    void reload();
}
