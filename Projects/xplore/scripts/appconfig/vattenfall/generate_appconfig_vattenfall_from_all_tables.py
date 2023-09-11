# Only for GeoVis Vy Vattenfall. Selects from all six database tables.
# Run following command to generate groups, layers and styles.  
# Then manually copy and paste it into the appConfig.
# $ python generate_appconfig_vattenfall.py
# This script checks also if any table related to "ombyggnation" is missing in Xplore geovisdb (for UTV)
# --- Reza Firouzfar 2021-07-05 ---

import os
import psycopg2

# ----------- asks for passwords to databases in Xplore and Geodatalagret ----------
geovisdb_pwd = ""
while (len(geovisdb_pwd) == 0):
  geovisdb_pwd = input('xplore-admin@xploredb01.prodstod.se, please input password for geovisdb: ')

vattenfall_db_pwd = ""
while (len(vattenfall_db_pwd) == 0):
  vattenfall_db_pwd = input('c_vfeldistr@pgdb01.vic-metria.nu, please input password for vattenfall in Geodatalagret: ')

# ---------- create the output file, asks question if file exists ----------
if os.path.exists("GeneratedAppConfig_Vattenfall_From_All_Tables.json"):
  answer = input('The file "GeneratedAppConfig_Vattenfall_From_All_Tables.json" already exists, overwrite? [y/n]: ')
  if answer == 'y':
    
    f = open("GeneratedAppConfig_Vattenfall_From_All_Tables.json", "w")
  else:
    print("Interupted!")
    exit()
else:
  f = open("GeneratedAppConfig_Vattenfall_From_All_Tables.json", "w")

# ---------- Connect to MetriaMaps databse ----------
try:
    connection = psycopg2.connect(user="c_vfeldistr",
                                  password=vattenfall_db_pwd,
                                  host="pgdb01.vic-metria.nu",
                                  port="5432",
                                  database="vattenfall")

# ---------- Select gruppniva1 from databse ----------
    cursor = connection.cursor()
    cursor.execute("SELECT DISTINCT gruppniva1 FROM vfeldistr.franskiljare WHERE gruppniva1 IS NOT NULL ORDER BY gruppniva1;")
    lista1_franskiljare = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1 FROM vfeldistr.hsp WHERE gruppniva1 IS NOT NULL ORDER BY gruppniva1;")
    lista1_hsp = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1 FROM vfeldistr.lsp WHERE gruppniva1 IS NOT NULL ORDER BY gruppniva1;")
    lista1_lsp = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1 FROM vfeldistr.natstation WHERE gruppniva1 IS NOT NULL ORDER BY gruppniva1;")
    lista1_natstation = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1 FROM vfeldistr.omradespolygon WHERE gruppniva1 IS NOT NULL ORDER BY gruppniva1;")
    lista1_omradespolygon = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1 FROM vfeldistr.transformatorstation WHERE gruppniva1 IS NOT NULL ORDER BY gruppniva1;")
    lista1_transformatorstation = cursor.fetchall()

    lista1_kombinerad = lista1_franskiljare + lista1_hsp + lista1_lsp + lista1_natstation + lista1_omradespolygon + lista1_transformatorstation
    lista1_unik = list(set(lista1_kombinerad))
    lista1_unik.sort()
    lista_niva1 = lista1_unik

# ---------- Check if any table relateds to "ombyggnation" is missing in Xplore geovisdb (for UTV) -----------
    connection_geovisdb = psycopg2.connect(user="xplore-admin",
                                  password=geovisdb_pwd,
                                  host="xploredb01.prodstod.se",
                                  port="5432",
                                  database="geovisdb")

    cursor_geovisdb = connection_geovisdb.cursor()
    cursor_geovisdb.execute("SELECT * FROM pg_catalog.pg_tables WHERE schemaname='vattenfall';")
    table_list = cursor_geovisdb.fetchall()

    for y in lista_niva1:
      table_exists = "false"
      for z in table_list:
        grniva1 = str(y[0]).lower()
        if grniva1 + "_ombyggnation" == str(z[1]):
          table_exists = "true"
      if table_exists == "false":
        print('WARNING: There is not any table named "vattenfall.' + grniva1 + '_ombyggnation" in the database. Please create it!')
        print("OBS! There is an example of SQL to create custom table in the Python file.") 

