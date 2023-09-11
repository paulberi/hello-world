package se.metria.matdatabas.service.matning;


import se.metria.matdatabas.openapi.model.GransvardeDto;

import java.util.Comparator;

public class GransvardeLarmComparator implements Comparator<GransvardeDto> {
    Short typAvKontroll;

    public GransvardeLarmComparator(Short typAvKontroll) {
        this.typAvKontroll = typAvKontroll;
    }

    @Override
    public int compare(GransvardeDto o1, GransvardeDto o2) {
        switch(typAvKontroll) {
            case TypAvKontroll.MAX:
                return compareMax(o1, o2);
            case TypAvKontroll.MIN:
                return compareMin(o1, o2);
            default:
                throw new RuntimeException("TypAvKontroll unknown: " + typAvKontroll);
        }
    }

    private int compareMax(GransvardeDto o1, GransvardeDto o2) {
        double g1 = o1.getGransvarde();
        double g2 = o2.getGransvarde();
        if (g1 == g2) {
            return 0;
        } else if (g1 < g2) {
            return 1;
        } else {
            return -1;
        }
    }

    private int compareMin(GransvardeDto o1, GransvardeDto o2) {
        double g1 = o1.getGransvarde();
        double g2 = o2.getGransvarde();
        if (g1 == g2) {
            return 0;
        } else if (g1 < g2) {
            return -1;
        } else {
            return 1;
        }
    }
}
