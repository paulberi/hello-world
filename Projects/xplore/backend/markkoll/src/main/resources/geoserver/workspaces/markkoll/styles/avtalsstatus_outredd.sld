<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Title>Outredd fastighet/samfällighet</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:Function name="dimension">
                <ogc:PropertyName>geom</ogc:PropertyName>
              </ogc:Function>
              <ogc:Literal>2</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#A5A7AC</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.15</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>shape://backslash</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#A5A7AC</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                      <sld:CssParameter name="stroke">#A5A7AC</sld:CssParameter>
                      <sld:CssParameter name="stroke-width">2</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>24</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#A5A7AC</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#A5A7AC</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Outredd fastighet/samfällighet</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:Function name="dimension">
                <ogc:PropertyName>geom</ogc:PropertyName>
              </ogc:Function>
              <ogc:Literal>1</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#A5A7AC</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:GraphicStroke>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>shape://backslash</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#A5A7AC</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                      <sld:CssParameter name="stroke">#A5A7AC</sld:CssParameter>
                      <sld:CssParameter name="stroke-width">2</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>24</sld:Size>
                </sld:Graphic>
              </sld:GraphicStroke>
              <sld:CssParameter name="stroke">#A5A7AC</sld:CssParameter>
              <sld:CssParameter name="stroke-width">2</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">4.0 16.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>

