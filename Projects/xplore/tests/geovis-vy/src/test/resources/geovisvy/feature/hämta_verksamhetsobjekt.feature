# language: sv
Egenskap: Hämta en kommuns verksamhetsobjekt
  # "Hämta en lista med objekt" och "tända lagret i kartan" är egentligen samma sak

  Scenario: En admin hämtar verksamhetsobjekt
    Givet att jag är inloggad som en admin
    Och att det finns ett läsbart lager
    När jag försöker läsa från lagret
    Så ska jag få en lista med objekt

  Scenario: En vanlig användare hämtar verksamhetsobjekt
    Givet att jag är inloggad som en vanlig användare
    Och att det finns ett läsbart lager
    När jag försöker läsa från lagret
    Så ska jag få en lista med objekt

