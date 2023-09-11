package se.metria.markkoll.repository.avtal;

import se.metria.markkoll.entity.avtal.AvtalMetadataEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollMetadataEntity;

import java.util.UUID;

public interface EditElnatVpView {
    UUID getFastighetsId();
    String getFastighetsbeteckning();
    AvtalMetadataEntity getAvtalMetadata();
    ElnatVarderingsprotokollMetadataEntity getElnatVpMetadata();
}
