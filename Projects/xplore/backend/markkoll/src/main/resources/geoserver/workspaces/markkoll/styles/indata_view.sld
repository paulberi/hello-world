<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <Rule>
          <Name>Line</Name>
          <Title>Blå linje</Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:Function name="dimension">
                <ogc:Function name="geometry"/>
              </ogc:Function>
              <ogc:Literal>1</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#0000FF</CssParameter>
              <CssParameter name="stroke-opacity">1</CssParameter>
            </Stroke>
          </LineSymbolizer>
        </Rule>
        <Rule>
          <Name>point</Name>
          <Title>Blå punkt, svart outline</Title>
          <ElseFilter/>
          <MinScaleDenominator>1000.0</MinScaleDenominator>
          <PointSymbolizer>
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#0000FF</CssParameter>
                </Fill>
                <Stroke>
                  <sld:CssParameter name="stroke">#000000</sld:CssParameter>
                </Stroke>
              </Mark>
              <Size>10</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        <Rule>
          <Name>point</Name>
          <Title>Blå punkt, svart outline, närbild</Title>
          <ElseFilter/>
          <MaxScaleDenominator>1000.0</MaxScaleDenominator>
          <PointSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <Graphic>
              <Mark>
                <WellKnownName>circle</WellKnownName>
                <Fill>
                  <CssParameter name="fill">#0000FF</CssParameter>
                </Fill>
                <Stroke>
                  <sld:CssParameter name="stroke">#000000</sld:CssParameter>
                  <sld:CssParameter name="stroke-width">0.3</sld:CssParameter>
                </Stroke>
              </Mark>
              <Size>3</Size>
            </Graphic>
          </PointSymbolizer>
        </Rule>
        
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>