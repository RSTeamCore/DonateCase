package net.ritasister.dc.cases;

import net.ritasister.dc.api.cases.LocationConverter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jspecify.annotations.NonNull;

public class LocationConverterImpl implements LocationConverter<Location> {

    @Override
    public String serialize(Location location) {
        if (location == null) {
            return "";
        }
        return location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
    }

    @Override
    public Location deserialize(String location) {
        if (location == null) {
            return null;
        }
        final String[] ex = location.split(";");

        return new Location(Bukkit.getWorld(ex[0]), Integer.parseInt(ex[1]),
                Integer.parseInt(ex[2]), Integer.parseInt(ex[3]));
    }

    @Override
    public boolean isHere(@NonNull Location location1, @NonNull Location location2) {
        return location1.getWorld() == location2.getWorld() && location1.distance(location2) < 1;
    }
}
