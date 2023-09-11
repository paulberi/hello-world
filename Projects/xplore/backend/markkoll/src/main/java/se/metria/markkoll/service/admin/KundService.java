package se.metria.markkoll.service.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.admin.UserRepository;
import se.metria.markkoll.service.UtskicksnummerService;
import se.metria.markkoll.service.geofence.GeofenceService;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.xplore.geoserveradmin.service.GeoserverAdminService;

import java.util.List;

@MarkkollService
@RequiredArgsConstructor
@Slf4j
public class KundService {
    @NonNull
    private final AclService aclService;

    @NonNull
    private final GeofenceService geofenceService;

    @NonNull
    private final GeoserverAdminService geoserverAdminService;

    @NonNull
    private final KundRepository kundRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final UtskicksnummerService utskicksnummerService;

    @Value("${markkoll.admin.disable-external-services-create:false}")
    boolean disableExternalServicesCreate;

    @Transactional(rollbackFor = JsonProcessingException.class)
    public void createIfNotExists(String kundId) throws JsonProcessingException {
        if (kundRepository.existsById(kundId)) {
            return;
        }

        log.info("Skapar ny kund: " + kundId);

        kundRepository.save(KundEntity.of(kundId));

        // Vi gör en koll eftersom de här objekten redan existerar om man har kört migrationsscript
        if (!aclService.objectExists(kundId, KundEntity.class)) {
            aclService.createObject(kundId, KundEntity.class);
        }

        utskicksnummerService.create(kundId);

        if (!disableExternalServicesCreate) {
            geoserverAdminService.createRole(kundId);
            geoserverAdminService.createRole(getExternalRole(kundId));

            geofenceService.deleteRulesForKund(kundId);
            geofenceService.createRulesForKund(kundId);
        }
    }

    public boolean exists(String kundId) {
        return kundRepository.existsById(kundId);
    }

    public List<MarkkollUserDto> getUsers(String kundId) {
        var entities = userRepository.getUsersForKund(kundId);

        return CollectionUtil.modelMapperList(entities, modelMapper, MarkkollUserDto.class);
    }

    public String getExternalRole(String roleName) {
        return roleName + ":EXTERN";
    }
}
