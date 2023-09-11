package se.metria.skogsmaskindata.service.forestlink;

import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import com.jcraft.jsch.JSchException;

import se.metria.skogsmaskindata.service.imports.DefaultImportsService;
import se.metria.skogsmaskindata.service.imports.exceptions.AlreadyImportedException;
import se.metria.skogsmaskindata.service.imports.exceptions.ImportException;
import se.metria.skogsmaskindata.service.imports.model.Import;
import se.metria.skogsmaskindata.service.imports.shapefile.ShapefileException;
import se.metria.skogsmaskindata.service.sftp.SftpService;
import se.metria.skogsmaskindata.service.sftp.exceptions.FileRetrievalException;
import se.metria.skogsmaskindata.service.sftp.exceptions.ListRemoteFilesException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static com.googlecode.catchexception.apis.BDDCatchException.when;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


class ForestlinkServiceTest {

	private SftpService sftpService;
	private DefaultImportsService importsService;
	private ForestLinkService forestLinkService;

	@BeforeEach
	void setup() {
		sftpService = mock(SftpService.class);
		importsService = mock(DefaultImportsService.class);
		forestLinkService = new ForestLinkService(importsService, sftpService);
	}

	@Test
	void importPackages_successful() throws JSchException, ListRemoteFilesException, ImportException, AlreadyImportedException, FileRetrievalException, ShapefileException {

		//given
		String outboxPath = "/Outbox/*.zip";
		given(sftpService.listFilesOnRemote("*")).willReturn(Arrays.asList("OrganisationA", "OrganisationB", "OrganisationC"));
		given(sftpService.listFilesOnRemote("OrganisationA" + outboxPath)).willReturn(Arrays.asList("PaketA1", "PaketA2"));
		given(sftpService.listFilesOnRemote("OrganisationB" + outboxPath)).willReturn(Arrays.asList("PaketB1"));
		given(sftpService.listFilesOnRemote("OrganisationC" + outboxPath)).willReturn(Arrays.asList("PaketC1", "PaketC2", "PaketC3"));
		given(sftpService.getRemoteFile(any())).willReturn(new ByteArrayInputStream(new byte[0]));
		given(importsService.processForestlinkPackage(notNull())).willReturn(new Import("", "", OffsetDateTime.now(), ""));

		//when
		List<Import> imports = forestLinkService.importPackages();

		//then
		assertEquals(6, imports.size());
	}

	@Test
	void importPackages_no_initial_sftp_connection() throws JSchException, ListRemoteFilesException {

		given(sftpService.listFilesOnRemote("*")).willThrow(new JSchException("TEST"));

		when(() -> forestLinkService.importPackages());

		then(caughtException()).isInstanceOf(JSchException.class);

	}

	@Test
	void importPackages_cant_list_organizations() throws JSchException, ListRemoteFilesException {

		given(sftpService.listFilesOnRemote("*")).willThrow(new ListRemoteFilesException("TEST", "TEST"));

		when(() -> forestLinkService.importPackages());

		then(caughtException()).isInstanceOf(ListRemoteFilesException.class);
	}

	@Test
	void importPackages_failed_to_retrieve_orgs() throws JSchException, ListRemoteFilesException, ImportException, AlreadyImportedException, FileRetrievalException, ShapefileException {

		//given
		String outboxPath = "/Outbox/*.zip";
		given(sftpService.listFilesOnRemote("*")).willReturn(Arrays.asList("OrganisationA", "OrganisationB", "OrganisationC"));
		given(sftpService.listFilesOnRemote("OrganisationA" + outboxPath)).willReturn(Arrays.asList("PaketA1", "PaketA2"));
		given(sftpService.listFilesOnRemote("OrganisationB" + outboxPath)).willThrow(new JSchException("TEST"));
		given(sftpService.listFilesOnRemote("OrganisationC" + outboxPath)).willThrow(new ListRemoteFilesException("TEST", "TEST"));
		given(sftpService.getRemoteFile(any())).willReturn(new ByteArrayInputStream(new byte[0]));
		given(importsService.processForestlinkPackage(notNull())).willReturn(new Import("", "", OffsetDateTime.now(), ""));

		//when
		List<Import> imports = forestLinkService.importPackages();

		//then
		assertEquals(2, imports.size());
	}

	@Test
	void importPackages_failed_to_retrieve_one_out_of_two__files() throws JSchException, ListRemoteFilesException, ImportException, AlreadyImportedException, FileRetrievalException, ShapefileException {

		//given
		String organisation = "OrganisationA";
		String outboxPath = organisation + "/Outbox/";
		given(sftpService.listFilesOnRemote("*")).willReturn(Arrays.asList(organisation));
		given(sftpService.listFilesOnRemote(outboxPath + "*.zip")).willReturn(Arrays.asList("PaketA1", "PaketA2", "PaketA3"));
		given(sftpService.getRemoteFile(outboxPath + "PaketA1")).willReturn(new ByteArrayInputStream(new byte[0]));
		given(sftpService.getRemoteFile(outboxPath + "PaketA2")).willThrow(new JSchException("TEST"));
		given(sftpService.getRemoteFile(outboxPath + "PaketA3")).willThrow(new FileRetrievalException("TEST", "TEST"));
		given(importsService.processForestlinkPackage(notNull())).willReturn(new Import("", "", OffsetDateTime.now(), ""));

		//when
		List<Import> imports = forestLinkService.importPackages();

		//then
		assertEquals(1, imports.size());
	}

}