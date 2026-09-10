package net.ritasister.dc.api.cases.anim;

import net.ritasister.dc.api.cases.Cases;

public interface CaseAnimation<P, L, C extends Cases<?, L>> {

    void start(P player, L location, C cases);
}
