package net.ritasister.dc.cases.obj;

import dc.old.DonateCase;
import net.ritasister.dc.DonateCasePaperPlugin;
import org.bukkit.Location;

import java.util.List;

public class CaseRepository {

    private final DonateCasePaperPlugin plugin;

    private final List<Case> cases;

    public CaseRepository(DonateCasePaperPlugin plugin, List<Case> cases) {
        this.plugin = plugin;
        this.cases = cases; }

    public boolean hasCaseByLocation(final Location location) {
        for (final Case cases : plugin.getCaseManager().getAllCases()) {
            for (final Location caseLocation : cases.getLocationManager().getLocation()) {
                if (DonateCase.t.isHere(caseLocation, location)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Case getCaseByLocation(final Location loc) {
        for (final Case cases : plugin.getCaseManager().getAllCases()) {
            for (final Location ca : cases.getLocationManager().getLocation()) {
                if (DonateCase.t.isHere(ca, loc)) {
                    return cases;
                }
            }
        }
        return null;
    }

    public boolean hasCaseByName(final String name) {
        for (final Case cases : plugin.getCaseManager().getAllCases()) {
            if (cases.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public Case getCaseByName(final String name) {
        for (final Case cases : plugin.getCaseManager().getAllCases()) {
            if (cases.getName().equalsIgnoreCase(name)) {
                return cases;
            }
        }
        return null;
    }

    public boolean hasCaseByTitle(final String title) {
        for (final Case cases : plugin.getCaseManager().getAllCases()) {
            if (cases.getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    public Case getCaseByTitle(final String title) {
        for (final Case cases : plugin.getCaseManager().getAllCases()) {
            if (cases.getTitle().equalsIgnoreCase(title)) {
                return cases;
            }
        }
        return null;
    }
}
