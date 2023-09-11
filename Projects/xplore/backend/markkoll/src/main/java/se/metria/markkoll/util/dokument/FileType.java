package se.metria.markkoll.util.dokument;

public enum FileType {
    NONE(""),
    PDF(".pdf"),
    ZIP(".zip"),
    XLSX(".xlsx"),
    FOLDER("/");

    private final String fileEnding;

    FileType(String fileEnding) {
        this.fileEnding = fileEnding;
    }

    @Override
    public String toString() {
        return fileEnding;
    }
}
