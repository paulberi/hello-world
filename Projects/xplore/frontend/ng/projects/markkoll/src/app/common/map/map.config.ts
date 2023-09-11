import { AppConfig, MapConfig, UrlConfig } from "../../../../../lib/config/config.service";
import { environment } from "../../../environments/environment";

export const appConfig: AppConfig = {
  appName: "Markkoll",
};

export const urlConfig: UrlConfig = {
  configurationUrl: null,
  wmsUrl: environment.metriaMapsUrl,
};

export const defaultMapConfig: MapConfig = {
  app: {},
  projectionCode: "EPSG:3006",
  proj4Defs: [
    {
      code: "EPSG:3006",
      alias: "urn:ogc:def:crs:EPSG:3006",
      projection:
        "+proj=utm +zone=33 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs +axis=neu",
      projectionExtent: [190000, 6100000, 950000, 7700000],
    },
  ],
  extent: [
    // Extentet för metria:Europa
    -2055269.5028416523709893,
    3251551.2163324421271682,
    4972096.7646753853186965,
    16485652.308223919942975,
  ],
  tileSize: [256, 256],
  geolocateOnStartup: false,
  center: [384270.2, 6744221.1],
  zoom: 8,
  resolutions: [
    8000,
    4000,
    2000,
    1146.8799999999999,
    573.4399999999999,
    286.71999999999997,
    143.35999999999999,
    71.67999999999999,
    35.839999999999996,
    17.919999999999998,
    8.959999999999999,
    4.4799999999999995,
    2.2399999999999998,
    1.1199999999999999,
    0.5599999999999999,
    0.27999999999999997,
    0.13999999999999999,
    0.06999999999999999,
  ],
  infoTemplates: {},
  groups: [
    {
      name: "background",
      type: "radio",
      title: "Bakgrundskartor",
      expanded: true,
    },
    {
      name: "intrang",
      type: "checkbox",
      title: "Intrång",
      expanded: true,
    },
  ],
  layers: [],
  styles: {},
  backgroundColor: "#C2DEF0", // Samma blå som kustvattenfärgen i MetriaFastighetGrå
  thumbnailCoordinates: [758379.97345, 7088001.97905],
};
