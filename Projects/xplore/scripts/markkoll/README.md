# VpConfigExport
Pythonscript för att generera en beräkningskonfiguration för värderingsprotokoll, d.v.s. nyckeltal
för värderingsprotokoll som t.ex. prisbasbelopp. Scriptet hämtar dessa värden från lämpliga
celler i ett värderingsprotokoll.

## Installation:
Förutom Python så behövs även Pythonbiblioteket openpyxl. Det hämtas lämpligen via Pythons pakethanterare pip (https://pip.pypa.io/en/stable/installation/):
```
> pip install openpyxl
```

## Användning:
```
> python vp_config_export.py in_path out_path
```

in_path: Sökväg till värderingsprotokoll i .xlsx-format  
out_path: Sökväg till den resulterande beräkningskonfigurationen, i .json-format

För bekvämlighet så finns även ett bashscript som startar Pythonscriptet och placerar beräkningskonfigurationen på rätt ställe i källfilsträdet i Markkolls frontend:
```
> ./vp_config_export.sh in_path
```