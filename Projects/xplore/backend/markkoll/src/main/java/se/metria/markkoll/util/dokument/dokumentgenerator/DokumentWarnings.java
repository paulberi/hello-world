package se.metria.markkoll.util.dokument.dokumentgenerator;

import lombok.Getter;

@Getter
public enum DokumentWarnings {
    DOCPROPERTY_NOT_SET("DocProperty in Word template not set: "),
    IMAGE_DATA_MISSING("DocProperty missing image data: "),
    ILLEGAL_FIELD_TYPE("Illegal field type: ");

    private final String value;

    private DokumentWarnings(String value) {
        this.value = value;
    }
}
