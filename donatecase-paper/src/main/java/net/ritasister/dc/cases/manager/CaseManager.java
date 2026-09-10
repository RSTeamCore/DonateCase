package net.ritasister.dc.cases.manager;

import net.ritasister.dc.cases.obj.Case;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CaseManager {

    private final Map<String, Case> casesByName = new HashMap<>();

    public void registerCase(Case c) {
        casesByName.put(c.getName().toLowerCase(), c);
    }

    public Case getCaseByName(@NotNull String name) {
        return casesByName.get(name.toLowerCase());
    }

    public boolean hasCaseByName(@NotNull String name) {
        return casesByName.containsKey(name.toLowerCase());
    }

    public Collection<Case> getAllCases() {
        return casesByName.values();
    }
}
