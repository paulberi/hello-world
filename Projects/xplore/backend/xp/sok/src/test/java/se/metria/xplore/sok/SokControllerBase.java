package se.metria.xplore.sok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import se.metria.xplore.sok.fastighet.FastighetClient;
import se.metria.xplore.sok.geocode.GeocodeClient;
import se.metria.xplore.sok.kommun.KommunClient;
import se.metria.xplore.sok.ort.OrtClient;

public abstract class SokControllerBase {

    @Autowired
    protected MockMvc sokController;

    @MockBean
    protected GeocodeClient geocodeClient;
    @MockBean
    protected FastighetClient fastighetClient;
    @MockBean
    protected OrtClient ortClient;
    @MockBean
    protected KommunClient kommunClient;
}