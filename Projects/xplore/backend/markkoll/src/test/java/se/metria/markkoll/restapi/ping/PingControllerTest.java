package se.metria.markkoll.restapi.ping;

import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.metria.markkoll.annotations.MarkkollMVCTest;
import se.metria.markkoll.restapi.PingController;
import se.metria.markkoll.security.mock.WithMockUserMarkhandlaggare;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Givet PingController")
@MarkkollMVCTest(controllers = PingController.class)
public class PingControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUserMarkhandlaggare
    public void om_en_markhandlaggare_pingar_så_fås_pong_tillbaka() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong!"));
    }

    @Ignore("Vi har ändrat hur behörigheter fungerar. Testet är inaktuellt.")
    @Test
    public void om_en_oauktoriserad_användare_pingar_så_fås_401_tillbaka() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/ping/auth"))
                .andExpect(status().isUnauthorized());
    }
}