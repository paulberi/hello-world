<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>korridor2</Name>
    <UserStyle>
      <Name>korridor2</Name>
      <FeatureTypeStyle>
        <Rule>
          <Name>Alternativ</Name>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">
                <ogc:Function name="Recode">
                  <!-- Value to transform -->
                  <ogc:Function name="strTrim">
                    <ogc:PropertyName>Alternativ</ogc:PropertyName>
                  </ogc:Function>

                  <!-- Map of input to output values -->
                  <ogc:Literal>A1</ogc:Literal>
                  <ogc:Literal>#EC6222</ogc:Literal>

                  <ogc:Literal>A2</ogc:Literal>
                  <ogc:Literal>#569ECF</ogc:Literal>

                  <ogc:Literal>A3</ogc:Literal>
                  <ogc:Literal>#7FEA46</ogc:Literal>

                  <ogc:Literal>A4</ogc:Literal>
                  <ogc:Literal>#D65CCE</ogc:Literal>

                  <ogc:Literal>A5</ogc:Literal>
                  <ogc:Literal>#E4C8D5</ogc:Literal>

                  <ogc:Literal>A6</ogc:Literal>
                  <ogc:Literal>#D5E4FC</ogc:Literal>
                </ogc:Function>
              </CssParameter>
              <CssParameter name="fill-opacity">0.7</CssParameter>              
            </Fill>
            <Stroke>
              <CssParameter name="stroke">#232323</CssParameter>
              <CssParameter name="stroke-width">0.1</CssParameter>
              <CssParameter name="stroke-opacity">0.7</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
          <TextSymbolizer>
            <Label>
              <ogc:Function name="Recode">
                <!-- Value to transform -->
                <ogc:Function name="strTrim">
                  <ogc:PropertyName>Alternativ</ogc:PropertyName>
                </ogc:Function>
                
                <ogc:Literal>A1</ogc:Literal>
                <ogc:Literal>A1</ogc:Literal>
                
                <ogc:Literal>A2</ogc:Literal>
                <ogc:Literal>A2</ogc:Literal>
                
                <ogc:Literal>A3</ogc:Literal>
                <ogc:Literal>A3</ogc:Literal>
                
                <ogc:Literal>A4</ogc:Literal>
                <ogc:Literal>A4</ogc:Literal>
                
                <ogc:Literal>A5</ogc:Literal>
                <ogc:Literal>A5</ogc:Literal>
                
                <ogc:Literal>A6</ogc:Literal>
                <ogc:Literal>A6</ogc:Literal>
              </ogc:Function>
            </Label>
       	  </TextSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>