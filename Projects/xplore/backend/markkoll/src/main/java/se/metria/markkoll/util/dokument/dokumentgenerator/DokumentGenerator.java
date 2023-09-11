package se.metria.markkoll.util.dokument.dokumentgenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.docx4j.Docx4J;
import org.docx4j.TraversalUtil;
import org.docx4j.XmlUtils;
import org.docx4j.finders.ClassFinder;
import org.docx4j.jaxb.Context;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.DocPropsCustomPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.w15.CTSdtRepeatedSection;
import org.docx4j.wml.*;
import org.jvnet.jaxb2_commons.ppp.Child;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import se.metria.markkoll.util.ReflectionUtil;
import se.metria.markkoll.util.dokument.DocProperty;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static se.metria.markkoll.util.dokument.dokumentgenerator.DokumentWarnings.IMAGE_DATA_MISSING;

@Slf4j
@Component
@RequiredArgsConstructor
public class DokumentGenerator {
    @Value("${dokument-generator.update-fields:true}")
    private Boolean updateFields = true;

    public ByteArrayResource
    docxFromTemplate(Resource docx, Object data)throws IOException, Docx4JException, IllegalAccessException {
        return docxFromTemplate(docx.getInputStream(), data);
    }

    public ByteArrayResource
    docxFromTemplate(InputStream docx, Object data) throws IOException, Docx4JException, IllegalAccessException {
        return docxFromTemplate(data, Docx4J.load(docx));
    }

    public ByteArrayResource
    docxFromTemplate(File docx, Object data) throws IOException, Docx4JException, IllegalAccessException {
        return docxFromTemplate(data, Docx4J.load(docx));
    }

    public ByteArrayResource
    addDocProperties(File docx, Class<?> clazz) throws Docx4JException, IOException {
        return addDocProperties(Docx4J.load(docx), clazz);
    }

    public ByteArrayResource
    addDocProperties(InputStream docx, Class<?> clazz) throws Docx4JException, IOException {
        return addDocProperties(Docx4J.load(docx), clazz);
    }

    public ByteArrayResource
    addDocProperties(Resource resource, Class<?> clazz) throws Docx4JException, IOException {
        return addDocProperties(resource.getInputStream(), clazz);
    }

    private ByteArrayResource
    docxFromTemplate(Object data,
                     WordprocessingMLPackage wmlPackage) throws Docx4JException, IOException, IllegalAccessException {
        log.info("Genererar Docx-fil från Wordmall");
        wmlPackage = applyBindings(wmlPackage, data);

        var baos = new ByteArrayOutputStream();
        Docx4J.save(wmlPackage, baos);

        baos.flush();
        baos.close();

        return new ByteArrayResource(baos.toByteArray());
    }

    private ByteArrayResource
    addDocProperties(WordprocessingMLPackage wmlPackage, Class<?> clazz) throws Docx4JException, IOException {
        addDocPropertes(wmlPackage, clazz);

        var baos = new ByteArrayOutputStream();
        Docx4J.save(wmlPackage, baos);

        baos.flush();
        baos.close();

        return new ByteArrayResource(baos.toByteArray());
    }

    private WordprocessingMLPackage
    applyBindings(WordprocessingMLPackage wmlPackage, Object data) throws Docx4JException, IllegalAccessException
    {
        var docProps = wmlPackage.getDocPropsCustomPart();

        // Dokumentet saknar doc properties, gör ingenting.
        if (docProps == null) {
            return wmlPackage;
        }

        var fvPairs = ReflectionUtil.getFieldValuesWithAnnotationRecursive(data, DocProperty.class);

        for (var fvp: fvPairs) {
            var docProperty = fvp.getField().getAnnotation(DocProperty.class).value();
            setFieldDocProperty(fvp.getField(), fvp.getValue(), docProperty, wmlPackage, docProps);
        }

        /* Egentligen så vill du uppdatera fälten i din docx-fil, så att de uppdaterade värdena syns när du öppnar
        dokumentet i Word. Det finns dock en bugg i Docx4J. Om vi har nästlade fält, och det nästlade fältet är samma
        fält som det innehållande fältet, så får vi oändlig rekursion när Docx4J anropar FieldRef.getFldName(). Se
        exempel nedan med fältet EA:

        { IF «EA»  > "0" "Totalt { MERGEFIELD EA \* CHARFORMAT } kr enligt värderingsprotokoll. }

        Som tur är så behöver vi inte Word, åtminstone för det vi ska göra. I dagsläget används dokumentgeneratorn
        slutändan för att generera en PDF. Detta görs med LibreOffice, som verkar uppdatera fälten automatiskt när man
        startar applikationen. Vi slipper uppdatera fälten här då, åtminstone för tillfället...*/
        if (updateFields) {
            var fu = new FieldUpdater(wmlPackage);
            fu.update(true);
        }

        return wmlPackage;
    }

