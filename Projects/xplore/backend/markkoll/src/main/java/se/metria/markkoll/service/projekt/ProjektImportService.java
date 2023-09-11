package se.metria.markkoll.service.projekt;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.metria.markkoll.openapi.model.GeometristatusDto;
import se.metria.markkoll.repository.fastighet.FastighetRepository;
import se.metria.markkoll.repository.intrang.FastighetsintrangRepository;
import se.metria.markkoll.repository.projekt.ProjektRepository;
import se.metria.markkoll.service.fastighet.RegisterenhetImportService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class ProjektImportService {
    @NonNull
    private final AvtalUpdater avtalUpdater;

    @NonNull
    private final ProjektService projektService;

    @NonNull
    private final FastighetRepository fastighetRepository;

    @NonNull
    private final FastighetsintrangRepository fastighetsintrangRepository;

    @NonNull
    private final ProjektRepository projektRepository;

    @NonNull
    private final RegisterenhetImportService registerenhetImportService;

    @Transactional
    public void
    updateProjektVersion(UUID projektId, UUID currentVersionId, List<UUID> fastighetIds) {
        var previousVersionId = projektService.setCurrentVersion(projektId, currentVersionId);

        if (previousVersionId == null) {
            updateAvtal(projektId, fastighetIds);
        }
        else {
            updateAvtal(projektId, currentVersionId, previousVersionId, fastighetIds);
        }

        log.info("Projektimport klar");
    }

    @Transactional
    public void importRegisterenheter(UUID projektId, Collection<UUID> registerenhetIds) {
        registerenhetIds.forEach(registerenhetId -> importRegisterenhet(projektId, registerenhetId));
    }

    @Transactional
    public void importRegisterenhet(UUID projektId, UUID registerenhetId) {
        log.info("Importerar registerenhet {} till projekt {}", registerenhetId, projektId);

        if (projektRepository.containsFastighet(projektId, registerenhetId)) {
            avtalUpdater.updateAvtal(projektId, registerenhetId);
        }
        else {
            registerenhetId = registerenhetImportService.importRegisterenhet(registerenhetId)
                .orElseThrow(IllegalArgumentException::new);

            avtalUpdater.addManuelltAvtal(projektId, registerenhetId);
        }
    }

    private List<UUID>
    getAddedRegisterenheter(Collection<UUID> previousRegisterenhetIds,
                            Collection<UUID> currentRegisterenhetIds) {

        return currentRegisterenhetIds.stream()
            .filter(id -> !previousRegisterenhetIds.contains(id))
            .collect(Collectors.toList());
    }

    private List<UUID>
    getRemovedRegisterenheter(Collection<UUID> previousRegisterenhetIds,
                              Collection<UUID> currentRegisterenhetIds) {

        return previousRegisterenhetIds.stream()
            .filter(id -> !currentRegisterenhetIds.contains(id))
            .collect(Collectors.toList());
    }

    private List<UUID>
    getRetainedRegisterenheter(Collection<UUID> previousRegisterenhetIds,
                               Collection<UUID> currentRegisterenhetIds) {

        return currentRegisterenhetIds.stream()
            .filter(id -> previousRegisterenhetIds.contains(id))
            .collect(Collectors.toList());
    }

    private void updateAvtal(UUID projektId, List<UUID> fastighetIds) {
        log.info("Inleder projektimport för tomt projekt {}...", projektId);

        avtalUpdater.addImportAvtal(projektId, fastighetIds, GeometristatusDto.OFORANDRAD);
    }

    private void
    updateAvtal(UUID projektId, UUID currentVersionId, UUID previousVersionId, List<UUID> currentFastighetIds) {

        log.info("Inleder projektimport för projekt {}, med tidigare version {}...", projektId, previousVersionId);

        var previousRegisterenhetIds = fastighetRepository.getAllByVersionId(previousVersionId);

        updateAddedRegisterenheter(projektId, previousRegisterenhetIds, currentFastighetIds);

        updateRemovedRegisterenheter(projektId, previousRegisterenhetIds, currentFastighetIds);

        updateRetainedRegisterenheter(projektId, previousVersionId, currentVersionId, previousRegisterenhetIds,
            currentFastighetIds);
    }

    private void
    updateAddedRegisterenheter(UUID projektId, List<UUID> previousRegisterenhetIds, List<UUID> currentRegisterenhetIds) {

        var addedRegisterenheter = getAddedRegisterenheter(previousRegisterenhetIds, currentRegisterenhetIds);

        var existingRegisterenheter = projektRepository.filterExistingRegisterenheterInProjekt(projektId,
            addedRegisterenheter);

        var newRegisterenheter = new ArrayList<>(addedRegisterenheter);
        newRegisterenheter.removeAll(existingRegisterenheter);

        avtalUpdater.addImportAvtal(projektId, newRegisterenheter, GeometristatusDto.NY);
        avtalUpdater.updateAvtal(projektId, existingRegisterenheter, GeometristatusDto.NY);
    }

    private void
    updateRemovedRegisterenheter(UUID projektId, List<UUID> previousRegisterenhetIds, List<UUID> currentRegisterenhetIds) {

        var removedRegisterenheter = getRemovedRegisterenheter(previousRegisterenhetIds, currentRegisterenhetIds);
        avtalUpdater.updateAvtal(projektId, removedRegisterenheter, GeometristatusDto.BORTTAGEN);
    }

    private void
    updateRetainedRegisterenheter(UUID projektId,
                                  UUID previousVersionId,
                                  UUID currentVersionId,
                                  List<UUID> previousRegisterenhetIds,
                                  List<UUID> currentRegisterenhetIds)
    {

        var retainedOforandrad = new ArrayList<UUID>();
        var retainedUppdaterad = new ArrayList<UUID>();
        for (var rid: getRetainedRegisterenheter(previousRegisterenhetIds, currentRegisterenhetIds)) {
            var geometriesEqual = fastighetsintrangRepository.geometriesEqual(rid, previousVersionId,
                currentVersionId);
            
            /* geometriesEqual blir null om vi försöker oss på jämförelser på fastigheter som saknar geometrier (t.ex.
            outredda fastigheter). Vi kan anta att geometristatusen för sagda fastighet nog är oförändrad då. */
            var geometristatusOforandrad =
                geometriesEqual == null ||
                (geometriesEqual && allAvtalstyperEqual(rid, previousVersionId, currentVersionId));
            if (geometristatusOforandrad) {
                retainedOforandrad.add(rid);
            }
            else {
                retainedUppdaterad.add(rid);
            }
        }

        avtalUpdater.updateAvtal(projektId, retainedOforandrad, GeometristatusDto.OFORANDRAD);
        avtalUpdater.updateAvtal(projektId, retainedUppdaterad, GeometristatusDto.UPPDATERAD);
    }

    private boolean allAvtalstyperEqual(UUID registerenhetId, UUID previousVersionId, UUID currentVersionId) {
        return fastighetsintrangRepository.avtalstypEqual(registerenhetId, previousVersionId, currentVersionId)
            .stream().allMatch(av -> av == true);
    }
}
