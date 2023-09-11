<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <!-- a named layer is the basic building block of an sld document -->

    <NamedLayer>
        <Name>Default Line</Name>
        <UserStyle>
            <!-- they have names, titles and abstracts -->

            <Title>Vandringsled</Title>
            <Abstract>Teststil för Ydre</Abstract>
            <!-- FeatureTypeStyles describe how to render different features -->
            <!-- a feature type for lines -->

            <FeatureTypeStyle>
                <!--FeatureTypeName>Feature</FeatureTypeName-->
                <Rule>
                    <Name>Vandringsled</Name>
                    <Title>Green Line</Title>
                    <!-- like a polygonsymbolizer -->
                    <LineSymbolizer>
                        <Stroke>
                            <CssParameter name="stroke">#E0E000</CssParameter>
                            <CssParameter name="stroke-width">4</CssParameter>
                            <CssParameter name="stroke-dasharray">4 6</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>
                <Rule>
                    <MaxScaleDenominator>
                        500
                    </MaxScaleDenominator>
                    <LineSymbolizer>
                        <Stroke>
                            <GraphicStroke>
                                <Graphic>
                                  <Mark>
                                    <WellKnownName>circle</WellKnownName>
                                    <Stroke>
                                      <CssParameter name="stroke">#ffff00</CssParameter>
                                      <CssParameter name="stroke-width">5</CssParameter>
                                    </Stroke>
                                  </Mark>
                                  <Size>18</Size>
                                </Graphic>
                              </GraphicStroke>
                              <CssParameter name="stroke-dasharray">18 200</CssParameter>
                              <CssParameter name="stroke-dashoffset">80</CssParameter>
                        </Stroke>
                    </LineSymbolizer>
                </Rule>
                <Rule>
                    <PointSymbolizer>
                        <Geometry>
                            <ogc:Function name="startPoint">
                                <ogc:PropertyName>geom</ogc:PropertyName>
                            </ogc:Function>
                        </Geometry>
                        <Graphic>
                            <Mark>
                                <WellKnownName>shape://oarrow</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#ffff00</CssParameter>
                                    <CssParameter name="fill-opacity">0.5</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#ffff00</CssParameter>
                                    <CssParameter name="stroke-width">2</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>40</Size>
                            <Rotation>
                                <ogc:Function name="startAngle">
                                    <ogc:PropertyName>geom</ogc:PropertyName>
                                </ogc:Function>
                            </Rotation>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>
                <Rule>
                    <MaxScaleDenominator>
                        10000
                    </MaxScaleDenominator>
                    <PointSymbolizer>
                        <Geometry>
                            <ogc:Function name="endPoint">
                                <ogc:PropertyName>geom</ogc:PropertyName>
                            </ogc:Function>
                        </Geometry>
                        <Graphic>
                            <Mark>
                                <WellKnownName>circle</WellKnownName>
                                <Fill>
                                    <CssParameter name="fill">#ffff00</CssParameter>
                                    <CssParameter name="fill-opacity">0.5</CssParameter>
                                </Fill>
                                <Stroke>
                                    <CssParameter name="stroke">#ffff00</CssParameter>
                                    <CssParameter name="stroke-width">2</CssParameter>
                                </Stroke>
                            </Mark>
                            <Size>20</Size>
                            <Rotation>
                                <ogc:Function name="startAngle">
                                    <ogc:PropertyName>geom</ogc:PropertyName>
                                </ogc:Function>
                            </Rotation>
                        </Graphic>
                    </PointSymbolizer>
                </Rule>

            </FeatureTypeStyle>
        </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>