# language: sv
Egenskap: Kartskikt

  Scenariomall: Visning av skiktet <skiktnamn>
    Givet att jag har tänt skiktet "<skiktnamn_wms>" från källan "<källa>"
    När panorerar runt i kartan
    Så ska jag se kartskiktet

    Exempel:
      | källa       | skiktnamn                 | skiktnamn_wms       |
      | Metria Maps | Brandbränseklassificering | msb:Brandrisk       |
      | Metria Maps | Basskikt                  | metria:NMD_basskikt |
      | Metria Maps | Objekthöjd 5-45 m         | metria:NMD_hojd     |
      | Metria Maps | Krontäthet                | metria:NMD_tackning |
      | LM ORTO     | Ortofoto 0.50             | Ortofoto_0.5        |
      | LM ORTO     | Ortofoto 0.25             | Ortofoto_0.25       |
      | LM TOPOWEBB | Topowebb                  | topowebb            |
      | LM TOPOWEBB | Topowebb Nedtonad         | topowebb_nedtonad   |
