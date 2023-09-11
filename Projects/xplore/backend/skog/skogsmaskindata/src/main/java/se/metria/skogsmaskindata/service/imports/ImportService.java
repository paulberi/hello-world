package se.metria.skogsmaskindata.service.imports;

import se.metria.skogsmaskindata.service.imports.exceptions.ImportException;
import se.metria.skogsmaskindata.service.imports.model.Import;

import java.io.InputStream;

public interface ImportService {
	Import processForestlinkPackage(InputStream inputStream) throws ImportException;
}
