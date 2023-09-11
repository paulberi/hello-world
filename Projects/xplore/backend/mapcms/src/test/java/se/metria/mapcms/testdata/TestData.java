package se.metria.mapcms.testdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.modelmapper.ModelMapper;
import se.metria.mapcms.entity.FilEntity;
import se.metria.mapcms.entity.KundEntity;
import se.metria.mapcms.openapi.model.FilRspDto;
import se.metria.mapcms.openapi.model.KundRspDto;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import se.metria.mapcms.openapi.model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(value = { "file" })
public class TestData {

    public static List<SprakDto> sprakData(){
        SprakDto sprakDto=new SprakDto();
        sprakDto.setKod("sv");
        sprakDto.setNamn("Svenska");
        sprakDto.setNamnOrg("Svenska");

        SprakDto sprakDto1=new SprakDto();
        sprakDto1.setKod("en");
        sprakDto1.setNamn("Engelska");
        sprakDto1.setNamnOrg("English");
        List<SprakDto> spraklist=new ArrayList<>();
        spraklist.add(sprakDto1);
        spraklist.add(sprakDto);

        return spraklist;
    }

    public static KundRspDto testKund(){
        KundRspDto kundRspDto = new KundRspDto();
        kundRspDto.setId(mockUUID(0));
        kundRspDto.setNamn("TestKund");
        kundRspDto.setSlug("TK");
        kundRspDto.setTillgangligaSprak(sprakData());
        ModelMapper mapper = new ModelMapper();
        final FilEntity filEntity = testFil();
        final FilRspDto filRspDto = mapper.map(filEntity, FilRspDto.class) ;
        kundRspDto.setLogotyp(filRspDto);

        return kundRspDto;
    }

    public static KundEntity testKundEntity(){
        KundEntity kundEntity = new KundEntity();
        kundEntity.setId(mockUUID(0));
        kundEntity.setNamn("TestKund");
        kundEntity.setSlug("TK");
        kundEntity.setLogotyp(testFil());

        return kundEntity;
    }

    public static FilEntity testFil(){
        FilEntity fil = new FilEntity();
        fil.setId(mockUUID(1));
        fil.setBeskrivning("Test Beskrivning");
        fil.setFilnamn("Test Fil");
        fil.setMimetyp("Test Mimetyp");
        fil.setFil("data".getBytes());

        return fil;
    }

    public static Resource filData() throws IOException {
        Resource file = new ClassPathResource("testdata/oneco-shapetest-1.zip");
        return  file;
    }

    public static ProjektRspDto projektdata(){
        ProjektRspDto projektRspDto=new ProjektRspDto();
        projektRspDto.setId(mockUUID(0));
        projektRspDto.setBrodtext("testData");
        projektRspDto.setIngress("test ingress");
        projektRspDto.setRubrik("Fiber");
        projektRspDto.setSlug("santa");
        projektRspDto.setPubliceringStatus(PubliceringStatusDto.PUBLICERAD);
        return projektRspDto;
    }

    public  static List<ProjektRspDto> listaAvProjekts(){

        ProjektRspDto projektRspDto=new ProjektRspDto();
        projektRspDto.setId(mockUUID(0));
        projektRspDto.setBrodtext("testData");
        projektRspDto.setIngress("test ingress");
        projektRspDto.setRubrik("Fiber");
        projektRspDto.setSlug("santa");
        projektRspDto.setPubliceringStatus(PubliceringStatusDto.PUBLICERAD);

        ProjektRspDto projektRspDto1=new ProjektRspDto();
        projektRspDto1.setId(mockUUID(1));
        projektRspDto1.setBrodtext("testData");
        projektRspDto1.setIngress("test ingress");
        projektRspDto1.setRubrik("Fiber");
        projektRspDto1.setSlug("santa");
        projektRspDto1.setPubliceringStatus(PubliceringStatusDto.PUBLICERAD);

        List<ProjektRspDto> projektRspDtoList=new ArrayList<>();
        projektRspDtoList.add(projektRspDto1);
        projektRspDtoList.add(projektRspDto);
        return projektRspDtoList;
    }

    /**
     * från markkoll
     * **/
    public static UUID mockUUID(Integer index) {
        if (index >= 0 && index <= 9) {
            return UUID.fromString("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx".replace("x", index.toString()));
        } else if (index <= 15) {
            char charVal = (char)(87 + index);
            return UUID.fromString("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx".replace('x', charVal));
        } else {
            // Vi håller tummarna för att man aldrig kommer behöva mer än 16 IDs...
            throw new IllegalArgumentException("För stort index: " + index);
        }
    }

    public static MockMultipartFile createMultipartFile() throws IOException {
        final var file = new MockMultipartFile("filer", "test.zip", "application/octet-stream", "dokument".getBytes());
        return file;
    }

    public static FilReqDto filReqDtoData(){
        FilReqDto filReqDto=new FilReqDto();
        filReqDto.setBeskrivning("Testing");
        filReqDto.setMimetyp("application/octet-stream");
        return  filReqDto;
    }

    public static List<FilRspDto> filRspDtoListData(){
        ModelMapper modelMapper= new ModelMapper();
        FilReqDto filReqDto=filReqDtoData();
        FilReqDto filReqDto1=filReqDtoData();
        List<FilRspDto> filRspDtoList=new ArrayList<>();
        filRspDtoList.add(modelMapper.map(filReqDto1, FilRspDto.class));
        filRspDtoList.add(modelMapper.map(filReqDto, FilRspDto.class));
        return filRspDtoList;
    }

