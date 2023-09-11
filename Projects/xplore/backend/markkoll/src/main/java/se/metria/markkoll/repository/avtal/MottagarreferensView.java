package se.metria.markkoll.repository.avtal;

import java.util.UUID;

public interface MottagarreferensView {
    UUID getAvtalspartId();
    String getMottagarreferens();
}
