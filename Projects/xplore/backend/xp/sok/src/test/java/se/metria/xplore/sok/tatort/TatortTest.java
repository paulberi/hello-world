package se.metria.xplore.sok.tatort;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import se.metria.xplore.sok.SokController;
import se.metria.xplore.sok.SokControllerBase;
import se.metria.xplore.sok.ort.model.OrtRequest;
import se.metria.xplore.sok.ort.model.OrtSokResultat;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SokController.class)
class TatortTest extends SokControllerBase {


    private static final String QUERY = "query";
    private static final String KOMMUN = "kommun";
    private static final String LAN = "län";
    private static final double X = 111.1;
    private static final double Y = 222.2;
    private static final List<String> DETALJTYPER = Arrays.asList("BEBTÄTTX");

    @Test
    void getTatorter_success() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, null, null, null, null, DETALJTYPER);
        given(ortClient.request(eq(request))).willReturn(
                new ResponseEntity<>(Arrays.asList(new OrtSokResultat(), new OrtSokResultat()), HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void getTatorter_kommun_success() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, KOMMUN, null, null, null, DETALJTYPER);
        given(ortClient.request(eq(request))).willReturn(
                new ResponseEntity<>(Arrays.asList(new OrtSokResultat()), HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}&k={k}", request.getQuery(), request.getKommun())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void getTatorter_lan_success() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, null, LAN, null, null, DETALJTYPER);
        given(ortClient.request(eq(request))).willReturn(
                new ResponseEntity<>(Arrays.asList(new OrtSokResultat()), HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}&l={l}", request.getQuery(), request.getLan())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void getTatorter_kommun_lan_success() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, KOMMUN, LAN, null, null, DETALJTYPER);
        given(ortClient.request(eq(request))).willReturn(
                new ResponseEntity<>(Arrays.asList(new OrtSokResultat()), HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}&k={k}&l={l}", request.getQuery(), request.getKommun(), request.getLan())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void getTatorter_coordinate_success() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, null, null, X, Y, DETALJTYPER);
        given(ortClient.request(eq(request))).willReturn(
                new ResponseEntity<>(Arrays.asList(new OrtSokResultat()), HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}&x={x}&y={y}", request.getQuery(), request.getCoordinate().getX(), request.getCoordinate().getY())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    void getTatorter_coordinate_failure() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, null, null, X, Y, DETALJTYPER);
        given(ortClient.request(any(OrtRequest.class))).willThrow(new IllegalArgumentException());
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}&x={x}", request.getQuery(), request.getCoordinate().getX())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTatorter_failed() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, null, null, null, null, DETALJTYPER);
        given(ortClient.request(eq(request))).willThrow(new NullPointerException());
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}", QUERY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getTatorter_auth_failed() throws Exception {
        OrtRequest request = new OrtRequest(QUERY, null, null, null, null, DETALJTYPER);
        given(ortClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .get("/tatort?q={q}", QUERY)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}