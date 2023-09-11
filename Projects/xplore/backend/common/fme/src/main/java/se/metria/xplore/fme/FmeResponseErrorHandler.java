package se.metria.xplore.fme;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;

public class FmeResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clienthttpresponse) throws IOException {
        return !clienthttpresponse.getStatusCode().equals(HttpStatus.OK);
    }

    @Override
    public void handleError(ClientHttpResponse clienthttpresponse) throws IOException {

        if (clienthttpresponse.getStatusCode().equals(HttpStatus.NO_CONTENT))
        {
            throw new FmeException(HttpStatus.INTERNAL_SERVER_ERROR, FmeError.NO_CONTENT);
        }
        else
        {
            String body = IOUtils.toString(clienthttpresponse.getBody(), StandardCharsets.UTF_8);
            JsonNode json = new ObjectMapper().readTree(body);
            String message = json.at("/serviceResponse/statusInfo/message").asText();

            if (noMessage(message))
            {
                throw new FmeException(HttpStatus.INTERNAL_SERVER_ERROR, FmeError.UNKNOWN_ERROR);
            }
            else if (fatalError(message))
            {
                throw new FmeException(HttpStatus.INTERNAL_SERVER_ERROR, FmeError.FATAL_ERROR);
            }
            else if (invalidGeometry(message))
            {
                throw new FmeException(HttpStatus.BAD_REQUEST, FmeError.INVALID_GEOMETRY);
            }
            else if (hugeGeometry(message))
            {
                throw new FmeException(HttpStatus.PAYLOAD_TOO_LARGE, FmeError.HUGE_GEOMETRY);
            }
            else
            {
                throw new FmeException(HttpStatus.INTERNAL_SERVER_ERROR, FmeError.UNKNOWN_ERROR);
            }
        }
    }

    static private boolean noMessage(String message) {
        return message == null;
    }

    static private boolean invalidGeometry(String message) {
        return message.contains("'Ogiltig polygon'") ||
                message.contains("'Polygon saknar värde'") ||
                message.contains("'Felaktig geometri'");
    }

    static private boolean hugeGeometry(String message) {
        return message.contains("'Polygonen måste vara mindre än 10 000 ha'");
    }

    static private boolean fatalError(String message) {
        return message.contains("FME Server transformation failed: A fatal error has occurred");
    }
}
