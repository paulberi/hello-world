package se.metria.xplore.sok.reversegeocode;

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
import se.metria.xplore.sok.geocode.request.GeocodeRequestCoordinate;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SokController.class)
class ReverseGeocodeTest extends SokControllerBase {

    private static final double X = 111.1;
    private static final double Y = 222.2;
    private static final String[] KOMMUN = new String[]{"kommun"};
    private static final String MASK_URL = "mask_url";

    private static final ResponseEntity RESPONSE = new ResponseEntity(Arrays.asList(
            new MediumReplyExtent(), new MediumReplyExtent(), new MediumReplyExtent()), HttpStatus.OK);

    @Test
    void getReverseGeocode_success() throws Exception {
        GeocodeRequest request = new GeocodeRequestCoordinate(X, Y, null, null);
        given(geocodeClient.request(eq(request))).willReturn(RESPONSE);
        sokController.perform(MockMvcRequestBuilders
                .get("/reverse-geocode?x={x}&y={y}", request.getX(), request.getY())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }

    @Test
    void getReverseGeocode_kommun_success() throws Exception {
        GeocodeRequest request = new GeocodeRequestCoordinate(X, Y, KOMMUN, null);
        given(geocodeClient.request(eq(request))).willReturn(RESPONSE);
        sokController.perform(MockMvcRequestBuilders
                .get("/reverse-geocode?x={x}&y={y}&k={k}", request.getX(), request.getY(), request.getKommun().get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }

    @Test
    void getReverseGeocode_kommun_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestCoordinate(X, Y, KOMMUN, null);
        given(geocodeClient.request(eq(request))).willThrow(new NullPointerException());
        sokController.perform(MockMvcRequestBuilders
                .get("/reverse-geocode?x={x}&y={y}&k={k}", request.getX(), request.getY(), request.getKommun().get(0))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getReverseGeocode_internal_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestCoordinate(X, Y, KOMMUN, MASK_URL);
        given(geocodeClient.request(eq(request))).willThrow(new InternalGeofilterException(GeofilterFailureType.MASK_DOWNLOAD));
        sokController.perform(MockMvcRequestBuilders
                .get("/reverse-geocode?x={x}&y={y}&k={k}&m={m}", request.getX(), request.getY(), request.getKommun().get(0), request.getMaskUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getReverseGeocode_external_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestCoordinate(X, Y, KOMMUN, MASK_URL);
        given(geocodeClient.request(eq(request))).willThrow(new ExternalGeofilterException(GeofilterFailureType.MISSING_MASK));
        sokController.perform(MockMvcRequestBuilders
                .get("/reverse-geocode?x={x}&y={y}&k={k}&m={m}", request.getX(), request.getY(), request.getKommun().get(0), request.getMaskUrl())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReverseGeocode_auth_fail() throws Exception {
        GeocodeRequest request = new GeocodeRequestCoordinate(X, Y, null, null);
        given(geocodeClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .get("/reverse-geocode?x={x}&y={y}", request.getX(), request.getY())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}