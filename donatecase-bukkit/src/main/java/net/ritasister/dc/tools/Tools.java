package net.ritasister.dc.tools;

import net.ritasister.dc.Case;
import net.ritasister.dc.DonateCase;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tools {
    public void launchFirework(final @NotNull Location l) {
        final Random r = new Random();
        final Firework fw = (Firework)l.getWorld().spawnEntity(l.subtract(new Vector(0.0, 0.5, 0.0)), EntityType.FIREWORK);
        final FireworkMeta meta = fw.getFireworkMeta();
        final Color[] c = { Color.RED, Color.AQUA, Color.GREEN, Color.ORANGE, Color.LIME, Color.BLUE, Color.MAROON, Color.WHITE };
        meta.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BALL).trail(false).withColor(new Color[] { c[r.nextInt(c.length)], c[r.nextInt(c.length)], c[r.nextInt(c.length)] }).build());
        fw.setFireworkMeta(meta);
        fw.setMetadata("case", new FixedMetadataValue(DonateCase.instance, "case"));
        fw.detonate();
    }
    
    public boolean hasLevelGroup(final Player player, String group) {
        if (!DonateCase.LevelGroup) {
            return true;
        }
        group = group.toLowerCase();
        if (DonateCase.levelGroup.containsKey(this.getGroup(player))) {
            try{
                if (DonateCase.levelGroup.get(group) <= DonateCase.levelGroup.get(this.getGroup(player))) {
                    return false;
                }
            }catch(Exception ex){}
            return true;
        }
        return true;
    }
    
    public String getGroup(final Player player) {
        try{
            return DonateCase.permission.getPrimaryGroup(player).toLowerCase();
        }catch(Exception var3){
            return "";
        }
    }
    
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
    
    public Case.ItemCase getRandomGroup(final @NotNull List<Case.ItemCase> groups) {
        final Random random = new Random();
        int maxChance = 0;
        int from = 0;
        for (final Case.ItemCase item : groups) {
            maxChance += item.getChance();
        }
        final int rand = random.nextInt(maxChance);
        for (final Case.ItemCase item2 : groups) {
            if (from <= rand && rand < from + item2.getChance()) {
                return item2;
            }
            from += item2.getChance();
        }
        return null;
    }
    
    public void msg(final CommandSender s, final String msg) {
        this.msg_(s, String.valueOf(DonateCase.lang.getString("Prefix")) + msg);
    }
    
    public void msg_(final @NotNull CommandSender s, final String msg)
    {
        s.sendMessage(this.rc(msg));
    }
    
    public String rc(final @NotNull String t)
    {
        return t.replace("&", "§");
    }
    
    public String rt(String text, final String @NotNull ... repl) {
        for (final String s : repl) {
            final int l = s.split(":")[0].length();
            text = text.replace(s.substring(0, l), s.substring(l + 1));
        }
        return text;
    }
    
    public List<String> rt(final @NotNull List<String> text, final String... repl) {
        final ArrayList<String> rt = new ArrayList<String>();
        for (final String t : text) 
        {
            rt.add(this.rt(t, repl));
        }
        return rt;
    }
    
    public List<String> rc(final @NotNull List<String> t) {
        final ArrayList<String> a = new ArrayList<String>();
        for (final String s : t) {
            a.add(this.rc(s));
        }
        return a;
    }
    
    public ItemStack createItem(final Material ma) 
    {
        return this.createItem(ma, 0, 1, null, null);
    }
    
    public ItemStack createItem(final Material ma, final List<String> lore) {
        return this.createItem(ma, 0, 1, "", lore);
    }
    
    public ItemStack createItem(final Material ma, final int amount) {
        return this.createItem(ma, 0, amount, "", null);
    }
    
    public ItemStack createItem(final Material ma, final int amount, final int data) {
        return this.createItem(ma, data, amount, "", null);
    }
    
    public ItemStack createItem(final Material ma, final int amount, final int data, final String dn) {
        return this.createItem(ma, data, amount, dn, null);
    }
    
    public ItemStack createItem(final Material ma, final String dn, final List<String> lore) {
        return this.createItem(ma, 0, 1, dn, lore);
    }
    
    public ItemStack createItem(final Material ma, final String dn)
    {
        return this.createItem(ma, 0, 1, dn, null);
    }
    
    public ItemStack createItem(final Material ma, final int data, final int amount, final String dn, final List<String> lore) {
        final ItemStack item = new ItemStack(ma, amount);
        final ItemMeta m = item.getItemMeta();
        if (dn != null) 
        {
            m.setDisplayName(this.rc(dn));
        }
        if (lore != null) 
        {
            m.setLore(this.rc(lore));
        }
        item.setItemMeta(m);
        return item;
    }
}