# Improvements
* Flytta services till lib som kan komma att användas av andra applikationer. T.ex:
  *  Inloggning
  *  Hämta produkter och produktvarianter
  *  Lägga till i varukorg
* Olika redirects för Sälj och Kund (startsida?).
* Riktlinjer för cookies-länken, gäller den för Mitt Metria. Länken är tagen från metria.se.
* Fixa line-height, använd samma som metria.se. (1.33 => 1.4)
* Sida för not-found
* Teknisk skuld för Mitt Metria Sälj. Behövs brytas ut i dumma och smarta komponenter.
* Performance:
  * Hämta typsnitt i förväg.
  * Mindre bilder där det går (product-card)
  * Kolla över queries (hämta bara det vi behöver)
  * cachea assets
  * ändra till change direction on push där det passar
  * Fyll på...
