package se.metria.markkoll.service.dokument.bindings.data;

import lombok.Builder;
import lombok.Data;
import se.metria.markkoll.util.dokument.DocProperty;

@Data
@Builder
public class IntrangBindings {
    @Builder.Default
    private ElIntrangBindings elIntrangBindings = new ElIntrangBindings();

    @DocProperty("MMK-Längd-i-luft")
    private Long langdILuft;

    @DocProperty("MMK-Längd-i-mark")
    private Long langdIMark;

    @DocProperty("MMK-Markförlagd-kanalisation")
    private Boolean markforlagdKanalisation;

    @DocProperty("MMK-Stolplinje")
    private Boolean stolplinje;
}
