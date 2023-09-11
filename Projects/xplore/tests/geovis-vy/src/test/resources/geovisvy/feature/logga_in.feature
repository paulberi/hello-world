# language: sv
Egenskap: Login

  Scenario: En admin är inloggad
    Givet att jag är inloggad som en admin
    När jag undersöker tillgängliga funktioner
    Så har jag tillgång till admin-specifika funktioner

  Scenario: En vanlig användare är inloggad
    Givet att jag är inloggad som en vanlig användare
    När jag undersöker tillgängliga funktioner
    Så har jag bara tillgång till funktioner för vanliga användare

