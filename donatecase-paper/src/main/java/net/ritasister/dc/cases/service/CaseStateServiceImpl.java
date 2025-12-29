package net.ritasister.dc.cases.service;

import net.ritasister.dc.api.cases.service.CaseStateService;
import net.ritasister.dc.cases.obj.Case;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CaseStateServiceImpl implements CaseStateService<Player, Location, Case> {

    private final Map<Location, Map<Player, Case>> openedCases = new HashMap<>();

    @Override
    public void markOpened(Player player, Location location, Case cases) {
        openedCases.computeIfAbsent(location, loc -> new HashMap<>())
                .put(player, cases);
    }

    @Override
    public void clear(Player player, Location location) {
        Map<Player, Case> map = openedCases.get(location);
        if (map != null) {
            map.remove(player);
            if (map.isEmpty()) openedCases.remove(location);
        }
    }

    public boolean isOpen(Player player, Location location) {
        return openedCases.getOrDefault(location, Map.of()).containsKey(player);
    }
}
