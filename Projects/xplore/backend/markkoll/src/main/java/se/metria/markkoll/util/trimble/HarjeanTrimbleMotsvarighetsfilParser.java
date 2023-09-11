package se.metria.markkoll.util.trimble;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class HarjeanTrimbleMotsvarighetsfilParser {

    public static HashMap<Long, HarjeanTrimbleMotsvarighetsfilRow> ParseStream(InputStream inputStream) throws IOException {
        var result = new HashMap<Long, HarjeanTrimbleMotsvarighetsfilRow>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";

        // Hoppa över headers
        br.readLine();

        while ((line = br.readLine()) != null) {
            var parsedRows = ParseLine(line);

            if (parsedRows == null || parsedRows.size() == 0)
                continue;

            for (var row : parsedRows) {
                if(!result.containsKey(row.getClassId())) {
                    result.put(row.getClassId(), row);
                }
            }
        }
        return result;
    }

    private static ArrayList<HarjeanTrimbleMotsvarighetsfilRow> ParseLine(String line) {
        var result = new ArrayList<HarjeanTrimbleMotsvarighetsfilRow>();

        String[] columns = line.split(";");
        if(columns.length < 12 || columns[11].length() == 0)
            return null;

        String ledningskonstruktion = columns[0];
        String kostnadskod = columns[10];

        double spanningsniva = 0.0;
        try {
            spanningsniva = Double.parseDouble(columns[8].replace(',','.'));
        }
        catch(NumberFormatException e) {
            log.warn("Kan ej tolka Spänningsnivå ({}) i motsvarighetsfil", columns[8]);
        }

        var classIDs = new ArrayList<Long>();
        var classIDStrings = columns[11].split(",");
        for (var classIDString: classIDStrings) {
            int classID;
            try {
                var id = Long.parseLong(classIDString.trim());
                classIDs.add(id);
            }
            catch(NumberFormatException e) {
                log.warn("Kan ej tolka ClassID ({}) i motsvarighetsfil", classIDString);
            }
        }

        for (var classId: classIDs)  {
            result.add(new HarjeanTrimbleMotsvarighetsfilRow(classId, ledningskonstruktion, spanningsniva, kostnadskod));
        }
        return result;
    }
}
