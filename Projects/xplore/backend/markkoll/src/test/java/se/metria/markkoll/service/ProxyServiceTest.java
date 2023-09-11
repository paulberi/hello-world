package se.metria.markkoll.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.metria.kundconfig.openapi.model.MetriaMapsAuthDto;
import se.metria.markkoll.annotations.MarkkollServiceTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MarkkollServiceTest
@DisplayName("Givet ProxyService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(SpringExtension.class)
public class ProxyServiceTest {
    ProxyService proxyService;

    KundConfigService mockKundConfigService;

    final String metriaMapsUrl = "metria.maps.url";

    @BeforeEach()
    void beforeEach() {
        mockKundConfigService = mock(KundConfigService.class);
        proxyService = new ProxyService(mockKundConfigService);
        proxyService.metriaMapsUrl = metriaMapsUrl;
    }

    @Test
    void så_ska_forwarding_till_metria_maps_vara_möjligt() throws Exception {
        // Given
        var proxy = mock(ProxyExchange.class);
        var proxyPath = "PATH";
        when(proxy.path(any())).thenReturn(proxyPath);
        when(proxy.sensitive(any())).thenReturn(proxy);
        when(proxy.header(any(), any())).thenReturn(proxy);
        when(proxy.uri(anyString())).thenReturn(proxy);

        var queryParams = new HashMap<String, String>();
        queryParams.put("query1", "value1");
        queryParams.put("query2", "value2");

        var auth = new MetriaMapsAuthDto()
            .username("metria")
            .password("markkoll");

        when(mockKundConfigService.getMetriaMapsAuth()).thenReturn(auth);

        // When
        var exch = proxyService.proxyMetriaMaps(proxy, queryParams);

        // Then
        assertEquals(exch, proxy);
        verify(proxy).sensitive("Cookie");
        verify(proxy).header(eq("Authorization"), eq("BASIC bWV0cmlhOm1hcmtrb2xs"));
        verify(proxy).uri(eq(metriaMapsUrl + proxyPath + "?query1=value1&query2=value2&"));
    }
}
