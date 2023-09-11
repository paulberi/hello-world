package se.metria.markkoll.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import se.metria.kundconfig.openapi.model.FastighetsokAuthDto;
import se.metria.markkoll.openapi.finfo.api.AgareApi;
import se.metria.markkoll.openapi.finfo.api.ImportJobApi;
import se.metria.markkoll.openapi.finfo.api.RegisterenhetApi;
import se.metria.markkoll.openapi.finfo.api.SamfallighetsforeningApi;
import se.metria.markkoll.openapi.finfo.model.*;
import se.metria.markkoll.service.finfo.FinfoJobException;
import se.metria.markkoll.service.finfo.FinfoJobFailedException;
import se.metria.markkoll.service.finfo.FinfoJobTimedOutException;
import se.metria.markkoll.service.finfo.FinfoService;

import java.util.UUID;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static se.metria.markkoll.testdata.TestData.mockUUID;

@ExtendWith(MockitoExtension.class)
public class FinfoServiceTest {
    @InjectMocks
    FinfoService finfoService;

    @Mock
    AgareApi agareApi;

    @Mock
    RegisterenhetApi registerenhetApi;

    @Mock
    SamfallighetsforeningApi samfallighetsforeningApi;

    @Mock
    ImportJobApi importJobApi;

    @Mock
    KundConfigService kundConfigService;

    @Test
    void importSamfallighetsforening() throws FinfoJobException {
        // Given
        var importJobStatusDto = FinfoImportJobStatusDto.DONE;
        var registerenhetId = mockUUID(0);
        var importJobId = mockUUID(1);

        var auth = fastighetsokAuthDto();
        when(kundConfigService.getFastighetsokAuth()).thenReturn(auth);

        var request = finfoRegisterenhetRequestDto(registerenhetId);
        var responseEntity = importJobresponseEntity(importJobId);
        when(samfallighetsforeningApi.createSamfallighetsforeningJobWithHttpInfo(eq(request))).thenReturn(responseEntity);

        var samfExpect = new FinfoSamfallighetsforeningDto();
        when(samfallighetsforeningApi.getSamfallighetsforening(eq(registerenhetId))).thenReturn(samfExpect);

        var jobInfo = new FinfoImportJobInfoDto()
            .resource(registerenhetId.toString())
            .status(importJobStatusDto);
        when(importJobApi.getImportJobStatus(any())).thenReturn(jobInfo);

        // When
        var samfActual = finfoService.importSamfallighetsforening(registerenhetId);

        // Then
        assertSame(samfExpect, samfActual);
    }

    @Test
    void importSamfallighetsforeningTimedOut() {
        // Given
        var registerenhetId = mockUUID(0);
        var importJobId = mockUUID(1);
        var importJobStatusDto = FinfoImportJobStatusDto.IN_PROGRESS;

        var auth = fastighetsokAuthDto();
        when(kundConfigService.getFastighetsokAuth()).thenReturn(auth);

        var request = finfoRegisterenhetRequestDto(registerenhetId);
        var responseEntity = importJobresponseEntity(importJobId);
        when(samfallighetsforeningApi.createSamfallighetsforeningJobWithHttpInfo(eq(request))).thenReturn(responseEntity);

        var jobInfo = new FinfoImportJobInfoDto().status(importJobStatusDto);
        when(importJobApi.getImportJobStatus(any())).thenReturn(jobInfo);

        assertThrows(FinfoJobTimedOutException.class, () -> finfoService.importSamfallighetsforening(registerenhetId));
    }

    @Test
    void importSamfallighetsforeningFailed() {
        // Given
        var registerenhetId = mockUUID(0);
        var importJobId = mockUUID(1);
        var importJobStatusDto = FinfoImportJobStatusDto.FAILED;

        var auth = fastighetsokAuthDto();
        when(kundConfigService.getFastighetsokAuth()).thenReturn(auth);

        var request = finfoRegisterenhetRequestDto(registerenhetId);
        var responseEntity = importJobresponseEntity(importJobId);
        when(samfallighetsforeningApi.createSamfallighetsforeningJobWithHttpInfo(eq(request))).thenReturn(responseEntity);

        var jobInfo = new FinfoImportJobInfoDto().status(importJobStatusDto);
        when(importJobApi.getImportJobStatus(any())).thenReturn(jobInfo);

        assertThrows(FinfoJobFailedException.class, () -> finfoService.importSamfallighetsforening(registerenhetId));
    }

