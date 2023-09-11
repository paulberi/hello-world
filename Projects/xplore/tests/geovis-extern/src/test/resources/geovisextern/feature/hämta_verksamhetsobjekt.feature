# language: sv
Egenskap: Hämta en kommuns verksamhetsobjekt
  # "Hämta en lista med objekt" och "tända lagret i kartan" är egentligen samma sak

  Scenario: En anonym användare hämtar verksamhetsobjekt
    Givet att det finns ett läsbart lager
    När jag försöker läsa från lagret
    Så ska jag få en lista med objekt

