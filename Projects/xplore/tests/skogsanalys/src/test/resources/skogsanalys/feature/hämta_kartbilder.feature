# language: sv
Egenskap: Kartskikt

  Scenariomall: Visning av skiktet <skiktnamn>
    Givet att jag är inloggad som en vanlig användare
    Och att jag har tänt skiktet "<skiktnamn_wms>"
    När panorerar runt i kartan
    Så ska jag se kartskiktet

    Exempel:
      | skiktnamn                 | skiktnamn_wms                                                                           |
      | FastighetPlus             | MetriaFastighetPlus                                                                     |
      | FastighetPlus + Hillshade | MetriaFastighetSA                                                                       |
      | Ortofoto                  | orto_farg_kombinerad,MetriaFastighetOver                                                |
      | Fornlämning               | metria:Fornlamning_yta_FMIS,metria:Fornlamning_linje_FMIS,metria:Fornlamning_punkt_FMIS |

  Scenario: Visning av kartskikt utan inloggning
    När jag försöker hämta ett kartskikt utan inloggning
    Så ska jag få 401 tillbaka från servern