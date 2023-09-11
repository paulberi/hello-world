package se.metria.xplore.sok.fastighet;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import se.metria.xplore.sok.SokController;
import se.metria.xplore.sok.SokControllerBase;
import se.metria.xplore.sok.fastighet.request.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.*;

@WebMvcTest(SokController.class)
class FastighetTest extends SokControllerBase {
    private static final String QUERY = "query";
    private static final String[] KOMMUN = new String[]{"kommun"};
    private static final String JSON = "json";
    private static final String UUID = "909a6a50-465b-90ec-e040-ed8f66444c3f";
    private static final String UUID_ARRAY = "909a6a88-8aa0-90ec-e040-ed8f66444c3f,909a6a88-5acd-90ec-e040-ed8f66444c3f";
    private static final String EXTERNID = "0604>KORPEN>3>1>>>>1";
    private static final String INVALID_UUID = "invalid_uuid";

    @Test
    void getFastighet_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(QUERY, null, "typeName");
        assertTrue(request instanceof FastighetRequestText);
        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/fastighet?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighet_kommun_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(QUERY, KOMMUN, "typeName");
        assertTrue(request instanceof FastighetRequestText);
        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/fastighet?q={q}&k={k}", request.getQuery(), request.getKommun().get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighet_auth_fail() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(QUERY, null, "typeName");
        given(fastighetClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .get("/fastighet?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getFastighet_Uuid_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(UUID, null, "typeName");
        assertTrue(request instanceof FastighetRequestUUID);

        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/fastighet?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighet_Uuid_Array_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(UUID_ARRAY, null, "typeName");
        assertTrue(request instanceof FastighetRequestUUIDArray);

        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                        .get("/fastighet?q={q}", request.getQuery())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }


    @Test
    void getFastighet_Externid_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest("externid:"+EXTERNID, null, "typeName");
        assertTrue(request instanceof FastighetRequestExternid);

        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                        .get("/fastighet?q={q}", request.getQuery())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }


    @Test
    void getFastighet_Uuid_failure() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(INVALID_UUID, null, "typeName");
        assertFalse(request instanceof FastighetRequestUUID);
        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/fastighet?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighet_Uuid_auth_fail() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getRequest(QUERY, null, "typeName");
        given(fastighetClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .get("/fastighet?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
