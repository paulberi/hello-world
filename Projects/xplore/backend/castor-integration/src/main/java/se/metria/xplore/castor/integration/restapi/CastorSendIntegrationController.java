package se.metria.xplore.castor.integration.restapi;

import com.metria.finfo.data.finfodatamodell.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.policy.IgnorablePolicyInterceptorProvider;
import org.apache.cxf.ws.policy.PolicyInterceptorProviderRegistry;
import org.datacontract.schemas._2004._07.prosonarealestatedatacontracts.*;
import org.datacontract.schemas._2004._07.prosonarealestateexchangeservice.ProsonaPartnerSelection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tempuri.IProsonaRealestateExchange;
import org.tempuri.ProsonaRealestateExchange;
import se.metria.xplore.castor.integration.service.FastighetsinfoService;
import se.metria.xplore.castor.openapi.api.SendToCastorApi;
import se.metria.xplore.castor.openapi.model.SendToCastorDataDto;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.util.*;

@RequestMapping(value = "/castorapi")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CastorSendIntegrationController implements SendToCastorApi {
    @Value("${castor.auth.customer-id}")
    private String castorCustomerId;

    @Value("${castor.auth.api-key}")
    private String castorApiKey;

    // @Value("${proxy.host}")
    private static String proxyHost ="webproxy.metria.net";

    // @Value("${proxy.port}")
    private static Integer proxyPort = 8080;

    @NonNull
    private final FastighetsinfoService fastighetsService;

    @Override
    @Transactional
    public ResponseEntity<Boolean> sendToCastor(SendToCastorDataDto data) {
        List<Fastighet> fastigheter;
        ProsonaPartnerSelection selection = new ProsonaPartnerSelection();
        try{

            GregorianCalendar createdDate = new GregorianCalendar();
            createdDate.setTime(new Date());
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(createdDate);
            selection.setCreated(xmlDate);
            selection.setName(data.getRequestName());
            selection.setRealestateSelection(new org.datacontract.schemas._2004._07.prosonarealestatedatacontracts.ArrayOfProsonaRealestate());
            var tmp = selection.getRealestateSelection().getProsonaRealestate();
            fastigheter = fastighetsService.getFastLista(data.getRealEstateIdentifierList());
            log.debug("Antal fastigheter: " + fastigheter.size());
            for(Fastighet fastighet : fastigheter){
                ProsonaRealestate realestate = new ProsonaRealestate();
                realestate.setBlock(fastighet.getGallandeBeteckning().getBlocknummer());
                var enhet = fastighet.getGallandeBeteckning().getEnhetsnummer();
                realestate.setEnhet(String.valueOf(enhet));
                realestate.setUUID(fastighet.getUUID());
                realestate.setFnr(fastighet.getFastighetsnyckel());
                realestate.setTrakt(fastighet.getGallandeBeteckning().getTrakt().getTraktnamn());
                realestate.setKommun(fastighet.getGallandeBeteckning().getTrakt().getKommun().getKommunnamn());
                realestate.setTkn(fastighet.getGallandeBeteckning().getTecken());
                realestate.setLansNamn(fastighet.getGallandeBeteckning().getTrakt().getKommun().getLan().getLansnamn());
                realestate.setLanKod(fastighet.getGallandeBeteckning().getTrakt().getKommun().getLan().getLanskod());
                realestate.setKomKod(fastighet.getGallandeBeteckning().getTrakt().getKommun().getKommunkod());
                realestate.setAdresser(new ArrayOfProsonaRealestateAddress());
                for(Adress adr: fastighet.getBelagenhetsadress()){
                    ProsonaRealestateAddress prosonaAdr = new ProsonaRealestateAddress();
                    prosonaAdr.setPostNr(adr.getAktuelltPostnummer().getPostnummer());
                    prosonaAdr.setKomKod(fastighet.getGallandeBeteckning().getTrakt().getKommun().getKommunkod());
                    if (adr instanceof Gardsadressplats) {
                        Gardsadressplats gard = (Gardsadressplats) adr;
                        prosonaAdr.setAdrOmrade(gard.getGardsadressomrade().getNamn());
                        prosonaAdr.setKomdelLage(gard.getGardsadressomrade().getInomBy().getKommundel().getNamn());
                        prosonaAdr.setAdrplTyp(gard.getGardsadressomrade().getInomBy().getAdressomradestyp().getValue());
                        prosonaAdr.setXKoordL(gard.getEKoordinat().toString());
                        prosonaAdr.setYKoord(gard.getNKoordinat().toString());
                    }
                    else if(adr instanceof Adressplats){
                        Adressplats adrPlats = (Adressplats) adr;
                        prosonaAdr.setXKoordL(adrPlats.getEKoordinat().toString());
                        prosonaAdr.setYKoord(adrPlats.getNKoordinat().toString());
                        prosonaAdr.setAdrPlats(adrPlats.getAdressplatsNr().get(0).getValue());
                        
                        if(adr instanceof Gatuadressplats){
                            Gatuadressplats gatu = (Gatuadressplats) adr;
                            prosonaAdr.setAdrOmrade(gatu.getAdressomrade().getAdressomradesnamn());
                            prosonaAdr.setKomdelLage(gatu.getAdressomrade().getKommundel().getNamn());
                        }
                        else if(adr instanceof Byadressplats){
                            Byadressplats bya = (Byadressplats) adr;
                            prosonaAdr.setAdrOmrade(bya.getAdressomrade().getAdressomradesnamn());
                            prosonaAdr.setKomdelLage(bya.getAdressomrade().getKommundel().getNamn());

                        }
                        else if(adr instanceof Metertalsadressplats) {
                            Metertalsadressplats meterplats = (Metertalsadressplats) adr;
                            prosonaAdr.setAdrOmrade(meterplats.getAdressomrade().getAdressomradesnamn());
                            prosonaAdr.setKomdelLage(meterplats.getAdressomrade().getKommundel().getNamn());
                        } else if(adr instanceof Odefinieradadressplats) {
                            Odefinieradadressplats odefinierad = (Odefinieradadressplats) adr;
                            prosonaAdr.setAdrOmrade(odefinierad.getAdressomrade().getAdressomradesnamn());
                            prosonaAdr.setKomdelLage(odefinierad.getAdressomrade().getKommundel().getNamn());
                        }
                    }

                    prosonaAdr.setPostOrt(adr.getAktuelltPostnummer().getInomPostort().getNamn());
                    prosonaAdr.setLanKod(adr.getKommun().getLan().getLanskod());

                    realestate.getAdresser().getProsonaRealestateAddress().add(prosonaAdr);
                }
                if(fastighet.getLagfart().size()>0){
                    realestate.setLagfarter(new ArrayOfProsonaRealestateOwner());
                }
                for(var lagfart: fastighet.getLagfart()){
                    ProsonaRealestateOwner agare = new ProsonaRealestateOwner();
                    agare.setAdresser(new ArrayOfProsonaRealestatePersonAddress());

                    if(lagfart.getAgaruppgifter() instanceof FysiskPerson){
                        FysiskPerson p = (FysiskPerson)lagfart.getAgaruppgifter();
                        agare.setIsOrganisation(false);

                        agare.setEfterNamn(p.getEfternamn());
                        agare.setForNamn(p.getFornamn());
                        agare.setMellanNamn(p.getMellannamn());
                        
                        agare.setPersonOrgNummer(p.getPersonnummer());
                        
                        ProsonaRealestatePersonAddress adr = new ProsonaRealestatePersonAddress();
                        var folkbokf = p.getFolkbokforingsadress();
                        adr.setPostNummer(folkbokf.getPostnummer());
                        adr.setPostOrt(folkbokf.getPostort());
                        if(folkbokf.getUtdelningsadress().size()>0) {
                            adr.setUtdelningsAdr1(folkbokf.getUtdelningsadress().get(0));
                        }
                        if(folkbokf.getUtdelningsadress().size()>1){
                            adr.setUtdelningsAdr2(folkbokf.getUtdelningsadress().get(1));
                        }
                        if(folkbokf.getUtdelningsadress().size()>2){
                            adr.setUtdelningsAdr3(folkbokf.getUtdelningsadress().get(2));
                        }
                        agare.getAdresser().getProsonaRealestatePersonAddress().add(adr);

                    }
                    else if(lagfart.getAgaruppgifter() instanceof Organisation){
                        Organisation o = (Organisation)lagfart.getAgaruppgifter();
                        agare.setIsOrganisation(true);
                        agare.setForNamn(o.getOrganisationensNamn());
                        agare.setPersonOrgNummer(o.getOrganisationsnummer());
                        ProsonaRealestatePersonAddress adr = new ProsonaRealestatePersonAddress();
                        adr.setUtdelningsAdr1(o.getUtdelningsadress1());
                        adr.setPostOrt(o.getPostort());
                        adr.setPostNummer(o.getPostnummer());
                        adr.setUtdelningsAdr2(o.getUtdelningsadress2());
                        adr.setNamnCo(o.getOrganisationensNamn());
                        agare.getAdresser().getProsonaRealestatePersonAddress().add(adr);
                    }
                    realestate.getLagfarter().getProsonaRealestateOwner().add(agare);
                }
                selection.getRealestateSelection().getProsonaRealestate().add(realestate);
            }

            ProsonaRealestateExchange castorService = new ProsonaRealestateExchange();
            var port = getCastorPort(castorService);
            log.debug("Castor customerId: " + castorCustomerId);
            log.debug("Castor ApiKey: " + castorApiKey);
            port.transferSelectionToProsona(selection, castorCustomerId, castorApiKey);
        }
        catch(Exception e){
            throw new RuntimeException("Error sending to Castor", e);
        }

        return ResponseEntity.ok(true);
    }

    public static IProsonaRealestateExchange getCastorPort(ProsonaRealestateExchange castorService){
        var port = castorService.getPort(IProsonaRealestateExchange.class);


        var client = ClientProxy.getClient(port);

        //För att komma runt felet
        //PolicyException: These policy alternatives can not be satisfied
        Bus bus = client.getBus();
        PolicyInterceptorProviderRegistry reg = bus.getExtension(PolicyInterceptorProviderRegistry.class);
        Set<QName> set = new HashSet<>();
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "TransportBinding"));
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "TransportToken"));
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "HttpsToken"));
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "AlgorithmSuite"));
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "Basic256"));
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "Layout"));
        set.add(new QName("http://schemas.xmlsoap.org/ws/2005/07/securitypolicy", "Strict"));
        reg.register(new IgnorablePolicyInterceptorProvider(set));

        //För att (försöka) komma runt att serverns certifikat inte
        //anses vara giltigt
        HTTPConduit httpConduit=(HTTPConduit)client.getConduit();

        httpConduit.getClient().setProxyServer(proxyHost);
        httpConduit.getClient().setProxyServerPort(proxyPort);


        TLSClientParameters tlsCP = new TLSClientParameters();
        tlsCP.setDisableCNCheck(true);
        httpConduit.setTlsClientParameters(tlsCP);
        return port;
    }
}
