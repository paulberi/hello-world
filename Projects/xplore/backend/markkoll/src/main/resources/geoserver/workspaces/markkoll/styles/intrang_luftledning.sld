<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Title>Ny luftledning</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>avtalstyp</ogc:PropertyName>
                <ogc:Literal>INTRANG</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>NY</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#F82396</sld:CssParameter>
              <sld:CssParameter name="stroke-width">2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:GraphicStroke>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>circle</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#F82396</sld:CssParameter>
                    </sld:Fill>
                  </sld:Mark>
                  <sld:Size>5</sld:Size>
                </sld:Graphic>
              </sld:GraphicStroke>
              <sld:CssParameter name="stroke-dasharray">5.0 30.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Ny luftledning - Servis</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>avtalstyp</ogc:PropertyName>
                <ogc:Literal>SERVIS</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>NY</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#707070</sld:CssParameter>
              <sld:CssParameter name="stroke-width">2</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">10.0 4.0 2.0 4.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:GraphicStroke>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>circle</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#707070</sld:CssParameter>
                    </sld:Fill>
                  </sld:Mark>
                  <sld:Size>5</sld:Size>
                </sld:Graphic>
              </sld:GraphicStroke>
              <sld:CssParameter name="stroke-dasharray">5.0 30.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Luftledning i enskilt vägområde</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>avtalstyp</ogc:PropertyName>
                <ogc:Literal>REV</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>NY</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:GraphicStroke>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>circle</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#F82396</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                        <sld:CssParameter name="stroke">#F82396</sld:CssParameter>
                        <sld:CssParameter name="stroke-width">1</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>4</sld:Size>
                </sld:Graphic>
              </sld:GraphicStroke>
              <sld:CssParameter name="stroke-dasharray">4.0 4.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Luftledning i statligt vägområde</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>avtalstyp</ogc:PropertyName>
                <ogc:Literal>ABEL07</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>NY</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#F82396</sld:CssParameter>
              <sld:CssParameter name="stroke-width">2</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">4.0 4.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Title>Luftledning som ska tas bort</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>status</ogc:PropertyName>
              <ogc:Literal>RASERAS</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#333333</sld:CssParameter>
              <sld:CssParameter name="stroke-width">2</sld:CssParameter>
              <sld:CssParameter name="stroke-dashoffset">14</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">8.0 20.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:GraphicStroke>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>x</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#333333</sld:CssParameter>
                    </sld:Fill>
                  </sld:Mark>
                  <sld:Size>8</sld:Size>
                </sld:Graphic>
              </sld:GraphicStroke>
              <sld:CssParameter name="stroke-dasharray">8.0 20.0</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>

