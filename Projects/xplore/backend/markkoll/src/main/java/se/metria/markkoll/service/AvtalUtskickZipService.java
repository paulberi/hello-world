package se.metria.markkoll.service;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import se.metria.markkoll.MarkkollService;
import se.metria.markkoll.exception.MarkkollException;
import se.metria.markkoll.openapi.model.ProjektTypDto;
import se.metria.markkoll.repository.avtal.AvtalRepository;
import se.metria.markkoll.service.dokument.DokumentGeneratorService;
import se.metria.markkoll.service.projekt.FiberProjektService;
import se.metria.markkoll.service.projekt.ProjektService;
import se.metria.markkoll.service.utskick.UtskickDto;
import se.metria.markkoll.service.utskick.UtskickService;
import se.metria.markkoll.service.vardering.elnat.ElnatVarderingsprotokollGeneratorService;
import se.metria.markkoll.service.vardering.fiber.FiberVarderingsprotokollGeneratorService;
import se.metria.markkoll.util.FileNameAwareByteArrayResource;
import se.metria.markkoll.util.dokument.DokumentUtil;
import se.metria.markkoll.util.dokument.FileType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@MarkkollService
@Slf4j
@Transactional
public class AvtalUtskickZipService {
    private final AvtalRepository avtalRepository;
    private final DokumentGeneratorService dokumentGeneratorService;
    private final ElnatVarderingsprotokollGeneratorService elnatVarderingsprotokollGeneratorService;
    private final FiberVarderingsprotokollGeneratorService fiberVarderingsprotokollGeneratorService;
    private final FilService filService;
    private final ProjektService projektService;
    private final UtskickService utskickService;
    private final FiberProjektService fiberProjektService;
    public AvtalUtskickZipService(AvtalRepository avtalRepository,
                                  @Lazy DokumentGeneratorService dokumentGeneratorService,
                                  @Lazy ElnatVarderingsprotokollGeneratorService elnatVarderingsprotokollGeneratorService,
                                  @Lazy FiberVarderingsprotokollGeneratorService fiberVarderingsprotokollGeneratorService,
                                  FilService filService,
                                  @Lazy ProjektService projektService,
                                  @Lazy UtskickService utskickService,
                                  @Lazy FiberProjektService fiberProjektService)
    {
        this.avtalRepository = avtalRepository;
        this.dokumentGeneratorService = dokumentGeneratorService;
        this.filService = filService;
        this.projektService = projektService;
        this.utskickService = utskickService;
        this.elnatVarderingsprotokollGeneratorService = elnatVarderingsprotokollGeneratorService;
        this.fiberVarderingsprotokollGeneratorService = fiberVarderingsprotokollGeneratorService;
        this.fiberProjektService = fiberProjektService;
    }

    @Deprecated
    public ByteArrayResource
    getAvtalZip(UUID projektId, UUID fastighetId, UUID dokumentmallId)
            throws IllegalAccessException, IOException, Docx4JException, InvalidFormatException {
        var avtalId = avtalRepository.getIdByProjektIdAndFastighetId(projektId, fastighetId);
        return getAvtalZip(avtalId, dokumentmallId);
    }

    public ByteArrayResource
    getAvtalZip(UUID avtalId, UUID dokumentmallId)
            throws IOException, IllegalAccessException, Docx4JException, MarkkollException, InvalidFormatException {
        var bilagor = filService.getBilagorFilerData(avtalId);
        var projekt = projektService.getProjektForAvtal(avtalId);
        var utskickBatch = utskickService.getAvtalUtskickBatch(avtalId);

        log.info("Skapar {} avtalsutskick för avtal {} med dokumentmall {}", utskickBatch.getUtskick().size(),
            avtalId, dokumentmallId);

        var baos = new ByteArrayOutputStream();
        try(var zos = new ZipOutputStream(baos)) {
            if (utskickBatch.getUtskick().size() == 1) {
                var utskick = utskickBatch.getUtskick().get(0);
                writeUtskick(zos, "", avtalId, dokumentmallId, utskick, projekt.getProjektTyp(), bilagor);
            }
            else for (var utskick: utskickBatch.getUtskick()) {
                var dirName = !utskickBatch.getTitle().equals(utskick.getTitel()) ?
                    DokumentUtil.createFilename(FileType.FOLDER, utskickBatch.getTitle(), utskick.getTitel()) :
                    DokumentUtil.createFilename(FileType.FOLDER, utskickBatch.getTitle());

                writeUtskick(zos, dirName, avtalId, dokumentmallId, utskick, projekt.getProjektTyp(), bilagor);
            }
        }

        var zipFilename = DokumentUtil.createFilename(FileType.ZIP, utskickBatch.getTitle());
        return new FileNameAwareByteArrayResource(baos.toByteArray(), zipFilename);
    }

