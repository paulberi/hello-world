package se.metria.xplore.kundconfig.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import se.metria.xplore.keycloak.service.KeyCloakService;
import se.metria.xplore.kundconfig.entity.KundEntity;
import se.metria.xplore.kundconfig.openapi.model.KundDto;
import se.metria.xplore.kundconfig.openapi.model.KundInfoDto;
import se.metria.xplore.kundconfig.openapi.model.KundPageDto;
import se.metria.xplore.kundconfig.repository.KundConfigRepository;
import se.metria.xplore.kundconfig.security.KundConfigRole;
import se.metria.xplore.kundconfig.service.geoserver.GeofenceService;
import se.metria.xplore.kundconfig.service.geoserver.GeoserverService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

import static se.metria.xplore.kundconfig.util.CollectionUtil.modelMapperList;

@Service
@RequiredArgsConstructor
@Slf4j
public class KundService {
    @NonNull
    private final KundConfigRepository kundConfigRepository;

    @NonNull
    private final GeoserverService geoserverService;

    @NonNull
    private final GeofenceService geofenceService;

    @NonNull
    private final KeyCloakService keyCloakService;

    @NonNull
    private final AuthService authService;

    @NonNull
    private final ModelMapper modelMapper;

    public KundDto getKundById(String kundId){
        return modelMapper.map(kundConfigRepository.getOne(kundId), KundDto.class);
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    @Transactional(rollbackFor = Exception.class)
    public KundDto createKund(KundInfoDto newKundInfo) throws JsonProcessingException {
        log.info("Skapar ny kund och sätter upp behörigheter");

        var orgnr = newKundInfo.getOrganisationsnummer();
        var namn = newKundInfo.getNamn();

        var kundEntity = modelMapper.map(newKundInfo, KundEntity.class);
        kundEntity.setId(orgnr);

        try {
            keyCloakService.createRealmRole(orgnr, namn);
        } catch (WebClientResponseException ex) {
            // Status 409 CONFLICT = det finns redan en roll, fortsätt skapa upp kund
            if (ex.getStatusCode() != HttpStatus.CONFLICT) {
                throw ex;
            }
        }

        geoserverService.createRole(orgnr);
        geoserverService.createRole(getExternalRole(orgnr));
        geofenceService.deleteRulesForKund(orgnr);
        geofenceService.createRulesForKund(orgnr);

        try {
            var newKund = this.kundConfigRepository.saveAndFlush(kundEntity);
            authService.createFastighetsokAuth(orgnr);
            authService.createMetriaMapsAuth(orgnr);

            log.info("Ny kund {} skapad: ", newKund);

            return modelMapper.map(newKund, KundDto.class);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Name already exists");
        }
    }

    public List<KundDto> getKunder(){
        return modelMapperList(kundConfigRepository.findAll(), modelMapper, KundDto.class);
    }

    public KundPageDto getKundPage(Integer page, Integer size) {
        var pageRequest = PageRequest.of(page, size, Sort.by("skapadDatum").descending());
        var kundPage = kundConfigRepository.findAll(pageRequest);

        return modelMapper.map(kundPage, KundPageDto.class);
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    @Transactional(rollbackFor = Exception.class)
    public KundDto updateKund(String kundId, KundInfoDto kund){
        var entity = kundConfigRepository.getOne(kundId);
        entity.setNamn(kund.getNamn());
        entity.setEpost(kund.getEpost());
        entity.setKontaktperson(kund.getKontaktperson());
        entity.setTelefon(kund.getTelefon());

        log.info("Kund {} uppdaterad", kundId);

        return modelMapper.map(entity, KundDto.class);
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    @Transactional(rollbackFor = Exception.class)
    public void deleteKund(String kundId) throws JsonProcessingException {
        if (kundConfigRepository.existsById(kundId)){
            kundConfigRepository.deleteById(kundId);
        }
        geofenceService.deleteRulesForKund(kundId);
        geoserverService.deleteRole(kundId);
        geoserverService.deleteRole(getExternalRole(kundId));
    }

    @RolesAllowed(KundConfigRole.GLOBAL_ADMIN)
    @Transactional(rollbackFor = Exception.class)
    public void resetGeofenceRules() throws JsonProcessingException {
        var kundIds = kundConfigRepository.findAll()
                .stream()
                .map(kund -> kund.getId())
                .collect(Collectors.toList());

        geofenceService.resetAllRules(kundIds);
    }

    private String getExternalRole(String roleName) {
        return roleName + ":EXTERN";
    }
}
