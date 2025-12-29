package net.ritasister.dc.util.tools;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class Tools {

    public Tools() {}
    
    public String getLoc(final Location loc) {
        return (loc == null) ? "" : (loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ());
    }
    
    public Location getLoc(final String loc) {
        if (loc == null) {
            return null;
        }
        final String[] ex = loc.split(";");
        final String w = ex[0];
        final int x = Integer.parseInt(ex[1]);
        final int y = Integer.parseInt(ex[2]);
        final int z = Integer.parseInt(ex[3]);
        return new Location(Bukkit.getWorld(w), x, y, z);
    }
    
    public int c(final int x, final int y) {
        final int x2 = x - 1;
        final int y2 = y - 1;
        return x2 + y2 * 9;
    }
    
    public String getEnding(final int k, final String... s) {
        return (k >= 2 && k < 5) ? s[0] : ((k != 0 && k < 5) ? "" : s[1]);
    }
    
    public boolean isHere(final @NotNull Location l1, final @NotNull Location l2) {
        return l1.getWorld() == l2.getWorld() && (int)l1.distance(l2) == 0;
    }
}
