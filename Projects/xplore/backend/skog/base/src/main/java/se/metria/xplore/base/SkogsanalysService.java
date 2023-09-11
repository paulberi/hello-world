package se.metria.xplore.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.metria.xplore.fme.FmeClient;
import se.metria.xplore.fme.FmeError;

public abstract class SkogsanalysService<T, V> {

    protected T properties;
    protected FmeClient<String, V> fmeClient;

    public SkogsanalysService(T properties, FmeClient fmeClient) {
        this.properties = properties;
        this.fmeClient = fmeClient;
    }

    protected boolean validateRequest(SkogligRequest request)
    {
        if (request.wkt == null && request.delomrade == null && request.fastighet == null )
        {
            return false;
        }

        return true;
    }

    protected String createRequestBody(SkogligRequest request)
    {
        if (request.wkt != null)
        {
            return String.format("polygon=%s", request.wkt);
        }
        else if (request.delomrade != null)
        {
            return String.format("fastighetsdelomrade=%s", request.delomrade);
        }
        else if (request.fastighet != null)
        {
            return String.format("fastighet=%s", request.fastighet);
        }

        return null;
    }

    protected ResponseEntity<V> invalidRequest()
    {
        return new ResponseEntity(FmeError.INVALID_REQUEST.name(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}