package se.metria.markkoll.testdata;

import se.metria.markkoll.openapi.model.AgartypDto;
import se.metria.markkoll.openapi.model.MarkagareDto;
import se.metria.markkoll.service.utskick.UtskickBatchDto;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickVpDto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static se.metria.markkoll.testdata.TestData.mockUUID;

public class UtskickTestData {
    public static List<MarkagareDto> markagareEnskild() {
        return Arrays.asList(
            new MarkagareDto().id(mockUUID(0)).agartyp(AgartypDto.LF).inkluderaIAvtal(true).namn("")
                .adress("").postort("").postnummer("").kontaktperson(true).andel("1")
        );
    }

    public static List<MarkagareDto> ombudEnskild() {
        return Arrays.asList(
            new MarkagareDto().id(mockUUID(1)).agartyp(AgartypDto.OMBUD).inkluderaIAvtal(true).namn("ombud")
                .adress("").postort("").postnummer("").kontaktperson(true)
        );
    }

    public static List<MarkagareDto> markagareFastighetIngenKontaktperson() {
        return Arrays.asList(
            new MarkagareDto().id(mockUUID(0)).agartyp(AgartypDto.LF).inkluderaIAvtal(true).namn("")
                .adress("").postort("").postnummer("").kontaktperson(false).andel("1"),
            new MarkagareDto().id(mockUUID(1)).agartyp(AgartypDto.OMBUD).inkluderaIAvtal(true).namn("ombud")
                .adress("").postort("").postnummer("").kontaktperson(false)
        );
    }

    public static List<MarkagareDto> markagare() {
        return Arrays.asList(
            new MarkagareDto().id(mockUUID(0)).agartyp(AgartypDto.LF).inkluderaIAvtal(true)
                .namn("Huvudagare Agarsson").adress("adress").postort("postort").postnummer("postnummer").andel("1/2"),
            new MarkagareDto().id(mockUUID(1)).agartyp(AgartypDto.LF).inkluderaIAvtal(true)
                .namn("Rene Descartes").adress("adress").postort("postort").postnummer("postnummer").andel(null),
            new MarkagareDto().id(mockUUID(2)).agartyp(AgartypDto.LF).inkluderaIAvtal(false)
                .namn("Utesluten Persson").andel("1/2"),
            new MarkagareDto().id(mockUUID(3)).agartyp(AgartypDto.LF).inkluderaIAvtal(true)
                .namn("Bill Gates").adress("annanadress").postort("annanpostort").postnummer("annatpostnummer")
                .kontaktperson(true).andel("1/2"),

            new MarkagareDto().id(mockUUID(4)).agartyp(AgartypDto.OMBUD).inkluderaIAvtal(true)
                .namn("Ombud 1").adress("adress").postort("postort").postnummer("postnummer"),
            new MarkagareDto().id(mockUUID(5)).agartyp(AgartypDto.OMBUD).inkluderaIAvtal(false),
            new MarkagareDto().id(mockUUID(6)).agartyp(AgartypDto.OMBUD).inkluderaIAvtal(true)
                .namn("Ombud 3").adress("adress").postort("postort").postnummer("postnummer"),

            new MarkagareDto().id(mockUUID(7)).agartyp(AgartypDto.TR).inkluderaIAvtal(false),
            new MarkagareDto().id(mockUUID(8)).agartyp(AgartypDto.TR).inkluderaIAvtal(true)
        );
    }

