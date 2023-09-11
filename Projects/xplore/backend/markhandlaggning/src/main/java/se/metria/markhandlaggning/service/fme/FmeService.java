package se.metria.markhandlaggning.service.fme;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import se.metria.markhandlaggning.MarkhandlaggningProperties;
import se.metria.markhandlaggning.openapi.model.FmeResponseDto;
import se.metria.markhandlaggning.openapi.model.ProjektStatusDto;
import se.metria.markhandlaggning.service.projekt.ProjektRepository;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.fme.FmeError;

import java.util.UUID;

@Service
public class FmeService {

    @Value("${miljo}")
    private String miljo;

    @NonNull
    private final ProjektRepository projektRepository;

    protected MarkhandlaggningProperties properties;
    protected FmeClient<String, FmeResponseDto> fmeClient;

    public FmeService(MarkhandlaggningProperties properties,
                      FmeClient<String, FmeResponseDto> fmeClient,
                      ProjektRepository projektRepository
                      ) {
        this.fmeClient = fmeClient;
        this.properties = properties;
        this.projektRepository = projektRepository;
    }

    public ResponseEntity<FmeResponseDto> submitJob(UUID id){
        String formattedId = String.format("projektId=%s&miljo=%s", id, this.miljo);
        try{
            return this.fmeClient.jobSubmitter(properties.getFmeScript(), formattedId, FmeResponseDto.class);
        }
        catch (HttpClientErrorException.UnprocessableEntity e){
            this.projektRepository.getOne(id).setStatus(ProjektStatusDto.MISSLYCKADES.toString());
            return invalidRequest();
        }
    }

    protected ResponseEntity<FmeResponseDto> invalidRequest()
    {
        return new ResponseEntity(FmeError.INVALID_REQUEST.name(), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
