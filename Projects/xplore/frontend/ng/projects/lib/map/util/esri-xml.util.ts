export function convertFromEsriXmlToJson(xml: string): string {
  // Todo: Det här funkar ganska kasst, hela översättningen borde skrivas om. Det blir problem med
  // tecken som escapas olika i xml och json

  const xmlDoc = (new DOMParser()).parseFromString(xml, "text/xml");
  if (getParserError(xmlDoc) != null) {
    throw new Error("XML parse error: " + getParserError(xmlDoc));
  }

  // Xslt kommer från ESRI dokumentationen,
  // https://enterprise.arcgis.com/en/server/latest/publish-services/windows/customizing-a-wms-getfeatureinfo-response.htm
  const xslt = `
    <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:esri_wms="http://www.esri.com/wms" xmlns="http://www.esri.com/wms">
  <xsl:output method="text" indent="no" encoding="ISO-8859-1"/>
  <xsl:template match="/">
    {
    "type": "FeatureCollection",
    <xsl:if test="count(esri_wms:FeatureInfoResponse/esri_wms:FeatureInfoCollection/esri_wms:FeatureInfo/esri_wms:CRS) > 0">
    "spatialReference": { "wkid": <xsl:value-of select="esri_wms:FeatureInfoResponse/esri_wms:FeatureInfoCollection/esri_wms:FeatureInfo/esri_wms:CRS"/> },
    </xsl:if>
    "features": [<xsl:for-each select="esri_wms:FeatureInfoResponse/esri_wms:FeatureInfoCollection/esri_wms:FeatureInfo">
      {
        "type": "Feature",
    <xsl:if test="count(esri_wms:Field/esri_wms:FieldGeometry/*) > 0">
        "geometry": {
          <xsl:for-each select="esri_wms:Field/esri_wms:FieldGeometry">
            <xsl:for-each select="esri_wms:Point">"type": "Point",              "coordinates": [<xsl:value-of select="."/>]</xsl:for-each>
            <xsl:for-each select="esri_wms:Multipoint">"type": "MultiPoint",    "coordinates": [<xsl:for-each select="esri_wms:Point">[<xsl:value-of select="."/>]<xsl:if test = "position() != last()">,</xsl:if></xsl:for-each>]</xsl:for-each>
            <xsl:for-each select="esri_wms:Polyline">"type": "MultiLineString", "coordinates": [<xsl:for-each select="esri_wms:Path"> [<xsl:for-each select="esri_wms:Point">[<xsl:value-of select="."/>]<xsl:if test = "position() != last()">,</xsl:if></xsl:for-each>]<xsl:if test = "position() != last()">,</xsl:if></xsl:for-each>]</xsl:for-each>
            <xsl:for-each select="esri_wms:Polygon">"type": "Polygon",          "coordinates": [<xsl:for-each select="esri_wms:Ring"> [<xsl:for-each select="esri_wms:Point">[<xsl:value-of select="."/>]<xsl:if test = "position() != last()">,</xsl:if></xsl:for-each>]<xsl:if test = "position() != last()">,</xsl:if></xsl:for-each>]</xsl:for-each>
          </xsl:for-each>
      },
    </xsl:if>
    "properties": {<xsl:for-each select="esri_wms:Field">
          "<xsl:value-of select="esri_wms:FieldName"/>": "<xsl:value-of select="esri_wms:FieldValue"/>"<xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>
        },
        "layerName": "<xsl:value-of select="../@layername"/>"\t
      }<xsl:if test="position() != last()">,</xsl:if>
      </xsl:for-each>
    ]
  }
  </xsl:template>
  </xsl:stylesheet>
    `;

  const xsltDoc = (new DOMParser()).parseFromString(xslt, "text/xml");
  if (getParserError(xsltDoc) != null) {
    throw new Error("XSLT parse error: " + getParserError(xsltDoc));
  }

  // För-escapa lite värden för json som inte får en XML escape
  const fieldValueNodes = xmlDoc.querySelectorAll("FieldName");
  for (let i = 0; i < fieldValueNodes.length; i++) {
    let fieldValueText = fieldValueNodes[i].innerHTML;
    fieldValueText = fieldValueText.replace(/\\/g, "&#92;");
    fieldValueText = fieldValueText.replace(/\r\n|\r|\n/g, "&#10;");
    fieldValueNodes[i].innerHTML = fieldValueText;
  }
  const fieldNameNodes = xmlDoc.querySelectorAll("FieldName");
  for (let i = 0; i < fieldNameNodes.length; i++) {
    let fieldNameText = fieldNameNodes[i].innerHTML;
    fieldNameText = fieldNameText.replace(/\\/g, "&#92;");
    fieldNameText = fieldNameText.replace(/\r\n|\r|\n/g, "&#10;");
    fieldNameNodes[i].innerHTML = fieldNameText;
  }

  const swedisPointRegex = /^([0-9]+),([0-9]+),([0-9]+),([0-9]+)/;

  // Fix number format (0 i början av siffror inte tillåtet i json)
  const pointNodes = xmlDoc.querySelectorAll("Point");
  for (let i = 0; i < pointNodes.length; i++) {
    let pointText = pointNodes[i].innerHTML;

    if (swedisPointRegex.test(pointText)) {
      const result = pointText.match(swedisPointRegex);

      pointText = result[1] + "." + result[2] + "," + result[3] + "." + result[4];
    }

    const numbers = pointText.split(",");
    const fiexdNumbers = numbers.map(s => {
      if (/^0+$/.test(s)) {
        return "0";
      }

      return s.replace(/^0+/, "");
    });
    pointNodes[i].innerHTML = fiexdNumbers.join(",");
  }

  // Convert to json
  const processor = new XSLTProcessor();
  processor.importStylesheet(xsltDoc);
  const json = (new XMLSerializer()).serializeToString(processor.transformToFragment(xmlDoc, document));

  // Change xml escape to json escape
  const unescapedJson = json
    .replace(/&gt;/g, ">")
    .replace(/&lt;/g, "<")
    .replace(/&amp;/g, "&")
    .replace(/&quot;/g, "\\\"")
    .replace(/&apos;/g, "'")
    .replace(/&#10;/g, "\\n")
    .replace(/&#92;/g, "\\");

  return unescapedJson;
}

function getParserError(document) {
  const err = document.getElementsByTagName("parsererror")[0];
  if (err) {
    return err.textContent ? err.textContent.split("\n", 1)[0] : "unknown parser error";
  }

  return null;
}

