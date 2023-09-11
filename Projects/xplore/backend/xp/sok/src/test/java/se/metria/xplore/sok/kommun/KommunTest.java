package se.metria.xplore.sok.kommun;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import se.metria.xplore.sok.SokController;
import se.metria.xplore.sok.SokControllerBase;
import se.metria.xplore.sok.exceptions.GeoJsonMergeException;
import se.metria.xplore.sok.kommun.model.KommunRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SokController.class)
class KommunTest extends SokControllerBase {

    private static final String QUERY = "query";
    private static final String EMPTY_QUERY = "";
    private static final String LAN = "län";
    private static final String JSON = "json";

    @Test
    void getKommun_success() throws Exception {
        KommunRequest request = new KommunRequest(QUERY, null);
        given(kommunClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/kommun?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getKommun_lan_success() throws Exception {
        KommunRequest request = new KommunRequest(QUERY, LAN);
        given(kommunClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/kommun?q={q}&l={l}", request.getQuery(), request.getLan())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getKommun_empty_request_failure() throws Exception {
        given(kommunClient.request(any(KommunRequest.class))).willThrow(new IllegalArgumentException());
        sokController.perform(MockMvcRequestBuilders
                .get("/kommun?q={q}", EMPTY_QUERY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getKommun_geoJson_merge_failure() throws Exception {
        KommunRequest request = new KommunRequest(QUERY, null);
        given(kommunClient.request(eq(request))).willThrow(new GeoJsonMergeException());
        sokController.perform(MockMvcRequestBuilders
                .get("/kommun?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getKommun_auth_fail() throws Exception {
        KommunRequest request = new KommunRequest(QUERY, null);
        given(kommunClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .get("/kommun?q={q}", QUERY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}