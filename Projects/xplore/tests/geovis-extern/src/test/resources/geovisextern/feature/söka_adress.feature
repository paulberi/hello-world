# language: sv
Egenskap: Söka adress

  Scenario: En anonym användare söker på adress inom kommun
    Givet en adress som ligger inom kommunens gränser
    När jag söker på adressen
    Så ska den ingå i träfflistan

  Scenario: En anonym användare söker på adress inom kommun
    Givet en adress som ligger utanför kommunens gränser
    När jag söker på adressen
    Så ska den inte ingå i träfflistan