#       --------- SQL example: put the new <gruppniva1> -------
#       cursor_geovisdb.execute("create table if not exists vattenfall.<gruppniva1>_ombyggnation (" + 
#	  "id serial not null constraint <gruppniva1>_ombyggnation_pkey primary key, " + 
#	  "geom    geometry(MultiLineString, 3006), " + 
#	  "namn    varchar(80), " +
# 	  "beskr   varchar(254), " + 
#	  "link    varchar(254), " +
#	  "adress  varchar(254), " +
# 	  "kontakt varchar(254) );" +
#	  "alter table vattenfall.<gruppniva1>_ombyggnation owner to geovis;")
#

# ---------- Create groups for each gruppniva1 and each subgroup gruppniva2 from database ----------
    f.write('  "groups": [\n')  
    for x in lista_niva1:
      m = -1
      grniva1 = str(x[0])
      f.write('    {\n')
      f.write('      "name": "' + grniva1 + '",\n')
      f.write('      "title": "' + grniva1 + '",\n')
      f.write('      "groups": [\n')

      # ---------- Select gruppniva2 from databse ----------
      cursor = connection.cursor()
      cursor.execute("SELECT DISTINCT gruppniva2 FROM vfeldistr.franskiljare WHERE gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva1=%s ORDER BY gruppniva2", [grniva1])
      lista2_franskiljare = cursor.fetchall()

      cursor.execute("SELECT DISTINCT gruppniva2 FROM vfeldistr.hsp WHERE gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva1=%s ORDER BY gruppniva2", [grniva1])
      lista2_hsp = cursor.fetchall()

      cursor.execute("SELECT DISTINCT gruppniva2 FROM vfeldistr.lsp WHERE gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva1=%s ORDER BY gruppniva2", [grniva1])
      lista2_lsp = cursor.fetchall()

      cursor.execute("SELECT DISTINCT gruppniva2 FROM vfeldistr.natstation WHERE gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva1=%s ORDER BY gruppniva2", [grniva1])
      lista2_natstation = cursor.fetchall()

      cursor.execute("SELECT DISTINCT gruppniva2 FROM vfeldistr.omradespolygon WHERE gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva1=%s ORDER BY gruppniva2", [grniva1])
      lista2_omradespolygon = cursor.fetchall()

      cursor.execute("SELECT DISTINCT gruppniva2 FROM vfeldistr.transformatorstation WHERE gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva1=%s ORDER BY gruppniva2", [grniva1])
      lista2_transformatorstation = cursor.fetchall()

      lista2_kombinerad = lista2_franskiljare + lista2_hsp + lista2_lsp + lista2_natstation + lista2_omradespolygon + lista2_transformatorstation
      lista2_unik = list(set(lista2_kombinerad))
      lista2_unik.sort()
      lista_niva2 = lista2_unik

      for y in lista_niva2:
        m = m + 1 
        grniva2 = str(y[0])

        f.write('        {\n')
        f.write('          "name": "' + grniva1 + '_' + grniva2 + '",\n')
        f.write('          "title": "' + grniva2 + '",\n')
        f.write('          "expanded": false\n')       
        if m < len(lista_niva2)-1:
          f.write('        },\n')
        else:
          f.write('        }\n')
      f.write('      ],\n')

      f.write('      "expanded": false\n')
      f.write('    },\n')

    f.write('    {\n')
    f.write('      "name": "ovrigt",\n')
    f.write('      "title": "Övrigt",\n')
    f.write('      "expanded": false\n')
    f.write('    },\n')
    f.write('    {\n')
    f.write('      "name": "background",\n')
    f.write('      "type": "radio",\n')
    f.write('      "title": "Bakgrundskartor",\n')
    f.write('      "expanded": true\n')
    f.write('    }\n')
    f.write('  ],\n')