    @Test
    void importRegisterenhet() throws FinfoJobException {
        // Given
        var importJobStatusDto = FinfoImportJobStatusDto.DONE;
        var registerenhetId = mockUUID(0);
        var importJobId = mockUUID(1);

        var auth = fastighetsokAuthDto();
        when(kundConfigService.getFastighetsokAuth()).thenReturn(auth);

        var request = finfoRegisterenhetRequestDto(registerenhetId);
        var responseEntity = importJobresponseEntity(importJobId);
        when(registerenhetApi.createAllmanMiniJobWithHttpInfo(eq(request))).thenReturn(responseEntity);

        var samfExpect = new FinfoSamfallighetDto();
        when(registerenhetApi.getAllmanMini(eq(registerenhetId))).thenReturn(samfExpect);

        var jobInfo = new FinfoImportJobInfoDto()
            .resource(registerenhetId.toString())
            .status(importJobStatusDto);
        when(importJobApi.getImportJobStatus(any())).thenReturn(jobInfo);

        // When
        var samfActual = finfoService.importRegisterenhet(registerenhetId);

        // Then
        assertSame(samfExpect, samfActual);
    }

    @Test
    void importRegisterenhetTimedOut() {
        // Given
        var registerenhetId = mockUUID(0);
        var importJobId = mockUUID(1);
        var importJobStatusDto = FinfoImportJobStatusDto.IN_PROGRESS;

        var auth = fastighetsokAuthDto();
        when(kundConfigService.getFastighetsokAuth()).thenReturn(auth);

        var request = finfoRegisterenhetRequestDto(registerenhetId);
        var responseEntity = importJobresponseEntity(importJobId);
        when(registerenhetApi.createAllmanMiniJobWithHttpInfo(eq(request))).thenReturn(responseEntity);

        var jobInfo = new FinfoImportJobInfoDto().status(importJobStatusDto);
        when(importJobApi.getImportJobStatus(any())).thenReturn(jobInfo);

        assertThrows(FinfoJobTimedOutException.class, () -> finfoService.importRegisterenhet(registerenhetId));
    }

    @Test
    void importRegisterenhetFailed() {
        // Given
        var registerenhetId = mockUUID(0);
        var importJobId = mockUUID(1);
        var importJobStatusDto = FinfoImportJobStatusDto.FAILED;

        var auth = fastighetsokAuthDto();
        when(kundConfigService.getFastighetsokAuth()).thenReturn(auth);

        var request = finfoRegisterenhetRequestDto(registerenhetId);
        var responseEntity = importJobresponseEntity(importJobId);
        when(registerenhetApi.createAllmanMiniJobWithHttpInfo(eq(request))).thenReturn(responseEntity);

        var jobInfo = new FinfoImportJobInfoDto().status(importJobStatusDto);
        when(importJobApi.getImportJobStatus(any())).thenReturn(jobInfo);

        assertThrows(FinfoJobFailedException.class, () -> finfoService.importRegisterenhet(registerenhetId));
    }

    FastighetsokAuthDto fastighetsokAuthDto() {
        return new FastighetsokAuthDto()
            .username("username")
            .password("password")
            .kundmarke("kundmarke");
    }

    FinfoRegisterenhetRequestDto finfoRegisterenhetRequestDto(UUID registerenhetId) {
        return new FinfoRegisterenhetRequestDto()
            .username("username")
            .password("password")
            .kundmarke("kundmarke")
            .nyckel(registerenhetId.toString());
    }

    ResponseEntity<Void> importJobresponseEntity(UUID importJobId) {
        return ResponseEntity.ok().header(HttpHeaders.LOCATION, "/"+importJobId).build();
    }
}
