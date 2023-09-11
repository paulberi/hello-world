# language: sv
Egenskap: Söka adress

  Scenario: Sökning på adress inom kommun
    Givet att jag är inloggad som en vanlig användare
    Och en adress som ligger inom kommunens gränser
    När jag söker på adressen
    Så ska den ingå i träfflistan

  Scenario: Sökning på adress utanför kommun
    Givet att jag är inloggad som en vanlig användare
    Och en adress som ligger utanför kommunens gränser
    När jag söker på adressen
    Så ska den inte ingå i träfflistan

