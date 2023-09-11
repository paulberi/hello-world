package se.metria.skogsmaskindata.service.forestlink;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jcraft.jsch.JSchException;

import se.metria.skogsmaskindata.service.imports.ImportService;
import se.metria.skogsmaskindata.service.imports.exceptions.AlreadyImportedException;
import se.metria.skogsmaskindata.service.imports.exceptions.CorruptImportPackageException;
import se.metria.skogsmaskindata.service.imports.model.Import;
import se.metria.skogsmaskindata.service.sftp.SftpService;
import se.metria.skogsmaskindata.service.sftp.exceptions.FileDeletionException;
import se.metria.skogsmaskindata.service.sftp.exceptions.FileMoveException;
import se.metria.skogsmaskindata.service.sftp.exceptions.ListRemoteFilesException;

import org.springframework.stereotype.Service;

@Service
public class ForestLinkService {

	private final String OUTBOX_PATH = "/Outbox/";
	private final String ERROR_PATH = "/Error/";
	private ImportService importService;
	private SftpService sftpService;

	public ForestLinkService(ImportService importService, SftpService sftpService) {
		this.importService = importService;
		this.sftpService = sftpService;
	}

	public List<Import> importPackages() throws ListRemoteFilesException, JSchException {
		try {
			List<String> organizations = this.sftpService.listFilesOnRemote("*");
			List<String> filepaths = new ArrayList<>();
			organizations.forEach(organization -> {
				String organisationOutboxPath = organization + OUTBOX_PATH;
				try {
					filepaths.addAll(this.sftpService.listFilesOnRemote(organisationOutboxPath + "*.zip").stream().map(file -> organisationOutboxPath + file).collect(Collectors.toList()));
				} catch (JSchException e) {
					System.out.println("Unable to obtain SFTP connection when attempting to list files for " + organization + ": " + e.getMessage());
				} catch (ListRemoteFilesException e) {
					System.out.println("Unable to list files for " + organization + ": " + e.getMessage());
				}
			});

			System.out.println("Files to retrieve:");
			filepaths.forEach(System.out::println);

			return filepaths.stream().map(filePath -> {
				Import anImport = null;
				boolean alreadyImported = false;
				boolean corruptPackage = false;

				try {
					anImport = this.importService.processForestlinkPackage(sftpService.getRemoteFile(filePath));
				} catch (Exception e) {
					corruptPackage = e instanceof CorruptImportPackageException;
					alreadyImported = e instanceof AlreadyImportedException;
					debugTrace(e, filePath);
				}

				if (alreadyImported || (anImport != null && anImport.isCompleted())) {
					try {
						this.sftpService.deleteRemoteFile(filePath);
					} catch (FileDeletionException e) {
						debugTrace(e, filePath);
					}
				} else if (corruptPackage) {
					try {
						this.sftpService.moveRemoteFile(filePath, filePath.replace(OUTBOX_PATH, ERROR_PATH));
					} catch(FileMoveException e) {
						debugTrace(e, filePath);
					}
				}

				return anImport;
			}).filter(Objects::nonNull).collect(Collectors.toList());
		} catch (ListRemoteFilesException e) {
			System.out.println("Unable to list files on remote: " + e.getMessage());
			throw e;
		} catch (JSchException e) {
			System.out.println("Unable to obtain SFTP connection:" + e.getMessage());
			throw e;
		}
	}

	private void debugTrace(Exception e, String filePath) {
		System.out.println(filePath + " : " + e.getClass().getSimpleName() + " : " + e.getMessage());
	}
}