    private void
    writeAvtalEntry(ZipOutputStream zos,
                    String dirName,
                    UUID avtalId,
                    UUID dokumentmallId,
                    UtskickDto utskick)
        throws IOException, Docx4JException, IllegalAccessException
    {
        var avtal = dokumentGeneratorService.getAvtal(avtalId, dokumentmallId, utskick);
        var avtalFilename = DokumentUtil.createFilename(FileType.PDF, utskick.getTitel(), "avtal");
        writeZipEntry(zos, dirName + avtalFilename, avtal);
    }

    private void writeDirEntry(ZipOutputStream zos, String dirName) throws IOException {
        var dirEntry = new ZipEntry(dirName);
        zos.putNextEntry(dirEntry);
        zos.closeEntry();
    }

    private void
    writeUtskick(ZipOutputStream zos, String dirName, UUID avtalId, UUID dokumentmallId, UtskickDto utskick,
                 ProjektTypDto projektTyp, List<Resource> bilagor) throws IOException, Docx4JException, IllegalAccessException, InvalidFormatException
    {
        if (!StringUtil.isNullOrEmpty(dirName)) {
            writeDirEntry(zos, dirName);
        }
        writeAvtalEntry(zos, dirName, avtalId, dokumentmallId, utskick);
        writeVPEntries(zos, projektTyp, dirName, avtalId, utskick);

        for (var bilaga : bilagor) {
            var bilagaFilename = DokumentUtil.createFilename(FileType.NONE, utskick.getTitel(),
                bilaga.getFilename());
            writeZipEntry(zos, dirName + bilagaFilename, bilaga);
        }
    }

    private void
    writeVPEntries(ZipOutputStream zos,
                   ProjektTypDto projektTyp,
                   String dirName,
                   UUID avtalId,
                   UtskickDto utskick)
            throws IOException, InvalidFormatException {

        for (var vpUtskick: utskick.getUtskickVp()) {
            var vpList = projektTyp == ProjektTypDto.FIBER ?
                fiberVarderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId, vpUtskick) :
                elnatVarderingsprotokollGeneratorService.generateVarderingsprotokoll(avtalId, vpUtskick);
            writeVpEntry(zos, dirName, vpList, vpUtskick.getTitle());
        }
    }

    private void
    writeVpEntry(ZipOutputStream zos,
                 String dirName,
                 List<ByteArrayResource> vpList,
                 String title)
        throws IOException
    {
        if (vpList.size() == 1) {
            var vpFilename = DokumentUtil.createFilename(FileType.XLSX, title, "vp");
            writeZipEntry(zos, dirName + vpFilename, vpList.get(0));
        }
        else for (int i = 0; i < vpList.size(); i++) {
            var vpFilename = DokumentUtil.createFilename(FileType.XLSX, title, "vp", i + 1);
            writeZipEntry(zos, dirName + vpFilename, vpList.get(0));
        }
    }

    private void writeZipEntry(ZipOutputStream zos, String entryName, Resource resource) throws IOException {
        zos.putNextEntry(new ZipEntry(entryName));
        zos.write(resource.getInputStream().readAllBytes());
        zos.closeEntry();
    }
}
