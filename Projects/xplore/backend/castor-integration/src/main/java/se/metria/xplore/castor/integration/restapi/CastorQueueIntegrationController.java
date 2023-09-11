package se.metria.xplore.castor.integration.restapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ProsonaPartnerSelection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tempuri.ProsonaRealestateExchange;
import se.metria.xplore.castor.openapi.api.GetCastorSelectionsApi;
import se.metria.xplore.castor.openapi.model.CastorSelectionItemDto;
import se.metria.xplore.castor.openapi.model.RealEstateIdentifierDto;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/castorapi")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CastorQueueIntegrationController implements GetCastorSelectionsApi {
    @Value("${castor.auth.customer-id}")
    private String castorCustomerId;

    @Value("${castor.auth.api-key}")
    private String castorApiKey;

    @Override
    public ResponseEntity<List<CastorSelectionItemDto>> getCastorSelections() {
        ArrayList<CastorSelectionItemDto> result = new ArrayList<>();
        //Testsvar
//        CastorSelectionItemDto testItem = new CastorSelectionItemDto();
//        testItem.setCreated(new DateTime().toLocalDateTime().toString());
//        testItem.setName("Testnamn");
//        testItem.addSelectionListItem(new RealEstateIdentifierDto()
//                .beteckning("VALDEMARSVIK ÅBY 2:6")
//                .uuid("909a6a4d-676e-90ec-e040-ed8f66444c3f")
//        );
//        result.add(testItem);

        //Slut på testsvar
        try{
            var castorService = new ProsonaRealestateExchange();
            var port = CastorSendIntegrationController.getCastorPort(castorService);
            log.debug("Castor customerId: " + castorCustomerId);
            log.debug("Castor ApiKey: " + castorApiKey);
            var selections = port.getPartnerSelections(castorCustomerId, castorApiKey);

            for(ProsonaPartnerSelection selection: selections.getProsonaPartnerSelection()){
                CastorSelectionItemDto item = new CastorSelectionItemDto();
                item.setName(selection.getName());
                item.setCreated(selection.getCreated()
                            .toGregorianCalendar()
                            .toZonedDateTime()
                            .toLocalDateTime().toString());

                for(var estate: selection.getRealestateSelection().getProsonaRealestate()){
                    RealEstateIdentifierDto e = new RealEstateIdentifierDto();
                    e.setUuid(estate.getUUID());
                    e.setBeteckning(estate.getKommun() + " " + estate.getTrakt()
                            + " " + estate.getBlock() + estate.getTkn() + estate.getEnhet());
                    item.addSelectionListItem(e);
                }

                result.add(item);
            }
        }
        catch(Exception e){
            throw new RuntimeException("Error getting queue from Castor", e);
        }

        return ResponseEntity.ok(result);
    }
}