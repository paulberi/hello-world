package se.metria.skogsmaskindata.service.imports;

import org.opengis.feature.simple.SimpleFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.skogsmaskindata.service.imports.exceptions.AlreadyImportedException;
import se.metria.skogsmaskindata.service.imports.exceptions.ImportException;
import se.metria.skogsmaskindata.service.imports.exceptions.InternalImportException;
import se.metria.skogsmaskindata.service.imports.model.Import;
import se.metria.skogsmaskindata.service.imports.model.Info;
import se.metria.skogsmaskindata.service.imports.packagehandler.PackageHandler;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileHandler;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileType;

import java.io.*;
import java.util.Set;


@Service
public class DefaultImportsService implements ImportService {

	private ImportRepository repository;
	private JdbcTemplate jdbcTemplate;

	private Logger logger = LoggerFactory.getLogger(DefaultImportsService.class);

	public DefaultImportsService(ImportRepository repository, JdbcTemplate jdbcTemplate) {
		this.repository = repository;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Transactional
	public Import processForestlinkPackage(InputStream inputStream) throws ImportException {
		try (PackageHandler packageHandler = new PackageHandler(inputStream)) {
			packageHandler.unzip();
			packageHandler.extractInfoFile();
			packageHandler.extractPdfFile();
			packageHandler.readShapefiles();

			Import importEntity = getImportEntity(packageHandler.getInfo());

			// New import must be saved & flushed to get parent_id
			if (importEntity.getId() == null) {
				repository.save(importEntity);
				repository.flush();
			}

			// From this point onwards we can actually save the status since we
			// have both a database connection and a package id
			try {
				addShapefiles(importEntity, packageHandler.getShapefileHandler());
				addImportdata(importEntity, packageHandler.getInfoFile(), packageHandler.getPdfFile());

				importEntity.setCompleted();
			} catch (Exception e) {
				importEntity.setError(e.getMessage());
			}

			repository.save(importEntity);

			return importEntity;
		}
	}

	private Import getImportEntity(Info info) throws AlreadyImportedException {
		Import newImport = Import.fromInfo(info);

		logger.debug("Created entity for package {} {} {}", newImport.getObjektnummer(), newImport.getOrganisation(), newImport.getPakettyp());

		// Throw an exception if the package has already been successfully imported
		Import existingImport = this.repository.findByObjektnummerAndOrganisationAndPakettyp(
				newImport.getObjektnummer(), newImport.getOrganisation(), newImport.getPakettyp());

		if (existingImport != null) {
			if (existingImport.isCompleted()) {
				throw new AlreadyImportedException();
			} else {
				return existingImport;
			}
		}

		return newImport;
	}

	private void addImportdata(Import parent, File xml, File pdf) throws ImportException {
		try (FileReader xmlReader = new FileReader(xml)) {
			if (pdf != null) {
				try (FileInputStream pdfInputStream = new FileInputStream(pdf)) {
					String insertSql = "INSERT INTO importdata (parent_id, xml, pdf) VALUES (?, ?, ?)";
					jdbcTemplate.execute(insertSql, (PreparedStatementCallback<Boolean>) preparedStatement -> {
						preparedStatement.setLong(1, parent.getId());
						preparedStatement.setCharacterStream(2, xmlReader);
						preparedStatement.setBinaryStream(3, pdfInputStream);
						return preparedStatement.execute();
					});
				}
			} else {
				String insertSql = "INSERT INTO importdata (parent_id, xml) VALUES (?, ?)";
				jdbcTemplate.execute(insertSql, (PreparedStatementCallback<Boolean>) preparedStatement -> {
					preparedStatement.setLong(1, parent.getId());
					preparedStatement.setCharacterStream(2, xmlReader);
					return preparedStatement.execute();
				});
			}
		} catch (Exception e) {
			throw new InternalImportException(e.getMessage());
		}
	}

	private void addShapefiles(Import i, ShapefileHandler shapefileHandler) {
		for (ShapefileType type : ShapefileType.values()) {
			Set<SimpleFeature> features = shapefileHandler.getFeatures(type);

			switch (type) {
				case INFORMATIONSLINJE:
					i.addInformationslinjer(features);
					break;
				case INFORMATIONSPOLYGON:
					i.addInformationspolygoner(features);
					break;
				case INFORMATIONSPUNKT:
					i.addInformationspunkter(features);
					break;
				case KORSPAR:
					i.addKorspar(features);
					break;
				case KORSPAR_FOR_TRANSPORT:
					i.addKorsparForTransporter(features);
					break;
				case PROVYTA:
					i.addProvytor(features);
					break;
				case RESULTAT:
					i.addResultat(features, shapefileHandler.isSuccess());
					break;
			}
		}
	}
}
