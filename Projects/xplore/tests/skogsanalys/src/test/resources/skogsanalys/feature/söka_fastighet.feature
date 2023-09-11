# language: sv
Egenskap: Söka fastighet

  Scenario: Sökning på inkomplett beteckning
    Givet att jag är inloggad som en vanlig användare
    När jag söker på fastigheter med söksträngen "luleå forell"
    Så ska LULEÅ FORELLEN komma överst i träfflistan

  Scenario: Sökning på komplett beteckning
    Givet att jag är inloggad som en vanlig användare
    När jag söker på fastigheter med söksträngen "luleå haren 14"
    Så ska jag bara få en exakt träff på LULEÅ HAREN 14

