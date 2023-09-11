<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        
        <!-- Intrång -->
        <sld:Rule>
          <sld:Title>Ny brunn</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>avtalstyp</ogc:PropertyName>
              <ogc:Literal>INTRANG</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#0681FA</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#002B55</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
        
        <!-- Servis -->
        <sld:Rule>
          <sld:Title>Ny brunn - Servis</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>avtalstyp</ogc:PropertyName>
              <ogc:Literal>SERVIS</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#FFFF00</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#002B55</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
        
        <!-- Rev -->
        <sld:Rule>
          <sld:Title>Ny brunn - Rev</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>avtalstyp</ogc:PropertyName>
              <ogc:Literal>REV</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#00AB00</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#002B55</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>
