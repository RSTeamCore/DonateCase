package net.ritasister.dc.cases.service;

import net.ritasister.dc.api.cases.CaseItem;
import net.ritasister.dc.api.cases.service.CaseBroadcastService;
import net.ritasister.dc.cases.obj.Case;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class CaseBroadcastServiceImpl implements CaseBroadcastService<Player, Case> {

    @Override
    public void broadcastWin(@NonNull Player player, @NonNull Case cases, @NonNull CaseItem<?> item) {
        final String message = String.format("%s выиграл %s в кейсе %s", player.getName(), item.getDisplayName(), cases.getTitle());
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(message));
    }
}