    private void
    setFieldDocProperty(Field field,
                        Object data,
                        String docProperty,
                        WordprocessingMLPackage wmlPackage,
                        DocPropsCustomPart docProps) throws IllegalAccessException
    {
        field.setAccessible(true);
        var fieldValue = field.get(data);

        if (fieldValue instanceof Collection) {
            replaceCollection(wmlPackage, field, (Collection<Object>)fieldValue, docProperty);
        }
        else if (field.getType() == ByteArrayResource.class) {
            docProps.setProperty(docProperty, "");
            if (fieldValue == null) {
                log.warn(IMAGE_DATA_MISSING.getValue() + docProperty);
            }
            else {
                replaceImage(wmlPackage, (ByteArrayResource)fieldValue, docProperty);
            }
        }
        else if (field.getType() == Boolean.class) {
            var checked = fieldValue != null && (Boolean) fieldValue;
            docProps.setProperty(docProperty, checked ? "☒" : "☐");
        }
        else {
            docProps.setProperty(docProperty, fieldValue == null ? "" : fieldValue.toString());
        }
    }

    private void
    replaceImage(WordprocessingMLPackage wmlPackage, ByteArrayResource fieldValue, String docProperty)
    {
        var node = findDocPropertyNode(wmlPackage.getMainDocumentPart().getContent(), docProperty);
        var p = findContainingParagraph(node);
        var index = removeComplexDocProperty(p);
        var imgNode  = createImage(wmlPackage, fieldValue.getByteArray());

        p.getContent().add(index, imgNode);
    }

    private void
    replaceCollection(WordprocessingMLPackage wmlPackage,
                      Field field,
                      Collection<Object> fieldValue,
                      String docProperty)
    {
        var fieldType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        var docPropertyNodes = findDocPropertyNodeAll(
            wmlPackage.getMainDocumentPart().getContent(),docProperty);

        for (var docPropertyNode: docPropertyNodes) {
            var repeatedSection = findContainingRepeatedSection(docPropertyNode);
            if (repeatedSection.isPresent()) {
                replaceCollectionRepeatableSection(wmlPackage, repeatedSection.get(), fieldValue,
                    fieldType, docProperty);
            } else {
                replaceCollectionParagraph(wmlPackage, docPropertyNode, fieldValue, fieldType);
            }
        }
    }

    private void
    replaceCollectionParagraph(WordprocessingMLPackage wmlPackage,
                               Object docPropertyNode,
                               Collection<Object> fieldValue,
                               Type fieldType)
    {
        if (isComplexDocProperty(docPropertyNode)) {
            var run = findContainingRun(docPropertyNode);
            var rpr = run == null ? null : run.getRPr();

            var p = findContainingParagraph(docPropertyNode);
            var startIndex = removeComplexDocProperty(p);

            var createdRuns = createRuns(wmlPackage, fieldValue, fieldType, rpr);
            p.getContent().addAll(startIndex, createdRuns);
        }
        else if (isSimpleDocProperty(docPropertyNode)) {
            var fldSimple = (CTSimpleField) docPropertyNode;
            var fldSimpleParent = fldSimple.getParent();

            if (fldSimpleParent instanceof R) {
                var p = findContainingParagraph(docPropertyNode);
                var run = (R) fldSimpleParent;
                var rpr = run == null ? null : run.getRPr();
                int runIndex = p.getContent().indexOf(run);
                p.getContent().remove(runIndex);

                var createdRuns = createRuns(wmlPackage, fieldValue, fieldType, rpr);
                p.getContent().addAll(runIndex, createdRuns);
            } else if (fldSimpleParent instanceof P) {
                var p = (P) fldSimpleParent;
                int fldSimpleIndex = p.getContent().indexOf(fldSimple);

                var createdRuns = createRuns(wmlPackage, fieldValue, fieldType, null);
                p.getContent().addAll(fldSimpleIndex, createdRuns);
            } else {
                throw new IllegalArgumentException("Okänd typ på nod: " + fldSimpleParent.getClass());
            }
        } else {
            throw new IllegalArgumentException("Kunde inte avgöra typen på field");
        }
    }