# --------------- Create Layers ----------------
# ---------- Create layers - Ritlager ----------
    f.write('  "layers": [\n')
    f.write('    {\n')
    f.write('      "group": "ovrigt",\n')
    f.write('      "title": "Ritlager",\n')
    f.write('      "style": {\n')
    f.write('        "styleTypePropertyName": "stil",\n')
    f.write('        "styles": ["kladdlager_red","kladdlager_green","kladdlager_blue","kladdlager_magenta","kladdlager_svart"]\n')
    f.write('      },\n')
    f.write('      "geometryName": "geometry",\n')
    f.write('      "source": {\n')
    f.write('        "type": "wfs",\n')
    f.write('        "typeName": "vattenfall:kladdlager",\n')
    f.write('        "canClear": false,\n')
    f.write('        "customGeometryTypes": [\n')
    f.write('          "Text"\n')
    f.write('        ]\n')
    f.write('      },\n')
    f.write('      "infoTemplate": {\n')
    f.write('        "properties": [\n')
    f.write('          {\n')
    f.write('            "name": "stil",\n')
    f.write('            "type": "style",\n')
    f.write('            "minLength": 1\n')
    f.write('          }\n')
    f.write('        ]\n')
    f.write('      },\n')
    f.write('      "queryable": true,\n')
    f.write('      "visible": false\n')
    f.write('    },\n')

# ---------- Create layers - ombyggnation för each gruppniva1 from database ----------
    for x in lista_niva1:
      m = -1
      grniva1 = str(x[0]) 
      f.write('    {\n')
      f.write('      "group": "' + grniva1 + '",\n')
      f.write('      "style": "vf_ombyggnation",\n')
      f.write('      "title": "Ombyggnation",\n')
      f.write('      "source": {\n')
      f.write('        "type": "wfs",\n')
      f.write('        "typeName": "vattenfall:' + grniva1.lower() + '_ombyggnation"\n')
      f.write('      },\n')
      f.write('      "visible": false,\n')
      f.write('      "queryable": true,\n') 
      f.write('      "infoTemplate": "geovis-standard-info"\n')
      f.write('    },\n')

# ---------- Create layers för each gruppniva3 from database ----------
    cursor = connection.cursor()
    cursor.execute("SELECT DISTINCT gruppniva1, gruppniva2, gruppniva3 from vfeldistr.franskiljare where gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva3 IS NOT NULL ORDER BY gruppniva1, gruppniva2, gruppniva3;")
    lista_franskiljare = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1, gruppniva2, gruppniva3 from vfeldistr.hsp where gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva3 IS NOT NULL ORDER BY gruppniva1, gruppniva2, gruppniva3;")
    lista_hsp = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1, gruppniva2, gruppniva3 from vfeldistr.lsp where gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva3 IS NOT NULL ORDER BY gruppniva1, gruppniva2, gruppniva3;")
    lista_lsp = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1, gruppniva2, gruppniva3 from vfeldistr.natstation where gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva3 IS NOT NULL ORDER BY gruppniva1, gruppniva2, gruppniva3;")
    lista_natstation = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1, gruppniva2, gruppniva3 from vfeldistr.omradespolygon where gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva3 IS NOT NULL ORDER BY gruppniva1, gruppniva2, gruppniva3;")
    lista_omradespolygon = cursor.fetchall()

    cursor.execute("SELECT DISTINCT gruppniva1, gruppniva2, gruppniva3 from vfeldistr.transformatorstation where gruppniva1 IS NOT NULL and gruppniva2 IS NOT NULL and gruppniva3 IS NOT NULL ORDER BY gruppniva1, gruppniva2, gruppniva3;")
    lista_transformatorstation = cursor.fetchall()


    lista_kombinerad = lista_franskiljare + lista_hsp + lista_lsp + lista_natstation + lista_omradespolygon + lista_transformatorstation
    lista_unik = list(set(lista_kombinerad))
    lista_unik.sort()
    lista_alla_3_nivaer = lista_unik

    for z in lista_alla_3_nivaer:
      grniva1 = str(z[0]) 
      grniva2 = str(z[1])
      grniva3 = str(z[2])

      f.write("    {\n")
      f.write('      "type": "tile",\n')
      f.write('      "group": "' + grniva1 + '_' + grniva2 + '",\n')
      f.write('      "title": "' + grniva3 + '",\n')
      f.write('      "source": {\n')
      f.write('        "url": "/metria-maps/geoserver/geovis/wms",\n')
      f.write('        "type": "tilewms",\n')
      f.write('        "params": {\n')
      f.write('          "FORMAT": "image/png",\n')
      f.write('          "LAYERS": "geovis:omraden,geovis:lsp,geovis:hsp,geovis:franskiljare,geovis:natstation,geovis:transformatorstation",\n')
      f.write('          "CQL_FILTER": "gruppniva3=' + "'" + grniva3 +"'" + ';gruppniva3=' + "'" + grniva3 +"'" + ';gruppniva3=' + "'" + grniva3 +"'" + ';gruppniva3=' + "'" + grniva3 +"'" + ';gruppniva3=' + "'" + grniva3 +"'" + ';gruppniva3=' + "'" + grniva3 +"'" + '",\n')
      f.write('          "TRANSPARENT": true\n')
      f.write('        },\n')
      f.write('        "discardLowResolutionTiles": true\n')
      f.write('      },\n')
      f.write('      "visible": false,\n')
      f.write('      "queryable": true,\n')
      f.write('      "infoTemplate": "vattenfall-info"\n')
      f.write('    },\n')


