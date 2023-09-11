<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:Title>Avtalssstatus</sld:Title>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Name>Avtalskonflikt</sld:Name>
          <sld:Title>Avtalskonflikt</sld:Title>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#F45959</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.15</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>x</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#F45959</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke/>
                  </sld:Mark>
                  <sld:Size>12</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#F45959</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">24</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#F45959</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>

