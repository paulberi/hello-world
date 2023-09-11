package se.metria.markkoll.util.dokument.forteckninggenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import se.metria.markkoll.util.ReflectionUtil;
import se.metria.markkoll.util.StringUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ForteckningGenerator {
    public byte[] generate(InputStream xlsxTemplate, List<?> data) throws IOException {
        var workbook = new XSSFWorkbook(xlsxTemplate);

        var dataMaps = data.stream().map(this::getDataMap).collect(Collectors.toList());

        for (var sheet: workbook) {
            generateSheet(sheet, dataMaps);
        }

        var baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }

    private void autosizeColumns(Sheet sheet, Map<Integer, VariableDefinition> variableDefinitions) {
        for (var column: variableDefinitions.keySet()) {
            sheet.autoSizeColumn(column);
        }
    }

    private void clearRow(Row row) {
        for (var cell: row) {
            row.removeCell(cell);
        }
    }

    private void clearVariableColumns(Row row, Map<Integer, VariableDefinition> variableDefinitions) {
        for (var column: variableDefinitions.keySet()) {
            var cell = row.getCell(column);
            if (cell != null) {
                cell.setCellValue("");
            }
        }
    }

    private void copyCellValue(Cell from, Cell to) {
        switch (from.getCellType()) {
            case NUMERIC:
                to.setCellValue(from.getNumericCellValue());
                break;
            case STRING:
                to.setCellValue(from.getStringCellValue());
                break;
            case BOOLEAN:
                to.setCellValue(from.getBooleanCellValue());
                break;
            case FORMULA:
                to.setCellValue(from.getCellFormula());
                break;
            case BLANK:
            case _NONE:
                break;
            case ERROR:
                to.setCellValue(from.getErrorCellValue());
                break;
            default:
                throw new IllegalArgumentException("Okänd celltyp: " + from.getCellType());
        }
    }

    private void copyRow(Row rowFrom, Row rowTo) {
        for (var cellFrom: rowFrom) {
            var cellTo = rowTo.createCell(cellFrom.getColumnIndex());
            copyCellValue(cellFrom, cellTo);
            cellTo.setCellStyle(cellFrom.getCellStyle());
        }
    }

    private Map<Integer, VariableDefinition> findVariableDefinitions(Row row) {
        var variablePattern = Pattern.compile("^(?<prefix>.*)\\$\\$(?<variable>.*)\\$\\$(?<suffix>.*)$");

        var defs = new HashMap<Integer, VariableDefinition>();
        for (var cell: row) {
            var cellValue = getCellValue(cell);
            var matcher = variablePattern.matcher(cellValue);
            if (matcher.find()) {
                var prefix = matcher.group("prefix");
                var variableName = matcher.group("variable").toUpperCase();
                var suffix = matcher.group("suffix");
                defs.put(cell.getColumnIndex(), new VariableDefinition(prefix, variableName, suffix));
            }
        }
        return defs;
    }

    private Map<String, String> getDataMap(Object row) {
        var map = new HashMap<String, String>();

        try {
            var fieldValues = ReflectionUtil.getFieldValuesWithAnnotationRecursive(row, JsonProperty.class);
            for (var fv : fieldValues) {
                var annotationValue = fv.getField().getAnnotation(JsonProperty.class).value().toUpperCase();
                var getterValue = invokeGetterMethod(fv);
                map.put(annotationValue, getterValue == null ? "" : getterValue.toString());
            }
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ForteckningGeneratorException(e);
        }

        return map;
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC:
                return Double.toString(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
            case _NONE:
                return null;
            case ERROR:
                return Byte.toString(cell.getErrorCellValue());
            default:
                throw new IllegalArgumentException("Okänd celltyp: " + cell.getCellType());
        }
    }

    private void generateSheet(Sheet sheet, List<Map<String, String>> dataMaps) {
        Row templateRow = null;
        Map<Integer, VariableDefinition> variableDefinitions = new HashMap<>();

        var rowIterator = sheet.iterator();
        while (rowIterator.hasNext() && variableDefinitions.isEmpty()) {
            var row = rowIterator.next();
            variableDefinitions = findVariableDefinitions(row);
            templateRow = row;
        }

        if (templateRow != null) {
            clearVariableColumns(templateRow, variableDefinitions);
        }
        if (!variableDefinitions.isEmpty()) {
            for (var r = 0; r < dataMaps.size(); r++) {
                Row row;
                if (r != 0) {
                    row = sheet.createRow(templateRow.getRowNum() + r);
                    clearRow(row);
                    copyRow(templateRow, row);
                }
                else {
                    row = sheet.getRow(templateRow.getRowNum());
                }
                substituteRowVariables(row, dataMaps.get(r), variableDefinitions);
            }
            autosizeColumns(sheet, variableDefinitions);
        }
    }

    private Object invokeGetterMethod(ReflectionUtil.FieldValuePair fv) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var getterMethodName = "get" + StringUtil.capitalize(fv.getField().getName());
        var getterMethod = fv.getValue().getClass().getMethod(getterMethodName);
        return getterMethod.invoke(fv.getValue());
    }

    private String getVariableValue(Map<String, String> dataMap, VariableDefinition def) {
        var variableValue = dataMap.get(def.variableName);

        return variableValue == null ? "" : def.getPrefix() + dataMap.get(def.variableName) + def.getSuffix();
    }

    private void
    substituteRowVariables(Row newRow, Map<String, String> dataMap, Map<Integer, VariableDefinition> variableDefinitions) {
        for (var entry: variableDefinitions.entrySet()) {
            var defColNum = entry.getKey();
            var def = entry.getValue();
            var cellValue = getVariableValue(dataMap, def);
            newRow.createCell(defColNum).setCellValue(cellValue);
        }
    }

    @Data
    @AllArgsConstructor
    private class VariableDefinition {
        private String prefix;
        private String variableName;
        private String suffix;
    }
}
