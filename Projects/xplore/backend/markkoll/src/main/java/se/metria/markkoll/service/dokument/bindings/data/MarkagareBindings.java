package se.metria.markkoll.service.dokument.bindings.data;

import lombok.Builder;
import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

import java.util.List;

@Builder
@Data
public class MarkagareBindings {
    @DocProperty("MMK-Markägare-multi")
    private List<String> markagareNamn;

    @DocProperty("MMK-Markägare-med-adress-multi")
    private List<String> markagareNamnAdress;

    @DocProperty("MMK-Markägare-med-adress-andel-multi")
    private List<String> markagareNamnAdressAndel;

    @DocProperty("MMK-Samtliga-fastighetsägare")
    private List<String> samtligaFastighetsagare;

    @DocProperty("MMK-Markägare-med-andel-multi")
    private List<String> markagareNamnAndel;
}