# ---------- Create background layers ----------
    background_titles = ["Fastighetskartan", "Fastighetskartan nedtonad", "Ortofoto"]
    background_layers = ["MetriaFastighetPlus", "MetriaFastighetGra", "metria:orto_farg_kombinerad"]
    m = -1
    for x in background_titles:
      m = m + 1
      f.write('    {\n')
      f.write('      "type": "tile",\n')
      f.write('      "group": "background",\n')
      f.write('      "title": "' + x + '",\n')
      f.write('      "source": {\n')
      f.write('        "type": "tilewms",\n')
      f.write('        "params": {\n')
      f.write('          "FORMAT": "image/png",\n')
      f.write('          "LAYERS": "' + background_layers[m] + '",\n')
      f.write('          "TRANSPARENT": true\n') 
      f.write('        },\n')
      f.write('        "discardLowResolutionTiles": true\n')
      f.write('      },\n')
      if m == 1:
        f.write('      "visible": true\n')
      else:
        f.write('      "visible": false\n')
      if m < 2:
        f.write('    },\n')
      else:
        f.write('    }\n')
        f.write('  ],\n')

# ---------- Create Styles ----------
    f.write('  "styles": {\n')
    kladdlager_color_names = ["kladdlager_red", "kladdlager_blue", "kladdlager_green", "kladdlager_svart", "kladdlager_magenta"]
    kladdlager_color_values = ["255,0,0,0.7", "0,0,255,0.7", "0,128,0,0.7", "10,10,10,0.7", "255,0,255,0.7"]
    m = -1
    for x in kladdlager_color_names:
      m = m + 1
      f.write('    "' + x + '": [\n')
      f.write('      [\n')
      f.write('        {\n')
      f.write('          "fill": {\n')
      f.write('            "color": "rgba(' + kladdlager_color_values[m] + ')"\n')
      f.write('          },\n')
      f.write('          "text": {\n')
      f.write('            "fill": {\n')
      f.write('              "color": "rgba(' + kladdlager_color_values[m] + ')"\n')
      f.write('            },\n')
      f.write('            "font": "bold 32px serif",\n')
      f.write('            "text": "",\n')
      f.write('            "stroke": {\n')
      f.write('              "color": "rgba(' + kladdlager_color_values[m] + ')",\n')
      f.write('              "width": 2\n')
      f.write('            }\n')
      f.write('          },\n')
      f.write('          "image": {\n')
      f.write('            "fill": {\n')
      f.write('              "color": "rgba(' + kladdlager_color_values[m] + ')"\n')
      f.write('            },\n')
      f.write('            "radius": 5,\n')
      f.write('            "stroke": {\n')
      f.write('              "color": "rgba(' + kladdlager_color_values[m] + ')",\n')
      f.write('              "width": 2\n')
      f.write('            }\n')
      f.write('          },\n')
      f.write('          "stroke": {\n')
      f.write('            "color": "rgba(' + kladdlager_color_values[m] + ')",\n')
      f.write('            "width": 2\n')
      f.write('          }\n')
      f.write('        }\n')
      f.write('      ]\n')    
      f.write('    ],\n')

    f.write('    "vf_ombyggnation": [\n')
    f.write('      [\n')
    f.write('        {\n')
    f.write('          "stroke": {\n')
    f.write('            "color": "rgba(245,209,66,0.8)",\n')
    f.write('            "width": 18\n')
    f.write('          }\n')
    f.write('        }\n')
    f.write('      ]\n')
    f.write('    ]\n')
    f.write('  },\n')

