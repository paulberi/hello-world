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
          <Name>Utredning</Name>
          <PolygonSymbolizer>
            <Stroke>
              <CssParameter name="stroke">
                <ogc:Function name="Recode">
                  <!-- Value to transform -->
                  <ogc:Function name="strTrim">
                    <ogc:PropertyName>Utrednings</ogc:PropertyName>
                  </ogc:Function>

                  <!-- Map of input to output values -->
                  <ogc:Literal>B1</ogc:Literal>
                  <ogc:Literal>#dca628</ogc:Literal>

                  <ogc:Literal>B2</ogc:Literal>
                  <ogc:Literal>#42a292</ogc:Literal>
                  
                  <ogc:Literal></ogc:Literal>
                  <ogc:Literal>#9523c9</ogc:Literal>
                </ogc:Function>
              </CssParameter>
              <CssParameter name="stroke-width">3</CssParameter>
              <CssParameter name="stroke-dasharray">4 2</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>