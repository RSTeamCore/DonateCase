package net.ritasister.dc.api.cases.group;

/**
 * Service to manage player groups and permissions.
 *
 * @param <P> The player type.
 */
public interface GroupService<P> {

    /**
     * Checks if the player belongs to the specified level group.
     *
     * @param player The player to check.
     * @param group  The group name.
     * @return True if the player belongs to the group, false otherwise.
     */
    boolean hasLevelGroup(P player, String group);

    /**
     * Retrieves the group name of the player.
     *
     * @param player The player whose group is to be retrieved.
     * @return The group name of the player.
     */
    String getGroup(P player);
}