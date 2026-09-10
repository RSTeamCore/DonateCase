package net.ritasister.dc.cases.manager;

import dc.old.DonateCase;
import net.ritasister.dc.api.cases.KeyStorage;

public class CaseKeyManager implements KeyStorage {

    @Override
    public void setKeys(String player, final int keys) {
        player = player.toLowerCase();
        if (DonateCase.Tconfig) {
            DonateCase.Ckeys.getConfig().set("DonatCase.Cases." + this.getName() + "." + player, (keys == 0) ? null : keys);
            DonateCase.Ckeys.save();
        } else {
            DonateCase.mysql.setKey(this.getName(), player, keys);
        }
    }

    @Override
    public int getKeys(String player) {
        player = player.toLowerCase();
        return DonateCase.Tconfig ? DonateCase.Ckeys.getConfig().getInt("DonatCase.Cases." + this.getName() + "." + player) : DonateCase.mysql.getKey(this.getName(), player);
    }

    @Override
    public void addKeys(final String player, final int keys) {
        this.setKeys(player, this.getKeys(player) + keys);
    }

    @Override
    public void removeKeys(String player, final int keys) {
        player = player.toLowerCase();
        this.setKeys(player, this.getKeys(player) - keys);
    }
}
