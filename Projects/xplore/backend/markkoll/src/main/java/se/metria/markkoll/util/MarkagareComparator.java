package se.metria.markkoll.util;

import se.metria.markkoll.openapi.model.MarkagareDto;

import java.util.Comparator;

public class MarkagareComparator implements Comparator<MarkagareDto> {
    @Override
    public int compare(MarkagareDto m1, MarkagareDto m2) {
        if (m1.getKontaktperson()) {
            return -1;
        }
        else if (m2.getKontaktperson()) {
            return 1;
        }
        else {
            return m1.getNamn().compareTo(m2.getNamn());
        }
    }
}
