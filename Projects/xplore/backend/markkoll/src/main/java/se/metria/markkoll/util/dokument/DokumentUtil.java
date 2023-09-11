package se.metria.markkoll.util.dokument;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import se.metria.kundconfig.openapi.model.ProduktDto;
import se.metria.markkoll.openapi.model.DokumentTypDto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.StringJoiner;

@Slf4j
public class DokumentUtil {
    public static String createFilename(FileType filetype, Object... objects) {
        var stringJoiner = getStringJoiner(filetype);

        var strings = Arrays.stream(objects)
            .map(Object::toString)
            .filter(string -> !StringUtil.isNullOrEmpty(string))
            .map(string -> string.replaceAll(":", "_"))
            .map(string -> string.replaceAll(" ", "_"))
            .map(String::toLowerCase);

        strings.forEach(s -> stringJoiner.add(s));
        var filenameFull = stringJoiner.toString();

        if (filenameFull.length() > 254) {
            var truncated = filenameFull.substring(0, 254 - filetype.toString().length()) + filetype.toString();
            log.warn("Filnamnet {} är för långt. Trunkerar filnamnet till {}.", filenameFull, truncated);
            return truncated;
        }
        else {
            return filenameFull;
        }
    }

    /**
     * Genererar ett filnamn med ändelsen .pdf
     *
     * @deprecated Använd createFilename() istället
     *
     * @param dokumentTyp typ av dokument
     * @param fastighetsbeteckning fastighetsbeteckning
     * @return Ett filnamn med endelsen .pdf, t.ex markkoll_infobrev_höljes 1_129_2021-11-23.pdf
     */
    @Deprecated(forRemoval = true)
    public static String getPdfFilename(DokumentTypDto dokumentTyp, String fastighetsbeteckning) {
        return getPdfFilename(dokumentTyp, fastighetsbeteckning, LocalDate.now());
    }

    /**
     * Genererar ett filnamn med ändelsen .pdf
     *
     * @deprecated Använd createFilename() istället
     *
     * @param dokumentTyp typ av dokument
     * @param fastighetsbeteckning fastighetsbeteckning
     * @return Ett filnamn med endelsen .pdf, t.ex markkoll_infobrev_höljes 1_129_2021-11-23.pdf
     */
    @Deprecated(forRemoval = true)
    public static String getPdfFilename(DokumentTypDto dokumentTyp, String fastighetsbeteckning, LocalDate date) {
        return getStringJoiner(FileType.PDF)
            .add("markkoll")
            .add(getDokumentTypeValue(dokumentTyp))
            .add(fastighetsbeteckning.replaceAll(":", "_").toLowerCase())
            .add(date.toString())
            .toString();
    }

    /**
     * Genererar ett filnamn med ändelsen .zip
     *
     * @deprecated Använd createFilename() istället
     *
     * @param dokumentTyp typ av dokument
     * @return Ett filnamn med ändelsen .zip, t.ex markkoll_infobrev_2021-11-23.pdf
     */
    @Deprecated(forRemoval = true)
    public static String getZipFilename(DokumentTypDto dokumentTyp) {
        return getStringJoiner(FileType.ZIP)
                .add(ProduktDto.MARKKOLL.getValue().toLowerCase())
                .add(getDokumentTypeValue(dokumentTyp))
                .add(LocalDate.now().toString())
                .toString();
    }

    /**
     * Genererar ett filnamn med ändelsen .zip
     *
     * @deprecated Använd createFilename() istället
     *
     * @param dokumentTyp typ av dokument
     * @param fastighetsbeteckning fastighetsbeteckning
     * @return Ett filnamn med ändelsen .zip, t.ex markkoll_infobrev_2021-11-23.pdf
     */
    @Deprecated(forRemoval = true)
    public static String getZipFilename(DokumentTypDto dokumentTyp, String fastighetsbeteckning) {
        return getStringJoiner(FileType.ZIP)
            .add(ProduktDto.MARKKOLL.getValue().toLowerCase())
            .add(getDokumentTypeValue(dokumentTyp))
            .add(fastighetsbeteckning.replaceAll(":", "_").toLowerCase())
            .add(LocalDate.now().toString())
            .toString();
    }

    /**
     * Genererar ett filnamn med ändelsen .xlsx
     *
     * @deprecated Använd createFilename() istället
     *
     * @param prefix prefix
     * @param fastighetsbeteckning fastighetsbeteckning
     * @return Ett filnamn med endelsen .xlsx, t.ex hms_höljes 1_129_2021-11-23.xlsx
     */
    @Deprecated(forRemoval = true)
    public static String getZipFilename(String prefix, String fastighetsbeteckning) {
        return getZipFilename(prefix, fastighetsbeteckning, LocalDate.now());
    }

    /**
     * Genererar ett filnamn med ändelsen .xlsx
     *
     * @deprecated Använd createFilename() istället
     *
     * @param prefix prefix
     * @param fastighetsbeteckning fastighetsbeteckning
     * @param date datum
     * @return Ett filnamn med endelsen .xlsx, t.ex hms_höljes 1_129_2021-11-23.xlsx
     */
    @Deprecated(forRemoval = true)
    public static String getZipFilename(String prefix, String fastighetsbeteckning, LocalDate date) {
        return getStringJoiner(FileType.XLSX)
            .add(prefix)
            .add(fastighetsbeteckning.replaceAll(":", "_").toLowerCase())
            .add(date.toString())
            .toString();
    }

    /**
     * Genererar ett filnamns prefix för tempfiler
     *
     * @deprecated Använd createFilename() istället
     *
     * @param dokumentTyp typ av dokument
     * @return Ett filnamns prefix, t.ex markkoll_infobrev_
     */
    @Deprecated(forRemoval = true)
    public static String getTempFilePrefix(DokumentTypDto dokumentTyp) {
        return new StringJoiner("_", "", "_")
                .add(ProduktDto.MARKKOLL.getValue().toLowerCase())
                .add(getDokumentTypeValue(dokumentTyp))
                .toString();
    }

    private static String getDokumentTypeValue(DokumentTypDto dokumentTyp) {
        if (dokumentTyp == DokumentTypDto.MARKUPPLATELSEAVTAL) {
            return "avtal";
        }

        // Temporärt tills vi stödjer fler dokumenttyper
        if (dokumentTyp == DokumentTypDto.INFOBREV) {
            return "övrigt_dokument";
        }

        return dokumentTyp.getValue().toLowerCase();
    }

    private static StringJoiner getStringJoiner(FileType filetype) {
        return new StringJoiner("_", "", filetype.toString());
    }
}
