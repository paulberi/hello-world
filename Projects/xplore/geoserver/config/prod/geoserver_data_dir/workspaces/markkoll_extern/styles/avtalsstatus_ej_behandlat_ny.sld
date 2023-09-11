<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:Title>Avtalssstatus</sld:Title>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Name>Ej behandlat - Ny</sld:Name>
          <sld:Title>Ej behandlat - Ny</sld:Title>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#0B87BE</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.12</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>cross</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#000000</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                      <sld:CssParameter name="stroke">#0B87BE</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>14</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#000000</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">26</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#0B87BE</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>