    public static UtskickBatchDto utskickmarkagareEnskildFastighet() {
        var markagareEnskild = markagareEnskild();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("fbet")
                        .lopnummer("23")
                        .kontaktperson(Optional.of(markagareEnskild.get(0)))
                        .signatarer(Arrays.asList(markagareEnskild.get(0)))
                        .build()
                )
            )
            .build();
    }

    public static UtskickBatchDto utskickmarkagareEnskild() {
        var markagareEnskild = markagareEnskild();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("")
                        .lopnummer("23")
                        .kontaktperson(Optional.of(markagareEnskild.get(0)))
                        .signatarer(Arrays.asList(markagareEnskild.get(0)))
                        .build()
                )
            )
            .build();
    }

    public static UtskickBatchDto utskickOmbudEnskild() {
        var ombudEnskild = ombudEnskild();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("ombud")
                        .lopnummer("23")
                        .kontaktperson(Optional.of(ombudEnskild.get(0)))
                        .signatarer(Arrays.asList(ombudEnskild.get(0)))
                        .build()
                )
            )
            .build();
    }

    public static UtskickBatchDto utskickMarkagareFastighetIngenKontaktperson() {
        var markagareFastighetIngenKontaktperson = markagareFastighetIngenKontaktperson();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("fbet")
                        .lopnummer("23A")
                        .kontaktperson(Optional.of(markagareFastighetIngenKontaktperson.get(0)))
                        .signatarer(Arrays.asList(markagareFastighetIngenKontaktperson.get(0)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud")
                        .lopnummer("23B")
                        .kontaktperson(Optional.of(markagareFastighetIngenKontaktperson.get(0)))
                        .signatarer(Arrays.asList(markagareFastighetIngenKontaktperson.get(1)))
                        .build()
                )
            )
            .build();
    }

    public static UtskickBatchDto utskickBatchSingle() {
        var markagare = markagare();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("fbet")
                        .lopnummer("23A")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(0), markagare.get(3)))
                        .utskickVp(Arrays.asList(
                            new UtskickVpDto("vp0", Optional.empty(), Arrays.asList(markagare.get(0), markagare.get(3)))
                        ))
                        .build()
                )
            ).build();
    }

    public static UtskickBatchDto utskickBatchFastighet() {
        var markagare = markagare();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("fbet")
                        .lopnummer("23A")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(0), markagare.get(3)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_1")
                        .lopnummer("23B")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(4)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_3")
                        .lopnummer("23C")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(6)))
                        .build()
                )
            ).build();
    }

    public static UtskickBatchDto utskickBatchFastighetMedVp() {
        var markagare = markagare();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("fbet")
                        .lopnummer("23A")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(0), markagare.get(3)))
                        .utskickVp(Arrays.asList(
                            new UtskickVpDto("vp0", Optional.empty(), Arrays.asList(markagare.get(0), markagare.get(3)))
                        ))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_1")
                        .lopnummer("23B")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(4)))
                        .utskickVp(Arrays.asList(
                            new UtskickVpDto("vp1", Optional.of(markagare.get(3)), Arrays.asList(markagare.get(4)))
                        ))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_3")
                        .lopnummer("23C")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(6)))
                        .utskickVp(Arrays.asList(
                            new UtskickVpDto("vp3", Optional.of(markagare.get(3)), Arrays.asList(markagare.get(6)))
                        ))
                        .build()
                )
            ).build();
    }

    public static UtskickBatchDto utskickBatchAdress() {
        var markagare = markagare();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .titel("annanadress_annanpostort")
                        .lopnummer("23A")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(3)))
                        .build(),
                    UtskickDto.builder()
                        .titel("adress_postort")
                        .lopnummer("23B")
                        .kontaktperson(Optional.of(markagare.get(0)))
                        .signatarer(Arrays.asList(markagare.get(0)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_1")
                        .lopnummer("23C")
                        .kontaktperson(Optional.of(markagare.get(4)))
                        .signatarer(Arrays.asList(markagare.get(4)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_3")
                        .lopnummer("23D")
                        .kontaktperson(Optional.of(markagare.get(6)))
                        .signatarer(Arrays.asList(markagare.get(6)))
                        .build()
                )
            ).build();
    }

    public static UtskickBatchDto utskickBatchFastighetsagare() {
        var markagare = markagare();

        return UtskickBatchDto.builder()
            .title("fbet")
            .utskick(
                Arrays.asList(
                    UtskickDto.builder()
                        .lopnummer("23A")
                        .titel("huvudagare_agarsson")
                        .kontaktperson(Optional.of(markagare.get(0)))
                        .signatarer(Arrays.asList(markagare.get(0)))
                        .build(),
                    UtskickDto.builder()
                        .lopnummer("23B")
                        .titel("bill_gates")
                        .kontaktperson(Optional.of(markagare.get(3)))
                        .signatarer(Arrays.asList(markagare.get(3)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_1")
                        .lopnummer("23C")
                        .kontaktperson(Optional.of(markagare.get(4)))
                        .signatarer(Arrays.asList(markagare.get(4)))
                        .build(),
                    UtskickDto.builder()
                        .titel("ombud_3")
                        .lopnummer("23D")
                        .kontaktperson(Optional.of(markagare.get(6)))
                        .signatarer(Arrays.asList(markagare.get(6)))
                        .build()
                )
            ).build();
    }
}
