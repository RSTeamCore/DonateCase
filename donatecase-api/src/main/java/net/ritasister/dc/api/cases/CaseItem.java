package net.ritasister.dc.api.cases;

/**
 * Represents an item that can be found in a case.
 * <p>
 * */
public interface CaseItem<T> {

    /**
     * Gets the name of the case item.
     */
    String getName();

    /**
     * Gets the group of the case item.
     */
    String getGroup();

    /**
     * Gets the display name of the case item.
     */
    String getDisplayName();

    /**
     * Gets the material ID of the case item.
     */
    String getMaterialId();

    /**
     * Gets the chance of obtaining the case item.
     */
    int getChance();

    /**
     * Converts the case item to an ItemStack.
     */
    T toItemStack();
}
