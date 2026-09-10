package net.ritasister.dc.cases.item;

import net.ritasister.dc.api.cases.CaseItem;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Random;

public class ItemRandomService {

    private final Random random = new Random();

    public CaseItem<?> getRandomItem(@NonNull List<? extends CaseItem<?>> items) {
        int totalChance = items.stream().mapToInt(CaseItem::getChance).sum();
        int rand = random.nextInt(totalChance);
        int from = 0;

        for (CaseItem<?> item : items) {
            if (rand < from + item.getChance()) {
                return item;
            }
            from += item.getChance();
        }

        return null;
    }
}