    public static FilEntity filEntityData() throws IOException {
        final var file = new MockMultipartFile("file", "test.zip", "application/octet-stream", new byte[0]);
        FilEntity filEntity=new FilEntity();
        filEntity.setFil(file.getBytes());
        filEntity.setMimetyp(file.getContentType());
        filEntity.setBeskrivning("test file");
        return filEntity;
    }

    public static FilRspDto filRspDtoData(){
        FilRspDto filRspDto=new FilRspDto();
        filRspDto.setId(mockUUID(0));
        filRspDto.setFilnamn("test.zip");
        filRspDto.setMimetyp("application/octet-stream");
        filRspDto.setBeskrivning("Testing");
        return filRspDto;
    }

    public static ProjektOversattningDto oversattProjekt(){
        ProjektOversattningDto projektOversattningDto=new ProjektOversattningDto();
        projektOversattningDto.setSprakkod("en");
        projektOversattningDto.setBrodtext("hello");
        projektOversattningDto.setIngress("hihi");
        projektOversattningDto.setRubrik("översättning");
        return projektOversattningDto;
    }

    public static ProjektTextDto projektTextDtoData(){
        ProjektTextDto projektTextDto= new ProjektTextDto();
        projektTextDto.setBrodtext("hello");
        projektTextDto.setIngress("hihi");
        projektTextDto.setRubrik("översättning");
        return projektTextDto;
    }

    public static ProjektReqDto projektReqDtoData(){

        ProjektReqDto projektReqDto=new ProjektReqDto();
        projektReqDto.setBrodtext("testData");
        projektReqDto.setIngress("test ingress");
        projektReqDto.setRubrik("Fiber");
        projektReqDto.setSlug("santa");
        projektReqDto.setPubliceringStatus(PubliceringStatusDto.PUBLICERAD);
        return projektReqDto;
    }

    public static KundRspDto kundRspDtoData(){

        KundRspDto kundRspDto=new KundRspDto();
        kundRspDto.setId(mockUUID(0));
        kundRspDto.setNamn("Metria");
        kundRspDto.setSlug("slug");
        kundRspDto.setStandardsprak("sv");
        kundRspDto.setVhtNyckel("12345");
        return kundRspDto;
    }

    public static SprakvalRspDto sprakValRspData() {
        SprakvalRspDto sprakDto = new SprakvalRspDto();
        sprakDto.setKod("sv");
        sprakDto.setNamn("Svenska");
        sprakDto.setNamnOrg("Svenska");
        sprakDto.setStandardsprak(true);
        return sprakDto;
    }

    public static GeometriReqDto geometriReqDtoData(){
        GeometriReqDto geometriReqDto=new GeometriReqDto();
        String geoJsonString = "{}";
        geometriReqDto.setGeom(geoJsonString);
        geometriReqDto.setProps(null);
        return geometriReqDto;
    }

    public static GeometriRspDto geometriData() {
        GeometriRspDto geometriRspDto = new GeometriRspDto();
        geometriRspDto.setId(mockUUID(0));
        geometriRspDto.setGeom("{}");
        geometriRspDto.setProps(null);
        return geometriRspDto;
    }

    public static DialogPartRspDto dialogPartRspForCreatingDialog(){
        DialogPartRspDto dialogPartRspDto=new DialogPartRspDto();
        dialogPartRspDto.setId(mockUUID(0));
        dialogPartRspDto.setNamn("namn");
        dialogPartRspDto.setEpost("epost");
        dialogPartRspDto.setSkapare(true);
        return dialogPartRspDto;
    }

    public static DialogPartRspDto dialogPartRspForMessaging(){
        DialogPartRspDto dialogPartRspDto=new DialogPartRspDto();
        dialogPartRspDto.setId(mockUUID(3));
        dialogPartRspDto.setNamn("namn");
        dialogPartRspDto.setEpost("epost");
        dialogPartRspDto.setSkapare(false);
        return dialogPartRspDto;
    }

    public static List<DialogPartRspDto> dialogPartRspDtoList(){
        return Arrays.asList(dialogPartRspForCreatingDialog(), dialogPartRspForMessaging());
    }

    public static DialogRspDto dialogRspMetadata(){
        DialogRspDto dialogRspDto=new DialogRspDto();
        dialogRspDto.setId(mockUUID(4));
        dialogRspDto.setParter(dialogPartRspDtoList());
        dialogRspDto.setDialogStatus(DialogStatusDto.PAGAENDE);
        dialogRspDto.setRubrik("rubrik");
        dialogRspDto.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        dialogRspDto.setPlats(null);
        return dialogRspDto;
    }

    public static MeddelandeRspDto meddelandeRspDtoMetadata(){
        MeddelandeRspDto meddelandeRspDto=new MeddelandeRspDto();
        meddelandeRspDto.setFran(dialogPartRspForMessaging());
        meddelandeRspDto.setFiler(Arrays.asList(filRspDtoData()));
        meddelandeRspDto.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        meddelandeRspDto.setText("text");
        meddelandeRspDto.setSkapadDatum(null);
        meddelandeRspDto.setId(mockUUID(5));
        return meddelandeRspDto;
    }

    public static DialogKomplettDto dialogKomplettMetadata(){
        DialogKomplettDto dialogKomplettDto=new DialogKomplettDto();
        dialogKomplettDto.setMeddelanden(Arrays.asList(meddelandeRspDtoMetadata()));
        dialogKomplettDto.setDialogStatus(DialogStatusDto.PAGAENDE);
        dialogKomplettDto.setParter(dialogPartRspDtoList());
        dialogKomplettDto.setPlats(null);
        dialogKomplettDto.setRubrik("rubrik");
        dialogKomplettDto.setPubliceringStatus(PubliceringStatusDto.UTKAST);
        dialogKomplettDto.setId(mockUUID(8));
        return dialogKomplettDto;
    }
}
