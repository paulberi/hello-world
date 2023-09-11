package se.metria.markkoll.vptestdatagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import se.metria.markkoll.util.FileUtil;
import se.metria.markkoll.util.vardering.VpGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public class VpTestdataGenerator {
    ObjectMapper objectMapper = new ObjectMapper();

    void generate(String inPath, String outPath) throws IOException, InvalidFormatException {
        var testdataFolder = new File(inPath);
        //var testdataFolder = new ClassPathResource("varderingsprotokoll/vpjson/").getFile();
        var baseFolder = new File(outPath);

        if (baseFolder.exists()) {
            FileUtils.deleteDirectory(baseFolder);
        }

        baseFolder.mkdir();
        writeFolder(testdataFolder, outPath);
    }

    private void writeFolder(File folder, String basePath) throws IOException, InvalidFormatException {
        var vpGenerator = VpGenerator.elnatVpGenerator();

        log.info("Skriver värderingsprotokoll till {}", folder.toPath());
        for (var file: folder.listFiles()) {
            if (file.isDirectory()) {
                var folderName = file.getName();
                var newFolderPath = Paths.get(basePath, folderName).toString();
                var newFolder = new File(newFolderPath);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
                writeFolder(file, newFolderPath);
            } else {
                var json = FileUtil.read(file);
                var obj = objectMapper.readValue(json, VpJsonWrapper.class);

                var vp = vpGenerator.generateVarderingsprotokoll(obj.getVarderingsprotokoll(), null, null,
                    null, null);

                var filenameXlsx = FilenameUtils.removeExtension(file.getName()) + ".xlsx";
                FileUtil.save(vp, Paths.get(basePath, filenameXlsx));
                FileUtils.copyFile(file, new File(Paths.get(basePath, file.getName()).toString()));
            }
        }
    }
}
