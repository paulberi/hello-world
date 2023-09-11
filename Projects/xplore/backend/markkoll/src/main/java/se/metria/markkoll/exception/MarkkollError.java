package se.metria.markkoll.exception;

import lombok.AllArgsConstructor;

/**
 * Fel som kan uppstå i Markkoll. Består av "name" som identifierar felet
 * och "message" som beskriver orsaken till det.
 */
@AllArgsConstructor
public enum MarkkollError {

    PROJEKT_ERROR("Kunde inte hämta projekt."),
    PROJEKT_CREATE_ERROR("Kunde inte skapa projekt."),
    FASTIGHET_ERROR("Kunde inte hämta fastighet(er)."),
    SAMFALLIGHET_ERROR("Kunde inte hämta samfällighet(er)."),
    MARKAGARE_ERROR("Kunde inte hämta markägare."),
    AVTAL_ERROR("Kunde inte hämta avtal."),
    AVTAL_CREATE_ERROR("Kunde inte skapa avtal."),
    INFOBREV_ERROR("Kunde inte hämta infobrev."),
    DOKUMENT_ERROR("Kunde inte hämta dokument"),
    DOKUMENT_FIND_ERROR("Kunde inte hitta dokumentmall av rätt typ"),
    DOKUMENT_SKAPA_ERROR("Kunde inte skapa dokument"),
    IMPORT_ERROR("Ett okänt fel uppstod vid import av fil."),
    IMPORT_ERROR_SHAPE_FILE_MISSING("Den importerade zip-filen saknar en shapefil."),
    IMPORT_ERROR_NO_OBJECT("Markkoll kan inte identifiera några objekt i filen."),
    IMPORT_ERROR_NEW_VERSION("Kunde inte importera en ny version av projektet."),
    IMPORT_SHAPE_INVALID("Kunde inte läsa upp innehållet i shapefilen, kontrollera att du har exporterat filen korrekt"),
    UPLOAD_ERROR_MAX_FILE_SIZE("Filen överstiger maximalt tillåten filstorlek (1 MB)."),
    FORM_ERROR("Ett formulärfält är inte korrekt ifyllt. Vänligen åtgärda detta och försök igen."),
    HAGLOF_JSON_INVALID("Gick inte att läsa importfilen"),
    HAGLOF_NO_COMMON_FASTIGHETER("Ingen av fastigheterna i importfilen existerar som skogsfastigheter i projektet"),
    BILAGA_ERROR_OPEN_FILE("Inte en giltig filtyp för bilagan"),
    BILAGA_ERROR_VALIDATION("Gick inte att validera ersättningsbeloppet i bilagan"),
    BILAGA_ERROR_VALUE("Gick inte att läsa ersättningsbelopp från bilaga"),
    FIBER_VARDERING_CONFIG_ALREADY_EXIST("Det finns redan en beräkningskonfiguration för det angivna person-/organisationsnumret"),
    PERSON_NOT_FOUND("Kunde inte hitta en matchande avtalspart");



    private final String message;

    public String getName() {
        return MarkkollError.this.name();
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.name() + ": " + message;
    }
}
