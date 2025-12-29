package net.ritasister.dc.cases.manager;

import dc.old.DonateCase;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationManager {

    private final List<Location> locations = new ArrayList<>();

    public List<Location> getLocation() {
        return this.locations;
    }

    public void addLocation(final Location loc) {
        this.getLocation().add(loc);
        this.saveLocation();
    }

    public void removeLocation(final Location loc) {
        this.getLocation().remove(loc);
        this.saveLocation();
    }

    public void saveLocation() {
        final ArrayList<String> lv = new ArrayList<>();
        for (final Location l : this.getLocation()) {
            lv.add(DonateCase.t.getLoc(l));
        }
        DonateCase.CCase.getConfig().set("DonatCase.Cases." + this.getName() + ".Case", lv);
        DonateCase.CCase.save();
    }
}