    private void
    replaceCollectionRepeatableSection(WordprocessingMLPackage wmlPackage,
                                       SdtElement repeatedElement,
                                       Collection<Object> fieldValue,
                                       Type fieldType,
                                       String docProperty)
    {
        var repeatedSectionParent = (ContentAccessor) repeatedElement.getParent();
        var repeatedElementIndex = repeatedSectionParent.getContent().indexOf(repeatedElement);
        var sdtContent = repeatedElement.getSdtContent();
        var docPropertyNode = findDocPropertyNode(sdtContent, docProperty);
        var docPropertyNodeParent = ((Child)docPropertyNode).getParent();

        repeatedSectionParent.getContent().remove(repeatedElement);

        if (isSimpleDocProperty(docPropertyNode)) {
            for (var item: fieldValue) {
                var repeatedSectionCopy = (ContentAccessor) XmlUtils.deepCopy(sdtContent);
                var docPropertyNodeCopy = findDocPropertyNode(repeatedSectionCopy, docProperty);
                var fldSimple = (CTSimpleField)docPropertyNodeCopy;
                var fldSimpleParent = fldSimple.getParent();

                if (docPropertyNodeParent instanceof R) {
                    var p = findContainingParagraph(docPropertyNodeCopy);
                    var run = (R)fldSimpleParent;
                    var rpr = run == null ? null : run.getRPr();
                    int runIndex = p.getContent().indexOf(run);
                    var newRun = createRun(wmlPackage, item, fieldType, rpr);
                    p.getContent().set(runIndex, newRun);
                }
                else if (docPropertyNodeParent instanceof P) {
                    var p = (P)fldSimpleParent;
                    int fldSimpleIndex = p.getContent().indexOf(fldSimple);
                    var newRun = createRun(wmlPackage, item, fieldType, null);
                    p.getContent().set(fldSimpleIndex, newRun);
                }
                else {
                    throw new IllegalArgumentException("Okänd typ på nod: " + fldSimpleParent.getClass());
                }

                repeatedSectionParent.getContent().addAll(repeatedElementIndex, repeatedSectionCopy.getContent());
            }
        }
        else if(isComplexDocProperty(docPropertyNode)) {
            for (var item: fieldValue) {
                var repeatedSectionCopy = (ContentAccessor) XmlUtils.deepCopy(sdtContent);
                var docPropertyNodeCopy = findDocPropertyNode(repeatedSectionCopy, docProperty);

                var run = findContainingRun(docPropertyNodeCopy);
                var rpr = run == null ? null : run.getRPr();

                var p = findContainingParagraph(docPropertyNodeCopy);
                var startIndex = removeComplexDocProperty(p);
                var createdRun = createRun(wmlPackage, item, fieldType, rpr);

                p.getContent().add(startIndex, createdRun);

                repeatedSectionParent.getContent().addAll(repeatedElementIndex, repeatedSectionCopy.getContent());
            }
        }
        else {
            throw new IllegalArgumentException("Kunde inte avgöra typen på field");
        }
    }

    private Object findDocPropertyNode(Object parent, String docProperty) {
        return findDocPropertySimple(parent, docProperty)
                .or(() -> findDocPropertyComplex(parent, docProperty))
                .orElseThrow(() -> new IllegalArgumentException("Kan inte hitta xml-nod för DocProperty " + docProperty));
    }

