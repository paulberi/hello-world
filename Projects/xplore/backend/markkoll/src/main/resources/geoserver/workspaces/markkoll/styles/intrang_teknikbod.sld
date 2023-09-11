<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        
        <!-- Intrång -->
        <sld:Rule>
          <sld:Title>Ny site/teknikbod</sld:Title>
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
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#0681FA</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#333333</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
              <sld:Rotation>45.0</sld:Rotation>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>        
        
        <!-- Servis -->
        <sld:Rule>
          <sld:Title>Ny site/teknikbod - Servis</sld:Title>
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
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#FFFF00</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#333333</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
              <sld:Rotation>45.0</sld:Rotation>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>        
        
        <!-- Rev -->
        <sld:Rule>
          <sld:Title>Ny site/teknikbod - Rev</sld:Title>
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
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#00AB00</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#333333</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
              <sld:Rotation>45.0</sld:Rotation>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
        
        <!-- Raseras -->
        <sld:Rule>
          <sld:Title>Site/teknikbod som ska tas bort</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>status</ogc:PropertyName>
              <ogc:Literal>RASERAS</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:Fill>
                  <sld:CssParameter name="fill">#ffffff</sld:CssParameter>
                </sld:Fill>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#333333</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>10</sld:Size>
              <sld:Rotation>45.0</sld:Rotation>
            </sld:Graphic>
          </sld:PointSymbolizer>
          <sld:PointSymbolizer>
            <sld:Graphic>
              <sld:Mark>
                <sld:WellKnownName>shape://slash</sld:WellKnownName>
                <sld:Stroke>
                  <sld:CssParameter name="stroke">#333333</sld:CssParameter>
                </sld:Stroke>
              </sld:Mark>
              <sld:Size>12</sld:Size>
              <sld:Rotation>0.5</sld:Rotation>
            </sld:Graphic>
          </sld:PointSymbolizer>
        </sld:Rule>
        
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>
