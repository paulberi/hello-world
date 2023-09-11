<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>Utredningsomrade</Name>
    <UserStyle>
      <Name>Utredningsomrade</Name>
      <FeatureTypeStyle>
        <Rule>
          <Name>B1</Name>
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>Utrednings</ogc:PropertyName>
              <ogc:Literal>B1</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <PolygonSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#dca628</CssParameter>
              <CssParameter name="stroke-width">3</CssParameter>
              <CssParameter name="stroke-dasharray">4 2</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
        <Rule>
          <Name>B2</Name>
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>Utrednings</ogc:PropertyName>
              <ogc:Literal>B2</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <PolygonSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#42a292</CssParameter>
              <CssParameter name="stroke-width">3</CssParameter>
              <CssParameter name="stroke-dasharray">4 2</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
        <Rule>
          <Name>Empty</Name>
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>Utrednings</ogc:PropertyName>
              <ogc:Literal></ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <PolygonSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#9523c9</CssParameter>
              <CssParameter name="stroke-width">3</CssParameter>
              <CssParameter name="stroke-dasharray">4 2</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>