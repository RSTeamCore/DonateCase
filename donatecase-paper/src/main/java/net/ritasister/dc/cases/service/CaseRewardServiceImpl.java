package net.ritasister.dc.cases.service;

import net.ritasister.dc.api.cases.CaseItem;
import net.ritasister.dc.api.cases.Cases;
import net.ritasister.dc.api.cases.service.CaseRewardService;
import net.ritasister.dc.cases.obj.Case;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CaseRewardServiceImpl implements CaseRewardService<Player, Case> {

    @Override
    public void applyReward(@NonNull Player player, @NonNull Case cases, @NonNull CaseItem<?> item) {
        for (String cmd : cases.getCommands()) {
            String parsedCmd = cmd.replace("%player%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parsedCmd);
        }
    }
}