    private List<Object> findDocPropertyNodeAll(Object parent, String docProperty) {
        var simple = findDocPropertySimpleAll(parent, docProperty);
        var complex = findDocPropertyComplexAll(parent, docProperty);
        return ListUtils.union(simple, complex);
    }

    private List<Object> findDocPropertySimpleAll(Object parent, String docProperty) {
        var fields = new ArrayList<>();

        ClassFinder simpleFieldFinder = new ClassFinder(CTSimpleField.class);
        new TraversalUtil(parent, simpleFieldFinder);
        for (Object node: simpleFieldFinder.results) {
            var simpleField = (CTSimpleField) node;
            if (isDocPropertyText(simpleField.getInstr(), docProperty)) {
                fields.add(simpleField);
            }
        }

        return fields;
    }

    private List<Object> findDocPropertyComplexAll(Object parent, String docProperty) {
        var fields = new ArrayList<>();

        ClassFinder textFinder = new ClassFinder(Text.class);
        new TraversalUtil(parent, textFinder);
        for (Object node: textFinder.results) {
            if (node instanceof Text) {
                Text t = (Text) XmlUtils.unwrap(node);

                if (isDocPropertyText(t.getValue(), docProperty)) {
                    fields.add(t);
                }
            }
        }

        return fields;
    }

    private Optional<Object> findDocPropertySimple(Object parent, String docProperty) {
        ClassFinder simpleFieldFinder = new ClassFinder(CTSimpleField.class);
        new TraversalUtil(parent, simpleFieldFinder);
        for (Object node: simpleFieldFinder.results) {
            var simpleField = (CTSimpleField) node;
            if (isDocPropertyText(simpleField.getInstr(), docProperty)) {
                return Optional.of(simpleField);
            }
        }

        return Optional.empty();
    }

    private Optional<Object> findDocPropertyComplex(Object parent, String docProperty) {
        ClassFinder textFinder = new ClassFinder(Text.class);
        new TraversalUtil(parent, textFinder);
        for (Object node: textFinder.results) {
            if (node instanceof Text) {
                Text t = (Text) XmlUtils.unwrap(node);

                if (isDocPropertyText(t.getValue(), docProperty)) {
                    return Optional.of(t);
                }
            }
        }

        return Optional.empty();
    }

    private P findContainingParagraph(Object node) {
        if (node instanceof P) {
            return (P) node;
        }
        else if (node instanceof Child) {
            var child = (Child) node;
            return findContainingParagraph(child.getParent());
        }
        else {
            return null;
        }
    }

    private R findContainingRun(Object node) {
        if (node instanceof R) {
            return (R) node;
        }
        else if (node instanceof Child) {
            var child = (Child) node;
            return findContainingRun(child.getParent());
        }
        else {
            return null;
        }
    }

    private Optional<SdtElement> findContainingRepeatedSection(Object node) {
        if (node instanceof SdtElement) {
            var sdtElement = (SdtElement) node;

            return hasRepeatableSection(sdtElement) ? Optional.of(sdtElement) :
                                                      findContainingRepeatedSection(sdtElement.getParent());
        }
        else if (node instanceof Child) {
            var child = (Child) node;
            return findContainingRepeatedSection(child.getParent());
        }
        else {
            return Optional.empty();
        }
    }

    private Object
    createRun(WordprocessingMLPackage wmlPackage, Object field, Type fieldType, RPr rpr) {
        if (fieldType == ByteArrayResource.class) {
            var img = (ByteArrayResource) field;
            return createImage(wmlPackage, img.getByteArray());
        }
        else if (fieldType == Boolean.class) {
            return createCheckbox((Boolean)field, rpr);
        }
        else {
            return createText(field.toString(), rpr);
        }
    }

    private List<Object>
    createRuns(WordprocessingMLPackage wmlPackage, Collection<Object> fields, Type fieldType, RPr rpr) {
        return fields.stream()
            .map(f -> createRun(wmlPackage, f, fieldType, rpr))
            .collect(Collectors.toList());
    }

