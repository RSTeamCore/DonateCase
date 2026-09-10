package net.ritasister.dc.cases.obj;

import net.ritasister.dc.api.cases.CaseItem;
import net.ritasister.dc.api.cases.Cases;
import net.ritasister.dc.cases.item.ItemCase;
import net.ritasister.dc.cases.manager.CaseKeyManager;
import net.ritasister.dc.cases.manager.LocationManager;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Case implements Cases<ItemCase, Location> {

    private final String name;
    private final String title;
    private final List<CaseItem<?>> items = new ArrayList<>();
    private List<String> commands = new ArrayList<>();
    private final LocationManager locationManager;
    private final CaseKeyManager caseKeyManager;

    public Case(String name, String title, LocationManager locationManager, CaseKeyManager caseKeyManager) {
        this.name = name;
        this.title = title;
        this.locationManager = locationManager;
        this.caseKeyManager = caseKeyManager;
    }

    public void addItem(ItemCase item) { items.add(item); }

    public List<CaseItem<?>> getItems() {
        return items;
    }

    public void addKeys(String player, int keys) {
        caseKeyManager.addKeys(player, keys);
    }

    public void addLocation(Location location) {
        locationManager.addLocation(location);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setCommands(final List<String> commands) {
        this.commands = commands;
    }

    @Override
    public List<String> getCommands() {
        return this.commands;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public CaseKeyManager getCaseKeyManager() {
        return caseKeyManager;
    }
}