# ---------- InfoTemplates ----------
# ---------- Create vattenfall-info for WMS ----------
    f.write('  "infoTemplates": {\n')
    f.write('    "vattenfall-info": {\n')
    f.write('      "properties": [\n')
    attribute_names = ["gruppniva2", "lagernamn", "placering", "namn", "littera", "rojning_ar", "linjenummer", "spanning", "typbeteckning", "anmarkning"]
    attribute_labels = ["Gruppniva2", "Lagernamn", "Placering", "Namn", "Littera", "Röjning_ar", "Linjenummer", "Spänning", "Typbeteckning", "Anmärkning"]

    m = -1
    for x in attribute_names:
      m = m + 1
      f.write('        {\n')
      f.write('          "name": "' + x + '",\n')
      f.write('          "label": "' + attribute_labels[m] + '",\n')
      f.write('          "maxLength": 254\n')
      if m < 9:    
        f.write('        },\n')
      else:
        f.write('        }\n')
    f.write('      ]\n')
    f.write('    },\n')

# ---------- Create geovis-standard-info for WFS ----------
    f.write('    "geovis-standard-info": {\n')
    f.write('      "properties": [\n')
    f.write('        {\n')
    f.write('          "name": "namn",\n')
    f.write('          "label": "Rubrik",\n')
    f.write('          "type": "title",\n')
    f.write('          "minLength": 2,\n')
    f.write('          "maxLength": 80,\n')
    f.write('          "linkRef": "title-link"\n')
    f.write('        },\n')
    f.write('        {\n')
    f.write('          "id": "title-link",\n')
    f.write('          "name": "link",\n')
    f.write('          "label": "Rubriklänk",\n')
    f.write('          "type": "url",\n')
    f.write('          "maxLength": 254\n')
    f.write('        },\n')
    attribute_names = ["beskr", "adress", "kontakt"]
    attribute_labels = ["Beskrivning", "Adress", "Kontakt"]
    m = -1
    for x in attribute_names:
      m = m + 1 
      f.write('        {\n')
      f.write('          "name": "' + x + '",\n')
      f.write('          "label": "' + attribute_labels[m] + '",\n')
      if m == 0: 
        f.write('          "inputType": "text-area",\n')
        f.write('          "hideLabel": true,\n')
      f.write('          "maxLength": 254\n')
      if m < 2:
        f.write('        },\n')
      else:
        f.write('        }\n')
    f.write('      ]\n')
    f.write('    }\n')
    f.write('  },\n')

# ---------- Print to screen ----------
    print("AppConfig has been generated successfully")

# ---------- Exception handling ----------
except (Exception, psycopg2.Error) as error:
    print("Error while connecting to PostgreSQL", error)
finally:
    if connection:
        cursor.close()
        connection.close()
        print("PostgreSQL connection is closed")

    if connection_geovisdb:
        cursor_geovisdb.close()
        connection_geovisdb.close()
        print("PostgreSQL connection_geovisdb is closed")
