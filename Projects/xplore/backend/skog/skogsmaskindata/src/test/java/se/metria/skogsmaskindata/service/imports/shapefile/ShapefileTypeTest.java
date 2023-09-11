package se.metria.skogsmaskindata.service.imports.shapefile;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.metria.skogsmaskindata.service.imports.shapefile.ShapefileType.*;

public class ShapefileTypeTest {

    private static Set<Testcase> testcases = new HashSet<>();

    @BeforeAll
    static void setUp() {
        testcases.add(new Testcase(KORSPAR, true, Arrays.asList("xxxyyy123.shp", "xxxyyy123.SHP", "G1254789.shp", "154975.shp")));
        testcases.add(new Testcase(KORSPAR, false, Arrays.asList("_xxxyyy123.shp", "G125_4789.shp", "154975_smth.shp")));

        testcases.add(new Testcase(INFORMATIONSLINJE, true, Arrays.asList("IL_xxxyyy123.shp", "IL_xxxyyy123.SHP", "IL_G1254789.shp", "IL_154975.shp")));
        testcases.add(new Testcase(INFORMATIONSLINJE, false, Arrays.asList("il_xxxyyy123.shp", "_IL_xxxyyy123.shp", "IL_G125_4789.shp", "IL_154975_smth.shp", "IL.shp")));

        testcases.add(new Testcase(INFORMATIONSPOLYGON, true, Arrays.asList("IPOL_xxxyyy123.shp", "IPOL_xxxyyy123.SHP", "IPOL_G1254789.shp", "IPOL_154975.shp")));
        testcases.add(new Testcase(INFORMATIONSPOLYGON, false, Arrays.asList("ipol_xxxyyy123.shp", "_IPOL_xxxyyy123.shp", "IPOL_G125_4789.shp", "IPOL_154975_smth.shp", "IPOL.shp")));

        testcases.add(new Testcase(INFORMATIONSPUNKT, true, Arrays.asList("IP_xxxyyy123.shp", "IP_xxxyyy123.SHP", "IP_G1254789.shp", "IP_154975.shp")));
        testcases.add(new Testcase(INFORMATIONSPUNKT, false, Arrays.asList("ip_xxxyyy123.shp", "_IP_xxxyyy123.shp", "IP_G125_4789.shp", "IP_154975_smth.shp", "IP.shp")));

        testcases.add(new Testcase(KORSPAR_FOR_TRANSPORT, true, Arrays.asList("Transport_to_xxxyyy123.shp", "Transport_to_xxxyyy123.SHP", "Transport_to_G1254789.shp", "Transport_to_154975.shp")));
        testcases.add(new Testcase(KORSPAR_FOR_TRANSPORT, false, Arrays.asList("transport_to_xxxyyy123.shp", "_Transport_to_xxxyyy123.shp", "Transport_to_G125_4789.shp", "Transport_to_154975_smth.shp", "Transport_to.shp")));

        testcases.add(new Testcase(PROVYTA, true, Arrays.asList("xxxyyy123_PY.shp", "xxxyyy123_PY.SHP", "G1254789_PY.shp", "154975_PY.shp")));
        testcases.add(new Testcase(PROVYTA, false, Arrays.asList("xxxyyy123_py.shp", "_xxxyyy123_PY.shp", "G125_4789_PY.shp", "154975_PY_smth.shp", "PY.shp")));

        testcases.add(new Testcase(RESULTAT, true, Arrays.asList("Result_G_xxxyyy123.shp", "Result_G_xxxyyy123.SHP", "Result_G_G1254789.shp", "Result_G_154975.shp")));
        testcases.add(new Testcase(RESULTAT, false, Arrays.asList("result_G_xxxyyy123.shp", "_Result_G_xxxyyy123.shp", "Result_G_G125_4789.shp", "Result_G_154975_smth.shp", "Result_G.shp")));

        testcases.add(new Testcase(RESULTAT, true, Arrays.asList("Result_U_xxxyyy123.shp", "Result_U_xxxyyy123.SHP", "Result_U_G1254789.shp", "Result_U_154975.shp")));
        testcases.add(new Testcase(RESULTAT, false, Arrays.asList("result_U_xxxyyy123.shp", "_Result_U_xxxyyy123.shp", "Result_U_G125_4789.shp", "Result_U_154975_smth.shp", "Result_U.shp")));

        testcases.add(new Testcase(RESULTAT, false, Arrays.asList("Result_F_xxxyyy123.shp", "Result_MVG_xxxyyy123.shp", "Result__154975.shp", "Result_154975.shp")));
    }

    @Test
    void testRegex() {
        for (Testcase testcase : testcases) {
            for (String filename: testcase.filenames) {
                assertEquals(testcase.expected, filename.matches(testcase.type.getRegex()));
            }
        }
    }

    private static class Testcase {
        public ShapefileType type;
        public boolean expected;
        public List<String> filenames;

        public Testcase(ShapefileType type, boolean expected, List<String> filenames) {
            this.type = type;
            this.expected = expected;
            this.filenames = filenames;
        }
    }
}
