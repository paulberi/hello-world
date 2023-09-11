# language: sv
Egenskap: Söka adress

  Scenario: Sökning på stad
    Givet att jag är inloggad som en vanlig användare
    När jag söker på adresser med söksträngen "stockholm"
    Så ska staden Stockholm komma överst i träfflistan

  Scenario: Sökning på address
    Givet att jag är inloggad som en vanlig användare
    När jag söker på adresser med söksträngen "mjölkuddsv"
    Så ska Mjölkuddsvägen, Luleå komma överst i träfflistan

  Scenario: Sökning på vanlig adress som finns över hela Sverige
    Givet att jag är inloggad som en vanlig användare
    Och att kartan är centrerad över Luleå
    När jag söker på adresser med söksträngen "kungsgatan"
    Så ska Kungsgatan, Luleå komma överst i träfflistan

