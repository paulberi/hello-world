# language: sv
Egenskap: Hämta avverkningstatistik

  Scenario: Hämtning av avverkningstatistik med en polygon
    Givet att jag är inloggad som en vanlig användare
    När jag hämtar avverkningstatistik med polygonen "MULTIPOLYGON(((820412.3200000022 7291512.480000021,820676.6400000022 7291454.240000021,820428.0000000022 7291203.360000021,820412.3200000022 7291512.480000021)))"
    Så ska avverkningstatistiken vara komplett
