package se.metria.matdatabas.service.matning.dto.csvImport;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportMatning {
      private String matobjekt;
      private String matningstyp;
      private String storhet;
      private String instrument;
      private String avlastDatum;
      private String avlastVarde;
      private String beraknatVarde;
      private String enhetAvlast;
      private String enhetBeraknat;
      private String felkod;
      private String kommentar;
      private String inomDetektionsomrade;
      private Integer matobjektId;
      private Integer matningstypId;
      private List<ImportError> importFel;
}

