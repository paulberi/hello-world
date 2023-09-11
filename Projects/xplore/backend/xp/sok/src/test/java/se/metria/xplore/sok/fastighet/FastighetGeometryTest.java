package se.metria.xplore.sok.fastighet;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import se.metria.xplore.sok.SokController;
import se.metria.xplore.sok.SokControllerBase;
import se.metria.xplore.sok.fastighet.model.GeometryOperation;
import se.metria.xplore.sok.fastighet.model.GeometryRequest;
import se.metria.xplore.sok.fastighet.request.FastighetRequest;
import se.metria.xplore.sok.fastighet.request.FastighetRequestFactory;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SokController.class)
class FastighetGeometryTest extends SokControllerBase {

    private static final String WKT = "POLYGON((479361.2636895094 6408036.790270894,479125.0371141922 6407534.051661885,479561.1477147778 6407709.7073204545,479361.2636895094 6408036.790270894))";
    private static final String INVALID_WKT = "POLYGON((479361.2636895094 6408036.790270894,479125.0371141922 6407534.051661885,479561.1477147778 6407709.7073204545,479361.2636895094 6408036.790270894";
    private static final String[] KOMMUN = new String[]{"kommun"};
    private static final String JSON = "json";

    @Test
    void getFastighetIntersection_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getGeometryRequest(new GeometryRequest(WKT, GeometryOperation.INTERSECTS), null, "typeName", 500, 0);
        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + request.getQuery() + "&o=" + GeometryOperation.INTERSECTS.name())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighetWithin_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getGeometryRequest(new GeometryRequest(WKT, GeometryOperation.WITHIN), null, "typeName", 500, 0);
        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + request.getQuery() + "&o=" + GeometryOperation.WITHIN.name())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighetIntersection_kommun_success() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getGeometryRequest(new GeometryRequest(WKT, GeometryOperation.INTERSECTS), KOMMUN, "typeName", 500, 0);
        given(fastighetClient.request(eq(request))).willReturn(new ResponseEntity(JSON, HttpStatus.OK));
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + request.getQuery() + "&o=" + GeometryOperation.INTERSECTS.name() + "&k=" + request.getKommun().get(0))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(JSON));
    }

    @Test
    void getFastighetIntersection_invalid_wkt() throws Exception {
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + INVALID_WKT + "&o=" + GeometryOperation.INTERSECTS.name())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFastighetIntersection_missing_operation() throws Exception {
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + WKT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFastighetIntersection_invalid_operation() throws Exception {
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + WKT + "&o=CROSSES")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFastighetIntersection_auth_fail() throws Exception {
        FastighetRequest request = FastighetRequestFactory.getGeometryRequest(new GeometryRequest(WKT, GeometryOperation.INTERSECTS), null, "typeName", 500 , 0);
        given(fastighetClient.request(eq(request))).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        sokController.perform(MockMvcRequestBuilders
                .post("/fastighet/geometry")
                .content("q=" + request.getQuery() + "&o=" + GeometryOperation.INTERSECTS.name())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
