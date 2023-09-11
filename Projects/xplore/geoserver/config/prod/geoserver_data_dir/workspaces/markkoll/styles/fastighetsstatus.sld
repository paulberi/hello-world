<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:Title>Fastighetsstatus</sld:Title>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Name>Outredd</sld:Name>
          <sld:Title>Outredd</sld:Title>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>outredd</ogc:PropertyName>
              <ogc:Literal>1</ogc:Literal>
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
          <sld:Name>Avtal signerat</sld:Name>
          <sld:Title>Avtal signerat</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>AVTAL_SIGNERAT</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#18BC11</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.12</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>star</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#18BC11</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke/>
                  </sld:Mark>
                  <sld:Size>12</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#18BC11</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">24</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#18BC11</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>Avtal skickat</sld:Name>
          <sld:Title>Avtal skickat</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>AVTAL_SKICKAT</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#D6C045</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.15</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>circle</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#000000</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                      <sld:CssParameter name="stroke">#D6C045</sld:CssParameter>
                      <sld:CssParameter name="stroke-width">2</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>7</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#000000</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">24</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#D6C045</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>Avtal justeras</sld:Name>
          <sld:Title>Avtal justeras</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>AVTAL_JUSTERAS</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#D6C045</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.1</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:WellKnownName>triangle</sld:WellKnownName>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#D6C045</sld:CssParameter>
                    </sld:Fill>
                  </sld:Mark>
                  <sld:Size>12</sld:Size>
                  <sld:Rotation>180.0</sld:Rotation>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#D6C045</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">24</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#D6C045</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>Avtalskonflikt</sld:Name>
          <sld:Title>Avtalskonflikt</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>AVTALSKONFLIKT</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
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
        <sld:Rule>
          <sld:Name>Ej behandlat</sld:Name>
          <sld:Title>Ej behandlat</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>EJ_BEHANDLAT</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>geometristatus</ogc:PropertyName>
                <ogc:Literal>OFORANDRAD</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#0B87BE</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.1</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#000000</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                      <sld:CssParameter name="stroke">#0B87BE</sld:CssParameter>
                      <sld:CssParameter name="stroke-width">2</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>7</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#000000</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">24</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#0B87BE</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>Ej behandlat - Uppdaterad</sld:Name>
          <sld:Title>Ej behandlat - Uppdaterad</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>EJ_BEHANDLAT</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>geometristatus</ogc:PropertyName>
                <ogc:Literal>UPPDATERAD</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#0B87BE</sld:CssParameter>
              <sld:CssParameter name="fill-opacity">0.1</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                    <sld:Fill>
                      <sld:CssParameter name="fill">#000000</sld:CssParameter>
                    </sld:Fill>
                    <sld:Stroke>
                      <sld:CssParameter name="stroke">#0B87BE</sld:CssParameter>
                      <sld:CssParameter name="stroke-width">2</sld:CssParameter>
                    </sld:Stroke>
                  </sld:Mark>
                  <sld:Size>7</sld:Size>
                </sld:Graphic>
              </sld:GraphicFill>
              <sld:CssParameter name="fill">#000000</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="graphic-margin">24</sld:VendorOption>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#0B87BE</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>Ej behandlat - Ny</sld:Name>
          <sld:Title>Ej behandlat - Ny</sld:Title>
          <ogc:Filter>
            <ogc:And>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>outredd</ogc:PropertyName>
                <ogc:Literal>0</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>status</ogc:PropertyName>
                <ogc:Literal>EJ_BEHANDLAT</ogc:Literal>
              </ogc:PropertyIsEqualTo>
              <ogc:PropertyIsEqualTo>
                <ogc:PropertyName>geometristatus</ogc:PropertyName>
                <ogc:Literal>NY</ogc:Literal>
              </ogc:PropertyIsEqualTo>
            </ogc:And>
          </ogc:Filter>
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