    private R createImage(WordprocessingMLPackage wmlPackage, byte[] bytes) {
        try {
            var imagePart = BinaryPartAbstractImage.createImagePart(wmlPackage, bytes);

            var inline = imagePart.createImageInline("hint", "altText",
                ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE),
                ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE),
                false);

            var factory = Context.getWmlObjectFactory();
            var run = factory.createR();
            var drawing = factory.createDrawing();

            run.getContent().add(drawing);

            drawing.getAnchorOrInline().add(inline);

            return run;
        } catch(Exception e) { //createImagePart och createImageInline kastar generella exceptions...
            // Fiffigt att de la till UncheckedIOException. Innebär att vi kan kasta IOExceptions från streams
            throw new UncheckedIOException(new IOException(e));
        }
    }

    private R createCheckbox(Boolean fieldValue, RPr rpr) {
        return createText(fieldValue ? "☒" : "☐", rpr);
    }

    private R createText(String msg, RPr rpr) {
        var factory = Context.getWmlObjectFactory();

        var text = factory.createText();
        var run = factory.createR();

        text.setValue(msg);
        run.getContent().add(text);
        run.setRPr(rpr);

        return run;
    }

    private boolean hasRepeatableSection(SdtElement sdtElement) {
        return sdtElement.getSdtPr()
                .getRPrOrAliasOrLock()
                .stream()
                .map(XmlUtils::unwrap)
                .filter(rpr -> rpr instanceof CTSdtRepeatedSection)
                .findAny()
                .isPresent();
    }

    private boolean isDocPropertyText(String text, String docProperty) {
        var textStripped = text.replaceAll("\\s+", "").replaceAll("\"", "");
        return textStripped.startsWith("DOCPROPERTY" + docProperty);
    }

    private void addDocPropertes(WordprocessingMLPackage wmlPackage, Class<?> clazz) {
        var docProps = wmlPackage.getDocPropsCustomPart();
        if (docProps == null) {
            wmlPackage.addDocPropsCustomPart();
            docProps = wmlPackage.getDocPropsCustomPart();
        }

        var availabaleDocProperties = ReflectionUtil.getFieldsWithAnnotationRecursive(clazz, DocProperty.class)
            .stream()
            .filter(f -> !f.isAnnotationPresent(Deprecated.class))
            .map(f -> f.getAnnotation(DocProperty.class).value())
            .sorted()
            .collect(Collectors.toList());

        for (var docProperty: availabaleDocProperties) {
            docProps.setProperty(docProperty, docProperty);
        }
    }

    private Boolean isSimpleDocProperty(Object docPropertyNode) {
        return docPropertyNode instanceof  CTSimpleField;
    }

    private Boolean isComplexDocProperty(Object docPropertyNode) {
        var p = findContainingParagraph(docPropertyNode);
        try {
            findFldCharTypeIndex(p, STFldCharType.BEGIN);
            findFldCharTypeIndex(p, STFldCharType.END);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    private Integer removeComplexDocProperty(P p) {
        int startIndex = findFldCharTypeIndex(p, STFldCharType.BEGIN);
        int endIndex = findFldCharTypeIndex(p, STFldCharType.END);

        if (startIndex < 0) {
            throw new IllegalArgumentException("Hittade inget startIndex");
        }
        if (endIndex < 0) {
            throw new IllegalArgumentException("Hittade inget endIndex");
        }

        for (var i = startIndex; i <= endIndex; i++) {
            p.getContent().remove(startIndex);
        }

        return startIndex;
    }

    private Integer findFldCharTypeIndex(ContentAccessor parent, STFldCharType fldCharType) {
        for (int i = 0; i < parent.getContent().size(); i++) {
            if (!(parent.getContent().get(i) instanceof R)) {
                continue;
            }
            var child = (R)parent.getContent().get(i);
            var fldCharFinder = new ClassFinder(FldChar.class);
            new TraversalUtil(child, fldCharFinder);
            for (var result: fldCharFinder.results) {
                var fldChar = (FldChar)result;
                if (fldChar.getFldCharType() == fldCharType) {
                    return i;
                }
            }
        }

        throw new IllegalArgumentException("Kunde inte hitta fieldtype " + fldCharType);
    }
}
