package net.ritasister.dc.cases.group;

import dc.old.DonateCase;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.ritasister.dc.DonateCasePaperPlugin;
import net.ritasister.dc.api.cases.group.GroupService;
import net.ritasister.dc.plugin.loader.LoadLuckPerms;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GroupServiceImpl implements GroupService<Player> {

    private final DonateCasePaperPlugin plugin;

    public GroupServiceImpl(DonateCasePaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasLevelGroup(Player player, String group) {
        if (!DonateCase.LevelGroup) {
            return true;
        }
        group = group.toLowerCase();
        final String playerGroup = getGroup(player);
        try {
            if (DonateCase.levelGroup.containsKey(playerGroup) && DonateCase.levelGroup.get(group) <= DonateCase.levelGroup.get(playerGroup)) {
                return false;
            }
        } catch (Exception ignored) {}
        return true;
    }

    @Override
    public String getGroup(Player player) {
        return Optional.ofNullable(plugin.getLuckPerms())
                .map(LoadLuckPerms::hookAPILuckPerms)
                .map(LuckPerms::getUserManager)
                .map(um -> um.getUser(player.getUniqueId()))
                .map(User::getPrimaryGroup)
                .map(String::toLowerCase)
                .orElse("");
    }
}
