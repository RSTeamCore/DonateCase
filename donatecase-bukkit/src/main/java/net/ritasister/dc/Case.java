package net.ritasister.dc;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import org.bukkit.Location;
import java.util.List;

public class Case {
    public static List<Case> classes = new ArrayList<>();
    private final String name;
    private final String title;
    private final List<ItemCase> items = new ArrayList<>();
    private final List<Location> locations = new ArrayList<>();
    private List<String> cmds = new ArrayList<>();
    
    public Case(final String name, final String title) {
        this.title = title;
        this.name = name;
        Case.classes.add(this);
    }
    
    public void setCmds(final List<String> g) {
        this.cmds = g;
    }
    
    public List<String> getCmds() {
        return this.cmds;
    }
    
    public void addItem(final ItemCase ic) {
        this.getItems().add(ic);
    }
    
    public String getTitle() {
        return DonateCase.t.rc(this.title);
    }
    
    public String getName() {
        return this.name;
    }
    
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
    
    public List<ItemCase> getItems() {
        return this.items;
    }
    
    public void setKeys(String player, final int keys) {
        player = player.toLowerCase();
        if (DonateCase.Tconfig) {
            DonateCase.Ckeys.getConfig().set("DonatCase.Cases." + this.getName() + "." + player, (keys == 0) ? null : keys);
            DonateCase.Ckeys.save();
        }
        else {
            DonateCase.mysql.setKey(this.getName(), player, keys);
        }
    }
    
    public void addKeys(final String player, final int keys) {
        this.setKeys(player, this.getKeys(player) + keys);
    }
    
    public void removeKeys(String player, final int keys) {
        player = player.toLowerCase();
        this.setKeys(player, this.getKeys(player) - keys);
    }
    
    public int getKeys(String player) {
        player = player.toLowerCase();
        return DonateCase.Tconfig ? DonateCase.Ckeys.getConfig().getInt("DonatCase.Cases." + this.getName() + "." + player) : DonateCase.mysql.getKey(this.getName(), player);
    }
    
    public static boolean hasCaseByLocation(final Location loc) {
        for (final Case c : Case.classes) {
            for (final Location ca : c.getLocation()) {
                if (DonateCase.t.isHere(ca, loc)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Case getCaseByLocation(final Location loc) {
        for (final Case c : Case.classes) {
            for (final Location ca : c.getLocation()) {
                if (DonateCase.t.isHere(ca, loc)) {
                    return c;
                }
            }
        }
        return null;
    }
    
    public static boolean hasCaseByName(final String name) {
        for (final Case c : Case.classes) {
            if (c.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public static Case getCaseByName(final String name) {
        for (final Case c : Case.classes) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
    
    public static boolean hasCaseByTitle(final String title) {
        for (final Case c : Case.classes) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }
    
    public static Case getCaseByTitle(final String title) {
        for (final Case c : Case.classes) {
            if (c.getTitle().equalsIgnoreCase(title)) {
                return c;
            }
        }
        return null;
    }
    
    public static class ItemCase {
        private final ItemStack item;
        private final String name;
        private final String group;
        private final String displayName;
        private final int chance;
        
        public ItemCase(final String name, final int chance, final String id, final String group, final String displayName) {
            this.name = name;
            this.group = group.toLowerCase();
            this.chance = chance;
            this.displayName = displayName;
            Material material;
            final int data = 0;
            material = Material.getMaterial(id.toUpperCase());
            if (material == null) {
                material = Material.STONE;
            }
            this.item = DonateCase.t.createItem(material, 1, data, displayName);
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public String getGroup() {
            return this.group;
        }
        
        public String getName() {
            return this.name;
        }
        
        public ItemStack getItem() {
            return this.item;
        }
        
        public int getChance() {
            return this.chance;
        }
    }
}
