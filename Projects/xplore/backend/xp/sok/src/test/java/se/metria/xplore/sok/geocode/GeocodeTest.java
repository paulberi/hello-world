package se.metria.xplore.sok.geocode;

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
import se.metria.xplore.sok.exceptions.ExternalGeofilterException;
import se.metria.xplore.sok.exceptions.GeofilterFailureType;
import se.metria.xplore.sok.exceptions.InternalGeofilterException;
import se.metria.xplore.sok.geocode.model.MediumReplyExtent;
import se.metria.xplore.sok.geocode.request.GeocodeRequest;
import se.metria.xplore.sok.geocode.request.GeocodeRequestAddress;
import se.metria.xplore.sok.geocode.request.GeocodeRequestAddressCoordinate;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SokController.class)
class GeocodeTest extends SokControllerBase {

    private static final String QUERY = "query";
    private static final String[] KOMMUN = new String[]{"kommun"};
    private static final String MASK_URL = "mask_url";
    private static final double X = 111.1;
    private static final double Y = 222.2;

    private static final ResponseEntity RESPONSE = new ResponseEntity(Arrays.asList(new MediumReplyExtent(), new MediumReplyExtent()), HttpStatus.OK);

    @Test
    void getGeocode_success() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddress(QUERY, null, null);
        given(geocodeClient.request(eq(request))).willReturn(RESPONSE);
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void getGeocode_kommun_success() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddress(QUERY, KOMMUN, null);
        given(geocodeClient.request(eq(request))).willReturn(RESPONSE);
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}&k={k}", request.getQuery(), request.getKommun().get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void getGeocode_kommun_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddress(QUERY, KOMMUN, null);
        given(geocodeClient.request(eq(request))).willThrow(new NullPointerException());
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}&k={k}", request.getQuery(), request.getKommun().get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getGeocode_coordinate_success() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddressCoordinate(QUERY, X, Y, null, null);
        given(geocodeClient.request(eq(request))).willReturn(RESPONSE);
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}&x={x}&y={y}", request.getQuery(), request.getX(), request.getY())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }
    @Test
    void getGeocode_kommun_geofilter_internal_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddress(QUERY, KOMMUN, MASK_URL);
        given(geocodeClient.request(eq(request))).willThrow(new InternalGeofilterException(GeofilterFailureType.MASK_DOWNLOAD));
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}&k={k}&m={m}", request.getQuery(), request.getKommun().get(0), request.getMaskUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getGeocode_kommun_geofilter_external_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddress(QUERY, KOMMUN, MASK_URL);
        given(geocodeClient.request(eq(request))).willThrow(new ExternalGeofilterException(GeofilterFailureType.MISSING_MASK));
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}&k={k}&m={m}", request.getQuery(), request.getKommun().get(0), request.getMaskUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getGeocode_kommun_auth_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestAddress(QUERY, null, null);
        given(geocodeClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .get("/geocode?q={q}", request.getQuery())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}