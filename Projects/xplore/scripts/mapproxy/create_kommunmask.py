#coding=UTF-8

import os
import sys
import requests
import json
from re import split
import shutil

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("*\n* Usage: python create_kommunmask.py fc_namn kommun_1 kommun_2 ... kommun_n" +
              "\n*\n*   fc_name     = namnet på FeatureCollection" +
              "\n*\n*   kommun_1..n = namnet på kommunen, med apostrof runt namnet om mellanslag finns i kommunnamnet" +
              "\n*\n* Ex. python create_kommunmask.py götakanalbolag luleå malung-sälen 'lilla edet'"+
              "\n*\n* Resultatet finns i kommmunmask.geojson\n*")
    else:
        URL =  "https://testmaps-v2.metria.se/geoserver/wfs"
        AUTH = 'Basic Z2VvZGVtbzpucERUeEdWSg=='
        PARAMS = {"service": "WFS", "version": "2.0.0", "srsName": "EPSG:3006", "request": "GetFeature", "typeName": "metria:Kommun_topo10", "outputFormat": "application/json"}

        f = open("kommunmask_template.geojson", "r")
        kommunmask = json.loads(f.read())
        id = 1
        maskName = str(sys.argv[1]).lower().replace(u'å', 'a').replace(u'ä', 'a').replace(u'ö', 'o')
        kommunmask['name'] = maskName

        for kommun in sys.argv[2:]:
            PARAMS['cql_filter'] = "kommunnamn = '" + str(kommun).upper() + "'"
            r = requests.get(url = URL, headers = {'Authorization': AUTH}, params = PARAMS)
            dl_json = r.json()

            kommunnamn = ''.join(a.capitalize() for a in split('( |-)', kommun))

            for feature in dl_json['features']:
                kommunmask['features'].append({
                    'type': 'Feature',
                    'properties': {'ID': id, 'namn': kommunnamn},
                    'geometry': feature['geometry']
                })
                id += 1


        if not os.path.exists("output"):
            os.mkdir("output")

        kommunmaskPath = "output/kommunmask.geojson"
        f = open(kommunmaskPath,"w+")
        f.write(json.dumps(kommunmask, indent=2, ensure_ascii=False))
        f.close()

        mappProxyFilePath = '../../mapproxy/config/coverages/' + maskName + '.geojson'
        coveragePath = shutil.copy(kommunmaskPath, mappProxyFilePath)

        print("\n>>>\n>>>  Kommunmask skapat!\n>>>\n>>>  Mask för MapProxy:\n>>>    " + mappProxyFilePath + "\n>>>\n>>>  Mask för filarea:\n>>>    " + kommunmaskPath + "\n>>>\n")

